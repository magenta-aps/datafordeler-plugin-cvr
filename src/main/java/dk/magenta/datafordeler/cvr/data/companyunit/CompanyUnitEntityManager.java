package dk.magenta.datafordeler.cvr.data.companyunit;

import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.core.database.RegistrationReference;
import dk.magenta.datafordeler.core.database.SessionManager;
import dk.magenta.datafordeler.core.fapi.FapiService;
import dk.magenta.datafordeler.cvr.data.CvrEntityManager;
import dk.magenta.datafordeler.cvr.records.CompanyUnitRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

/**
 * Created by lars on 16-05-17.
 */
@Component
public class CompanyUnitEntityManager extends CvrEntityManager<CompanyUnitEntity, CompanyUnitRegistration, CompanyUnitEffect, CompanyUnitBaseData, CompanyUnitRecord> {

    public static final OffsetDateTime MIN_SQL_SERVER_DATETIME = OffsetDateTime.of(
        1, 1, 1, 0, 0, 0, 0,
        ZoneOffset.UTC
    );

    @Autowired
    private CompanyUnitEntityService companyUnitEntityService;

    @Autowired
    private QueryManager queryManager;

    @Autowired
    private SessionManager sessionManager;

    private Logger log = LogManager.getLogger(CompanyUnitEntityManager.class);

    public CompanyUnitEntityManager() {
        this.managedEntityClass = CompanyUnitEntity.class;
        this.managedEntityReferenceClass = CompanyUnitEntityReference.class;
        this.managedRegistrationClass = CompanyUnitRegistration.class;
        this.managedRegistrationReferenceClass = CompanyUnitRegistrationReference.class;
    }

    @Override
    protected String getBaseName() {
        return "companyunit";
    }

    @Override
    public FapiService getEntityService() {
        return this.companyUnitEntityService;
    }

    @Override
    public String getSchema() {
        return CompanyUnitEntity.schema;
    }

    @Override
    protected RegistrationReference createRegistrationReference(URI uri) {
        return new CompanyUnitRegistrationReference(uri);
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
        return "VrproduktionsEnhed";
    }

    @Override
    protected Class<CompanyUnitRecord> getRecordClass() {
        return CompanyUnitRecord.class;
    }

    @Override
    protected Class<CompanyUnitEntity> getEntityClass() {
        return CompanyUnitEntity.class;
    }

    @Override
    protected UUID generateUUID(CompanyUnitRecord record) {
        return CompanyUnitEntity.generateUUID(record.getpNumber());
    }

    @Override
    protected CompanyUnitEntity createBasicEntity(CompanyUnitRecord record) {
        CompanyUnitEntity entity = new CompanyUnitEntity();
        entity.setPNumber(record.getpNumber());
        return entity;
    }

    @Override
    protected CompanyUnitBaseData createDataItem() {
        return new CompanyUnitBaseData();
    }

    /*
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
        if (type != null && !type.equals(CompanyUnitEntity.schema)) {
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
        if (jsonNode.has("VrproduktionsEnhed")) {
            jsonNode = jsonNode.get("VrproduktionsEnhed");
        }
        Session session = sessionManager.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        CompanyUnitRecord companyUnitRecord;
        try {
            companyUnitRecord = getObjectMapper().treeToValue(jsonNode, CompanyUnitRecord.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
        CompanyUnitEntity companyUnit = new CompanyUnitEntity(UUID.randomUUID(), CvrPlugin.getDomain());
        companyUnit.setPNumber(companyUnitRecord.getpNumber());

        List<BaseRecord> records = companyUnitRecord.getAll();

        ListHashMap<OffsetDateTime, BaseRecord> ajourRecords = new ListHashMap<>();
        TreeSet<OffsetDateTime> sortedTimestamps = new TreeSet<>();
        for (BaseRecord record : records) {
            OffsetDateTime registrationFrom = record.getLastUpdated();
            if (registrationFrom == null) {
                System.out.println("falling back to default");
                registrationFrom = MIN_SQL_SERVER_DATETIME;
            }
            ajourRecords.add(registrationFrom, record);
            sortedTimestamps.add(registrationFrom);
        }

        CompanyUnitRegistration lastRegistration = null;
        for (OffsetDateTime registrationFrom : sortedTimestamps) {

            // Get any existing registrering that matches this date, or create a new one
            CompanyUnitRegistration registration = companyUnit.getRegistration(registrationFrom);
            if (registration == null) {
                registration = new CompanyUnitRegistration();
                registration.setRegistreringFra(registrationFrom);
                registration.setEntity(companyUnit);
            }

            // Copy data over from the previous registrering, by cloning all virkninger and point underlying dataitems to the clones as well as the originals
            if (lastRegistration != null) {
                for (CompanyUnitEffect originalEffect : lastRegistration.getVirkninger()) {
                    CompanyUnitEffect newEffect = new CompanyUnitEffect(registration, originalEffect.getVirkningFra(), originalEffect.getVirkningTil());
                    for (CompanyUnitBaseData originalData : originalEffect.getDataItems()) {
                        originalData.addVirkning(newEffect);
                    }
                }
            }

            for (BaseRecord record : ajourRecords.get(registrationFrom)) {
                CompanyUnitEffect effect = registration.getEffect(record.getValidFrom(), record.getValidTo());
                if (effect == null) {
                    effect = new CompanyUnitEffect(registration, record.getValidFrom(), record.getValidTo());
                }

                if (effect.getDataItems().isEmpty()) {
                    CompanyUnitBaseData baseData = new CompanyUnitBaseData();
                    baseData.addVirkning(effect);
                }
                for (CompanyUnitBaseData baseData : effect.getDataItems()) {
                    // There really should be only one item for each effect right now
                    record.populateBaseData(baseData, this.queryManager, session);
                    companyUnitRecord.populateBaseData(baseData, this.queryManager, session);
                }
            }
            lastRegistration = registration;
            registrations.add(registration);

            try {
                queryManager.saveRegistrering(session, companyUnit, registration);
            } catch (DataFordelerException e) {
                e.printStackTrace();
            }
        }
        log.info("Created " + companyUnit.getRegistreringer().size() + " registrations");
        transaction.commit();
        session.close();
        return registrations;
    }*/

}
