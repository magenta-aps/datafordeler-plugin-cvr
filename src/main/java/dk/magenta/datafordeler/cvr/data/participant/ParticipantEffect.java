package dk.magenta.datafordeler.cvr.data.participant;

import dk.magenta.datafordeler.core.database.Effect;

import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAccessor;

/**
 * Created by lars on 16-05-17.
 */
@javax.persistence.Entity
@Table(name="cvr_participant_effect")
public class ParticipantEffect extends Effect<ParticipantRegistration, ParticipantEffect, ParticipantBaseData> {
    public ParticipantEffect() {
    }

    public ParticipantEffect(ParticipantRegistration registration, OffsetDateTime effectFrom, OffsetDateTime effectTo) {
        super(registration, effectFrom, effectTo);
    }

    public ParticipantEffect(ParticipantRegistration registration, TemporalAccessor effectFrom, TemporalAccessor effectTo) {
        super(registration, effectFrom, effectTo);
    }

    public ParticipantEffect(ParticipantRegistration registration, String effectFrom, String effectTo) {
        super(registration, effectFrom, effectTo);
    }
}
