package dk.magenta.datafordeler.cvr.data.participant;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.cvr.data.UnversionedEntity;

import javax.persistence.Column;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by lars on 09-06-17.
 */
@Table(name = "cvr_participant_role", indexes = {@Index(name = "name", columnList = "name")})
public class ParticipantRole extends ParticipantClassification {
}
