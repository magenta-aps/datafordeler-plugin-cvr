package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
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
    public Set<OrganizationAttributeRecord> attributes = new HashSet<>();

    public void setAttributes(Set<OrganizationAttributeRecord> attributes) {
        this.attributes = attributes;
        for (OrganizationAttributeRecord attributeRecord : attributes) {
            attributeRecord.setOrganizationMemberdataRecord(this);
        }
    }

    public void addAttribute(OrganizationAttributeRecord attribute) {
        if (attribute != null && !this.attributes.contains(attribute)) {
            attribute.setOrganizationMemberdataRecord(this);
            this.attributes.add(attribute);
        }
    }

    public Set<OrganizationAttributeRecord> getAttributes() {
        return this.attributes;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationMemberdataRecord that = (OrganizationMemberdataRecord) o;
        return Objects.equals(attributes, that.attributes);
    }

    @Override
    public int hashCode() {

        return Objects.hash(attributes);
    }
}