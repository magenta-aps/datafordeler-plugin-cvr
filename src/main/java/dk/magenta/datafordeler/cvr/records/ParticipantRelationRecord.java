package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Record for one participant on a Company or CompanyUnit
 */
@Entity
@Table(name = "cvr_record_participant_relation_participant")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParticipantRelationRecord extends CvrBitemporalRecord {

    @JsonProperty(value = "enhedsNummer")
    public long unitNumber;

    @JsonProperty(value = "enhedstype")
    public String unitType;

    public UUID generateUUID() {
        String uuidInput = "participant:"+this.unitType+"/"+this.unitNumber;
        return UUID.nameUUIDFromBytes(uuidInput.getBytes());
    }

    @Override
    public OffsetDateTime getRegistrationFrom() {
        OffsetDateTime registrationFrom = super.getRegistrationFrom();
        if (registrationFrom == null) {
            registrationFrom = this.getLastUpdated();
        }
        return registrationFrom;
    }
}
