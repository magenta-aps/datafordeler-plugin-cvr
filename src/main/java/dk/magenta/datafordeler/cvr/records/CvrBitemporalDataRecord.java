package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class CvrBitemporalDataRecord extends CvrBitemporalRecord {


    public static final String DB_FIELD_COMPANY = "companyRecord";

    @JsonIgnore
    @ManyToOne(targetEntity = CompanyRecord.class)
    private CompanyRecord companyRecord;

    public void setCompanyRecord(CompanyRecord companyRecord) {
        this.companyRecord = companyRecord;
    }

    public CompanyRecord getCompanyRecord() {
        return this.companyRecord;
    }



    public static final String DB_FIELD_COMPANYUNIT = "companyUnitRecord";

    @JsonIgnore
    @ManyToOne(targetEntity = CompanyUnitRecord.class)
    private CompanyUnitRecord companyUnitRecord;

    public void setCompanyUnitRecord(CompanyUnitRecord companyUnitRecord) {
        this.companyUnitRecord = companyUnitRecord;
    }



    public static final String DB_FIELD_PARTICIPANT = "participantRecord";

    @JsonIgnore
    @ManyToOne(targetEntity = ParticipantRecord.class)
    private ParticipantRecord participantRecord;

    public void setParticipantRecord(ParticipantRecord participantRecord) {
        this.participantRecord = participantRecord;
    }

}
