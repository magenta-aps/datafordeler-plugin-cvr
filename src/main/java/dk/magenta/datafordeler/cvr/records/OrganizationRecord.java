package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

/**
 * Created by lars on 30-06-17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganizationRecord {

    @JsonProperty(value = "enhedsNummerOrganisation")
    public int unitNumber;

    @JsonProperty(value = "hovedtype")
    public String mainType;

    public UUID generateUUID() {
        String uuidInput = "participant:"+this.mainType+"/"+this.unitNumber;
        return UUID.nameUUIDFromBytes(uuidInput.getBytes());
    }

}
