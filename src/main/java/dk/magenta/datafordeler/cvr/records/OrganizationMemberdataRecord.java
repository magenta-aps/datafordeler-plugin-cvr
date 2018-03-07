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



    public static final String DB_FIELD_INDEX = "index";

    @Column(name = DB_FIELD_INDEX)
    @JsonIgnore
    private int index;

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }



    public static final String DB_FIELD_ATTRIBUTES = "attributes";
    public static final String IO_FIELD_ATTRIBUTES = "attributter";

    @OneToMany(mappedBy = AttributeRecord.DB_FIELD_ORGANIZATION_MEMBERDATA, targetEntity = AttributeRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = IO_FIELD_ATTRIBUTES)
    public Set<AttributeRecord> attributes = new HashSet<>();

    public void setAttributes(Set<AttributeRecord> attributes) {
        this.attributes = attributes;
        for (AttributeRecord attributeRecord : attributes) {
            attributeRecord.setOrganizationMemberdataRecord(this);
        }
    }

    public void addAttribute(AttributeRecord attribute) {
        if (attribute != null && !this.attributes.contains(attribute)) {
            attribute.setOrganizationMemberdataRecord(this);
            this.attributes.add(attribute);
        }
    }

    public Set<AttributeRecord> getAttributes() {
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

    public void merge(OrganizationMemberdataRecord other) {
        if (other != null) {
            for (AttributeRecord attribute : other.getAttributes()) {
                this.addAttribute(attribute);
            }
        }
    }
}