package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.DatabaseEntry;

import javax.persistence.*;
import java.util.Objects;

/**
 * Record for Company, CompanyUnit or Participant name.
 */
@Entity
@Table(name = "cvr_record_name1", indexes = {
        @Index(name = "cvr_record_name1_companymetadata", columnList = BaseNameRecord.DB_FIELD_COMPANY_METADATA + DatabaseEntry.REF),
        @Index(name = "cvr_record_name1_unitmetadata", columnList = BaseNameRecord.DB_FIELD_UNIT_METADATA + DatabaseEntry.REF),
        @Index(name = "cvr_record_name1_participantrelation", columnList = BaseNameRecord.DB_FIELD_PARTICIPANT_RELATION + DatabaseEntry.REF),
        @Index(name = "cvr_record_name1_organization", columnList = BaseNameRecord.DB_FIELD_ORGANIZATION + DatabaseEntry.REF),
        @Index(name = "cvr_record_name1_office", columnList = BaseNameRecord.DB_FIELD_OFFICE_UNIT + DatabaseEntry.REF),
        @Index(name = "cvr_record_name1_fusion", columnList = BaseNameRecord.DB_FIELD_FUSION + DatabaseEntry.REF),
        @Index(name = "cvr_record_name1_data", columnList = BaseNameRecord.DB_FIELD_NAME),
})
public class BaseNameRecord extends CvrBitemporalMetaRecord {

    public static final String DB_FIELD_NAME = "name";
    public static final String IO_FIELD_NAME = "navn";

    @Column(name = DB_FIELD_NAME)
    @JsonProperty(value = "navn")
    private String name;

    public String getName() {
        return this.name;
    }



    public static final String DB_FIELD_PARTICIPANT_RELATION = "participantRelationRecord";

    @ManyToOne(targetEntity = ParticipantRelationRecord.class)
    @JoinColumn(name = DB_FIELD_PARTICIPANT_RELATION + DatabaseEntry.REF)
    @JsonIgnore
    private ParticipantRelationRecord participantRelationRecord;

    public void setParticipantRelationRecord(ParticipantRelationRecord participantRelationRecord) {
        this.participantRelationRecord = participantRelationRecord;
    }



    public static final String DB_FIELD_ORGANIZATION = "organizationRecord";

    @JsonIgnore
    @ManyToOne(targetEntity = OrganizationRecord.class)
    @JoinColumn(name = DB_FIELD_ORGANIZATION + DatabaseEntry.REF)
    private OrganizationRecord organizationRecord;

    public OrganizationRecord getOrganizationRecord() {
        return this.organizationRecord;
    }

    public void setOrganizationRecord(OrganizationRecord organizationRecord) {
        this.organizationRecord = organizationRecord;
    }



    public static final String DB_FIELD_OFFICE_UNIT = "officeUnitRecord";

    @JsonIgnore
    @ManyToOne(targetEntity = OfficeRelationUnitRecord.class)
    @JoinColumn(name = DB_FIELD_OFFICE_UNIT + DatabaseEntry.REF)
    private OfficeRelationUnitRecord officeUnitRecord;

    public OfficeRelationUnitRecord getOfficeUnitRecord() {
        return this.officeUnitRecord;
    }

    public void setOfficeUnitRecord(OfficeRelationUnitRecord officeUnitRecord) {
        this.officeUnitRecord = officeUnitRecord;
    }



    public static final String DB_FIELD_FUSION = "fusionSplitRecord";

    @JsonIgnore
    @ManyToOne(targetEntity = FusionSplitRecord.class)
    @JoinColumn(name = DB_FIELD_FUSION + DatabaseEntry.REF)
    private FusionSplitRecord fusionSplitRecord;

    public FusionSplitRecord getFusionSplitRecord() {
        return this.fusionSplitRecord;
    }

    public void setFusionSplitRecord(FusionSplitRecord fusionSplitRecord) {
        this.fusionSplitRecord = fusionSplitRecord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BaseNameRecord that = (BaseNameRecord) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), name);
    }
}
