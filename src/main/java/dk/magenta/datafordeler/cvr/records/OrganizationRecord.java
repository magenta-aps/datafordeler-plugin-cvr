package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.DatabaseEntry;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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




    @OneToMany(mappedBy = OrganizationAttributeRecord.DB_FIELD_ORGANIZATION, targetEntity = OrganizationAttributeRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = "attributter")
    public Set<OrganizationAttributeRecord> attributes;

    public void setAttributes(Set<OrganizationAttributeRecord> attributes) {
        this.attributes = attributes;
        for (OrganizationAttributeRecord attributeRecord : attributes) {
            attributeRecord.setOrganizationRecord(this);
        }
    }




    @OneToMany(mappedBy = OrganizationMemberdataRecord.DB_FIELD_ORGANIZATION, targetEntity = OrganizationMemberdataRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = "medlemsData")
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

}
