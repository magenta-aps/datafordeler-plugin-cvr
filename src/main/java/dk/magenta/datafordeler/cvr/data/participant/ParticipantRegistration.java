package dk.magenta.datafordeler.cvr.data.participant;

import dk.magenta.datafordeler.core.database.Registration;
import dk.magenta.datafordeler.cvr.data.CvrRegistration;

import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAccessor;

/**
 * Created by lars on 16-05-17.
 */
@javax.persistence.Entity
@Table(name="cvr_participant_registration")
public class ParticipantRegistration extends CvrRegistration<ParticipantEntity, ParticipantRegistration, ParticipantEffect> {
    public ParticipantRegistration() {
    }

    public ParticipantRegistration(OffsetDateTime registrationFrom, OffsetDateTime registrationTo, int sequenceNumber) {
        super(registrationFrom, registrationTo, sequenceNumber);
    }

    public ParticipantRegistration(TemporalAccessor registrationFrom, TemporalAccessor registrationTo, int sequenceNumber) {
        super(registrationFrom, registrationTo, sequenceNumber);
    }

    public ParticipantRegistration(String registrationFrom, String registrationTo, int sequenceNumber) {
        super(registrationFrom, registrationTo, sequenceNumber);
    }

    @Override
    protected ParticipantEffect createEmptyEffect(OffsetDateTime effectFrom, OffsetDateTime effectTo) {
        return new ParticipantEffect(this, effectFrom, effectTo);
    }
}
