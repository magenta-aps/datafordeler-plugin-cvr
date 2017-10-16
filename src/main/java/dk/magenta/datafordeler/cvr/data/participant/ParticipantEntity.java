package dk.magenta.datafordeler.cvr.data.participant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dk.magenta.datafordeler.core.database.Identification;
import dk.magenta.datafordeler.cvr.data.CvrEntity;

import javax.persistence.Column;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import java.util.UUID;

import static dk.magenta.datafordeler.cvr.data.company.CompanyEntity.DB_FIELD_CVR;

/**
 * Created by lars on 16-05-17.
 */
@javax.persistence.Entity
@Table(name="cvr_participant_entity", indexes = {
        @Index(name = "cvr_participant_identification", columnList = "identification_id"),
        @Index(name = "cvr_participant_participantNumber", columnList = "participantNumber")
})
public class ParticipantEntity extends CvrEntity<ParticipantEntity, ParticipantRegistration> {

    @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="type")
    public static final String schema = "deltager";

    public ParticipantEntity() {
    }

    public ParticipantEntity(Identification identification) {
        super(identification);
    }

    public ParticipantEntity(UUID uuid, String domain) {
        super(uuid, domain);
    }

    @Override
    protected ParticipantRegistration createEmptyRegistration() {
        return new ParticipantRegistration();
    }

    @Column(nullable = false, insertable = true, updatable = false)
    private long participantNumber;

    @JsonProperty
    @XmlElement
    public long getParticipantNumber() {
        return this.participantNumber;
    }

    public void setParticipantNumber(long participantNumber) {
        this.participantNumber = participantNumber;
    }

    public static UUID generateUUID(String unitType, long unitNumber) {
        String uuidInput = "participant:"+unitType+"/"+unitNumber;
        return UUID.nameUUIDFromBytes(uuidInput.getBytes());
    }
}
