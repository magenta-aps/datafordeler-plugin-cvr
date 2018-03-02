package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.DatabaseEntry;
import dk.magenta.datafordeler.core.database.Effect;
import dk.magenta.datafordeler.core.database.Registration;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.company.CompanyEntity;
import org.hibernate.Session;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.*;

/**
 * Base record for Company data, parsed from JSON into a tree of objects
 * with this class at the base.
 */
@Entity
@Table(name="cvr_record_companyrecord", indexes = {
        @Index(name = "cvr_record_company_cvrnumber", columnList = CompanyRecord.DB_FIELD_CVR_NUMBER),
        @Index(name = "cvr_record_company_advertprotection", columnList = CompanyRecord.DB_FIELD_ADVERTPROTECTION)
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyRecord extends CvrEntityRecord {

    public static final String DB_FIELD_CVR_NUMBER = "cvrNumber";
    public static final String IO_FIELD_CVR_NUMBER = "cvrNummer";

    @Column(name = DB_FIELD_CVR_NUMBER)
    @JsonProperty(value = IO_FIELD_CVR_NUMBER)
    private int cvrNumber;

    public int getCvrNumber() {
        return this.cvrNumber;
    }

    @JsonIgnore
    public Map<String, Object> getIdentifyingFilter() {
        return Collections.singletonMap(DB_FIELD_CVR_NUMBER, this.cvrNumber);
    }



    public static final String DB_FIELD_REG_NUMBER = "regNumber";
    public static final String IO_FIELD_REG_NUMBER = "regNummer";

    @OneToMany(mappedBy = CompanyRegNumberRecord.DB_FIELD_COMPANY, targetEntity = CompanyRegNumberRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = IO_FIELD_REG_NUMBER)
    private Set<CompanyRegNumberRecord> regNumber;

    public void setRegNumber(Set<CompanyRegNumberRecord> regNumber) {
        this.regNumber = regNumber;
        for (CompanyRegNumberRecord regNumberRecord : regNumber) {
            regNumberRecord.setCompanyRecord(this);
        }
    }

    public void addRegNumber(CompanyRegNumberRecord record) {
        if (!this.regNumber.contains(record)) {
            record.setCompanyRecord(this);
            this.regNumber.add(record);
        }
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



    public static final String DB_FIELD_UNITTYPE = "unitType";
    public static final String IO_FIELD_UNITTYPE = "enhedstype";

    @Column(name = DB_FIELD_UNITTYPE)
    @JsonProperty(value = IO_FIELD_UNITTYPE)
    private String unitType;



    public static final String DB_FIELD_INDUSTRY_RESPONSIBILITY_CODE = "industryResponsibilityCode";
    public static final String IO_FIELD_INDUSTRY_RESPONSIBILITY_CODE = "brancheAnsvarskode";

    @Column(name = DB_FIELD_INDUSTRY_RESPONSIBILITY_CODE, nullable = true)
    @JsonProperty(value = IO_FIELD_INDUSTRY_RESPONSIBILITY_CODE)
    private Integer industryResponsibilityCode;



    public static final String DB_FIELD_NAMES = "names";
    public static final String IO_FIELD_NAMES = "navne";

    @OneToMany(mappedBy = NameRecord.DB_FIELD_COMPANY, targetEntity = NameRecord.class, cascade = CascadeType.ALL)
    @Where(clause = NameRecord.DB_FIELD_SECONDARY+"=false")
    @Filters({
            //@Filter(name = Registration.FILTER_REGISTRATION_FROM, condition="(registrationTo >= :"+Registration.FILTERPARAM_REGISTRATION_FROM+" OR registrationTo is null)"),
            @Filter(name = Registration.FILTER_REGISTRATION_TO, condition="("+CvrBitemporalRecord.DB_FIELD_LAST_UPDATED+" < :"+Registration.FILTERPARAM_REGISTRATION_TO+")"),
            @Filter(name = Effect.FILTER_EFFECT_FROM, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_TO+" >= :" + Effect.FILTERPARAM_EFFECT_FROM + " OR "+CvrRecordPeriod.DB_FIELD_VALID_TO+" is null)"),
            @Filter(name = Effect.FILTER_EFFECT_TO, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_FROM+" < :" + Effect.FILTERPARAM_EFFECT_TO + " OR "+CvrRecordPeriod.DB_FIELD_VALID_FROM+" is null)")
    })
    @JsonProperty(value = IO_FIELD_NAMES)
    private Set<NameRecord> names = new HashSet<>();

    public Set<NameRecord> getNames() {
        return this.names;
    }

    public void setNames(Set<NameRecord> names) {
        this.names = names;
        for (NameRecord record : names) {
            record.setSecondary(false);
            record.setCompanyRecord(this);
        }
    }

    public void addName(NameRecord record) {
        if (!this.names.contains(record)) {
            record.setSecondary(false);
            record.setCompanyRecord(this);
            this.names.add(record);
        }
    }



    public static final String DB_FIELD_SECONDARY_NAMES = "secondaryNames";
    public static final String IO_FIELD_SECONDARY_NAMES = "binavne";

    @OneToMany(mappedBy = NameRecord.DB_FIELD_COMPANY, targetEntity = NameRecord.class, cascade = CascadeType.ALL)
    @Where(clause = NameRecord.DB_FIELD_SECONDARY+"=true")
    @JsonProperty(value = IO_FIELD_SECONDARY_NAMES)
    private Set<NameRecord> secondaryNames = new HashSet<>();

    public Set<NameRecord> getSecondaryNames() {
        return this.secondaryNames;
    }

    public void setSecondaryNames(Set<NameRecord> secondaryNames) {
        this.secondaryNames = secondaryNames;
        for (NameRecord record : secondaryNames) {
            record.setSecondary(true);
            record.setCompanyRecord(this);
        }
    }

    public void addSecondaryName(NameRecord record) {
        if (!this.secondaryNames.contains(record)) {
            record.setSecondary(true);
            record.setCompanyRecord(this);
            this.secondaryNames.add(record);
        }
    }



    public static final String DB_FIELD_LOCATION_ADDRESS = "locationAddress";
    public static final String IO_FIELD_LOCATION_ADDRESS = "beliggenhedsadresse";

    @OneToMany(mappedBy = AddressRecord.DB_FIELD_COMPANY, targetEntity = AddressRecord.class, cascade = CascadeType.ALL)
    @Where(clause = AddressRecord.DB_FIELD_TYPE+"="+AddressRecord.TYPE_LOCATION)
    @Filters({
            @Filter(name = Registration.FILTER_REGISTRATION_TO, condition="("+CvrBitemporalRecord.DB_FIELD_LAST_UPDATED+" < :"+Registration.FILTERPARAM_REGISTRATION_TO+")"),
            @Filter(name = Effect.FILTER_EFFECT_FROM, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_TO+" >= :" + Effect.FILTERPARAM_EFFECT_FROM + " OR "+CvrRecordPeriod.DB_FIELD_VALID_TO+" is null)"),
            @Filter(name = Effect.FILTER_EFFECT_TO, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_FROM+" < :" + Effect.FILTERPARAM_EFFECT_TO + " OR "+CvrRecordPeriod.DB_FIELD_VALID_FROM+" is null)")
    })
    @JsonProperty(value = IO_FIELD_LOCATION_ADDRESS)
    private Set<AddressRecord> locationAddress = new HashSet<>();

    public void setLocationAddress(Set<AddressRecord> locationAddress) {
        for (AddressRecord record : locationAddress) {
            record.setType(AddressRecord.TYPE_LOCATION);
            record.setCompanyRecord(this);
        }
        this.locationAddress = locationAddress;
    }

    public void addLocationAddress(AddressRecord record) {
        if (!this.locationAddress.contains(record)) {
            record.setType(AddressRecord.TYPE_LOCATION);
            record.setCompanyRecord(this);
            this.locationAddress.add(record);
        }
    }

    public Set<AddressRecord> getLocationAddress() {
        return this.locationAddress;
    }




    public static final String DB_FIELD_POSTAL_ADDRESS = "postalAddress";
    public static final String IO_FIELD_POSTAL_ADDRESS = "postadresse";

    @OneToMany(mappedBy = AddressRecord.DB_FIELD_COMPANY, targetEntity = AddressRecord.class, cascade = CascadeType.ALL)
    @Where(clause = AddressRecord.DB_FIELD_TYPE+"="+AddressRecord.TYPE_POSTAL)
    @Filters({
            @Filter(name = Registration.FILTER_REGISTRATION_TO, condition="("+CvrBitemporalRecord.DB_FIELD_LAST_UPDATED+" < :"+Registration.FILTERPARAM_REGISTRATION_TO+")"),
            @Filter(name = Effect.FILTER_EFFECT_FROM, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_TO+" >= :" + Effect.FILTERPARAM_EFFECT_FROM + " OR "+CvrRecordPeriod.DB_FIELD_VALID_TO+" is null)"),
            @Filter(name = Effect.FILTER_EFFECT_TO, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_FROM+" < :" + Effect.FILTERPARAM_EFFECT_TO + " OR "+CvrRecordPeriod.DB_FIELD_VALID_FROM+" is null)")
    })
    @JsonProperty(value = IO_FIELD_POSTAL_ADDRESS)
    private Set<AddressRecord> postalAddress = new HashSet<>();

    public void setPostalAddress(Set<AddressRecord> postalAddress) {
        for (AddressRecord record : postalAddress) {
            record.setType(AddressRecord.TYPE_POSTAL);
            record.setCompanyRecord(this);
        }
        this.postalAddress = postalAddress;
    }

    public void addPostalAddress(AddressRecord record) {
        if (!this.postalAddress.contains(record)) {
            record.setType(AddressRecord.TYPE_POSTAL);
            record.setCompanyRecord(this);
            this.postalAddress.add(record);
        }
    }

    public Set<AddressRecord> getPostalAddress() {
        return this.postalAddress;
    }



    public static final String DB_FIELD_PHONE = "phoneNumber";
    public static final String IO_FIELD_PHONE = "telefonNummer";

    @OneToMany(mappedBy = ContactRecord.DB_FIELD_COMPANY, targetEntity = ContactRecord.class, cascade = CascadeType.ALL)
    @Where(clause = ContactRecord.DB_FIELD_TYPE+"="+ContactRecord.TYPE_TELEFONNUMMER+" AND "+ContactRecord.DB_FIELD_SECONDARY+"=false")
    @Filters({
            @Filter(name = Registration.FILTER_REGISTRATION_TO, condition="("+CvrBitemporalRecord.DB_FIELD_LAST_UPDATED+" < :"+Registration.FILTERPARAM_REGISTRATION_TO+")"),
            @Filter(name = Effect.FILTER_EFFECT_FROM, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_TO+" >= :" + Effect.FILTERPARAM_EFFECT_FROM + " OR "+CvrRecordPeriod.DB_FIELD_VALID_TO+" is null)"),
            @Filter(name = Effect.FILTER_EFFECT_TO, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_FROM+" < :" + Effect.FILTERPARAM_EFFECT_TO + " OR "+CvrRecordPeriod.DB_FIELD_VALID_FROM+" is null)")
    })
    @JsonProperty(value = IO_FIELD_PHONE)
    private Set<ContactRecord> phoneNumber = new HashSet<>();

    public void setPhoneNumber(Set<ContactRecord> phoneNumber) {
        for (ContactRecord record : phoneNumber) {
            record.setType(ContactRecord.TYPE_TELEFONNUMMER);
            record.setSecondary(false);
            record.setCompanyRecord(this);
        }
        this.phoneNumber = phoneNumber;
    }

    public void addPhoneNumber(ContactRecord record) {
        if (!this.phoneNumber.contains(record)) {
            record.setType(ContactRecord.TYPE_TELEFONNUMMER);
            record.setCompanyRecord(this);
            record.setSecondary(false);
            this.phoneNumber.add(record);
        }
    }

    public Set<ContactRecord> getPhoneNumber() {
        return this.phoneNumber;
    }



    public static final String DB_FIELD_PHONE_SECONDARY = "secondaryPhoneNumber";
    public static final String IO_FIELD_PHONE_SECONDARY = "sekundaertTelefonNummer";

    @OneToMany(mappedBy = ContactRecord.DB_FIELD_COMPANY, targetEntity = ContactRecord.class, cascade = CascadeType.ALL)
    @Where(clause = ContactRecord.DB_FIELD_TYPE+"="+ContactRecord.TYPE_TELEFONNUMMER+" AND "+ContactRecord.DB_FIELD_SECONDARY+"=true")
    @Filters({
            @Filter(name = Registration.FILTER_REGISTRATION_TO, condition="("+CvrBitemporalRecord.DB_FIELD_LAST_UPDATED+" < :"+Registration.FILTERPARAM_REGISTRATION_TO+")"),
            @Filter(name = Effect.FILTER_EFFECT_FROM, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_TO+" >= :" + Effect.FILTERPARAM_EFFECT_FROM + " OR "+CvrRecordPeriod.DB_FIELD_VALID_TO+" is null)"),
            @Filter(name = Effect.FILTER_EFFECT_TO, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_FROM+" < :" + Effect.FILTERPARAM_EFFECT_TO + " OR "+CvrRecordPeriod.DB_FIELD_VALID_FROM+" is null)")
    })
    @JsonProperty(value = IO_FIELD_PHONE_SECONDARY)
    private Set<ContactRecord> secondaryPhoneNumber = new HashSet<>();

    public void setSecondaryPhoneNumber(Set<ContactRecord> secondaryPhoneNumber) {
        for (ContactRecord record : secondaryPhoneNumber) {
            record.setType(ContactRecord.TYPE_TELEFONNUMMER);
            record.setSecondary(true);
            record.setCompanyRecord(this);
        }
        this.secondaryPhoneNumber = secondaryPhoneNumber;
    }

    public void addSecondaryPhoneNumber(ContactRecord record) {
        if (!this.phoneNumber.contains(record)) {
            record.setType(ContactRecord.TYPE_TELEFONNUMMER);
            record.setCompanyRecord(this);
            record.setSecondary(true);
            this.phoneNumber.add(record);
        }
    }

    public Set<ContactRecord> getSecondaryPhoneNumber() {
        return this.secondaryPhoneNumber;
    }



    public static final String DB_FIELD_FAX = "faxNumber";
    public static final String IO_FIELD_FAX = "telefaxNummer";

    @OneToMany(mappedBy = ContactRecord.DB_FIELD_COMPANY, targetEntity = ContactRecord.class, cascade = CascadeType.ALL)
    @Where(clause = ContactRecord.DB_FIELD_TYPE+"="+ContactRecord.TYPE_TELEFAXNUMMER)
    @Filters({
            @Filter(name = Registration.FILTER_REGISTRATION_TO, condition="("+CvrBitemporalRecord.DB_FIELD_LAST_UPDATED+" < :"+Registration.FILTERPARAM_REGISTRATION_TO+")"),
            @Filter(name = Effect.FILTER_EFFECT_FROM, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_TO+" >= :" + Effect.FILTERPARAM_EFFECT_FROM + " OR "+CvrRecordPeriod.DB_FIELD_VALID_TO+" is null)"),
            @Filter(name = Effect.FILTER_EFFECT_TO, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_FROM+" < :" + Effect.FILTERPARAM_EFFECT_TO + " OR "+CvrRecordPeriod.DB_FIELD_VALID_FROM+" is null)")
    })
    @JsonProperty(value = IO_FIELD_FAX)
    private Set<ContactRecord> faxNumber = new HashSet<>();

    public void setFaxNumber(Set<ContactRecord> faxNumber) {
        for (ContactRecord record : faxNumber) {
            record.setType(ContactRecord.TYPE_TELEFAXNUMMER);
            record.setSecondary(false);
            record.setCompanyRecord(this);
        }
        this.faxNumber = faxNumber;
    }

    public void addFaxNumber(ContactRecord record) {
        if (!this.faxNumber.contains(record)) {
            record.setType(ContactRecord.TYPE_TELEFAXNUMMER);
            record.setCompanyRecord(this);
            record.setSecondary(false);
            this.faxNumber.add(record);
        }
    }

    public Set<ContactRecord> getFaxNumber() {
        return this.faxNumber;
    }



    public static final String DB_FIELD_FAX_SECONDARY = "secondaryFaxNumber";
    public static final String IO_FIELD_FAX_SECONDARY = "sekundaertTelefaxNummer";

    @OneToMany(mappedBy = ContactRecord.DB_FIELD_COMPANY, targetEntity = ContactRecord.class, cascade = CascadeType.ALL)
    @Where(clause = ContactRecord.DB_FIELD_TYPE+"="+ContactRecord.TYPE_TELEFAXNUMMER+" AND "+ContactRecord.DB_FIELD_SECONDARY+"=true")
    @Filters({
            @Filter(name = Registration.FILTER_REGISTRATION_TO, condition="("+CvrBitemporalRecord.DB_FIELD_LAST_UPDATED+" < :"+Registration.FILTERPARAM_REGISTRATION_TO+")"),
            @Filter(name = Effect.FILTER_EFFECT_FROM, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_TO+" >= :" + Effect.FILTERPARAM_EFFECT_FROM + " OR "+CvrRecordPeriod.DB_FIELD_VALID_TO+" is null)"),
            @Filter(name = Effect.FILTER_EFFECT_TO, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_FROM+" < :" + Effect.FILTERPARAM_EFFECT_TO + " OR "+CvrRecordPeriod.DB_FIELD_VALID_FROM+" is null)")
    })
    @JsonProperty(value = IO_FIELD_FAX_SECONDARY)
    private Set<ContactRecord> secondaryFaxNumber = new HashSet<>();

    public void setSecondaryFaxNumber(Set<ContactRecord> secondaryFaxNumber) {
        for (ContactRecord record : secondaryFaxNumber) {
            record.setType(ContactRecord.TYPE_TELEFAXNUMMER);
            record.setSecondary(true);
            record.setCompanyRecord(this);
        }
        this.secondaryFaxNumber = secondaryFaxNumber;
    }

    public void addSecondaryFaxNumber(ContactRecord record) {
        if (!this.faxNumber.contains(record)) {
            record.setType(ContactRecord.TYPE_TELEFAXNUMMER);
            record.setCompanyRecord(this);
            record.setSecondary(true);
            this.faxNumber.add(record);
        }
    }

    public Set<ContactRecord> getSecondaryFaxNumber() {
        return this.secondaryFaxNumber;
    }



    public static final String DB_FIELD_EMAIL = "emailAddress";
    public static final String IO_FIELD_EMAIL = "elektroniskPost";

    @OneToMany(mappedBy = ContactRecord.DB_FIELD_COMPANY, targetEntity = ContactRecord.class, cascade = CascadeType.ALL)
    @Where(clause = ContactRecord.DB_FIELD_TYPE+"="+ContactRecord.TYPE_EMAILADRESSE)
    @Filters({
            @Filter(name = Registration.FILTER_REGISTRATION_TO, condition="("+CvrBitemporalRecord.DB_FIELD_LAST_UPDATED+" < :"+Registration.FILTERPARAM_REGISTRATION_TO+")"),
            @Filter(name = Effect.FILTER_EFFECT_FROM, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_TO+" >= :" + Effect.FILTERPARAM_EFFECT_FROM + " OR "+CvrRecordPeriod.DB_FIELD_VALID_TO+" is null)"),
            @Filter(name = Effect.FILTER_EFFECT_TO, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_FROM+" < :" + Effect.FILTERPARAM_EFFECT_TO + " OR "+CvrRecordPeriod.DB_FIELD_VALID_FROM+" is null)")
    })
    @JsonProperty(value = IO_FIELD_EMAIL)
    private Set<ContactRecord> emailAddress = new HashSet<>();

    public void setEmailAddress(Set<ContactRecord> emailAddress) {
        for (ContactRecord record : emailAddress) {
            record.setType(ContactRecord.TYPE_EMAILADRESSE);
            record.setCompanyRecord(this);
        }
        this.emailAddress = emailAddress;
    }

    public void addEmailAddress(ContactRecord record) {
        if (!this.emailAddress.contains(record)) {
            record.setType(ContactRecord.TYPE_EMAILADRESSE);
            record.setCompanyRecord(this);
            this.emailAddress.add(record);
        }
    }

    public Set<ContactRecord> getEmailAddress() {
        return this.emailAddress;
    }



    public static final String DB_FIELD_HOMEPAGE = "homepage";
    public static final String IO_FIELD_HOMEPAGE = "hjemmeside";

    @OneToMany(mappedBy = ContactRecord.DB_FIELD_COMPANY, targetEntity = ContactRecord.class, cascade = CascadeType.ALL)
    @Where(clause = ContactRecord.DB_FIELD_TYPE+"="+ContactRecord.TYPE_HJEMMESIDE)
    @Filters({
            @Filter(name = Registration.FILTER_REGISTRATION_TO, condition="("+CvrBitemporalRecord.DB_FIELD_LAST_UPDATED+" < :"+Registration.FILTERPARAM_REGISTRATION_TO+")"),
            @Filter(name = Effect.FILTER_EFFECT_FROM, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_TO+" >= :" + Effect.FILTERPARAM_EFFECT_FROM + " OR "+CvrRecordPeriod.DB_FIELD_VALID_TO+" is null)"),
            @Filter(name = Effect.FILTER_EFFECT_TO, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_FROM+" < :" + Effect.FILTERPARAM_EFFECT_TO + " OR "+CvrRecordPeriod.DB_FIELD_VALID_FROM+" is null)")
    })
    @JsonProperty(value = IO_FIELD_HOMEPAGE)
    private Set<ContactRecord> homepage = new HashSet<>();

    public void setHomepage(Set<ContactRecord> homepage) {
        for (ContactRecord record : homepage) {
            record.setType(ContactRecord.TYPE_HJEMMESIDE);
            record.setCompanyRecord(this);
        }
        this.homepage = homepage;
    }

    public void addHomepage(ContactRecord record) {
        if (!this.homepage.contains(record)) {
            record.setType(ContactRecord.TYPE_HJEMMESIDE);
            record.setCompanyRecord(this);
            this.homepage.add(record);
        }
    }

    public Set<ContactRecord> getHomepage() {
        return this.homepage;
    }



    public static final String DB_FIELD_MANDATORY_EMAIL = "mandatoryEmailAddress";
    public static final String IO_FIELD_MANDATORY_EMAIL = "obligatoriskEmail";

    @OneToMany(mappedBy = ContactRecord.DB_FIELD_COMPANY, targetEntity = ContactRecord.class, cascade = CascadeType.ALL)
    @Where(clause = ContactRecord.DB_FIELD_TYPE+"="+ContactRecord.TYPE_OBLIGATORISK_EMAILADRESSE)
    @Filters({
            @Filter(name = Registration.FILTER_REGISTRATION_TO, condition="("+CvrBitemporalRecord.DB_FIELD_LAST_UPDATED+" < :"+Registration.FILTERPARAM_REGISTRATION_TO+")"),
            @Filter(name = Effect.FILTER_EFFECT_FROM, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_TO+" >= :" + Effect.FILTERPARAM_EFFECT_FROM + " OR "+CvrRecordPeriod.DB_FIELD_VALID_TO+" is null)"),
            @Filter(name = Effect.FILTER_EFFECT_TO, condition = "("+CvrRecordPeriod.DB_FIELD_VALID_FROM+" < :" + Effect.FILTERPARAM_EFFECT_TO + " OR "+CvrRecordPeriod.DB_FIELD_VALID_FROM+" is null)")
    })
    @JsonProperty(value = IO_FIELD_MANDATORY_EMAIL)
    private Set<ContactRecord> mandatoryEmailAddress = new HashSet<>();

    public void setMandatoryEmailAddress(Set<ContactRecord> mandatoryEmailAddress) {
        for (ContactRecord record : mandatoryEmailAddress) {
            record.setType(ContactRecord.TYPE_OBLIGATORISK_EMAILADRESSE);
            record.setCompanyRecord(this);
        }
        this.mandatoryEmailAddress = mandatoryEmailAddress;
    }

    public void addMandatoryEmailAddress(ContactRecord record) {
        if (!this.mandatoryEmailAddress.contains(record)) {
            record.setType(ContactRecord.TYPE_OBLIGATORISK_EMAILADRESSE);
            record.setCompanyRecord(this);
            this.mandatoryEmailAddress.add(record);
        }
    }

    public Set<ContactRecord> getMandatoryEmailAddress() {
        return this.mandatoryEmailAddress;
    }



    public static final String DB_FIELD_LIFECYCLE = "lifecycle";
    public static final String IO_FIELD_LIFECYCLE = "livsforloeb";

    @OneToMany(mappedBy = LifecycleRecord.DB_FIELD_COMPANY, targetEntity = LifecycleRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = IO_FIELD_LIFECYCLE)
    private Set<LifecycleRecord> lifecycle = new HashSet<>();

    public void setLifecycle(Set<LifecycleRecord> lifecycle) {
        this.lifecycle = lifecycle;
        for (LifecycleRecord lifecycleRecord : lifecycle) {
            lifecycleRecord.setCompanyRecord(this);
        }
    }

    public void addLifecycle(LifecycleRecord record) {
        if (!this.lifecycle.contains(record)) {
            record.setCompanyRecord(this);
            this.lifecycle.add(record);
        }
    }

    public Set<LifecycleRecord> getLifecycle() {
        return this.lifecycle;
    }



    public static final String DB_FIELD_PRIMARY_INDUSTRY = "primaryIndustry";
    public static final String IO_FIELD_PRIMARY_INDUSTRY = "hovedbranche";

    @OneToMany(mappedBy = CompanyIndustryRecord.DB_FIELD_COMPANY, targetEntity = CompanyIndustryRecord.class, cascade = CascadeType.ALL)
    @Where(clause = CompanyIndustryRecord.DB_FIELD_INDEX+"=0")
    @JsonProperty(value = IO_FIELD_PRIMARY_INDUSTRY)
    private Set<CompanyIndustryRecord> primaryIndustry = new HashSet<>();

    public void setPrimaryIndustry(Set<CompanyIndustryRecord> primaryIndustry) {
        for (CompanyIndustryRecord record : primaryIndustry) {
            record.setIndex(0);
            record.setCompanyRecord(this);
        }
        this.primaryIndustry = primaryIndustry;
    }

    public void addPrimaryIndustry(CompanyIndustryRecord record) {
        if (!this.primaryIndustry.contains(record)) {
            record.setIndex(0);
            record.setCompanyRecord(this);
            this.primaryIndustry.add(record);
        }
    }

    public Set<CompanyIndustryRecord> getPrimaryIndustry() {
        return this.primaryIndustry;
    }



    public static final String DB_FIELD_SECONDARY_INDUSTRY1 = "secondaryIndustry1";
    public static final String IO_FIELD_SECONDARY_INDUSTRY1 = "bibranche1";

    @OneToMany(mappedBy = CompanyIndustryRecord.DB_FIELD_COMPANY, targetEntity = CompanyIndustryRecord.class, cascade = CascadeType.ALL)
    @Where(clause = CompanyIndustryRecord.DB_FIELD_INDEX+"=1")
    @JsonProperty(value = IO_FIELD_SECONDARY_INDUSTRY1)
    private Set<CompanyIndustryRecord> secondaryIndustry1 = new HashSet<>();

    public void setSecondaryIndustry1(Set<CompanyIndustryRecord> secondaryIndustryRecords) {
        for (CompanyIndustryRecord record : secondaryIndustryRecords) {
            record.setIndex(1);
            record.setCompanyRecord(this);
        }
        this.secondaryIndustry1 = secondaryIndustryRecords;
    }

    public void addSecondaryIndustry1(CompanyIndustryRecord record) {
        if (!this.secondaryIndustry1.contains(record)) {
            record.setIndex(1);
            record.setCompanyRecord(this);
            this.secondaryIndustry1.add(record);
        }
    }

    public Set<CompanyIndustryRecord> getSecondaryIndustry1() {
        return this.secondaryIndustry1;
    }



    public static final String DB_FIELD_SECONDARY_INDUSTRY2 = "secondaryIndustry2";
    public static final String IO_FIELD_SECONDARY_INDUSTRY2 = "bibranche2";

    @OneToMany(mappedBy = CompanyIndustryRecord.DB_FIELD_COMPANY, targetEntity = CompanyIndustryRecord.class, cascade = CascadeType.ALL)
    @Where(clause = CompanyIndustryRecord.DB_FIELD_INDEX+"=2")
    @JsonProperty(value = IO_FIELD_SECONDARY_INDUSTRY2)
    private Set<CompanyIndustryRecord> secondaryIndustry2 = new HashSet<>();

    public void setSecondaryIndustry2(Set<CompanyIndustryRecord> secondaryIndustryRecords) {
        for (CompanyIndustryRecord record : secondaryIndustryRecords) {
            record.setIndex(2);
            record.setCompanyRecord(this);
        }
        this.secondaryIndustry2 = secondaryIndustryRecords;
    }

    public void addSecondaryIndustry2(CompanyIndustryRecord record) {
        if (!this.secondaryIndustry2.contains(record)) {
            record.setIndex(2);
            record.setCompanyRecord(this);
            this.secondaryIndustry2.add(record);
        }
    }

    public Set<CompanyIndustryRecord> getSecondaryIndustry2() {
        return this.secondaryIndustry2;
    }



    public static final String DB_FIELD_SECONDARY_INDUSTRY3 = "secondaryIndustry3";
    public static final String IO_FIELD_SECONDARY_INDUSTRY3 = "bibranche3";

    @OneToMany(mappedBy = CompanyIndustryRecord.DB_FIELD_COMPANY, targetEntity = CompanyIndustryRecord.class, cascade = CascadeType.ALL)
    @Where(clause = CompanyIndustryRecord.DB_FIELD_INDEX+"=3")
    @JsonProperty(value = IO_FIELD_SECONDARY_INDUSTRY3)
    private Set<CompanyIndustryRecord> secondaryIndustry3 = new HashSet<>();

    public void setSecondaryIndustry3(Set<CompanyIndustryRecord> secondaryIndustryRecords) {
        for (CompanyIndustryRecord record : secondaryIndustryRecords) {
            record.setIndex(3);
            record.setCompanyRecord(this);
        }
        this.secondaryIndustry3 = secondaryIndustryRecords;
    }

    public void addSecondaryIndustry3(CompanyIndustryRecord record) {
        if (!this.secondaryIndustry3.contains(record)) {
            record.setIndex(3);
            record.setCompanyRecord(this);
            this.secondaryIndustry3.add(record);
        }
    }

    public Set<CompanyIndustryRecord> getSecondaryIndustry3() {
        return this.secondaryIndustry3;
    }



    public static final String DB_FIELD_STATUS = "status";
    public static final String IO_FIELD_STATUS = "status";

    @OneToMany(mappedBy = CompanyStatusRecord.DB_FIELD_COMPANY, targetEntity = StatusRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = IO_FIELD_STATUS)
    private Set<StatusRecord> status = new HashSet<>();

    public void setStatus(Set<StatusRecord> status) {
        this.status = status;
        for (StatusRecord statusRecord : status) {
            statusRecord.setCompanyRecord(this);
        }
    }

    public void addStatus(StatusRecord record) {
        if (!this.status.contains(record)) {
            record.setCompanyRecord(this);
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
            statusRecord.setCompanyRecord(this);
        }
    }

    public void addCompanyStatus(CompanyStatusRecord record) {
        if (!this.companyStatus.contains(record)) {
            record.setCompanyRecord(this);
            this.companyStatus.add(record);
        }
    }

    public Set<CompanyStatusRecord> getCompanyStatus() {
        return this.companyStatus;
    }



    public static final String DB_FIELD_FORM = "companyForm";
    public static final String IO_FIELD_FORM = "virksomhedsform";

    @OneToMany(mappedBy = CompanyFormRecord.DB_FIELD_COMPANY, targetEntity = CompanyFormRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = IO_FIELD_FORM)
    private Set<CompanyFormRecord> companyForm = new HashSet<>();

    public void setCompanyForm(Set<CompanyFormRecord> companyForm) {
        this.companyForm = companyForm;
        for (CompanyFormRecord formRecord : companyForm) {
            formRecord.setCompanyRecord(this);
        }
    }

    public void addCompanyForm(CompanyFormRecord record) {
        if (!this.companyForm.contains(record)) {
            record.setCompanyRecord(this);
            this.companyForm.add(record);
        }
    }

    public Set<CompanyFormRecord> getCompanyForm() {
        return this.companyForm;
    }



    public static final String DB_FIELD_YEARLY_NUMBERS = "yearlyNumbers";
    public static final String IO_FIELD_YEARLY_NUMBERS = "aarsbeskaeftigelse";

    @OneToMany(mappedBy = CompanyYearlyNumbersRecord.DB_FIELD_COMPANY, targetEntity = CompanyYearlyNumbersRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = IO_FIELD_YEARLY_NUMBERS)
    private Set<CompanyYearlyNumbersRecord> yearlyNumbers = new HashSet<>();

    public void setYearlyNumbers(Set<CompanyYearlyNumbersRecord> yearlyNumbers) {
        this.yearlyNumbers = yearlyNumbers;
        for (CompanyYearlyNumbersRecord yearlyNumbersRecord : yearlyNumbers) {
            yearlyNumbersRecord.setCompanyRecord(this);
        }
    }

    public void addYearlyNumbers(CompanyYearlyNumbersRecord record) {
        if (!this.yearlyNumbers.contains(record)) {
            record.setCompanyRecord(this);
            this.yearlyNumbers.add(record);
        }
    }

    public Set<CompanyYearlyNumbersRecord> getYearlyNumbers() {
        return this.yearlyNumbers;
    }



    public static final String DB_FIELD_QUARTERLY_NUMBERS = "quarterlyNumbers";
    public static final String IO_FIELD_QUARTERLY_NUMBERS = "kvartalsbeskaeftigelse";

    @OneToMany(mappedBy = CompanyQuarterlyNumbersRecord.DB_FIELD_COMPANY, targetEntity = CompanyQuarterlyNumbersRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = IO_FIELD_QUARTERLY_NUMBERS)
    private Set<CompanyQuarterlyNumbersRecord> quarterlyNumbers = new HashSet<>();

    public void setQuarterlyNumbers(Set<CompanyQuarterlyNumbersRecord> quarterlyNumbers) {
        this.quarterlyNumbers = quarterlyNumbers;
        for (CompanyQuarterlyNumbersRecord quarterlyNumbersRecord : quarterlyNumbers) {
            quarterlyNumbersRecord.setCompanyRecord(this);
        }
    }

    public void addQuarterlyNumbers(CompanyQuarterlyNumbersRecord record) {
        if (!this.quarterlyNumbers.contains(record)) {
            record.setCompanyRecord(this);
            this.quarterlyNumbers.add(record);
        }
    }

    public Set<CompanyQuarterlyNumbersRecord> getQuarterlyNumbers() {
        return this.quarterlyNumbers;
    }



    public static final String DB_FIELD_MONTHLY_NUMBERS = "monthlyNumbers";
    public static final String IO_FIELD_MONTHLY_NUMBERS = "maanedsbeskaeftigelse";

    @OneToMany(mappedBy = CompanyMonthlyNumbersRecord.DB_FIELD_COMPANY, targetEntity = CompanyMonthlyNumbersRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = IO_FIELD_MONTHLY_NUMBERS)
    private Set<CompanyMonthlyNumbersRecord> monthlyNumbers = new HashSet<>();

    public void setMonthlyNumbers(Set<CompanyMonthlyNumbersRecord> monthlyNumbers) {
        this.monthlyNumbers = monthlyNumbers;
        for (CompanyMonthlyNumbersRecord monthlyNumbersRecord : monthlyNumbers) {
            monthlyNumbersRecord.setCompanyRecord(this);
        }
    }

    public void addMonthlyNumbers(CompanyMonthlyNumbersRecord record) {
        if (!this.monthlyNumbers.contains(record)) {
            record.setCompanyRecord(this);
            this.monthlyNumbers.add(record);
        }
    }

    public Set<CompanyMonthlyNumbersRecord> getMonthlyNumbers() {
        return this.monthlyNumbers;
    }



    public static final String DB_FIELD_ATTRIBUTES = "attributes";
    public static final String IO_FIELD_ATTRIBUTES = "attributter";

    @OneToMany(mappedBy = AttributeRecord.DB_FIELD_COMPANY, targetEntity = AttributeRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = IO_FIELD_ATTRIBUTES)
    private Set<AttributeRecord> attributes = new HashSet<>();

    public void setAttributes(Set<AttributeRecord> attributes) {
        this.attributes = attributes;
        for (AttributeRecord attributeRecord : attributes) {
            attributeRecord.setCompanyRecord(this);
        }
    }

    public void addAttributes(AttributeRecord record) {
        if (!this.attributes.contains(record)) {
            record.setCompanyRecord(this);
            this.attributes.add(record);
        }
    }

    public Set<AttributeRecord> getAttributes() {
        return this.attributes;
    }



    public static final String DB_FIELD_P_UNITS = "productionUnits";
    public static final String IO_FIELD_P_UNITS = "penheder";

    @OneToMany(mappedBy = CompanyUnitLinkRecord.DB_FIELD_COMPANY, targetEntity = CompanyUnitLinkRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = IO_FIELD_P_UNITS)
    private Set<CompanyUnitLinkRecord> productionUnits = new HashSet<>();

    public void setProductionUnits(Set<CompanyUnitLinkRecord> productionUnits) {
        this.productionUnits = productionUnits;
        for (CompanyUnitLinkRecord unitLinkRecord : productionUnits) {
            unitLinkRecord.setCompanyRecord(this);
        }
    }

    public void addProductionUnit(CompanyUnitLinkRecord record) {
        if (!this.productionUnits.contains(record)) {
            record.setCompanyRecord(this);
            this.productionUnits.add(record);
        }
    }

    public Set<CompanyUnitLinkRecord> getProductionUnits() {
        return this.productionUnits;
    }



    public static final String DB_FIELD_PARTICIPANTS = "participants";
    public static final String IO_FIELD_PARTICIPANTS = "deltagerRelation";

    @OneToMany(mappedBy = CompanyParticipantRelationRecord.DB_FIELD_COMPANY, targetEntity = CompanyParticipantRelationRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = IO_FIELD_PARTICIPANTS)
    private Set<CompanyParticipantRelationRecord> participants = new HashSet<>();

    public void setParticipants(Set<CompanyParticipantRelationRecord> participants) {
        this.participants = participants;
        for (CompanyParticipantRelationRecord participantRelationRecord : participants) {
            participantRelationRecord.setCompanyRecord(this);
        }
    }

    public void addParticipant(CompanyParticipantRelationRecord record) {
        if (!this.participants.contains(record)) {
            record.setCompanyRecord(this);
            this.participants.add(record);
        }
    }

    public Set<CompanyParticipantRelationRecord> getParticipants() {
        return this.participants;
    }



    public static final String DB_FIELD_FUSIONS = "fusions";
    public static final String IO_FIELD_FUSIONS = "fusioner";

    @OneToMany(mappedBy = FusionSplitRecord.DB_FIELD_COMPANY, targetEntity = FusionSplitRecord.class, cascade = CascadeType.ALL)
    @Where(clause = FusionSplitRecord.DB_FIELD_SPLIT+"=false")
    @JsonProperty(value = IO_FIELD_FUSIONS)
    private Set<FusionSplitRecord> fusions = new HashSet<>();

    public Set<FusionSplitRecord> getFusions() {
        return this.fusions;
    }

    public void setFusions(Set<FusionSplitRecord> fusions) {
        this.fusions = fusions;
        for (FusionSplitRecord fusionSplitRecord : fusions) {
            fusionSplitRecord.setCompanyRecord(this);
            fusionSplitRecord.setSplit(false);
        }
    }

    public void addFusion(FusionSplitRecord record) {
        if (!this.fusions.contains(record)) {
            record.setCompanyRecord(this);
            record.setSplit(false);
            this.fusions.add(record);
        }
    }




    public static final String DB_FIELD_SPLITS = "splits";
    public static final String IO_FIELD_SPLITS = "spaltninger";

    @OneToMany(mappedBy = FusionSplitRecord.DB_FIELD_COMPANY, targetEntity = FusionSplitRecord.class, cascade = CascadeType.ALL)
    @Where(clause = FusionSplitRecord.DB_FIELD_SPLIT+"=true")
    @JsonProperty(value = IO_FIELD_SPLITS)
    private Set<FusionSplitRecord> splits = new HashSet<>();

    public Set<FusionSplitRecord> getSplits() {
        return this.splits;
    }

    public void setSplits(Set<FusionSplitRecord> splits) {
        this.splits = splits;
        for (FusionSplitRecord fusionSplitRecord : splits) {
            fusionSplitRecord.setCompanyRecord(this);
            fusionSplitRecord.setSplit(true);
        }
    }

    public void addSplit(FusionSplitRecord record) {
        if (!this.fusions.contains(record)) {
            record.setCompanyRecord(this);
            record.setSplit(true);
            this.fusions.add(record);
        }
    }



    public static final String DB_FIELD_META = "metadata";
    public static final String IO_FIELD_META = "virksomhedMetadata";

    @OneToOne(mappedBy = CompanyMetadataRecord.DB_FIELD_COMPANY, targetEntity = CompanyMetadataRecord.class, cascade = CascadeType.ALL)
    @JoinColumn(name = DB_FIELD_META + DatabaseEntry.REF)
    @JsonProperty(value = IO_FIELD_META)
    private CompanyMetadataRecord metadata;

    public void setMetadata(CompanyMetadataRecord metadata) {
        this.metadata = metadata;
        this.metadata.setCompanyRecord(this);
    }

    public CompanyMetadataRecord getMetadata() {
        return this.metadata;
    }








    @JsonIgnore
    @Override
    public List<CvrRecord> getAll() {
        ArrayList<CvrRecord> list = new ArrayList<>();
        if (this.regNumber != null) {
            list.addAll(this.regNumber);
        }
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
        if (this.homepage != null) {
            list.addAll(this.homepage);
        }
        if (this.mandatoryEmailAddress != null) {
            list.addAll(this.mandatoryEmailAddress);
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
        if (this.companyStatus != null) {
            list.addAll(this.companyStatus);
        }
        if (this.companyForm != null) {
            list.addAll(this.companyForm);
        }
        if (this.yearlyNumbers != null) {
            list.addAll(this.yearlyNumbers);
        }
        if (this.quarterlyNumbers != null) {
            list.addAll(this.quarterlyNumbers);
        }
        if (this.monthlyNumbers != null) {
            list.addAll(this.monthlyNumbers);
        }
        if (this.attributes != null) {
            for (AttributeRecord attributeRecord : this.attributes) {
                list.addAll(attributeRecord.getValues());
            }
        }
        if (this.productionUnits != null && !this.productionUnits.isEmpty()) {
            list.addAll(this.productionUnits);
        }
        if (this.participants != null) {
            list.addAll(this.participants);
        }

        if (this.metadata != null) {
            list.addAll(this.metadata.extractRecords(this, true));
        }

        return list;
    }

    @Override
    public UUID generateUUID() {
        return CompanyEntity.generateUUID(this.cvrNumber);
    }

    @Override
    public void populateBaseData(CompanyBaseData baseData, Session session) {
        baseData.setAdvertProtection(this.advertProtection);
        baseData.setCvrNumber(this.cvrNumber);
    }

    @Override
    public void save(Session session) {
        for (AddressRecord address : this.locationAddress) {
            address.wire(session);
        }
        for (AddressRecord address : this.postalAddress) {
            address.wire(session);
        }
        for (CompanyFormRecord form : this.companyForm) {
            form.wire(session);
        }
        if (this.metadata != null) {
            this.metadata.wire(session);
        }
        super.save(session);
    }

    @Override
    public boolean merge(CvrEntityRecord other) {
        if (other != null && !other.getId().equals(this.getId()) && other instanceof CompanyRecord) {
            CompanyRecord existing = (CompanyRecord) other;
            for (NameRecord nameRecord : this.getNames()) {
                existing.addName(nameRecord);
            }
            for (NameRecord nameRecord : this.getSecondaryNames()) {
                existing.addSecondaryName(nameRecord);
            }
            for (AddressRecord addressRecord : this.getLocationAddress()) {
                existing.addLocationAddress(addressRecord);
            }
            for (AddressRecord addressRecord : this.getPostalAddress()) {
                existing.addPostalAddress(addressRecord);
            }
            for (ContactRecord contactRecord : this.getPhoneNumber()) {
                existing.addPhoneNumber(contactRecord);
            }
            for (ContactRecord contactRecord : this.getSecondaryPhoneNumber()) {
                existing.addSecondaryPhoneNumber(contactRecord);
            }
            for (ContactRecord contactRecord : this.getFaxNumber()) {
                existing.addFaxNumber(contactRecord);
            }
            for (ContactRecord contactRecord : this.getSecondaryFaxNumber()) {
                existing.addSecondaryFaxNumber(contactRecord);
            }
            for (ContactRecord contactRecord : this.getEmailAddress()) {
                existing.addEmailAddress(contactRecord);
            }
            for (ContactRecord contactRecord : this.getHomepage()) {
                existing.addHomepage(contactRecord);
            }
            for (ContactRecord contactRecord : this.getMandatoryEmailAddress()) {
                existing.addMandatoryEmailAddress(contactRecord);
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
            for (StatusRecord statusRecord : this.getStatus()) {
                existing.addStatus(statusRecord);
            }
            for (CompanyStatusRecord statusRecord : this.getCompanyStatus()) {
                existing.addCompanyStatus(statusRecord);
            }
            for (CompanyFormRecord formRecord : this.getCompanyForm()) {
                existing.addCompanyForm(formRecord);
            }
            for (CompanyYearlyNumbersRecord yearlyNumbersRecord : this.getYearlyNumbers()) {
                existing.addYearlyNumbers(yearlyNumbersRecord);
            }
            for (CompanyQuarterlyNumbersRecord quarterlyNumbersRecord : this.getQuarterlyNumbers()) {
                existing.addQuarterlyNumbers(quarterlyNumbersRecord);
            }
            for (CompanyMonthlyNumbersRecord monthlyNumbersRecord : this.getMonthlyNumbers()) {
                existing.addMonthlyNumbers(monthlyNumbersRecord);
            }
            for (AttributeRecord attributeRecord : this.getAttributes()) {
                existing.addAttributes(attributeRecord);
            }
            for (CompanyUnitLinkRecord companyUnitLinkRecord : this.getProductionUnits()) {
                existing.addProductionUnit(companyUnitLinkRecord);
            }
            for (CompanyParticipantRelationRecord participantRelationRecord : this.getParticipants()) {
                existing.addParticipant(participantRelationRecord);
            }
            for (FusionSplitRecord fusionSplitRecord : this.getFusions()) {
                existing.addFusion(fusionSplitRecord);
            }
            for (FusionSplitRecord fusionSplitRecord : this.getSplits()) {
                existing.addSplit(fusionSplitRecord);
            }
            this.metadata.merge(existing.getMetadata());
            return true;
        }
        return false;
    }

}
