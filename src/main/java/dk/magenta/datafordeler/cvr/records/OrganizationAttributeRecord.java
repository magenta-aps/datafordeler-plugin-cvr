package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.DatabaseEntry;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Record for Company, CompanyUnit or Participant attributes.
 * Attributes with a given name may have more than one value, so values are
 * kept in {@link AttributeValueRecord}
 */
@Entity
@Table(name = "cvr_record_organization_attribute", indexes = {
        @Index(name = "cvr_record_organization_attribute_organization", columnList = OrganizationAttributeRecord.DB_FIELD_ORGANIZATION + DatabaseEntry.REF)
})
public class OrganizationAttributeRecord extends CvrNontemporalRecord {

    public static final String DB_FIELD_SEQUENCENUMBER = "sequenceNumber";
    public static final String IO_FIELD_SEQUENCENUMBER = "sekvensnr";

    @Column(name = DB_FIELD_SEQUENCENUMBER)
    @JsonProperty(value = IO_FIELD_SEQUENCENUMBER)
    private int sequenceNumber;

    public int getSequenceNumber() {
        return this.sequenceNumber;
    }


    public static final String DB_FIELD_TYPE = "type";
    public static final String IO_FIELD_TYPE = "type";

    @Column(name = DB_FIELD_TYPE)
    @JsonProperty(value = IO_FIELD_TYPE)
    private String type;

    public String getType() {
        return this.type;
    }


    public static final String DB_FIELD_VALUETYPE = "valueType";
    public static final String IO_FIELD_VALUETYPE = "vaerditype";

    @Column(name = DB_FIELD_VALUETYPE)
    @JsonProperty(value = IO_FIELD_VALUETYPE)
    private String valueType;

    public String getValueType() {
        return this.valueType;
    }



    public static final String IO_FIELD_VALUES = "vaerdier";

    @OneToMany(mappedBy = OrganizationAttributeValueRecord.DB_FIELD_ATTRIBUTE, targetEntity = OrganizationAttributeValueRecord.class, cascade = CascadeType.ALL)
    private Set<OrganizationAttributeValueRecord> values;

    @JsonProperty(value = IO_FIELD_VALUES)
    public void setValues(Set<OrganizationAttributeValueRecord> values) {
        for (OrganizationAttributeValueRecord record : values) {
            record.setAttribute(this);
        }
        this.values = new HashSet<>(values);
    }

    public Set<OrganizationAttributeValueRecord> getValues() {
        return this.values;
    }



    public static final String DB_FIELD_ORGANIZATION = "organizationRecord";
    public static final String IO_FIELD_ORGANIZATION = "organisation";

    @JsonIgnore
    @JoinColumn(name = DB_FIELD_ORGANIZATION + DatabaseEntry.REF)
    @ManyToOne(targetEntity = OrganizationRecord.class)
    private OrganizationRecord organizationRecord;

    public OrganizationRecord getOrganizationRecord() {
        return this.organizationRecord;
    }

    public void setOrganizationRecord(OrganizationRecord organizationRecord) {
        this.organizationRecord = organizationRecord;
    }



    public static final String DB_FIELD_ORGANIZATION_MEMBERDATA = "organizationMemberdataRecord";
    public static final String IO_FIELD_ORGANIZATION_MEMBERDATA = "organisationMember";

    @JsonIgnore
    @ManyToOne(targetEntity = OrganizationMemberdataRecord.class)
    private OrganizationMemberdataRecord organizationMemberdataRecord;

    public void setOrganizationMemberdataRecord(OrganizationMemberdataRecord organizationMemberdataRecord) {
        this.organizationMemberdataRecord = organizationMemberdataRecord;
    }



    public static final String DB_FIELD_FUSION = "fusionSplitRecord";

    @JsonIgnore
    @ManyToOne(targetEntity = FusionSplitRecord.class)
    private FusionSplitRecord fusionSplitRecord;

    public void setFusionSplitRecord(FusionSplitRecord fusionSplitRecord) {
        this.fusionSplitRecord = fusionSplitRecord;
    }



    public static final String FUSION_OUTGOING = "fusionOutgoing";

    @JsonIgnore
    @Column(name = FUSION_OUTGOING)
    private boolean fusionOutgoing;

    public void setFusionOutgoing(boolean fusionOutgoing) {
        this.fusionOutgoing = fusionOutgoing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationAttributeRecord that = (OrganizationAttributeRecord) o;
        return sequenceNumber == that.sequenceNumber &&
                fusionOutgoing == that.fusionOutgoing &&
                Objects.equals(type, that.type) &&
                Objects.equals(valueType, that.valueType) &&
                Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sequenceNumber, type, valueType, values, fusionOutgoing);
    }
}
