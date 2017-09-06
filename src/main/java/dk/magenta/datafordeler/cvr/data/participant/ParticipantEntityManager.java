package dk.magenta.datafordeler.cvr.data.participant;

import dk.magenta.datafordeler.core.database.*;
import dk.magenta.datafordeler.core.fapi.FapiService;
import dk.magenta.datafordeler.cvr.data.CvrEntityManager;
import dk.magenta.datafordeler.cvr.records.ParticipantRecord;
import java.time.ZoneOffset;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Created by lars on 16-05-17.
 */
@Component
public class ParticipantEntityManager extends CvrEntityManager<ParticipantEntity, ParticipantRegistration, ParticipantEffect, ParticipantBaseData, ParticipantRecord> {

    public static final OffsetDateTime MIN_SQL_SERVER_DATETIME = OffsetDateTime.of(
        1, 1, 1, 0, 0, 0, 0,
        ZoneOffset.UTC
    );

    @Autowired
    private ParticipantEntityService participantEntityService;

    @Autowired
    private QueryManager queryManager;

    @Autowired
    private SessionManager sessionManager;

    private Logger log = LogManager.getLogger(ParticipantEntityManager.class);

    public ParticipantEntityManager() {
        this.managedEntityClass = ParticipantEntity.class;
        this.managedEntityReferenceClass = ParticipantEntityReference.class;
        this.managedRegistrationClass = ParticipantRegistration.class;
        this.managedRegistrationReferenceClass = ParticipantRegistrationReference.class;
    }

    @Override
    protected String getBaseName() {
        return "participant";
    }

    @Override
    public FapiService getEntityService() {
        return this.participantEntityService;
    }

    @Override
    public String getSchema() {
        return ParticipantEntity.schema;
    }


    @Override
    protected RegistrationReference createRegistrationReference(URI uri) {
        return new ParticipantRegistrationReference(uri);
    }

    @Override
    protected SessionManager getSessionManager() {
        return this.sessionManager;
    }

    @Override
    protected QueryManager getQueryManager() {
        return this.queryManager;
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
        if (type != null && !type.equals(ParticipantEntity.schema)) {
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
        if (jsonNode.has("Vrdeltagerperson")) {
            jsonNode = jsonNode.get("Vrdeltagerperson");
        }
        Session session = sessionManager.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        ParticipantRecord participantRecord;
        try {
            participantRecord = getObjectMapper().treeToValue(jsonNode, ParticipantRecord.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
        ParticipantEntity participant = new ParticipantEntity(participantRecord.generateUUID(), CvrPlugin.getDomain());
        participant.setParticipantNumber(participantRecord.unitNumber);

        List<BaseRecord> records = participantRecord.getAll();

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

        ParticipantRegistration lastRegistration = null;
        for (OffsetDateTime registrationFrom : sortedTimestamps) {

            // Get any existing registrering that matches this date, or create a new one
            ParticipantRegistration registration = participant.getRegistration(registrationFrom);
            if (registration == null) {
                registration = new ParticipantRegistration();
                registration.setRegistreringFra(registrationFrom);
                registration.setEntity(participant);
            }

            // Copy data over from the previous registrering, by cloning all virkninger and point underlying dataitems to the clones as well as the originals
            if (lastRegistration != null) {
                for (ParticipantEffect originalEffect : lastRegistration.getVirkninger()) {
                    ParticipantEffect newEffect = new ParticipantEffect(registration, originalEffect.getVirkningFra(), originalEffect.getVirkningTil());
                    for (ParticipantBaseData originalData : originalEffect.getDataItems()) {
                        originalData.addVirkning(newEffect);
                    }
                }
            }

            for (BaseRecord record : ajourRecords.get(registrationFrom)) {
                ParticipantEffect effect = registration.getEffect(record.getValidFrom(), record.getValidTo());
                if (effect == null) {
                    effect = new ParticipantEffect(registration, record.getValidFrom(), record.getValidTo());
                }

                if (effect.getDataItems().isEmpty()) {
                    ParticipantBaseData baseData = new ParticipantBaseData();
                    baseData.addVirkning(effect);
                }
                for (ParticipantBaseData baseData : effect.getDataItems()) {
                    // There really should be only one item for each effect right now
                    record.populateBaseData(baseData, this.queryManager, session);
                    participantRecord.populateBaseData(baseData, this.queryManager, session);
                }
            }
            lastRegistration = registration;
            registrations.add(registration);

            try {
                queryManager.saveRegistrering(session, participant, registration);
            } catch (DataFordelerException e) {
                e.printStackTrace();
            }
        }
        log.info("Created " + participant.getRegistreringer().size() + " registrations");
        transaction.commit();
        session.close();
        return registrations;
    }
    */

    @Override
    protected String getJsonTypeName() {
        return "Vrdeltagerperson";
    }

    @Override
    protected Class getRecordClass() {
        return ParticipantRecord.class;
    }

    @Override
    protected Class<ParticipantEntity> getEntityClass() {
        return ParticipantEntity.class;
    }

    @Override
    protected UUID generateUUID(ParticipantRecord record) {
        return ParticipantEntity.generateUUID(record.unitType, record.unitNumber);
    }

    @Override
    protected ParticipantEntity createBasicEntity(ParticipantRecord record) {
        ParticipantEntity participant = new ParticipantEntity();
        participant.setParticipantNumber(record.unitNumber);
        return participant;
    }

    @Override
    protected ParticipantBaseData createDataItem() {
        return new ParticipantBaseData();
    }

}
