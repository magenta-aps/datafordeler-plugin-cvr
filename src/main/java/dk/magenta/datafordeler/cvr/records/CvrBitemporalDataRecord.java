package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.magenta.datafordeler.core.database.Bitemporal;
import dk.magenta.datafordeler.core.database.DatabaseEntry;
import dk.magenta.datafordeler.cvr.data.CvrEntity;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.time.OffsetDateTime;

@MappedSuperclass
public abstract class CvrBitemporalDataRecord extends CvrBitemporalRecord implements Bitemporal<CvrEntityRecord> {


    public static final String DB_FIELD_COMPANY = "companyRecord";

    @JsonIgnore
    @ManyToOne(targetEntity = CompanyRecord.class, fetch = FetchType.LAZY)
    @JoinColumn(name = DB_FIELD_COMPANY + DatabaseEntry.REF)
    private CompanyRecord companyRecord;

    public void setCompanyRecord(CompanyRecord companyRecord) {
        this.companyRecord = companyRecord;
    }

    public CompanyRecord getCompanyRecord() {
        return this.companyRecord;
    }



    public static final String DB_FIELD_COMPANYUNIT = "companyUnitRecord";

    @JsonIgnore
    @ManyToOne(targetEntity = CompanyUnitRecord.class, fetch = FetchType.LAZY)
    @JoinColumn(name = DB_FIELD_COMPANYUNIT + DatabaseEntry.REF)
    private CompanyUnitRecord companyUnitRecord;

    public void setCompanyUnitRecord(CompanyUnitRecord companyUnitRecord) {
        this.companyUnitRecord = companyUnitRecord;
    }



    public static final String DB_FIELD_PARTICIPANT = "participantRecord";

    @JsonIgnore
    @ManyToOne(targetEntity = ParticipantRecord.class, fetch = FetchType.LAZY)
    @JoinColumn(name = DB_FIELD_PARTICIPANT + DatabaseEntry.REF)
    private ParticipantRecord participantRecord;

    public void setParticipantRecord(ParticipantRecord participantRecord) {
        this.participantRecord = participantRecord;
    }

    @Override
    public CvrEntityRecord getEntity() {
        return this.companyRecord;
    }

    @Override
    public void setEntity(CvrEntityRecord cvrEntityRecord) {
        if (cvrEntityRecord instanceof CompanyRecord) {
            this.companyRecord = (CompanyRecord) cvrEntityRecord;
        }
    }


    @Override
    public CvrBitemporalDataRecord setRegistrationFrom(OffsetDateTime registrationFrom) {
        super.setRegistrationFrom(registrationFrom);
        return this;
    }

    public CvrBitemporalDataRecord setRegistrationTo(OffsetDateTime registrationTo) {
        super.setRegistrationTo(registrationTo);
        return this;
    }

    @Override
    public CvrBitemporalDataRecord setDafoUpdated(OffsetDateTime dafoUpdated) {
        super.setDafoUpdated(dafoUpdated);
        return this;
    }

    /*@Override
    public boolean equalData(Object o) {
        if (this == o) return true;
        return o != null && getClass() == o.getClass();
    }*/
}
