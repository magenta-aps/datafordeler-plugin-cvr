package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class CvrNontemporalDataRecord extends CvrRecord {

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

    @ManyToOne(targetEntity = CompanyUnitRecord.class)
    private CompanyUnitRecord companyUnitRecord;

    public void setCompanyUnitRecord(CompanyUnitRecord companyUnitRecord) {
        this.companyUnitRecord = companyUnitRecord;
    }


    public static final String DB_FIELD_PARTICIPANT = "participantRecord";

    @ManyToOne(targetEntity = ParticipantRecord.class)
    private ParticipantRecord participantRecord;

    public void setParticipantRecord(ParticipantRecord participantRecordRecord) {
        this.participantRecord = participantRecordRecord;
    }

}
