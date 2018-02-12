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

    @ManyToOne(targetEntity = CompanyUnitRecord.class)
    private CompanyUnitRecord companyUnitRecord;

    public void setCompanyUnitRecord(CompanyUnitRecord companyUnitRecord) {
        this.companyUnitRecord = companyUnitRecord;
    }

/*

    @ManyToOne(targetEntity = ParticipantRecord.class)
    private ParticipantRecord participantRecordRecord;

    public void setParticipantRecordRecord(ParticipantRecord participantRecordRecord) {
        this.participantRecordRecord = participantRecordRecord;
    }*/

}
