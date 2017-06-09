package dk.magenta.datafordeler.cvr.data.companyunit;

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
public class CompanyUnitEntityManager extends CvrEntityManager {

    @Autowired
    private CompanyUnitEntityService companyUnitEntityService;

    public CompanyUnitEntityManager() {
        this.managedEntityClass = CompanyUnitEntity.class;
        this.managedEntityReferenceClass = CompanyUnitEntityReference.class;
        this.managedRegistrationClass = CompanyUnitRegistration.class;
        this.managedRegistrationReferenceClass = CompanyUnitRegistrationReference.class;
    }

    @Override
    protected String getBaseName() {
        return "company";
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
    public Registration parseRegistration(JsonNode jsonNode) throws ParseException {
        // Parse a Jackson JsonNode into an instance of CompanyRegistration.
        return null;
    }

    @Override
    protected RegistrationReference createRegistrationReference(URI uri) {
        return new CompanyUnitRegistrationReference(uri);
    }

}
