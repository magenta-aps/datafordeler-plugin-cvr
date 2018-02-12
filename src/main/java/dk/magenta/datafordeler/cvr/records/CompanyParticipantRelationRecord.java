package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.Identification;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.cvr.CvrPlugin;
import org.hibernate.Session;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * Record for Company and CompanyUnit participant relations.
 */

@Entity
@Table(name = "cvr_record_participant_relation")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyParticipantRelationRecord extends CvrBitemporalDataRecord {

    @OneToOne
    @JsonProperty(value = "deltager")
    private ParticipantRelationRecord participant;

    @OneToMany(mappedBy = OrganizationRecord.DB_FIELD_PARTICIPANT_RELATATION, targetEntity = OrganizationRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = "organisationer")
    private Set<OrganizationRecord> organizations;

    // Our source omits temporality on this object, so we must gather it elsewhere
    public OffsetDateTime getRegistrationFrom() {
        OffsetDateTime registrationFrom = super.getRegistrationFrom();
        if (registrationFrom == null && this.participant != null) {
            registrationFrom = this.participant.getRegistrationFrom();
        }
        if (registrationFrom == null) {
            registrationFrom = this.getLastUpdated();
        }
        return registrationFrom;
    }

    private Identification getParticipantIdentification(Session session) {
        if (this.participant != null) {
            UUID participantUUID = this.participant.generateUUID();
            Identification participantIdentification = QueryManager.getOrCreateIdentification(session, participantUUID, CvrPlugin.getDomain());
            return participantIdentification;
        } else {
            return null;
        }
    }

    public void save(Session session) {
        super.save(session);
        this.participant.save(session);
        /*for (OrganizationRecord organizationRecord : this.organizations) {
            organizationRecord.save(session);
        }*/
    }

    /*private Set<Identification> getOrganizationIdentifications(Session session) {
        HashSet<Identification> organizationIdentifications = new HashSet<>();
        for (OrganizationRecord organizationRecord : this.organizations) {
            UUID organizationUUID = organizationRecord.generateUUID();
            Identification organizationIdentification = QueryManager.getOrCreateIdentification(session, organizationUUID, CvrPlugin.getDomain());
            organizationIdentifications.add(organizationIdentification);
        }
        return organizationIdentifications;
    }*/
/*
    @Override
    public void populateBaseData(CompanyBaseData baseData, Session session) {
        baseData.addParticipantRelation(
                this.getParticipantIdentification(session),
                this.getOrganizationIdentifications(session)
        );
    }

    @Override
    public void populateBaseData(CompanyUnitBaseData baseData, Session session) {
        baseData.addParticipantRelation(
                this.getParticipantIdentification(session),
                this.getOrganizationIdentifications(session)
        );
    }*/
}
