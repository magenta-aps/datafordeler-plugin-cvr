package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantBaseData;
import org.hibernate.Session;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Record for Company, CompanyUnit or Participant name.
 */
@Entity
@Table(name = "cvr_record_name")
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
    private OrganizationRecord organizationRecord;

    public OrganizationRecord getOrganizationRecord() {
        return this.organizationRecord;
    }

    public void setOrganizationRecord(OrganizationRecord organizationRecord) {
        this.organizationRecord = organizationRecord;
    }




    @Override
    public void populateBaseData(CompanyBaseData baseData, Session session) {
        baseData.setCompanyName(this.name);
    }

    @Override
    public void populateBaseData(CompanyUnitBaseData baseData, Session session) {
        baseData.setName(this.name);
    }

    @Override
    public void populateBaseData(ParticipantBaseData baseData, Session session) {
        baseData.addName(this.name);
    }
}
