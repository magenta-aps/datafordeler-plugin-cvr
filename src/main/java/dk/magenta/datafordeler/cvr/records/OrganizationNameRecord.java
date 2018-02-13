package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.DatabaseEntry;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantBaseData;
import org.hibernate.Session;

import javax.persistence.*;

/**
 * Record for Company, CompanyUnit or Participant name.
 */
@Entity
@Table(name = "cvr_record_organization_name", indexes = {
        @Index(name = "cvr_record_organization_name_organization", columnList = OrganizationNameRecord.DB_FIELD_ORGANIZATION + DatabaseEntry.REF),
        @Index(name = "cvr_record_organization_name_fusion", columnList = OrganizationNameRecord.DB_FIELD_FUSION + DatabaseEntry.REF)
})
public class OrganizationNameRecord extends CvrBitemporalRecord {

    public static final String DB_FIELD_NAME = "name";
    public static final String IO_FIELD_NAME = "navn";

    @Column(name = DB_FIELD_NAME)
    @JsonProperty(value = "navn")
    private String name;

    public String getName() {
        return this.name;
    }


    public static final String DB_FIELD_ORGANIZATION = "organizationRecord";
    public static final String IO_FIELD_ORGANIZATION = "organisation";

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






    public static final String DB_FIELD_FUSION = "fusionRecord";

    @JsonIgnore
    @ManyToOne(targetEntity = FusionRecord.class)
    @JoinColumn(name = DB_FIELD_FUSION + DatabaseEntry.REF)
    private FusionRecord fusionRecord;

    public FusionRecord getFusionRecord() {
        return this.fusionRecord;
    }

    public void setFusionRecord(FusionRecord fusionRecord) {
        this.fusionRecord = fusionRecord;
    }

}
