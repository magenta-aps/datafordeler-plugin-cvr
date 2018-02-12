package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "cvr_record_participant_relation_organization_memberdata")
public class OrganizationMemberdataRecord extends CvrRecord {

    public static final String DB_FIELD_ORGANIZATION = "organizationRecord";

    @ManyToOne(targetEntity = OrganizationRecord.class)
    @JsonIgnore
    private OrganizationRecord organizationRecord;

    public void setOrganizationRecord(OrganizationRecord organizationRecord) {
        this.organizationRecord = organizationRecord;
    }


    @OneToMany(mappedBy = OrganizationAttributeRecord.DB_FIELD_ORGANIZATION_MEMBERDATA, targetEntity = OrganizationAttributeRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = "attributter")
    public Set<OrganizationAttributeRecord> attributes;

    public void setAttributes(Set<OrganizationAttributeRecord> attributes) {
        this.attributes = attributes;
        for (OrganizationAttributeRecord attributeRecord : attributes) {
            attributeRecord.setOrganizationMemberdataRecord(this);
        }
    }
}