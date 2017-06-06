package dk.magenta.datafordeler.cvr.data.participant;

import dk.magenta.datafordeler.core.database.Effect;

import javax.persistence.Table;

/**
 * Created by lars on 16-05-17.
 */
@javax.persistence.Entity
@Table(name="cvr_participant_effect")
public class ParticipantEffect extends Effect<ParticipantRegistration, ParticipantEffect, ParticipantData> {
    // Each effect has an effectFrom and an effectTo timestamp, both java.time.OffsetDateTime, with setters
    // Also, a collection of CompanyData instances
}
