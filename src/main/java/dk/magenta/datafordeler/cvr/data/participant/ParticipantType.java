package dk.magenta.datafordeler.cvr.data.participant;

import dk.magenta.datafordeler.core.database.QueryManager;
import org.hibernate.Session;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Collections;

/**
 * Created by lars on 09-06-17.
 */
@Entity
@Table(name = "cvr_participant_type", indexes = {@Index(name = "participantName", columnList = "name")})
public class ParticipantType extends ParticipantClassification {

    public static ParticipantType getType(String navn, Session session) {
        if(navn == null) {
            return null;
        }
        ParticipantType type = QueryManager.getItem(session, ParticipantType.class, Collections.singletonMap("name", navn));
        if (type == null) {
            type = new ParticipantType();
            type.setName(navn);
            session.save(type);
        }
        return type;
    }
}
