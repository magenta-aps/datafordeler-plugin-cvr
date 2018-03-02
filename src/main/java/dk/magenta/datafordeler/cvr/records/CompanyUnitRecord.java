package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.DatabaseEntry;
import dk.magenta.datafordeler.core.database.Effect;
import dk.magenta.datafordeler.core.database.Registration;
import dk.magenta.datafordeler.core.exception.ParseException;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitEntity;
import org.hibernate.Session;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.*;

/**
 * Base record for CompanyUnit data, parsed from JSON into a tree of objects
 * with this class at the base.
 */
@Entity
@Table(name="cvr_record_companyunitrecord", indexes = {
        @Index(name = "cvr_record_companyunit_pnumber", columnList = CompanyUnitRecord.DB_FIELD_P_NUMBER),
        @Index(name = "cvr_record_companyunit_unitnumber", columnList = CompanyUnitRecord.DB_FIELD_UNITNUMBER),
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyUnitRecord extends CvrEntityRecord {

    public static final String DB_FIELD_P_NUMBER = "pNumber";
    public static final String IO_FIELD_P_NUMBER = "pNummer";

    @Column(name = DB_FIELD_P_NUMBER)
    @JsonProperty(value = IO_FIELD_P_NUMBER)
    private int pNumber;

    public int getpNumber() {
        return this.pNumber;
    }

    @JsonIgnore
    public Map<String, Object> getIdentifyingFilter() {
        return Collections.singletonMap(DB_FIELD_P_NUMBER, this.pNumber);
    }


    public static final String DB_FIELD_ADVERTPROTECTION = "advertProtection";
    public static final String IO_FIELD_ADVERTPROTECTION = "reklamebeskyttet";


    @Column(name = DB_FIELD_ADVERTPROTECTION)
    @JsonProperty(value = IO_FIELD_ADVERTPROTECTION)
    private boolean advertProtection;



    public static final String DB_FIELD_UNITNUMBER = "unitNumber";
    public static final String IO_FIELD_UNITNUMBER = "enhedsNummer";

    @Column(name = DB_FIELD_UNITNUMBER)
    @JsonProperty(value = IO_FIELD_UNITNUMBER)
    private long unitNumber;



    public static final String DB_FIELD_NAMES = "names";
    public static final String IO_FIELD_NAMES = "navne";

    @OneToMany(mappedBy = NameRecord.DB_FIELD_COMPANYUNIT, targetEntity = NameRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = IO_FIELD_NAMES)
    private Set<NameRecord> names;

    public Set<NameRecord> getNames() {
        return this.names;
    }

    public void setNames(Set<NameRecord> names) {
        this.names = names;
        for (NameRecord record : names) {
            record.setCompanyUnitRecord(this);
        }
    }

    public void addName(NameRecord record) {
        if (!this.names.contains(record)) {
            record.setSecondary(false);
            record.setCompanyUnitRecord(this);
            this.names.add(record);
        }
    }



    public static final String DB_FIELD_LOCATION_ADDRESS = "locationAddress";
    public static final String IO_FIELD_LOCATION_ADDRESS = "beliggenhedsadresse";

    @OneToMany(mappedBy = AddressRecord.DB_FIELD_COMPANYUNIT, targetEntity = AddressRecord.class, cascade = CascadeType.ALL)
    @Where(clause = AddressRecord.DB_FIELD_TYPE+"="+AddressRecord.TYPE_LOCATION)
    @Filters({
            @Filter(name = Registration.FILTER_REGISTRATION_TO, condition="("+CvrBitemporalRecord.DB_FIELD_LAST_UPDATED+" < :"+Registration.FILTERPARAM_REGISTRATION_TO+")"),
            @Filter(name = Effect.FILTER_EFFECT_FROM, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_TO+" >= :" + Effect.FILTERPARAM_EFFECT_FROM + " OR "+CvrRecordPeriod.DB_FIELD_VALID_TO+" is null)"),
            @Filter(name = Effect.FILTER_EFFECT_TO, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_FROM+" < :" + Effect.FILTERPARAM_EFFECT_TO + " OR "+CvrRecordPeriod.DB_FIELD_VALID_FROM+" is null)")
    })
    @JsonProperty(value = IO_FIELD_LOCATION_ADDRESS)
    private Set<AddressRecord> locationAddress;

    public void setLocationAddress(Set<AddressRecord> locationAddress) {
        for (AddressRecord record : locationAddress) {
            record.setType(AddressRecord.TYPE_LOCATION);
            record.setCompanyUnitRecord(this);
        }
        this.locationAddress = locationAddress;
    }

    public void addLocationAddress(AddressRecord record) {
        if (!this.locationAddress.contains(record)) {
            record.setType(AddressRecord.TYPE_LOCATION);
            record.setCompanyUnitRecord(this);
            this.locationAddress.add(record);
        }
    }

    public Set<AddressRecord> getLocationAddress() {
        return this.locationAddress;
    }



    public static final String DB_FIELD_POSTAL_ADDRESS = "postalAddress";
    public static final String IO_FIELD_POSTAL_ADDRESS = "postadresse";

    @OneToMany(mappedBy = AddressRecord.DB_FIELD_COMPANYUNIT, targetEntity = AddressRecord.class, cascade = CascadeType.ALL)
    @Where(clause = AddressRecord.DB_FIELD_TYPE+"="+AddressRecord.TYPE_POSTAL)
    @Filters({
            @Filter(name = Registration.FILTER_REGISTRATION_TO, condition="("+CvrBitemporalRecord.DB_FIELD_LAST_UPDATED+" < :"+Registration.FILTERPARAM_REGISTRATION_TO+")"),
            @Filter(name = Effect.FILTER_EFFECT_FROM, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_TO+" >= :" + Effect.FILTERPARAM_EFFECT_FROM + " OR "+CvrRecordPeriod.DB_FIELD_VALID_TO+" is null)"),
            @Filter(name = Effect.FILTER_EFFECT_TO, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_FROM+" < :" + Effect.FILTERPARAM_EFFECT_TO + " OR "+CvrRecordPeriod.DB_FIELD_VALID_FROM+" is null)")
    })@JsonProperty(value = IO_FIELD_POSTAL_ADDRESS)
    private Set<AddressRecord> postalAddress;

    public void setPostalAddress(Set<AddressRecord> postalAddress) {
        for (AddressRecord record : postalAddress) {
            record.setType(AddressRecord.TYPE_POSTAL);
            record.setCompanyUnitRecord(this);
        }
        this.postalAddress = postalAddress;
    }

    public void addPostalAddress(AddressRecord record) {
        if (!this.postalAddress.contains(record)) {
            record.setType(AddressRecord.TYPE_POSTAL);
            record.setCompanyUnitRecord(this);
            this.postalAddress.add(record);
        }
    }

    public Set<AddressRecord> getPostalAddress() {
        return this.postalAddress;
    }


    public static final String DB_FIELD_PHONE = "phoneNumber";
    public static final String IO_FIELD_PHONE = "telefonNummer";

    @OneToMany(mappedBy = ContactRecord.DB_FIELD_COMPANYUNIT, targetEntity = ContactRecord.class, cascade = CascadeType.ALL)
    @Where(clause = ContactRecord.DB_FIELD_TYPE+"="+ContactRecord.TYPE_TELEFONNUMMER)
    @Filters({
            @Filter(name = Registration.FILTER_REGISTRATION_TO, condition="("+CvrBitemporalRecord.DB_FIELD_LAST_UPDATED+" < :"+Registration.FILTERPARAM_REGISTRATION_TO+")"),
            @Filter(name = Effect.FILTER_EFFECT_FROM, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_TO+" >= :" + Effect.FILTERPARAM_EFFECT_FROM + " OR "+CvrRecordPeriod.DB_FIELD_VALID_TO+" is null)"),
            @Filter(name = Effect.FILTER_EFFECT_TO, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_FROM+" < :" + Effect.FILTERPARAM_EFFECT_TO + " OR "+CvrRecordPeriod.DB_FIELD_VALID_FROM+" is null)")
    })@JsonProperty(value = IO_FIELD_PHONE)
    private Set<ContactRecord> phoneNumber;

    public void setPhoneNumber(Set<ContactRecord> phoneNumber) {
        for (ContactRecord record : phoneNumber) {
            record.setType(ContactRecord.TYPE_TELEFONNUMMER);
            record.setCompanyUnitRecord(this);
        }
        this.phoneNumber = phoneNumber;
    }

    public void addPhoneNumber(ContactRecord record) {
        if (!this.phoneNumber.contains(record)) {
            record.setType(ContactRecord.TYPE_TELEFONNUMMER);
            record.setCompanyUnitRecord(this);
            record.setSecondary(false);
            this.phoneNumber.add(record);
        }
    }

    public Set<ContactRecord> getPhoneNumber() {
        return this.phoneNumber;
    }



    public static final String DB_FIELD_FAX = "faxNumber";
    public static final String IO_FIELD_FAX = "telefaxNummer";

    @OneToMany(mappedBy = ContactRecord.DB_FIELD_COMPANYUNIT, targetEntity = ContactRecord.class, cascade = CascadeType.ALL)
    @Where(clause = ContactRecord.DB_FIELD_TYPE+"="+ContactRecord.TYPE_TELEFAXNUMMER)
    @Filters({
            @Filter(name = Registration.FILTER_REGISTRATION_TO, condition="("+CvrBitemporalRecord.DB_FIELD_LAST_UPDATED+" < :"+Registration.FILTERPARAM_REGISTRATION_TO+")"),
            @Filter(name = Effect.FILTER_EFFECT_FROM, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_TO+" >= :" + Effect.FILTERPARAM_EFFECT_FROM + " OR "+CvrRecordPeriod.DB_FIELD_VALID_TO+" is null)"),
            @Filter(name = Effect.FILTER_EFFECT_TO, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_FROM+" < :" + Effect.FILTERPARAM_EFFECT_TO + " OR "+CvrRecordPeriod.DB_FIELD_VALID_FROM+" is null)")
    })@JsonProperty(value = IO_FIELD_FAX)
    private Set<ContactRecord> faxNumber;

    public void setFaxNumber(Set<ContactRecord> faxNumber) {
        for (ContactRecord record : faxNumber) {
            record.setType(ContactRecord.TYPE_TELEFAXNUMMER);
            record.setCompanyUnitRecord(this);
        }
        this.faxNumber = faxNumber;
    }

    public void addFaxNumber(ContactRecord record) {
        if (!this.faxNumber.contains(record)) {
            record.setType(ContactRecord.TYPE_TELEFAXNUMMER);
            record.setCompanyUnitRecord(this);
            record.setSecondary(false);
            this.faxNumber.add(record);
        }
    }

    public Set<ContactRecord> getFaxNumber() {
        return this.faxNumber;
    }



    public static final String DB_FIELD_EMAIL = "emailAddress";
    public static final String IO_FIELD_EMAIL = "elektroniskPost";

    @OneToMany(mappedBy = ContactRecord.DB_FIELD_COMPANYUNIT, targetEntity = ContactRecord.class, cascade = CascadeType.ALL)
    @Where(clause = ContactRecord.DB_FIELD_TYPE+"="+ContactRecord.TYPE_EMAILADRESSE)
    @Filters({
            @Filter(name = Registration.FILTER_REGISTRATION_TO, condition="("+CvrBitemporalRecord.DB_FIELD_LAST_UPDATED+" < :"+Registration.FILTERPARAM_REGISTRATION_TO+")"),
            @Filter(name = Effect.FILTER_EFFECT_FROM, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_TO+" >= :" + Effect.FILTERPARAM_EFFECT_FROM + " OR "+CvrRecordPeriod.DB_FIELD_VALID_TO+" is null)"),
            @Filter(name = Effect.FILTER_EFFECT_TO, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_FROM+" < :" + Effect.FILTERPARAM_EFFECT_TO + " OR "+CvrRecordPeriod.DB_FIELD_VALID_FROM+" is null)")
    })@JsonProperty(value = IO_FIELD_EMAIL)
    private Set<ContactRecord> emailAddress;

    public void setEmailAddress(Set<ContactRecord> emailAddress) {
        for (ContactRecord record : emailAddress) {
            record.setType(ContactRecord.TYPE_EMAILADRESSE);
            record.setCompanyUnitRecord(this);
        }
        this.emailAddress = emailAddress;
    }

    public void addEmailAddress(ContactRecord record) {
        if (!this.emailAddress.contains(record)) {
            record.setType(ContactRecord.TYPE_EMAILADRESSE);
            record.setCompanyUnitRecord(this);
            this.emailAddress.add(record);
        }
    }

    public Set<ContactRecord> getEmailAddress() {
        return this.emailAddress;
    }



    public static final String DB_FIELD_LIFECYCLE = "lifecycle";
    public static final String IO_FIELD_LIFECYCLE = "livsforloeb";

    @OneToMany(mappedBy = LifecycleRecord.DB_FIELD_COMPANYUNIT, targetEntity = LifecycleRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = IO_FIELD_LIFECYCLE)
    private Set<LifecycleRecord> lifecycle;

    public void setLifecycle(Set<LifecycleRecord> lifecycle) {
        this.lifecycle = lifecycle;
        for (LifecycleRecord lifecycleRecord : lifecycle) {
            lifecycleRecord.setCompanyUnitRecord(this);
        }
    }

    public void addLifecycle(LifecycleRecord record) {
        if (!this.lifecycle.contains(record)) {
            record.setCompanyUnitRecord(this);
            this.lifecycle.add(record);
        }
    }

    public Set<LifecycleRecord> getLifecycle() {
        return this.lifecycle;
    }



    public static final String DB_FIELD_PRIMARY_INDUSTRY = "primaryIndustry";
    public static final String IO_FIELD_PRIMARY_INDUSTRY = "hovedbranche";

    @OneToMany(mappedBy = CompanyIndustryRecord.DB_FIELD_COMPANYUNIT, targetEntity = CompanyIndustryRecord.class, cascade = CascadeType.ALL)
    @Where(clause = CompanyIndustryRecord.DB_FIELD_INDEX+"=0")
    @Filters({
            @Filter(name = Registration.FILTER_REGISTRATION_TO, condition="("+CvrBitemporalRecord.DB_FIELD_LAST_UPDATED+" < :"+Registration.FILTERPARAM_REGISTRATION_TO+")"),
            @Filter(name = Effect.FILTER_EFFECT_FROM, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_TO+" >= :" + Effect.FILTERPARAM_EFFECT_FROM + " OR "+CvrRecordPeriod.DB_FIELD_VALID_TO+" is null)"),
            @Filter(name = Effect.FILTER_EFFECT_TO, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_FROM+" < :" + Effect.FILTERPARAM_EFFECT_TO + " OR "+CvrRecordPeriod.DB_FIELD_VALID_FROM+" is null)")
    })
    @JsonProperty(value = IO_FIELD_PRIMARY_INDUSTRY)
    private Set<CompanyIndustryRecord> primaryIndustry;

    public void setPrimaryIndustry(Set<CompanyIndustryRecord> primaryIndustry) {
        for (CompanyIndustryRecord record : primaryIndustry) {
            record.setIndex(0);
            record.setCompanyUnitRecord(this);
        }
        this.primaryIndustry = primaryIndustry;
    }

    public void addPrimaryIndustry(CompanyIndustryRecord record) {
        if (!this.primaryIndustry.contains(record)) {
            record.setIndex(0);
            record.setCompanyUnitRecord(this);
            this.primaryIndustry.add(record);
        }
    }

    public Set<CompanyIndustryRecord> getPrimaryIndustry() {
        return this.primaryIndustry;
    }


    public static final String DB_FIELD_SECONDARY_INDUSTRY1 = "secondaryIndustry1";
    public static final String IO_FIELD_SECONDARY_INDUSTRY1 = "bibranche1";

    @OneToMany(mappedBy = CompanyIndustryRecord.DB_FIELD_COMPANYUNIT, targetEntity = CompanyIndustryRecord.class, cascade = CascadeType.ALL)
    @Where(clause = CompanyIndustryRecord.DB_FIELD_INDEX+"=1")
    @Filters({
            @Filter(name = Registration.FILTER_REGISTRATION_TO, condition="("+CvrBitemporalRecord.DB_FIELD_LAST_UPDATED+" < :"+Registration.FILTERPARAM_REGISTRATION_TO+")"),
            @Filter(name = Effect.FILTER_EFFECT_FROM, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_TO+" >= :" + Effect.FILTERPARAM_EFFECT_FROM + " OR "+CvrRecordPeriod.DB_FIELD_VALID_TO+" is null)"),
            @Filter(name = Effect.FILTER_EFFECT_TO, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_FROM+" < :" + Effect.FILTERPARAM_EFFECT_TO + " OR "+CvrRecordPeriod.DB_FIELD_VALID_FROM+" is null)")
    })@JsonProperty(value = IO_FIELD_SECONDARY_INDUSTRY1)
    private Set<CompanyIndustryRecord> secondaryIndustry1;

    public void setSecondaryIndustry1(Set<CompanyIndustryRecord> secondaryIndustryRecords) {
        for (CompanyIndustryRecord record : secondaryIndustryRecords) {
            record.setIndex(1);
            record.setCompanyUnitRecord(this);
        }
        this.secondaryIndustry1 = secondaryIndustryRecords;
    }

    public void addSecondaryIndustry1(CompanyIndustryRecord record) {
        if (!this.secondaryIndustry1.contains(record)) {
            record.setIndex(1);
            record.setCompanyUnitRecord(this);
            this.secondaryIndustry1.add(record);
        }
    }

    public Set<CompanyIndustryRecord> getSecondaryIndustry1() {
        return this.secondaryIndustry1;
    }



    public static final String DB_FIELD_SECONDARY_INDUSTRY2 = "secondaryIndustry2";
    public static final String IO_FIELD_SECONDARY_INDUSTRY2 = "bibranche2";

    @OneToMany(mappedBy = CompanyIndustryRecord.DB_FIELD_COMPANYUNIT, targetEntity = CompanyIndustryRecord.class, cascade = CascadeType.ALL)
    @Where(clause = CompanyIndustryRecord.DB_FIELD_INDEX+"=2")
    @Filters({
            @Filter(name = Registration.FILTER_REGISTRATION_TO, condition="("+CvrBitemporalRecord.DB_FIELD_LAST_UPDATED+" < :"+Registration.FILTERPARAM_REGISTRATION_TO+")"),
            @Filter(name = Effect.FILTER_EFFECT_FROM, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_TO+" >= :" + Effect.FILTERPARAM_EFFECT_FROM + " OR "+CvrRecordPeriod.DB_FIELD_VALID_TO+" is null)"),
            @Filter(name = Effect.FILTER_EFFECT_TO, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_FROM+" < :" + Effect.FILTERPARAM_EFFECT_TO + " OR "+CvrRecordPeriod.DB_FIELD_VALID_FROM+" is null)")
    })
    @JsonProperty(value = IO_FIELD_SECONDARY_INDUSTRY2)
    private Set<CompanyIndustryRecord> secondaryIndustry2;

    public void setSecondaryIndustry2(Set<CompanyIndustryRecord> secondaryIndustryRecords) {
        for (CompanyIndustryRecord record : secondaryIndustryRecords) {
            record.setIndex(2);
            record.setCompanyUnitRecord(this);
        }
        this.secondaryIndustry2 = secondaryIndustryRecords;
    }

    public void addSecondaryIndustry2(CompanyIndustryRecord record) {
        if (!this.secondaryIndustry2.contains(record)) {
            record.setIndex(2);
            record.setCompanyUnitRecord(this);
            this.secondaryIndustry2.add(record);
        }
    }

    public Set<CompanyIndustryRecord> getSecondaryIndustry2() {
        return this.secondaryIndustry2;
    }



    public static final String DB_FIELD_SECONDARY_INDUSTRY3 = "secondaryIndustry3";
    public static final String IO_FIELD_SECONDARY_INDUSTRY3 = "bibranche3";

    @OneToMany(mappedBy = CompanyIndustryRecord.DB_FIELD_COMPANYUNIT, targetEntity = CompanyIndustryRecord.class, cascade = CascadeType.ALL)
    @Where(clause = CompanyIndustryRecord.DB_FIELD_INDEX+"=3")
    @Filters({
            @Filter(name = Registration.FILTER_REGISTRATION_TO, condition="("+CvrBitemporalRecord.DB_FIELD_LAST_UPDATED+" < :"+Registration.FILTERPARAM_REGISTRATION_TO+")"),
            @Filter(name = Effect.FILTER_EFFECT_FROM, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_TO+" >= :" + Effect.FILTERPARAM_EFFECT_FROM + " OR "+CvrRecordPeriod.DB_FIELD_VALID_TO+" is null)"),
            @Filter(name = Effect.FILTER_EFFECT_TO, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_FROM+" < :" + Effect.FILTERPARAM_EFFECT_TO + " OR "+CvrRecordPeriod.DB_FIELD_VALID_FROM+" is null)")
    })
    @JsonProperty(value = IO_FIELD_SECONDARY_INDUSTRY3)
    private Set<CompanyIndustryRecord> secondaryIndustry3;

    public void setSecondaryIndustry3(Set<CompanyIndustryRecord> secondaryIndustryRecords) {
        for (CompanyIndustryRecord record : secondaryIndustryRecords) {
            record.setIndex(3);
            record.setCompanyUnitRecord(this);
        }
        this.secondaryIndustry3 = secondaryIndustryRecords;
    }

    public void addSecondaryIndustry3(CompanyIndustryRecord record) {
        if (!this.secondaryIndustry3.contains(record)) {
            record.setIndex(3);
            record.setCompanyUnitRecord(this);
            this.secondaryIndustry3.add(record);
        }
    }

    public Set<CompanyIndustryRecord> getSecondaryIndustry3() {
        return this.secondaryIndustry3;
    }



    public static final String DB_FIELD_YEARLY_NUMBERS = "yearlyNumbers";
    public static final String IO_FIELD_YEARLY_NUMBERS = "aarsbeskaeftigelse";

    @OneToMany(mappedBy = CompanyYearlyNumbersRecord.DB_FIELD_COMPANYUNIT, targetEntity = CompanyYearlyNumbersRecord.class, cascade = CascadeType.ALL)
    @Filters({
            @Filter(name = Registration.FILTER_REGISTRATION_TO, condition="("+CvrBitemporalRecord.DB_FIELD_LAST_UPDATED+" < :"+Registration.FILTERPARAM_REGISTRATION_TO+")"),
            @Filter(name = Effect.FILTER_EFFECT_FROM, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_TO+" >= :" + Effect.FILTERPARAM_EFFECT_FROM + " OR "+CvrRecordPeriod.DB_FIELD_VALID_TO+" is null)"),
            @Filter(name = Effect.FILTER_EFFECT_TO, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_FROM+" < :" + Effect.FILTERPARAM_EFFECT_TO + " OR "+CvrRecordPeriod.DB_FIELD_VALID_FROM+" is null)")
    })
    @JsonProperty(value = IO_FIELD_YEARLY_NUMBERS)
    private Set<CompanyYearlyNumbersRecord> yearlyNumbers;

    public void setYearlyNumbers(Set<CompanyYearlyNumbersRecord> yearlyNumbers) {
        this.yearlyNumbers = yearlyNumbers;
        for (CompanyYearlyNumbersRecord yearlyNumbersRecord : yearlyNumbers) {
            yearlyNumbersRecord.setCompanyUnitRecord(this);
        }
    }

    public void addYearlyNumbers(CompanyYearlyNumbersRecord record) {
        if (!this.yearlyNumbers.contains(record)) {
            record.setCompanyUnitRecord(this);
            this.yearlyNumbers.add(record);
        }
    }

    public Set<CompanyYearlyNumbersRecord> getYearlyNumbers() {
        return this.yearlyNumbers;
    }



    public static final String DB_FIELD_QUARTERLY_NUMBERS = "quarterlyNumbers";
    public static final String IO_FIELD_QUARTERLY_NUMBERS = "kvartalsbeskaeftigelse";

    @OneToMany(mappedBy = CompanyQuarterlyNumbersRecord.DB_FIELD_COMPANYUNIT, targetEntity = CompanyQuarterlyNumbersRecord.class, cascade = CascadeType.ALL)
    @Filters({
            @Filter(name = Registration.FILTER_REGISTRATION_TO, condition="("+CvrBitemporalRecord.DB_FIELD_LAST_UPDATED+" < :"+Registration.FILTERPARAM_REGISTRATION_TO+")"),
            @Filter(name = Effect.FILTER_EFFECT_FROM, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_TO+" >= :" + Effect.FILTERPARAM_EFFECT_FROM + " OR "+CvrRecordPeriod.DB_FIELD_VALID_TO+" is null)"),
            @Filter(name = Effect.FILTER_EFFECT_TO, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_FROM+" < :" + Effect.FILTERPARAM_EFFECT_TO + " OR "+CvrRecordPeriod.DB_FIELD_VALID_FROM+" is null)")
    })
    @JsonProperty(value = IO_FIELD_QUARTERLY_NUMBERS)
    private Set<CompanyQuarterlyNumbersRecord> quarterlyNumbers;

    public void setQuarterlyNumbers(Set<CompanyQuarterlyNumbersRecord> quarterlyNumbers) {
        this.quarterlyNumbers = quarterlyNumbers;
        for (CompanyQuarterlyNumbersRecord quarterlyNumbersRecord : quarterlyNumbers) {
            quarterlyNumbersRecord.setCompanyUnitRecord(this);
        }
    }

    public void addQuarterlyNumbers(CompanyQuarterlyNumbersRecord record) {
        if (!this.quarterlyNumbers.contains(record)) {
            record.setCompanyUnitRecord(this);
            this.quarterlyNumbers.add(record);
        }
    }

    public Set<CompanyQuarterlyNumbersRecord> getQuarterlyNumbers() {
        return this.quarterlyNumbers;
    }



    //public static final String DB_FIELD_MONTHLY_NUMBERS = "monthlyNumbers";
    //public static final String IO_FIELD_MONTHLY_NUMBERS = "maanedsbeskaeftigelse";

    //@OneToMany(mappedBy = CompanyMonthlyNumbersRecord.DB_FIELD_COMPANY, cascade = CascadeType.ALL)
    //@JsonProperty(value = "maanedsbeskaeftigelse")
    //private Set<CompanyMonthlyNumbersRecord> monthlyNumbers;


    public static final String DB_FIELD_ATTRIBUTES = "attributes";
    public static final String IO_FIELD_ATTRIBUTES = "attributter";

    @OneToMany(mappedBy = AttributeRecord.DB_FIELD_COMPANYUNIT, targetEntity = AttributeRecord.class, cascade = CascadeType.ALL)
    @Filters({
            @Filter(name = Registration.FILTER_REGISTRATION_TO, condition="("+CvrBitemporalRecord.DB_FIELD_LAST_UPDATED+" < :"+Registration.FILTERPARAM_REGISTRATION_TO+")"),
            @Filter(name = Effect.FILTER_EFFECT_FROM, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_TO+" >= :" + Effect.FILTERPARAM_EFFECT_FROM + " OR "+CvrRecordPeriod.DB_FIELD_VALID_TO+" is null)"),
            @Filter(name = Effect.FILTER_EFFECT_TO, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_FROM+" < :" + Effect.FILTERPARAM_EFFECT_TO + " OR "+CvrRecordPeriod.DB_FIELD_VALID_FROM+" is null)")
    })
    @JsonProperty(value = IO_FIELD_ATTRIBUTES)
    private Set<AttributeRecord> attributes;

    public void setAttributes(Set<AttributeRecord> attributes) {
        this.attributes = attributes;
        for (AttributeRecord attributeRecord : attributes) {
            attributeRecord.setCompanyUnitRecord(this);
        }
    }

    public void addAttributes(AttributeRecord record) {
        if (!this.attributes.contains(record)) {
            record.setCompanyUnitRecord(this);
            this.attributes.add(record);
        }
    }

    public Set<AttributeRecord> getAttributes() {
        return this.attributes;
    }



    @OneToMany(mappedBy = CompanyParticipantRelationRecord.DB_FIELD_COMPANYUNIT, targetEntity = CompanyParticipantRelationRecord.class, cascade = CascadeType.ALL)
    @Filters({
            @Filter(name = Registration.FILTER_REGISTRATION_TO, condition="("+CvrBitemporalRecord.DB_FIELD_LAST_UPDATED+" < :"+Registration.FILTERPARAM_REGISTRATION_TO+")"),
            @Filter(name = Effect.FILTER_EFFECT_FROM, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_TO+" >= :" + Effect.FILTERPARAM_EFFECT_FROM + " OR "+CvrRecordPeriod.DB_FIELD_VALID_TO+" is null)"),
            @Filter(name = Effect.FILTER_EFFECT_TO, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_FROM+" < :" + Effect.FILTERPARAM_EFFECT_TO + " OR "+CvrRecordPeriod.DB_FIELD_VALID_FROM+" is null)")
    })
    @JsonProperty(value = "deltagerRelation")
    private Set<CompanyParticipantRelationRecord> participantRelations;

    public void setParticipantRelations(Set<CompanyParticipantRelationRecord> participantRelations) {
        this.participantRelations = participantRelations;
        for (CompanyParticipantRelationRecord participantRelationRecord : participantRelations) {
            participantRelationRecord.setCompanyUnitRecord(this);
        }
    }

    public void addParticipant(CompanyParticipantRelationRecord record) {
        if (!this.participantRelations.contains(record)) {
            record.setCompanyUnitRecord(this);
            this.participantRelations.add(record);
        }
    }

    public Set<CompanyParticipantRelationRecord> getParticipants() {
        return this.participantRelations;
    }



    public static final String DB_FIELD_COMPANY_LINK = "companyLinkRecords";
    public static final String IO_FIELD_COMPANY_LINK = "virksomhedsrelation";

    @OneToMany(mappedBy = CompanyLinkRecord.DB_FIELD_COMPANYUNIT, targetEntity = CompanyLinkRecord.class, cascade = CascadeType.ALL)
    @Filters({
            @Filter(name = Registration.FILTER_REGISTRATION_TO, condition="("+CvrBitemporalRecord.DB_FIELD_LAST_UPDATED+" < :"+Registration.FILTERPARAM_REGISTRATION_TO+")"),
            @Filter(name = Effect.FILTER_EFFECT_FROM, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_TO+" >= :" + Effect.FILTERPARAM_EFFECT_FROM + " OR "+CvrRecordPeriod.DB_FIELD_VALID_TO+" is null)"),
            @Filter(name = Effect.FILTER_EFFECT_TO, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_FROM+" < :" + Effect.FILTERPARAM_EFFECT_TO + " OR "+CvrRecordPeriod.DB_FIELD_VALID_FROM+" is null)")
    })
    @JsonProperty(value = IO_FIELD_COMPANY_LINK)
    private Set<CompanyLinkRecord> companyLinkRecords;

    public void setCompanyLinkRecords(Set<CompanyLinkRecord> companyLinkRecords) {
        this.companyLinkRecords = companyLinkRecords;
        for (CompanyLinkRecord companyLinkRecord : companyLinkRecords) {
            companyLinkRecord.setCompanyUnitRecord(this);
        }
    }

    public void addCompanyLinkRecord(CompanyLinkRecord record) {
        if (!this.companyLinkRecords.contains(record)) {
            record.setCompanyUnitRecord(this);
            this.companyLinkRecords.add(record);
        }
    }

    public Set<CompanyLinkRecord> getCompanyLinkRecords() {
        return this.companyLinkRecords;
    }



    public static final String DB_FIELD_META = "metadata";
    public static final String IO_FIELD_META = "produktionsEnhedMetadata";

    @OneToOne(mappedBy = CompanyUnitMetadataRecord.DB_FIELD_COMPANYUNIT, targetEntity = CompanyUnitMetadataRecord.class, cascade = CascadeType.ALL)
    @JoinColumn(name = DB_FIELD_META + DatabaseEntry.REF)
    @Filters({
            @Filter(name = Registration.FILTER_REGISTRATION_TO, condition="("+CvrBitemporalRecord.DB_FIELD_LAST_UPDATED+" < :"+Registration.FILTERPARAM_REGISTRATION_TO+")"),
            @Filter(name = Effect.FILTER_EFFECT_FROM, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_TO+" >= :" + Effect.FILTERPARAM_EFFECT_FROM + " OR "+CvrRecordPeriod.DB_FIELD_VALID_TO+" is null)"),
            @Filter(name = Effect.FILTER_EFFECT_TO, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_FROM+" < :" + Effect.FILTERPARAM_EFFECT_TO + " OR "+CvrRecordPeriod.DB_FIELD_VALID_FROM+" is null)")
    })
    @JsonProperty(value = IO_FIELD_META)
    private CompanyUnitMetadataRecord metadata;

    public void setMetadata(CompanyUnitMetadataRecord metadata) {
        this.metadata = metadata;
        this.metadata.setCompanyUnitRecord(this);
    }

    public CompanyUnitMetadataRecord getMetadata() {
        return this.metadata;
    }



    public static final String DB_FIELD_INDUSTRY_RESPONSIBILITY_CODE = "industryResponsibilityCode";
    public static final String IO_FIELD_INDUSTRY_RESPONSIBILITY_CODE = "brancheAnsvarskode";

    @Column(name = DB_FIELD_INDUSTRY_RESPONSIBILITY_CODE, nullable = true)
    @JsonProperty(value = IO_FIELD_INDUSTRY_RESPONSIBILITY_CODE)
    private Integer industryResponsibilityCode;





    public static final String DB_FIELD_UNITTYPE = "unitType";
    public static final String IO_FIELD_UNITTYPE = "enhedstype";

    @Column(name = DB_FIELD_UNITTYPE)
    @JsonProperty(value = IO_FIELD_UNITTYPE)
    private String unitType;



    // enhedstype
    // dataAdgang
    // unitNumber
    // advertProtection
    // deltagere
    // virkningsAktoer
    // brancheAnsvarskode
    // naermesteFremtidigeDato
    // samtId
    @JsonIgnore
    public List<CvrRecord> getAll() {
        ArrayList<CvrRecord> list = new ArrayList<>();
        if (this.names != null) {
            list.addAll(this.names);
        }
        if (this.locationAddress != null) {
            list.addAll(this.locationAddress);
        }
        if (this.postalAddress != null) {
            list.addAll(this.postalAddress);
        }
        if (this.phoneNumber != null) {
            list.addAll(this.phoneNumber);
        }
        if (this.faxNumber != null) {
            list.addAll(this.faxNumber);
        }
        if (this.emailAddress != null) {
            list.addAll(this.emailAddress);
        }
        if (this.lifecycle != null) {
            list.addAll(this.lifecycle);
        }
        if (this.primaryIndustry != null) {
            list.addAll(this.primaryIndustry);
        }
        if (this.secondaryIndustry1 != null) {
            list.addAll(this.secondaryIndustry1);
        }
        if (this.secondaryIndustry2 != null) {
            list.addAll(this.secondaryIndustry2);
        }
        if (this.secondaryIndustry3 != null) {
            list.addAll(this.secondaryIndustry3);
        }
        if (this.yearlyNumbers != null) {
            list.addAll(this.yearlyNumbers);
        }
        if (this.quarterlyNumbers != null) {
            list.addAll(this.quarterlyNumbers);
        }
        if (this.attributes != null) {
            for (AttributeRecord attributeRecord : this.attributes) {
                list.addAll(attributeRecord.getValues());
            }
        }
        if (this.participantRelations != null) {
            list.addAll(this.participantRelations);
        }
        if (this.companyLinkRecords != null) {
            list.addAll(this.companyLinkRecords);
        }
        return list;
    }


    public UUID generateUUID() {
        return CompanyUnitEntity.generateUUID(this.pNumber);
    }

    @Override
    public void populateBaseData(CompanyUnitBaseData baseData, Session session) throws ParseException {
        baseData.setAdvertProtection(this.advertProtection);
        baseData.setPNumber(this.unitNumber);
    }

    @Override
    public void save(Session session) {
        for (AddressRecord address : this.locationAddress) {
            address.wire(session);
        }
        for (AddressRecord address : this.postalAddress) {
            address.wire(session);
        }
        if (this.metadata != null) {
            this.metadata.wire(session);
        }
        super.save(session);
    }


    @Override
    public boolean merge(CvrEntityRecord other) {
        if (other != null && !other.getId().equals(this.getId()) && other instanceof CompanyUnitRecord) {
            CompanyUnitRecord existing = (CompanyUnitRecord) other;
            for (NameRecord nameRecord : this.getNames()) {
                existing.addName(nameRecord);
            }
//            for (NameRecord nameRecord : this.getSecondaryNames()) {
//                existing.addSecondaryName(nameRecord);
//            }
            for (AddressRecord addressRecord : this.getLocationAddress()) {
                existing.addLocationAddress(addressRecord);
            }
            for (AddressRecord addressRecord : this.getPostalAddress()) {
                existing.addPostalAddress(addressRecord);
            }
            for (ContactRecord contactRecord : this.getPhoneNumber()) {
                existing.addPhoneNumber(contactRecord);
            }
//            for (ContactRecord contactRecord : this.getSecondaryPhoneNumber()) {
//                existing.addSecondaryPhoneNumber(contactRecord);
//            }
            for (ContactRecord contactRecord : this.getFaxNumber()) {
                existing.addFaxNumber(contactRecord);
            }
//            for (ContactRecord contactRecord : this.getSecondaryFaxNumber()) {
//                existing.addSecondaryFaxNumber(contactRecord);
//            }
            for (ContactRecord contactRecord : this.getEmailAddress()) {
                existing.addEmailAddress(contactRecord);
            }
            for (LifecycleRecord lifecycleRecord : this.getLifecycle()) {
                existing.addLifecycle(lifecycleRecord);
            }
            for (CompanyIndustryRecord companyIndustryRecord : this.getPrimaryIndustry()) {
                existing.addPrimaryIndustry(companyIndustryRecord);
            }
            for (CompanyIndustryRecord companyIndustryRecord : this.getSecondaryIndustry1()) {
                existing.addSecondaryIndustry1(companyIndustryRecord);
            }
            for (CompanyIndustryRecord companyIndustryRecord : this.getSecondaryIndustry2()) {
                existing.addSecondaryIndustry2(companyIndustryRecord);
            }
            for (CompanyIndustryRecord companyIndustryRecord : this.getSecondaryIndustry3()) {
                existing.addSecondaryIndustry3(companyIndustryRecord);
            }
            for (CompanyYearlyNumbersRecord yearlyNumbersRecord : this.getYearlyNumbers()) {
                existing.addYearlyNumbers(yearlyNumbersRecord);
            }
            for (CompanyQuarterlyNumbersRecord quarterlyNumbersRecord : this.getQuarterlyNumbers()) {
                existing.addQuarterlyNumbers(quarterlyNumbersRecord);
            }
            for (AttributeRecord attributeRecord : this.getAttributes()) {
                existing.addAttributes(attributeRecord);
            }
            for (CompanyParticipantRelationRecord participantRelationRecord : this.getParticipants()) {
                existing.addParticipant(participantRelationRecord);
            }
            this.metadata.merge(existing.getMetadata());
            return true;
        }
        return false;
    }

}
