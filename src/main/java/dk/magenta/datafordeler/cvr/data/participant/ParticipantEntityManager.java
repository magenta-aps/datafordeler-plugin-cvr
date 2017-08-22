package dk.magenta.datafordeler.cvr.data.participant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import dk.magenta.datafordeler.core.database.*;
import dk.magenta.datafordeler.core.exception.DataFordelerException;
import dk.magenta.datafordeler.core.exception.ParseException;
import dk.magenta.datafordeler.core.fapi.FapiService;
import dk.magenta.datafordeler.core.plugin.EntityManager;
import dk.magenta.datafordeler.core.util.ListHashMap;
import dk.magenta.datafordeler.cvr.CvrPlugin;
import dk.magenta.datafordeler.cvr.data.CvrEntityManager;
import dk.magenta.datafordeler.cvr.records.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;

/**
 * Created by lars on 16-05-17.
 */
@Component
public class ParticipantEntityManager extends CvrEntityManager<ParticipantRecord, ParticipantEntity, ParticipantRegistration, ParticipantEffect, ParticipantBaseData> {

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
