package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Record for one participating organization on a Company or CompanyUnit
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganizationRecord {

    @JsonProperty(value = "enhedsNummerOrganisation")
    public long unitNumber;

    @JsonProperty(value = "hovedtype")
    public String mainType;

    public UUID generateUUID() {
        String uuidInput = "participant:"+this.mainType+"/"+this.unitNumber;
        return UUID.nameUUIDFromBytes(uuidInput.getBytes());
    }

}
