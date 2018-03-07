package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Record for one participant on a Company or CompanyUnit
 */
@Entity
@Table(name = RelationCompanyRecord.TABLE_NAME)
public class RelationCompanyRecord extends CvrBitemporalRecord {

    public static final String TABLE_NAME = CompanyParticipantRelationRecord.TABLE_NAME + "_company";

    public static final String DB_FIELD_UNITNUMBER = "unitNumber";
    public static final String IO_FIELD_UNITNUMBER = "enhedsNummer";

    @Column(name = DB_FIELD_UNITNUMBER)
    @JsonProperty(value = IO_FIELD_UNITNUMBER)
    public long unitNumber;



    public static final String DB_FIELD_UNITTYPE = "unitType";
    public static final String IO_FIELD_UNITTYPE = "enhedstype";

    @Column(name = DB_FIELD_UNITTYPE)
    @JsonProperty(value = IO_FIELD_UNITTYPE)
    public String unitType;



    public static final String DB_FIELD_CVRNUMBER = "cvrNumber";
    public static final String IO_FIELD_CVRNUMBER = "cvrNummer";

    @Column(name = DB_FIELD_CVRNUMBER)
    @JsonProperty(value = IO_FIELD_CVRNUMBER)
    public long cvrNumber;




    public static final String DB_FIELD_REGISTER_ERROR = "registerError";
    public static final String IO_FIELD_REGISTER_ERROR = "fejlRegistreret";

    @Column(name = DB_FIELD_REGISTER_ERROR)
    @JsonProperty(value = IO_FIELD_REGISTER_ERROR)
    private boolean registerError;



    public static final String DB_FIELD_REG_NUMBER = "regNumber";
    public static final String IO_FIELD_REG_NUMBER = "regNummer";

    @OneToMany(mappedBy = CompanyRegNumberRecord.DB_FIELD_PARTICIPANT_COMPANY_RELATION, targetEntity = CompanyRegNumberRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = IO_FIELD_REG_NUMBER)
    private Set<CompanyRegNumberRecord> regNumber;

    public void setRegNumber(Set<CompanyRegNumberRecord> regNumber) {
        this.regNumber = regNumber;
        for (CompanyRegNumberRecord regNumberRecord : regNumber) {
            regNumberRecord.setParticipantRelationCompanyRecord(this);
        }
    }

    public void addRegNumber(CompanyRegNumberRecord record) {
        if (!this.regNumber.contains(record)) {
            record.setParticipantRelationCompanyRecord(this);
            this.regNumber.add(record);
        }
    }



    public static final String DB_FIELD_NAMES = "names";
    public static final String IO_FIELD_NAMES = "navne";

    @OneToMany(mappedBy = BaseNameRecord.DB_FIELD_COMPANY_RELATION, targetEntity = BaseNameRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = IO_FIELD_NAMES)
    private Set<BaseNameRecord> names = new HashSet<>();

    public Set<BaseNameRecord> getNames() {
        return this.names;
    }

    public void setNames(Set<BaseNameRecord> names) {
        this.names = names;
        for (BaseNameRecord record : names) {
            record.setRelationCompanyRecord(this);
        }
    }

    public void addName(BaseNameRecord record) {
        if (!this.names.contains(record)) {
            record.setRelationCompanyRecord(this);
            this.names.add(record);
        }
    }



    public static final String DB_FIELD_LIFECYCLE = "lifecycle";
    public static final String IO_FIELD_LIFECYCLE = "livsforloeb";

    @OneToMany(targetEntity = LifecycleRecord.class, mappedBy = LifecycleRecord.DB_FIELD_PARTICIPANT_COMPANY_RELATION, cascade = CascadeType.ALL)
    @JsonProperty(value = IO_FIELD_LIFECYCLE)
    private Set<LifecycleRecord> lifecycle = new HashSet<>();

    public void setLifecycle(Set<LifecycleRecord> lifecycle) {
        this.lifecycle = lifecycle;
        for (LifecycleRecord lifecycleRecord : lifecycle) {
            lifecycleRecord.setRelationCompanyRecord(this);
        }
    }

