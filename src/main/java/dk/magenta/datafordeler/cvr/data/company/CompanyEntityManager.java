package dk.magenta.datafordeler.cvr.data.company;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.core.database.Registration;
import dk.magenta.datafordeler.core.database.RegistrationReference;
import dk.magenta.datafordeler.core.database.SessionManager;
import dk.magenta.datafordeler.core.exception.DataFordelerException;
import dk.magenta.datafordeler.core.exception.ParseException;
import dk.magenta.datafordeler.core.fapi.FapiService;
import dk.magenta.datafordeler.core.plugin.EntityManager;
import dk.magenta.datafordeler.core.util.ListHashMap;
import dk.magenta.datafordeler.cvr.CvrPlugin;
import dk.magenta.datafordeler.cvr.data.CvrEntityManager;
import dk.magenta.datafordeler.cvr.records.BaseRecord;
import dk.magenta.datafordeler.cvr.records.CompanyRecord;
import java.time.ZoneOffset;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.UUID;

/**
 * Created by lars on 16-05-17.
 */
@Component
public class CompanyEntityManager extends CvrEntityManager {

    public static final OffsetDateTime MIN_SQL_SERVER_DATETIME = OffsetDateTime.of(
        1, 1, 1, 0, 0, 0, 0,
        ZoneOffset.UTC
    );

    @Autowired
    private CompanyEntityService companyEntityService;

    @Autowired
    private QueryManager queryManager;

    @Autowired
    private SessionManager sessionManager;

    private Logger log = LogManager.getLogger(CompanyEntityManager.class);

    public CompanyEntityManager() {
        this.managedEntityClass = CompanyEntity.class;
        this.managedEntityReferenceClass = CompanyEntityReference.class;
        this.managedRegistrationClass = CompanyRegistration.class;
        this.managedRegistrationReferenceClass = CompanyRegistrationReference.class;
    }

    @Override
    protected String getBaseName() {
        return "company";
    }

    @Override
    public FapiService getEntityService() {
        return this.companyEntityService;
    }

    @Override
    public String getSchema() {
        return CompanyEntity.schema;
    }

    @Override
    protected RegistrationReference createRegistrationReference(URI uri) {
        return new CompanyRegistrationReference(uri);
    }


    @Override
    public List<? extends Registration> parseRegistration(JsonNode jsonNode) throws ParseException {

        ArrayList<Registration> registrations = new ArrayList<>();

        if (jsonNode.has("hits")) {
            jsonNode = jsonNode.get("hits");
            if (jsonNode.has("hits")) {
                jsonNode = jsonNode.get("hits");
            }
            if (jsonNode.isArray()) {
                // We have a list of results
                for (JsonNode item : jsonNode) {
                    registrations.addAll(this.parseRegistration(item));
                }
                return registrations;
            }
        }

        String type = jsonNode.has("_type") ? jsonNode.get("_type").asText() : null;
        if (type != null && !type.equals(CompanyEntity.schema)) {
            // Wrong type. See if we have another EntityManager that can handle it
            EntityManager otherManager = this.getRegisterManager().getEntityManager(type);
            if (otherManager != null) {
                return otherManager.parseRegistration(jsonNode);
            }
            return null;
        }

        if (jsonNode.has("_source")) {
            jsonNode = jsonNode.get("_source");
        }
        if (jsonNode.has("Vrvirksomhed")) {
            jsonNode = jsonNode.get("Vrvirksomhed");
        }
        Session session = sessionManager.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        CompanyRecord companyRecord;
        try {
            companyRecord = getObjectMapper().treeToValue(jsonNode, CompanyRecord.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
        CompanyEntity company = new CompanyEntity(UUID.randomUUID(), CvrPlugin.getDomain());
        company.setCVRNummer(companyRecord.getCVRNummer());

        List<BaseRecord> records = companyRecord.getAll();

        ListHashMap<OffsetDateTime, BaseRecord> ajourRecords = new ListHashMap<>();
        TreeSet<OffsetDateTime> sortedTimestamps = new TreeSet<>();
        for (BaseRecord record : records) {
            OffsetDateTime registrationFrom = record.getLastUpdated();
            System.out.println("registreringFra: "+registrationFrom);
            if (registrationFrom == null) {
                System.out.println("falling back to default");
                registrationFrom = MIN_SQL_SERVER_DATETIME;
            }
            ajourRecords.add(registrationFrom, record);
            sortedTimestamps.add(registrationFrom);
        }

        CompanyRegistration lastRegistration = null;
        for (OffsetDateTime registrationFrom : sortedTimestamps) {

            // Get any existing registrering that matches this date, or create a new one
            CompanyRegistration registration = company.getRegistration(registrationFrom);
            if (registration == null) {
                registration = new CompanyRegistration();
                registration.setRegistreringFra(registrationFrom);
                registration.setEntity(company);
            }

            // Copy data over from the previous registrering, by cloning all virkninger and point underlying dataitems to the clones as well as the originals
            if (lastRegistration != null) {
                for (CompanyEffect originalEffect : lastRegistration.getVirkninger()) {
                    CompanyEffect newEffect = new CompanyEffect(registration, originalEffect.getVirkningFra(), originalEffect.getVirkningTil());
                    for (CompanyBaseData originalData : originalEffect.getDataItems()) {
                        originalData.addVirkning(newEffect);
                    }
                }
            }

            for (BaseRecord record : ajourRecords.get(registrationFrom)) {
                CompanyEffect effect = registration.getEffect(record.getValidFrom(), record.getValidTo());
                if (effect == null) {
                    effect = new CompanyEffect(registration, record.getValidFrom(), record.getValidTo());
                }

                if (effect.getDataItems().isEmpty()) {
                    CompanyBaseData baseData = new CompanyBaseData();
                    baseData.addVirkning(effect);
                }
                for (CompanyBaseData baseData : effect.getDataItems()) {
                    // There really should be only one item for each effect right now
                    record.populateBaseData(baseData, this.queryManager, session);
                    companyRecord.populateBaseData(baseData, this.queryManager, session);
                }
            }
            lastRegistration = registration;
            registrations.add(registration);

            try {
                queryManager.saveRegistrering(session, company, registration);
            } catch (DataFordelerException e) {
                e.printStackTrace();
            }
        }
        log.info("Created "+company.getRegistreringer().size()+" registrations");
        transaction.commit();
        session.close();
        return registrations;
    }

}
