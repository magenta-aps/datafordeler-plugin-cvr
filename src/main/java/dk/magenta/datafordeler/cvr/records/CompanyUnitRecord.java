package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.exception.ParseException;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitEntity;
import org.hibernate.Session;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Base record for CompanyUnit data, parsed from JSON into a tree of objects
 * with this class at the base.
 */
@Entity
@Table(name="cvr_record_companyunitrecord")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyUnitRecord extends CvrEntityRecord {

    public static final String DB_FIELD_P_NUMBER = "cvrNumber";
    public static final String P_NUMBER = "cvrNummer";

    @Column(name = DB_FIELD_P_NUMBER)
    @JsonProperty(value = "pNummer")
    private int pNumber;

    public int getpNumber() {
        return this.pNumber;
    }



    public static final String DB_FIELD_ADVERTPROTECTION = "advertProtection";
    public static final String IO_FIELD_ADVERTPROTECTION = "reklamebeskyttet";


    @Column(name = DB_FIELD_ADVERTPROTECTION)
    @JsonProperty(value = "reklamebeskyttet")
    private boolean advertProtection;



    public static final String DB_FIELD_UNITNUMBER = "unitNumber";
    public static final String IO_FIELD_UNITNUMBER = "enhedsNummer";

    @Column(name = DB_FIELD_UNITNUMBER)
    @JsonProperty(value = "enhedsNummer")
    private int unitNumber;



    public static final String DB_FIELD_NAMES = "names";
    public static final String IO_FIELD_NAMES = "navne";

    @OneToMany(mappedBy = NameRecord.DB_FIELD_COMPANYUNIT, targetEntity = NameRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = "navne")
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



    public static final String DB_FIELD_LOCATION_ADDRESS = "locationAddress";
    public static final String IO_FIELD_LOCATION_ADDRESS = "beliggenhedsadresse";

    @OneToMany(mappedBy = AddressRecord.DB_FIELD_COMPANYUNIT, targetEntity = AddressRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = "beliggenhedsadresse")
    private Set<AddressRecord> locationAddress;

    public void setLocationAddress(Set<AddressRecord> locationAddress) {
        for (AddressRecord record : locationAddress) {
            record.setType(AddressRecord.Type.LOCATION);
            record.setCompanyUnitRecord(this);
        }
        this.locationAddress = locationAddress;
    }
    public Set<AddressRecord> getLocationAddress() {
        return this.locationAddress;
    }


    public static final String DB_FIELD_POSTAL_ADDRESS = "postalAddress";
    public static final String IO_FIELD_POSTAL_ADDRESS = "postadresse";

    @OneToMany(mappedBy = AddressRecord.DB_FIELD_COMPANYUNIT, targetEntity = AddressRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = "postadresse")
    private Set<AddressRecord> postalAddress;

    public void setPostalAddress(Set<AddressRecord> postalAddress) {
        for (AddressRecord record : postalAddress) {
            record.setType(AddressRecord.Type.POSTAL);
        }
        this.postalAddress = postalAddress;
    }

    public Set<AddressRecord> getPostalAddress() {
        return this.postalAddress;
    }


    public static final String DB_FIELD_PHONE = "phoneNumber";
    public static final String IO_FIELD_PHONE = "telefonNummer";

    @OneToMany(mappedBy = ContactRecord.DB_FIELD_COMPANYUNIT, targetEntity = ContactRecord.class, cascade = CascadeType.ALL)
    @Where(clause = ContactRecord.DB_FIELD_TYPE+"="+ContactRecord.TYPE_TELEFONNUMMER)
    @JsonProperty(value = "telefonNummer")
    private Set<ContactRecord> phoneNumber;

    public void setPhoneNumber(Set<ContactRecord> phoneNumber) {
        for (ContactRecord record : phoneNumber) {
            record.setType(ContactRecord.TYPE_TELEFONNUMMER);
        }
        this.phoneNumber = phoneNumber;
    }



    public static final String DB_FIELD_FAX = "faxNumber";
    public static final String IO_FIELD_FAX = "telefaxNummer";

    @OneToMany(mappedBy = ContactRecord.DB_FIELD_COMPANYUNIT, targetEntity = ContactRecord.class, cascade = CascadeType.ALL)
    @Where(clause = ContactRecord.DB_FIELD_TYPE+"="+ContactRecord.TYPE_TELEFAXNUMMER)
    @JsonProperty(value = "telefaxNummer")
    private Set<ContactRecord> faxNumber;

    public void setFaxNumber(Set<ContactRecord> faxNumber) {
        for (ContactRecord record : faxNumber) {
            record.setType(ContactRecord.TYPE_TELEFAXNUMMER);
        }
        this.faxNumber = faxNumber;
    }



    public static final String DB_FIELD_EMAIL = "emailAddress";
    public static final String IO_FIELD_EMAIL = "elektroniskPost";

    @OneToMany(mappedBy = ContactRecord.DB_FIELD_COMPANYUNIT, targetEntity = ContactRecord.class, cascade = CascadeType.ALL)
    @Where(clause = ContactRecord.DB_FIELD_TYPE+"="+ContactRecord.TYPE_EMAILADRESSE)
    @JsonProperty(value = "elektroniskPost")
    private Set<ContactRecord> emailAddress;

    public void setEmailAddress(Set<ContactRecord> emailAddress) {
        for (ContactRecord record : emailAddress) {
            record.setType(ContactRecord.TYPE_EMAILADRESSE);
        }
        this.emailAddress = emailAddress;
    }

    @OneToMany(mappedBy = LifecycleRecord.DB_FIELD_COMPANYUNIT, targetEntity = LifecycleRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = "livsforloeb")
    private Set<LifecycleRecord> lifecycle;

    public Set<LifecycleRecord> getLifecycle() {
        return this.lifecycle;
    }

    public static final String DB_FIELD_PRIMARY_INDUSTRY = "primaryIndustry";
    public static final String IO_FIELD_PRIMARY_INDUSTRY = "hovedbranche";

    @OneToMany(mappedBy = CompanyIndustryRecord.DB_FIELD_COMPANY, targetEntity = CompanyIndustryRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = "hovedbranche")
    private Set<CompanyIndustryRecord> primaryIndustry;

    public void setPrimaryIndustry(Set<CompanyIndustryRecord> primaryIndustry) {
        for (CompanyIndustryRecord record : primaryIndustry) {
            record.setIndex(0);
            record.setCompanyUnitRecord(this);
        }
        this.primaryIndustry = primaryIndustry;
    }

    public Set<CompanyIndustryRecord> getPrimaryIndustry() {
        return this.primaryIndustry;
    }


    public static final String DB_FIELD_SECONDARY_INDUSTRY1 = "secondaryIndustry1";
    public static final String IO_FIELD_SECONDARY_INDUSTRY1 = "bibranche1";

    @OneToMany(mappedBy = CompanyIndustryRecord.DB_FIELD_COMPANY, targetEntity = CompanyIndustryRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = "bibranche1")
    private Set<CompanyIndustryRecord> secondaryIndustry1;

    public void setSecondaryIndustry1(Set<CompanyIndustryRecord> secondaryIndustryRecords) {
        for (CompanyIndustryRecord record : secondaryIndustryRecords) {
            record.setIndex(1);
            record.setCompanyUnitRecord(this);
        }
        this.secondaryIndustry1 = secondaryIndustryRecords;
    }

    public static final String DB_FIELD_SECONDARY_INDUSTRY2 = "secondaryIndustry2";
    public static final String IO_FIELD_SECONDARY_INDUSTRY2 = "bibranche2";

    @OneToMany(mappedBy = CompanyIndustryRecord.DB_FIELD_COMPANY, targetEntity = CompanyIndustryRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = "bibranche2")
    private Set<CompanyIndustryRecord> secondaryIndustry2;

    public void setSecondaryIndustry2(Set<CompanyIndustryRecord> secondaryIndustryRecords) {
        for (CompanyIndustryRecord record : secondaryIndustryRecords) {
            record.setIndex(2);
            record.setCompanyUnitRecord(this);
        }
        this.secondaryIndustry2 = secondaryIndustryRecords;
    }

    public static final String DB_FIELD_SECONDARY_INDUSTRY3 = "secondaryIndustry3";
    public static final String IO_FIELD_SECONDARY_INDUSTRY3 = "bibranche3";

    @OneToMany(mappedBy = CompanyIndustryRecord.DB_FIELD_COMPANY, targetEntity = CompanyIndustryRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = "bibranche3")
    private Set<CompanyIndustryRecord> secondaryIndustry3;

    public void setSecondaryIndustry3(Set<CompanyIndustryRecord> secondaryIndustryRecords) {
        for (CompanyIndustryRecord record : secondaryIndustryRecords) {
            record.setIndex(3);
            record.setCompanyUnitRecord(this);
        }
        this.secondaryIndustry3 = secondaryIndustryRecords;
    }

    public static final String DB_FIELD_YEARLY_NUMBERS = "yearlyNumbers";
    public static final String IO_FIELD_YEARLY_NUMBERS = "aarsbeskaeftigelse";

    @OneToMany(mappedBy = CompanyYearlyNumbersRecord.DB_FIELD_COMPANY, targetEntity = CompanyYearlyNumbersRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = "aarsbeskaeftigelse")
    private Set<CompanyYearlyNumbersRecord> yearlyNumbers;

    public void setYearlyNumbers(Set<CompanyYearlyNumbersRecord> yearlyNumbers) {
        this.yearlyNumbers = yearlyNumbers;
        for (CompanyYearlyNumbersRecord yearlyNumbersRecord : yearlyNumbers) {
            yearlyNumbersRecord.setCompanyUnitRecord(this);
        }
    }
    public static final String DB_FIELD_QUARTERLY_NUMBERS = "quarterlyNumbers";
    public static final String IO_FIELD_QUARTERLY_NUMBERS = "kvartalsbeskaeftigelse";

    @OneToMany(mappedBy = CompanyQuarterlyNumbersRecord.DB_FIELD_COMPANY, targetEntity = CompanyQuarterlyNumbersRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = "kvartalsbeskaeftigelse")
    private Set<CompanyQuarterlyNumbersRecord> quarterlyNumbers;

    public void setQuarterlyNumbers(Set<CompanyQuarterlyNumbersRecord> quarterlyNumbers) {
        this.quarterlyNumbers = quarterlyNumbers;
        for (CompanyQuarterlyNumbersRecord quarterlyNumbersRecord : quarterlyNumbers) {
            quarterlyNumbersRecord.setCompanyUnitRecord(this);
        }
    }

    //public static final String DB_FIELD_MONTHLY_NUMBERS = "monthlyNumbers";
    //public static final String IO_FIELD_MONTHLY_NUMBERS = "maanedsbeskaeftigelse";

    //@OneToMany(mappedBy = CompanyMonthlyNumbersRecord.DB_FIELD_COMPANY, cascade = CascadeType.ALL)
    //@JsonProperty(value = "maanedsbeskaeftigelse")
    //private Set<CompanyMonthlyNumbersRecord> monthlyNumbers;


    public static final String DB_FIELD_ATTRIBUTES = "attributes";
    public static final String IO_FIELD_ATTRIBUTES = "attributter";

    @OneToMany(mappedBy = AttributeRecord.DB_FIELD_COMPANYUNIT, targetEntity = AttributeRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = "attributter")
    private Set<AttributeRecord> attributes;

    @OneToMany(mappedBy = CompanyParticipantRelationRecord.DB_FIELD_COMPANYUNIT, targetEntity = CompanyParticipantRelationRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = "deltagerRelation")
    private Set<CompanyParticipantRelationRecord> participantRelations;

    @OneToMany(mappedBy = CompanyLinkRecord.DB_FIELD_COMPANYUNIT, targetEntity = CompanyLinkRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = "virksomhedsrelation")
    private Set<CompanyLinkRecord> companyLinkRecords;


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

}