    public void addLifecycle(LifecycleRecord record) {
        if (!this.lifecycle.contains(record)) {
            record.setRelationCompanyRecord(this);
            this.lifecycle.add(record);
        }
    }

    public Set<LifecycleRecord> getLifecycle() {
        return this.lifecycle;
    }




    public static final String DB_FIELD_STATUS = "status";
    public static final String IO_FIELD_STATUS = "status";

    @OneToMany(mappedBy = CompanyStatusRecord.DB_FIELD_COMPANY, targetEntity = StatusRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = IO_FIELD_STATUS)
    private Set<StatusRecord> status = new HashSet<>();

    public void setStatus(Set<StatusRecord> status) {
        this.status = status;
        for (StatusRecord statusRecord : status) {
            statusRecord.setRelationCompanyRecord(this);
        }
    }

    public void addStatus(StatusRecord record) {
        if (!this.status.contains(record)) {
            record.setRelationCompanyRecord(this);
            this.status.add(record);
        }
    }

    public Set<StatusRecord> getStatus() {
        return this.status;
    }



    public static final String DB_FIELD_COMPANYSTATUS = "companyStatus";
    public static final String IO_FIELD_COMPANYSTATUS = "virksomhedsstatus";

    @OneToMany(mappedBy = CompanyStatusRecord.DB_FIELD_COMPANY, targetEntity = CompanyStatusRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = IO_FIELD_COMPANYSTATUS)
    private Set<CompanyStatusRecord> companyStatus = new HashSet<>();


    public void setCompanyStatus(Set<CompanyStatusRecord> companyStatus) {
        this.companyStatus = companyStatus;
        for (CompanyStatusRecord statusRecord : companyStatus) {
            statusRecord.setRelationCompanyRecord(this);
        }
    }

    public void addCompanyStatus(CompanyStatusRecord record) {
        if (!this.companyStatus.contains(record)) {
            record.setRelationCompanyRecord(this);
            this.companyStatus.add(record);
        }
    }

    public Set<CompanyStatusRecord> getCompanyStatus() {
        return this.companyStatus;
    }




    public static final String DB_FIELD_FORM = "companyForm";
    public static final String IO_FIELD_FORM = "virksomhedsform";

    @OneToMany(mappedBy = FormRecord.DB_FIELD_COMPANY, targetEntity = FormRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = IO_FIELD_FORM)
    private Set<FormRecord> companyForm = new HashSet<>();

    public void setCompanyForm(Set<FormRecord> companyForm) {
        this.companyForm = companyForm;
        for (FormRecord formRecord : companyForm) {
            formRecord.setParticipantRelationCompanyRecord(this);
        }
    }

    public void addCompanyForm(FormRecord record) {
        if (!this.companyForm.contains(record)) {
            record.setParticipantRelationCompanyRecord(this);
            this.companyForm.add(record);
        }
    }

    public Set<FormRecord> getCompanyForm() {
        return this.companyForm;
    }





    @OneToOne(targetEntity = CompanyParticipantRelationRecord.class, mappedBy = CompanyParticipantRelationRecord.DB_FIELD_COMPANY_RELATION)
    @JsonIgnore
    private CompanyParticipantRelationRecord companyParticipantRelationRecord;

    public void setCompanyParticipantRelationRecord(CompanyParticipantRelationRecord companyParticipantRelationRecord) {
        this.companyParticipantRelationRecord = companyParticipantRelationRecord;
    }



    public UUID generateUUID() {
        String uuidInput = "participant:"+this.unitType+"/"+this.unitNumber;
        return UUID.nameUUIDFromBytes(uuidInput.getBytes());
    }

    @Override
    public OffsetDateTime getRegistrationFrom() {
        OffsetDateTime registrationFrom = super.getRegistrationFrom();
        if (registrationFrom == null) {
            registrationFrom = this.getLastUpdated();
        }
        return registrationFrom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RelationCompanyRecord that = (RelationCompanyRecord) o;
        return unitNumber == that.unitNumber &&
                cvrNumber == that.cvrNumber &&
                Objects.equals(unitType, that.unitType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), unitNumber, unitType, cvrNumber);
    }
}
