package dk.magenta.datafordeler.cvr.data.industry;

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
public class IndustryEntityManager extends CvrEntityManager {

    @Autowired
    private IndustryEntityService industryEntityService;

    public IndustryEntityManager() {
        this.managedEntityClass = IndustryEntity.class;
        this.managedEntityReferenceClass = IndustryEntityReference.class;
        this.managedRegistrationClass = IndustryRegistration.class;
        this.managedRegistrationReferenceClass = IndustryRegistrationReference.class;
    }

    @Override
    protected String getBaseName() {
        return "industry";
    }

    @Override
    public FapiService getEntityService() {
        return this.industryEntityService;
    }

    @Override
    public String getSchema() {
        return IndustryEntity.schema;
    }

    @Override
    public Registration parseRegistration(JsonNode jsonNode) throws ParseException {
        // Parse a Jackson JsonNode into an instance of IndustryRegistration.
        return null;
    }

    @Override
    protected RegistrationReference createRegistrationReference(URI uri) {
        return new IndustryRegistrationReference(uri);
    }

}
