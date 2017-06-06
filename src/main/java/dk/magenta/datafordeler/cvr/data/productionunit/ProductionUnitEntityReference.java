package dk.magenta.datafordeler.cvr.data.productionunit;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.EntityReference;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by lars on 16-05-17.
 */
public class ProductionUnitEntityReference extends EntityReference<ProductionUnitEntity, ProductionUnitRegistrationReference> {
    @Override
    public Class<ProductionUnitEntity> getEntityClass() {
        return ProductionUnitEntity.class;
    }

    @JsonProperty
    private String type;

    public String getType() {
        return this.type;
    }

    @JsonProperty("objectID")
    public void setObjectId(UUID objectId) {
        this.objectId = objectId;
    }

    @JsonProperty("registreringer")
    public void setRegistrations(List<ProductionUnitRegistrationReference> registrations) {
        this.registrationReferences = new ArrayList<ProductionUnitRegistrationReference>(registrations);
    }
}
