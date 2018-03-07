package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.DatabaseEntry;
import dk.magenta.datafordeler.core.database.Identification;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.cvr.CvrPlugin;
import org.hibernate.Session;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Record for Company and CompanyUnit participant relations.
 */

@Entity
@Table(name = "cvr_record_participant_relation", indexes = {
        @Index(name = "cvr_record_participant_relation_company", columnList = CompanyParticipantRelationRecord.DB_FIELD_COMPANY + DatabaseEntry.REF),
        @Index(name = "cvr_record_participant_relation_companyunit", columnList = CompanyParticipantRelationRecord.DB_FIELD_COMPANYUNIT + DatabaseEntry.REF),
        @Index(name = "cvr_record_participant_relation_participant", columnList = CompanyParticipantRelationRecord.DB_FIELD_PARTICIPANT + DatabaseEntry.REF),
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyParticipantRelationRecord extends CvrBitemporalDataRecord {

    public static final String DB_FIELD_PARTICIPANT_RELATION = "participant";
    public static final String IO_FIELD_PARTICIPANT_RELATION = "deltager";

    @OneToOne(targetEntity = ParticipantRelationRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = IO_FIELD_PARTICIPANT_RELATION)
    private ParticipantRelationRecord participant;

    public void setParticipant(ParticipantRelationRecord participant) {
        this.participant = participant;
        if (participant != null) {
            participant.setCompanyParticipantRelationRecord(this);
        }
    }

    public ParticipantRelationRecord getParticipant() {
        return this.participant;
    }

    @JsonIgnore
    public Long getParticipantUnitNumber() {
        return this.participant != null ? this.participant.getUnitNumber() : null;
    }



    public static final String DB_FIELD_OFFICES = "offices";
    public static final String IO_FIELD_OFFICES = "kontorsteder";

    @OneToMany(targetEntity = OfficeRelationRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = IO_FIELD_OFFICES)
    private Set<OfficeRelationRecord> offices = new HashSet<>();

    public void setOffices(Set<OfficeRelationRecord> offices) {
        this.offices = offices;
        for (OfficeRelationRecord office : offices) {
            office.setCompanyParticipantRelationRecord(this);
        }
    }

    public Set<OfficeRelationRecord> getOffices() {
        return this.offices;
    }



    public static final String DB_FIELD_COMPANY_RELATION = "company";
    public static final String IO_FIELD_COMPANY_RELATION = "virksomhed";

    @OneToOne(targetEntity = CompanyRelationRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = IO_FIELD_COMPANY_RELATION)
    private CompanyRelationRecord company;

    public void setCompany(CompanyRelationRecord company) {
        this.company = company;
        company.setCompanyParticipantRelationRecord(this);
    }



    @OneToMany(mappedBy = OrganizationRecord.DB_FIELD_PARTICIPANT_RELATION, targetEntity = OrganizationRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = "organisationer")
    private Set<OrganizationRecord> organizations;

    public void setOrganizations(Set<OrganizationRecord> organizations) {
        this.organizations = organizations;
        for (OrganizationRecord organizationRecord : organizations) {
            organizationRecord.setCompanyParticipantRelationRecord(this);
        }
    }

    public void addOrganization(OrganizationRecord organization) {
        if (!this.organizations.contains(organization)) {
            organization.setCompanyParticipantRelationRecord(this);
            this.organizations.add(organization);
        }
    }

    public Set<OrganizationRecord> getOrganizations() {
        return this.organizations;
    }



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
        if (this.participant != null) {
            this.participant.save(session);
        }
        /*for (OrganizationRecord organizationRecord : this.organizations) {
            organizationRecord.save(session);
        }*/
    }

    public void wire(Session session) {
        for (OfficeRelationRecord officeRelationRecord : this.offices) {
            officeRelationRecord.wire(session);
        }
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

/*
    public static void merge(Set<CompanyParticipantRelationRecord> origin, Set<CompanyParticipantRelationRecord> destination, CompanyRecord companyRecord) {
        for (CompanyParticipantRelationRecord originParticipantRelation : origin) {
            ParticipantRelationRecord originParticipant = originParticipantRelation.getParticipant();
            if (originParticipant != null) {
                long originUnitNumber = originParticipant.getUnitNumber();
                for (CompanyParticipantRelationRecord destinationParticipantRelation : destination) {
                    ParticipantRelationRecord destinationParticipant = destinationParticipantRelation.getParticipant();
                    if (destinationParticipant != null) {
                        if (destinationParticipant.getUnitNumber() == originUnitNumber) {

                            // Merge participant
                            destinationParticipant.merge(originParticipant);
*/
                            // Merge offices
/*
                            hvis samme enhedsnummer, merge objects
                            ellers tilføj til liste

                            // Merge organization

                            hvis samme enhedsnummer, merge objects
                            ellers tilføj til liste

*/
/*
                            return;
                        }
                    }
                }
            }
            originParticipantRelation.setCompanyRecord(companyRecord);
            destination.add(originParticipantRelation);
        }
    }
*/


    public void merge(CompanyParticipantRelationRecord other) {

        this.getParticipant().merge(other.getParticipant());

        for (OfficeRelationRecord otherOffice : other.getOffices()) {
            Long otherUnitNumber = otherOffice.getOfficeUnitNumber();
            boolean found = false;
            if (otherUnitNumber != null) {
                for (OfficeRelationRecord ourOffice : this.offices) {
                    if (otherUnitNumber.equals(ourOffice.getOfficeUnitNumber())) {
                        ourOffice.merge(otherOffice);
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                otherOffice.setCompanyParticipantRelationRecord(this);
                this.offices.add(otherOffice);
            }
        }

        for (OrganizationRecord otherOrganization : other.getOrganizations()) {
            long otherUnitNumber = otherOrganization.getUnitNumber();
            boolean found = false;
            for (OrganizationRecord ourOrganization : this.organizations) {
                if (ourOrganization.getUnitNumber() == otherUnitNumber) {
                    ourOrganization.merge(otherOrganization);
                    found = true;
                    break;
                }
            }
            if (!found) {
                this.addOrganization(otherOrganization);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CompanyParticipantRelationRecord that = (CompanyParticipantRelationRecord) o;
        return Objects.equals(participant, that.participant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), participant);
    }
}
