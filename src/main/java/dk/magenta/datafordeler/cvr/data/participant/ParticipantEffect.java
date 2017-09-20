package dk.magenta.datafordeler.cvr.data.participant;

import dk.magenta.datafordeler.cvr.data.CvrEffect;

import javax.persistence.Table;
import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 * Created by lars on 16-05-17.
 */
@javax.persistence.Entity
@Table(name="cvr_participant_effect")
public class ParticipantEffect extends CvrEffect<ParticipantRegistration, ParticipantEffect, ParticipantBaseData> {
    public ParticipantEffect() {
    }

    public ParticipantEffect(ParticipantRegistration registration, OffsetDateTime effectFrom, OffsetDateTime effectTo) {
        super(registration, effectFrom, effectTo);
    }
    public ParticipantEffect(ParticipantRegistration registration, LocalDate effectFrom, LocalDate effectTo) {
        super(registration, effectFrom, effectTo);
    }

    /*public ParticipantEffect(ParticipantRegistration registrering, TemporalAccessor effectFrom, TemporalAccessor effectTo) {
        super(registrering, effectFrom, effectTo);
    }*/

    public ParticipantEffect(ParticipantRegistration registration, String effectFrom, String effectTo) {
        super(registration, effectFrom, effectTo);
    }
}
