package dk.magenta.datafordeler.cvr.data.company;

import dk.magenta.datafordeler.core.database.*;
import dk.magenta.datafordeler.core.fapi.FapiService;
import dk.magenta.datafordeler.cvr.data.CvrEntityManager;
import dk.magenta.datafordeler.cvr.records.CompanyRecord;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.*;

/**
 * Created by lars on 16-05-17.
 */
@Component
public class CompanyEntityManager extends CvrEntityManager<CompanyEntity, CompanyRegistration, CompanyEffect, CompanyBaseData, CompanyRecord> {

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
    protected SessionManager getSessionManager() {
        return this.sessionManager;
    }

    @Override
    protected QueryManager getQueryManager() {
        return this.queryManager;
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
    protected Class getEntityClass() {
        return CompanyEntity.class;
    }

    @Override
    protected UUID generateUUID(CompanyRecord record) {
        return CompanyEntity.generateUUID(record.getCvrNumber());
    }

    @Override
    protected CompanyEntity createBasicEntity(CompanyRecord record) {
        CompanyEntity entity = new CompanyEntity();
        entity.setCvrNumber(record.getCvrNumber());
        return entity;
    }

    @Override
    protected CompanyBaseData createDataItem() {
        return new CompanyBaseData();
    }
    /*
    public List<? extends Registration> parseRegistration(JsonNode jsonNode) throws DataFordelerException {

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
        company.setCvrNumber(companyRecord.getCvrNumber());

        List<BaseRecord> records = companyRecord.getAll();

        ListHashMap<OffsetDateTime, BaseRecord> ajourRecords = new ListHashMap<>();
        TreeSet<OffsetDateTime> sortedTimestamps = new TreeSet<>();
        for (BaseRecord record : records) {
            OffsetDateTime registrationFrom = record.getLastUpdated();
            System.out.println("registreringFra: " + registrationFrom);
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
        log.info("Created " + company.getRegistreringer().size() + " registrations");
        transaction.commit();
        session.close();
        return registrations;
    }*/
}
