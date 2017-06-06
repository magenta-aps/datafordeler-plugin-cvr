package dk.magenta.datafordeler.cvr.data.participant;

import com.fasterxml.jackson.databind.JsonNode;
import dk.magenta.datafordeler.core.database.Registration;
import dk.magenta.datafordeler.core.database.RegistrationReference;
import dk.magenta.datafordeler.core.exception.ParseException;
import dk.magenta.datafordeler.core.fapi.FapiService;
import dk.magenta.datafordeler.cvr.data.CvrEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;

/**
 * Created by lars on 16-05-17.
 */
@Component
public class ParticipantEntityManager extends CvrEntityManager {

    @Autowired
    private ParticipantEntityService participantEntityService;

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
    public Registration parseRegistration(JsonNode jsonNode) throws ParseException {
        // Parse a Jackson JsonNode into an instance of ParticipantRegistration.
        return null;
    }

    @Override
    protected RegistrationReference createRegistrationReference(URI uri) {
        return new ParticipantRegistrationReference(uri);
    }

}
