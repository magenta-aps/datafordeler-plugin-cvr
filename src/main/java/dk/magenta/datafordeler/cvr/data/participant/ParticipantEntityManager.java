package dk.magenta.datafordeler.cvr.data.participant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.core.database.Registration;
import dk.magenta.datafordeler.core.database.RegistrationReference;
import dk.magenta.datafordeler.core.database.SessionManager;
import dk.magenta.datafordeler.core.exception.ParseException;
import dk.magenta.datafordeler.core.fapi.FapiService;
import dk.magenta.datafordeler.core.util.ListHashMap;
import dk.magenta.datafordeler.cvr.data.CvrEntityManager;
import dk.magenta.datafordeler.cvr.records.BaseRecord;
import dk.magenta.datafordeler.cvr.records.ParticipantRecord;
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
public class ParticipantEntityManager extends CvrEntityManager {

    @Autowired
    private ParticipantEntityService participantEntityService;

    @Autowired
    private QueryManager queryManager;

    @Autowired
    private SessionManager sessionManager;

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
    public List<Registration> parseRegistration(JsonNode jsonNode) throws ParseException {
        ArrayList<Registration> registrations = new ArrayList<>();
        Session session = sessionManager.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        System.out.println("Parse jsonNode");
        jsonNode = this.unwrap(jsonNode);

        ParticipantEntity company = new ParticipantEntity(UUID.randomUUID(), "cvr", jsonNode.get("enhedsNummer").asInt());
        ParticipantRecord participantRecord;
        try {
            participantRecord = getObjectMapper().treeToValue(jsonNode, ParticipantRecord.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
        List<BaseRecord> records = participantRecord.getAll();

        ListHashMap<OffsetDateTime, BaseRecord> ajourRecords = new ListHashMap<>();
        TreeSet<OffsetDateTime> sortedTimestamps = new TreeSet<>();
        for (BaseRecord record : records) {
            System.out.println("record: "+record);
            OffsetDateTime registrationFrom = record.getLastUpdated();
            ajourRecords.add(registrationFrom, record);
            sortedTimestamps.add(registrationFrom);
        }

        ParticipantRegistration lastRegistration = null;
        for (OffsetDateTime registrationFrom : sortedTimestamps) {

            // Get any existing registration that matches this date, or create a new one
            ParticipantRegistration registration = company.getRegistration(registrationFrom);
            if (registration == null) {
                registration = new ParticipantRegistration();
                registration.setRegistrationFrom(registrationFrom);
                registration.setEntity(company);
            }

            // Copy data over from the previous registration, by cloning all effects and point underlying dataitems to the clones as well as the originals
            if (lastRegistration != null) {
                for (ParticipantEffect originalEffect : lastRegistration.getEffects()) {
                    ParticipantEffect newEffect = new ParticipantEffect(registration, originalEffect.getEffectFrom(), originalEffect.getEffectTo());
                    for (ParticipantBaseData originalData : originalEffect.getDataItems()) {
                        originalData.addEffect(newEffect);
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
                    baseData.addEffect(effect);
                }
                for (ParticipantBaseData baseData : effect.getDataItems()) {
                    // There really should be only one item for each effect right now
                    record.populateParticipantBaseData(baseData, this.queryManager, session);
                }
            }
            lastRegistration = registration;
            registrations.add(registration);

            try {
                System.out.println(getObjectMapper().writeValueAsString(registration));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            System.out.println("==========================================================");
        }
        //System.out.println(company.getRegistrations());
        transaction.commit();
        session.close();
        return registrations;
    }


    private JsonNode unwrap(JsonNode jsonNode) {
        if (jsonNode.has("_source")) {
            jsonNode = jsonNode.get("_source");
        }
        if (jsonNode.has("Vrdeltagerperson")) {
            jsonNode = jsonNode.get("Vrdeltagerperson");
        }
        return jsonNode;
    }

}
