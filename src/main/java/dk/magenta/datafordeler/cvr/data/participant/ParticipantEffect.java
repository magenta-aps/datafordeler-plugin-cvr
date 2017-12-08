package dk.magenta.datafordeler.cvr.data.participant;

import dk.magenta.datafordeler.cvr.data.CvrEffect;

import javax.persistence.Index;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 * Created by lars on 16-05-17.
 */
@javax.persistence.Entity
@Table(name="cvr_participant_effect", indexes = {
        @Index(name = "cvr_participant_effect_registration", columnList = "registration_id"),
        @Index(name = "cvr_participant_effect_from", columnList = "effectFrom"),
        @Index(name = "cvr_participant_effect_to", columnList = "effectTo")
})
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
