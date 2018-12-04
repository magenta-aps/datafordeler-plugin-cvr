package dk.magenta.datafordeler.cvr.data.company;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectReader;
import dk.magenta.datafordeler.core.database.SessionManager;
import dk.magenta.datafordeler.core.exception.DataStreamException;
import dk.magenta.datafordeler.core.exception.HttpStatusException;
import dk.magenta.datafordeler.core.fapi.FapiBaseService;
import dk.magenta.datafordeler.core.plugin.ScanScrollCommunicator;
import dk.magenta.datafordeler.cvr.CvrRegisterManager;
import dk.magenta.datafordeler.cvr.configuration.CvrConfiguration;
import dk.magenta.datafordeler.cvr.configuration.CvrConfigurationManager;
import dk.magenta.datafordeler.cvr.data.CvrEntityManager;
import dk.magenta.datafordeler.cvr.records.CompanyRecord;
import dk.magenta.datafordeler.cvr.records.service.CompanyRecordService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.StringJoiner;
import java.util.UUID;

/**
 * Company-specific EntityManager, specifying various settings that methods in the superclass
 * will use to import data.
 */
@Component
public class CompanyEntityManager extends CvrEntityManager<CompanyRecord> {

    public static final OffsetDateTime MIN_SQL_SERVER_DATETIME = OffsetDateTime.of(
            1, 1, 1, 0, 0, 0, 0,
            ZoneOffset.UTC
    );

    @Autowired
    private CompanyRecordService companyEntityService;

    @Autowired
    private SessionManager sessionManager;

    private Logger log = LogManager.getLogger(CompanyEntityManager.class);

    public CompanyEntityManager() {
    }

    @Override
    protected String getBaseName() {
        return "company";
    }

    @Override
    public FapiBaseService getEntityService() {
        return this.companyEntityService;
    }

    @Override
    public String getSchema() {
        return CompanyRecord.schema;
    }

    @Override
    protected SessionManager getSessionManager() {
        return this.sessionManager;
    }

    @Override
    protected String getJsonTypeName() {
        return "Vrvirksomhed";
    }

    @Override
    protected Class getRecordClass() {
        return CompanyRecord.class;
    }

    @Override
    protected UUID generateUUID(CompanyRecord record) {
        return CompanyRecord.generateUUID(record.getCvrNumber());
    }

    @Autowired
    private CvrConfigurationManager configurationManager;

    @Override
    public boolean pullEnabled() {
        CvrConfiguration configuration = configurationManager.getConfiguration();
        return (configuration.getCompanyRegisterType() != CvrConfiguration.RegisterType.DISABLED);
    }


    public HashSet<CompanyRecord> directLookup(HashSet<String> cvrNumbers, OffsetDateTime since) {
        HashSet<CompanyRecord> records = new HashSet<>();
        CvrRegisterManager registerManager = (CvrRegisterManager) this.getRegisterManager();
        ScanScrollCommunicator eventCommunicator = (ScanScrollCommunicator) registerManager.getEventFetcher();
        CvrConfiguration configuration = this.configurationManager.getConfiguration();

        String schema = CompanyRecord.schema;

        eventCommunicator.setUsername(configuration.getUsername(schema));
        eventCommunicator.setPassword(configuration.getPassword(schema));
        eventCommunicator.setThrottle(0);

        StringJoiner csep = new StringJoiner(",");
        for (String cvrNumber : cvrNumbers) {
            csep.add("\"" + cvrNumber + "\"");
        }

        String requestBody = "{" +
                "\"query\": {" +
                "\"terms\": {" +
                "\"Vrvirksomhed.cvrNummer\": [" + csep.toString() + "]" +
                "}";
        if (since != null) {
            requestBody += "}," +
                    "\"range\": {" +
                    "\"Vrvirksomhed.sidstOpdateret\": {" +
                    "\"gte\": \""+since.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)+"\"" +
                    "}" +
                    "}";
        } else {
            requestBody += "}";
        }
         requestBody += "}";

        InputStream rawData;
        try {
            rawData = eventCommunicator.fetch(
                    new URI(configuration.getStartAddress(schema)),
                    new URI(configuration.getScrollAddress(schema)),
                    requestBody
            );
            JsonNode topNode = this.getObjectMapper().readTree(rawData);
            ObjectReader reader = this.getObjectMapper().readerFor(CompanyRecord.class);
            if (topNode != null && topNode.has("hits")) {
                topNode = topNode.get("hits");
                if (topNode.has("hits")) {
                    topNode = topNode.get("hits");
                }
                if (topNode.isArray()) {
                    for (JsonNode item : topNode) {

                        if (item.has("_source")) {
                            item = item.get("_source");
                        }
                        String jsonTypeName = this.getJsonTypeName();
                        if (item.has(jsonTypeName)) {
                            item = item.get(jsonTypeName);
                        }
                        CompanyRecord record = reader.readValue(item);
                        records.add(record);
                    }
                }
            }
        } catch (HttpStatusException | DataStreamException | URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return records;
    }
}