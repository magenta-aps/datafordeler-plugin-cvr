package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.DatabaseEntry;
import dk.magenta.datafordeler.core.database.Effect;
import dk.magenta.datafordeler.core.database.Registration;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;

import javax.persistence.*;
import java.util.*;

/**
 * Record for Company, CompanyUnit or Participant attributes.
 * Attributes with a given name may have more than one value, so values are
 * kept in {@link dk.magenta.datafordeler.cvr.records.AttributeValueRecord}
 */
@Entity
@Table(name = AttributeRecord.TABLE_NAME, indexes = {
        @Index(name = AttributeRecord.TABLE_NAME + "__company", columnList = AttributeRecord.DB_FIELD_COMPANY + DatabaseEntry.REF),
        @Index(name = AttributeRecord.TABLE_NAME + "__unit", columnList = AttributeRecord.DB_FIELD_COMPANYUNIT + DatabaseEntry.REF),
        @Index(name = AttributeRecord.TABLE_NAME + "__participant", columnList = AttributeRecord.DB_FIELD_PARTICIPANT + DatabaseEntry.REF),
        @Index(name = AttributeRecord.TABLE_NAME + "__organization", columnList = AttributeRecord.DB_FIELD_ORGANIZATION + DatabaseEntry.REF),
        @Index(name = AttributeRecord.TABLE_NAME + "__organization_memberdata", columnList = AttributeRecord.DB_FIELD_ORGANIZATION_MEMBERDATA + DatabaseEntry.REF),
        @Index(name = AttributeRecord.TABLE_NAME + "__fusion", columnList = AttributeRecord.DB_FIELD_FUSION + DatabaseEntry.REF),
        @Index(name = AttributeRecord.TABLE_NAME + "__office", columnList = AttributeRecord.DB_FIELD_OFFICE + DatabaseEntry.REF)
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class AttributeRecord extends CvrNontemporalDataRecord {

    public static final String TABLE_NAME = "cvr_record_attribute";

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

    @OneToMany(mappedBy = AttributeValueRecord.DB_FIELD_ATTRIBUTE, targetEntity = AttributeValueRecord.class, cascade = CascadeType.ALL)
    @Filters({
            @Filter(name = Registration.FILTER_REGISTRATION_TO, condition=CvrBitemporalRecord.FILTER_LAST_UPDATED),
            @Filter(name = Effect.FILTER_EFFECT_FROM, condition = CvrBitemporalRecord.FILTER_EFFECT_FROM),
            @Filter(name = Effect.FILTER_EFFECT_TO, condition = CvrBitemporalRecord.FILTER_EFFECT_TO)
    })
    private Set<AttributeValueRecord> values;

    @JsonProperty(value = IO_FIELD_VALUES)
    public void setValues(Collection<AttributeValueRecord> values) {
        for (AttributeValueRecord record : values) {
            record.setAttribute(this);
        }
        this.values = new HashSet<>(values);
    }

    public void addValue(AttributeValueRecord attributeValueRecord) {
        if (attributeValueRecord != null && !this.values.contains(attributeValueRecord)) {
            attributeValueRecord.setAttribute(this);
            this.values.add(attributeValueRecord);
        }
    }

    public Set<AttributeValueRecord> getValues() {
        return this.values;
    }






    public static final String DB_FIELD_ORGANIZATION = "organizationRecord";

    @JsonIgnore
    @JoinColumn(name = DB_FIELD_ORGANIZATION + DatabaseEntry.REF)
    @ManyToOne(targetEntity = OrganizationRecord.class, fetch = FetchType.LAZY)
    private OrganizationRecord organizationRecord;

    public OrganizationRecord getOrganizationRecord() {
        return this.organizationRecord;
    }

    public void setOrganizationRecord(OrganizationRecord organizationRecord) {
        this.organizationRecord = organizationRecord;
    }



    public static final String DB_FIELD_ORGANIZATION_MEMBERDATA = "organizationMemberdataRecord";

    @JsonIgnore
    @ManyToOne(targetEntity = OrganizationMemberdataRecord.class, fetch = FetchType.LAZY)
    @JoinColumn(name = DB_FIELD_ORGANIZATION_MEMBERDATA + DatabaseEntry.REF)
    private OrganizationMemberdataRecord organizationMemberdataRecord;

    public void setOrganizationMemberdataRecord(OrganizationMemberdataRecord organizationMemberdataRecord) {
        this.organizationMemberdataRecord = organizationMemberdataRecord;
    }



    public static final String DB_FIELD_FUSION = "fusionSplitRecord";

    @JsonIgnore
    @ManyToOne(targetEntity = FusionSplitRecord.class, fetch = FetchType.LAZY)
    @JoinColumn(name = DB_FIELD_FUSION + DatabaseEntry.REF)
    private FusionSplitRecord fusionSplitRecord;

    public void setFusionSplitRecord(FusionSplitRecord fusionSplitRecord) {
        this.fusionSplitRecord = fusionSplitRecord;
    }



    public static final String DB_FIELD_FUSION_OUTGOING = "fusionOutgoing";

    @JsonIgnore
    @Column(name = DB_FIELD_FUSION_OUTGOING)
    private boolean fusionOutgoing;

    public void setFusionOutgoing(boolean fusionOutgoing) {
        this.fusionOutgoing = fusionOutgoing;
    }



    public static final String DB_FIELD_OFFICE = "officeRelationRecord";

    @JsonIgnore
    @ManyToOne(targetEntity = OfficeRelationRecord.class, fetch = FetchType.LAZY)
    @JoinColumn(name = DB_FIELD_OFFICE + DatabaseEntry.REF)
    private OfficeRelationRecord officeRelationRecord;

    public void setOfficeRelationRecord(OfficeRelationRecord officeRelationRecord) {
        this.officeRelationRecord = officeRelationRecord;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttributeRecord that = (AttributeRecord) o;
        return sequenceNumber == that.sequenceNumber &&
                Objects.equals(type, that.type) &&
                Objects.equals(valueType, that.valueType) &&
                Objects.equals(values, that.values) &&
                fusionOutgoing == that.fusionOutgoing;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sequenceNumber, type, valueType, values, fusionOutgoing);
    }

    public void merge(AttributeRecord otherRecord) {
        if (
                otherRecord != null &&
                this.sequenceNumber == otherRecord.getSequenceNumber() &&
                Objects.equals(this.type, otherRecord.getType()) &&
                Objects.equals(this.valueType, otherRecord.getValueType())
            ) {
            for (AttributeValueRecord attributeValueRecord : otherRecord.getValues()) {
                this.addValue(attributeValueRecord);
            }
        }
    }

    @Override
    public List<CvrRecord> subs() {
        ArrayList<CvrRecord> subs = new ArrayList<>(super.subs());
        subs.addAll(this.values);
        return subs;
    }
}
