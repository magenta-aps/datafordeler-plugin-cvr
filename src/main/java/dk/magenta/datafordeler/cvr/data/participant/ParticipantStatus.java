package dk.magenta.datafordeler.cvr.data.participant;

import javax.persistence.Index;
import javax.persistence.Table;

/**
 * Created by lars on 09-06-17.
 */
@Table(name = "cvr_participant_status", indexes = {@Index(name = "name", columnList = "name")})
public class ParticipantStatus extends ParticipantClassification {
}
