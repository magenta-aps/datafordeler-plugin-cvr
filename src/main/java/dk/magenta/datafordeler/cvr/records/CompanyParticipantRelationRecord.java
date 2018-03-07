package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * Record for Company and CompanyUnit relationParticipantRecord relations.
 */

@Entity
@Table(name = CompanyParticipantRelationRecord.TABLE_NAME, indexes = {
        @Index(name = CompanyParticipantRelationRecord.TABLE_NAME + "__company", columnList = CompanyParticipantRelationRecord.DB_FIELD_COMPANY + DatabaseEntry.REF),
        @Index(name = CompanyParticipantRelationRecord.TABLE_NAME + "__unit", columnList = CompanyParticipantRelationRecord.DB_FIELD_COMPANYUNIT + DatabaseEntry.REF),
        @Index(name = CompanyParticipantRelationRecord.TABLE_NAME + "__participant", columnList = CompanyParticipantRelationRecord.DB_FIELD_PARTICIPANT + DatabaseEntry.REF),
})
public class CompanyParticipantRelationRecord extends CvrBitemporalDataRecord {

    public static final String TABLE_NAME = "cvr_record_company_participant_relation";

    public static final String DB_FIELD_PARTICIPANT_RELATION = "relationParticipantRecord";
    public static final String IO_FIELD_PARTICIPANT_RELATION = "deltager";

    @OneToOne(targetEntity = RelationParticipantRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = IO_FIELD_PARTICIPANT_RELATION)
    private RelationParticipantRecord relationParticipantRecord;

    public void setRelationParticipantRecord(RelationParticipantRecord relationParticipantRecord) {
        this.relationParticipantRecord = relationParticipantRecord;
        if (relationParticipantRecord != null) {
            relationParticipantRecord.setCompanyParticipantRelationRecord(this);
        }
    }

    public RelationParticipantRecord getRelationParticipantRecord() {
        return this.relationParticipantRecord;
    }

    @JsonIgnore
    public Long getParticipantUnitNumber() {
        return this.relationParticipantRecord != null ? this.relationParticipantRecord.getUnitNumber() : null;
    }



    public static final String DB_FIELD_COMPANY_RELATION = "relationCompanyRecord";
    public static final String IO_FIELD_COMPANY_RELATION = "virksomhed";

    @OneToOne(targetEntity = RelationCompanyRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = IO_FIELD_COMPANY_RELATION)
    private RelationCompanyRecord relationCompanyRecord;

    public void setRelationCompanyRecord(RelationCompanyRecord relationCompanyRecord) {
        this.relationCompanyRecord = relationCompanyRecord;
        relationCompanyRecord.setCompanyParticipantRelationRecord(this);
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



    public static final String DB_FIELD_ORGANIZATIONS = "organizations";
    public static final String IO_FIELD_ORGANIZATIONS = "organisationer";

    @OneToMany(mappedBy = OrganizationRecord.DB_FIELD_PARTICIPANT_RELATION, targetEntity = OrganizationRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = IO_FIELD_ORGANIZATIONS)
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
        if (registrationFrom == null && this.relationParticipantRecord != null) {
            registrationFrom = this.relationParticipantRecord.getRegistrationFrom();
        }
        if (registrationFrom == null) {
            registrationFrom = this.getLastUpdated();
        }
        return registrationFrom;
    }

    private Identification getParticipantIdentification(Session session) {
        if (this.relationParticipantRecord != null) {
            UUID participantUUID = this.relationParticipantRecord.generateUUID();
            Identification participantIdentification = QueryManager.getOrCreateIdentification(session, participantUUID, CvrPlugin.getDomain());
            return participantIdentification;
        } else {
            return null;
        }
    }

    public void save(Session session) {
        super.save(session);
        if (this.relationParticipantRecord != null) {
            this.relationParticipantRecord.save(session);
        }
        /*for (OrganizationRecord organizationRecord : this.organizations) {
            organizationRecord.save(session);
        }*/
    }

    public void wire(Session session) {
        this.relationParticipantRecord.wire(session);
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


    public void merge(CompanyParticipantRelationRecord other) {

        this.getRelationParticipantRecord().merge(other.getRelationParticipantRecord());

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
        return Objects.equals(relationParticipantRecord, that.relationParticipantRecord) &&
                Objects.equals(relationCompanyRecord, that.relationCompanyRecord) &&
                Objects.equals(offices, that.offices) &&
                Objects.equals(organizations, that.organizations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), relationParticipantRecord, relationCompanyRecord, offices, organizations);
    }
}
