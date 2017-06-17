package dk.magenta.datafordeler.cvr.data.participant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dk.magenta.datafordeler.core.database.Entity;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by lars on 16-05-17.
 */
@javax.persistence.Entity
@Table(name="cvr_participant_entity")
public class ParticipantEntity extends Entity<ParticipantEntity, ParticipantRegistration> {

    @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="type")
    public static final String schema = "Participant";

    @Column(nullable = false, insertable = true, updatable = false)
    private int participantNumber;

    @JsonProperty
    @XmlElement
    public int getParticipantNumber() {
        return this.participantNumber;
    }

    public void setParticipantNumber(int participantNumber) {
        this.participantNumber = participantNumber;
    }
}
