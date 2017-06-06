package dk.magenta.datafordeler.cvr.data.industry;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.EntityReference;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by lars on 16-05-17.
 */
public class IndustryEntityReference extends EntityReference<IndustryEntity, IndustryRegistrationReference> {
    @Override
    public Class<IndustryEntity> getEntityClass() {
        return IndustryEntity.class;
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
    public void setRegistrations(List<IndustryRegistrationReference> registrations) {
        this.registrationReferences = new ArrayList<IndustryRegistrationReference>(registrations);
    }
}
