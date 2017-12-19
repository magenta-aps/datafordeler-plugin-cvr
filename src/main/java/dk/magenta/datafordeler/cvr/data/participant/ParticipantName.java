package dk.magenta.datafordeler.cvr.data.participant;

import dk.magenta.datafordeler.cvr.data.shared.SingleData;

import javax.persistence.*;

import static dk.magenta.datafordeler.cvr.data.participant.ParticipantName.DB_FIELD_BASEDATA;
import static dk.magenta.datafordeler.cvr.data.shared.SingleData.DB_FIELD_VALUE;

@Entity
@Table(name = "cvr_participant_name", indexes = {
        @Index(name = "cvr_participant_name_data", columnList = DB_FIELD_VALUE),
        @Index(name = "cvr_participant_name_base", columnList = DB_FIELD_BASEDATA + "_id")
})
public class ParticipantName extends SingleData<String> {

    public static final String DB_FIELD_BASEDATA = "participantBaseData";

    @ManyToOne(targetEntity = ParticipantBaseData.class, optional = false)
    @JoinColumn(name = DB_FIELD_BASEDATA + "_id")
    private ParticipantBaseData participantBaseData;

    public void setParticipantBaseData(ParticipantBaseData participantBaseData) {
        this.participantBaseData = participantBaseData;
    }

}
