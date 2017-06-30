package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.jaxrs.json.annotation.JSONP;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by lars on 26-06-17.
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
