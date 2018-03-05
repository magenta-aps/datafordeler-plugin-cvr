package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.DatabaseEntry;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantBaseData;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Record for Company, CompanyUnit or Participant attributes.
 * Attributes with a given name may have more than one value, so values are
 * kept in {@link dk.magenta.datafordeler.cvr.records.AttributeValueRecord}
 */
@Entity
@Table(name = "cvr_record_attribute", indexes = {
        @Index(name = "cvr_record_attribute_company", columnList = AttributeRecord.DB_FIELD_COMPANY + DatabaseEntry.REF),
        @Index(name = "cvr_record_attribute_companyunit", columnList = AttributeRecord.DB_FIELD_COMPANYUNIT + DatabaseEntry.REF),
        @Index(name = "cvr_record_attribute_participant", columnList = AttributeRecord.DB_FIELD_PARTICIPANT + DatabaseEntry.REF),
        @Index(name = "cvr_record_attribute_organization", columnList = AttributeRecord.DB_FIELD_ORGANIZATION + DatabaseEntry.REF),
        @Index(name = "cvr_record_attribute_fusion", columnList = AttributeRecord.DB_FIELD_FUSION + DatabaseEntry.REF),
        @Index(name = "cvr_record_attribute_office", columnList = AttributeRecord.DB_FIELD_OFFICE + DatabaseEntry.REF)
})
public class AttributeRecord extends CvrNontemporalDataRecord {

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
    private Set<AttributeValueRecord> values;

    @JsonProperty(value = IO_FIELD_VALUES)
    public void setValues(Collection<AttributeValueRecord> values) {
        for (AttributeValueRecord record : values) {
            record.setAttribute(this);
        }
        this.values = new HashSet<>(values);
    }

    public Set<AttributeValueRecord> getValues() {
        return this.values;
    }






    public static final String DB_FIELD_ORGANIZATION = "organizationRecord";

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

    @JsonIgnore
    @ManyToOne(targetEntity = OrganizationMemberdataRecord.class)
    @JoinColumn(name = DB_FIELD_ORGANIZATION_MEMBERDATA + DatabaseEntry.REF)
    private OrganizationMemberdataRecord organizationMemberdataRecord;

    public void setOrganizationMemberdataRecord(OrganizationMemberdataRecord organizationMemberdataRecord) {
        this.organizationMemberdataRecord = organizationMemberdataRecord;
    }



    public static final String DB_FIELD_FUSION = "fusionSplitRecord";

    @JsonIgnore
    @ManyToOne(targetEntity = FusionSplitRecord.class)
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
    @ManyToOne(targetEntity = OfficeRelationRecord.class)
    @JoinColumn(name = DB_FIELD_OFFICE + DatabaseEntry.REF)
    private OfficeRelationRecord officeRelationRecord;

    public void setOfficeRelationRecord(OfficeRelationRecord officeRelationRecord) {
        this.officeRelationRecord = officeRelationRecord;
    }







    @Override
    public void populateBaseData(CompanyBaseData baseData, Session session) {
        for (AttributeValueRecord record : values) {
            record.populateBaseData(baseData, session);
        }
    }

    @Override
    public void populateBaseData(CompanyUnitBaseData baseData, Session session) {
        for (AttributeValueRecord record : values) {
            record.populateBaseData(baseData, session);
        }
    }

    @Override
    public void populateBaseData(ParticipantBaseData baseData, Session session) {
        for (AttributeValueRecord record : values) {
            record.populateBaseData(baseData, session);
        }
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
}
