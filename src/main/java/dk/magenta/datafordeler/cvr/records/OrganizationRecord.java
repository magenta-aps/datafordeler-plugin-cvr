package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.DatabaseEntry;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

/**
 * Record for one participating organization on a Company or CompanyUnit
 */
@Entity
@Table(name = "cvr_record_participant_relation_organization")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganizationRecord extends DatabaseEntry {


    public static final String DB_FIELD_PARTICIPANT_RELATION = "companyParticipantRelationRecord";


    @ManyToOne(targetEntity = CompanyParticipantRelationRecord.class)
    private CompanyParticipantRelationRecord companyParticipantRelationRecord;

    public void setCompanyParticipantRelationRecord(CompanyParticipantRelationRecord companyParticipantRelationRecord) {
        this.companyParticipantRelationRecord = companyParticipantRelationRecord;
    }



    @Column
    @JsonProperty(value = "enhedsNummerOrganisation")
    public long unitNumber;




    @Column
    @JsonProperty(value = "hovedtype")
    public String mainType;




    @OneToMany(mappedBy = OrganizationNameRecord.DB_FIELD_ORGANIZATION, targetEntity = OrganizationNameRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = "organisationsNavn")
    public Set<OrganizationNameRecord> name;

    public Set<OrganizationNameRecord> getName() {
        return this.name;
    }

    public void setName(Set<OrganizationNameRecord> name) {
        this.name = name;
        for (OrganizationNameRecord nameRecord : name) {
            nameRecord.setOrganizationRecord(this);
        }
    }



    public static final String DB_FIELD_ATTRIBUTES = "attributes";
    public static final String IO_FIELD_ATTRIBUTES = "attributter";

    @OneToMany(mappedBy = AttributeRecord.DB_FIELD_ORGANIZATION, targetEntity = AttributeRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = IO_FIELD_ATTRIBUTES)
    public Set<AttributeRecord> attributes;

    public void setAttributes(Set<AttributeRecord> attributes) {
        this.attributes = attributes;
        for (AttributeRecord attributeRecord : attributes) {
            attributeRecord.setOrganizationRecord(this);
        }
    }

    public void addAttribute(AttributeRecord attribute) {
        if (attribute != null && !this.attributes.contains(attribute)) {
            attribute.setOrganizationRecord(this);
            this.attributes.add(attribute);
        }
    }

    public Set<AttributeRecord> getAttributes() {
        return this.attributes;
    }



    public static final String DB_FIELD_MEMBERDATA = "memberData";
    public static final String IO_FIELD_MEMBERDATA = "medlemsData";

    @OneToMany(mappedBy = OrganizationMemberdataRecord.DB_FIELD_ORGANIZATION, targetEntity = OrganizationMemberdataRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = IO_FIELD_MEMBERDATA)
    public Set<OrganizationMemberdataRecord> memberData;

    public void setMemberData(Set<OrganizationMemberdataRecord> memberData) {
        this.memberData = memberData;
        for (OrganizationMemberdataRecord memberdataRecord : memberData) {
            memberdataRecord.setOrganizationRecord(this);
        }
    }

    public void save(Session session) {
        session.save(this);
    }
    /*public UUID generateUUID() {
        String uuidInput = "participant:"+this.mainType+"/"+this.unitNumber;
        return UUID.nameUUIDFromBytes(uuidInput.getBytes());
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationRecord that = (OrganizationRecord) o;
        return unitNumber == that.unitNumber &&
                Objects.equals(mainType, that.mainType) &&
                Objects.equals(name, that.name) &&
                Objects.equals(attributes, that.attributes) &&
                Objects.equals(memberData, that.memberData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unitNumber, mainType, name, attributes, memberData);
    }
}
