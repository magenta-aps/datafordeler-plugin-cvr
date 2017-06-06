package dk.magenta.datafordeler.cvr.data.productionunit;

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
public class ProductionUnitEntityManager extends CvrEntityManager {

    @Autowired
    private ProductionUnitEntityService productionUnitEntityService;

    public ProductionUnitEntityManager() {
        this.managedEntityClass = ProductionUnitEntity.class;
        this.managedEntityReferenceClass = ProductionUnitEntityReference.class;
        this.managedRegistrationClass = ProductionUnitRegistration.class;
        this.managedRegistrationReferenceClass = ProductionUnitRegistrationReference.class;
    }

    @Override
    protected String getBaseName() {
        return "productionunit";
    }

    @Override
    public FapiService getEntityService() {
        return this.productionUnitEntityService;
    }

    @Override
    public String getSchema() {
        return ProductionUnitEntity.schema;
    }

    @Override
    public Registration parseRegistration(JsonNode jsonNode) throws ParseException {
        // Parse a Jackson JsonNode into an instance of CompanyRegistration. Make note of our superclasses as you read this
        return null;
    }

    @Override
    protected RegistrationReference createRegistrationReference(URI uri) {
        return new ProductionUnitRegistrationReference(uri);
    }

}
