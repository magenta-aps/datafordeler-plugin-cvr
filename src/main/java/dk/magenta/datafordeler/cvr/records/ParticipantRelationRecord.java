package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Record for one participant on a Company or CompanyUnit
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParticipantRelationRecord extends CvrRecord {

    @JsonProperty(value = "enhedsNummer")
    public int unitNumber;

    @JsonProperty(value = "enhedstype")
    public String unitType;

    public UUID generateUUID() {
        String uuidInput = "participant:"+this.unitType+"/"+this.unitNumber;
        return UUID.nameUUIDFromBytes(uuidInput.getBytes());
    }

}
