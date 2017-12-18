package dk.magenta.datafordeler.cvr.data.participant;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * Storage for data on a Participant's status
 * referenced by {@link dk.magenta.datafordeler.cvr.data.participant.ParticipantBaseData}
 */
@Entity
@Table(
    name = "cvr_participant_status",
    indexes = {@Index(name = "participantStatusName", columnList = "name")}
)
public class ParticipantStatus extends ParticipantClassification {
}
