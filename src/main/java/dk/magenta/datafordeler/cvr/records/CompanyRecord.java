package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.company.CompanyEntity;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Base record for Company data, parsed from JSON into a tree of objects
 * with this class at the base.
 */
@Entity
@Table(name="cvr_record_companyrecord")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyRecord extends CvrEntityRecord {

    public static final String DB_FIELD_CVR_NUMBER = "cvrNumber";
    public static final String CVR_NUMBER = "cvrNummer";

    @Column(name = DB_FIELD_CVR_NUMBER)
    @JsonProperty(value = "cvrNummer")
    private int cvrNumber;

    @JsonProperty(value = CVR_NUMBER)
    public int getCvrNumber() {
        return this.cvrNumber;
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



    public static final String DB_FIELD_UNITTYPE = "unitType";
    public static final String IO_FIELD_UNITTYPE = "enhedstype";

    @Column(name = DB_FIELD_UNITTYPE)
    @JsonProperty(value = "enhedstype")
    private String unitType;



    public static final String DB_FIELD_NAMES = "names";
    public static final String IO_FIELD_NAMES = "navne";

    @OneToMany(mappedBy = "companyRecord", cascade = CascadeType.ALL)
    @JsonProperty(value = "navne")
    private Set<NameRecord> names;

    public Set<NameRecord> getNames() {
        return this.names;
    }

    public void setNames(Set<NameRecord> names) {
        this.names = names;
        for (NameRecord record : names) {
            record.setCompanyRecord(this);
        }
    }



    public static final String DB_FIELD_LOCATION_ADDRESS = "locationAddress";
    public static final String IO_FIELD_LOCATION_ADDRESS = "beliggenhedsadresse";

    @OneToMany(mappedBy = "companyRecord", cascade = CascadeType.ALL)
    @JsonProperty(value = "beliggenhedsadresse")
    private Set<AddressRecord> locationAddress;

    public void setLocationAddress(Set<AddressRecord> locationAddress) {
        for (AddressRecord record : locationAddress) {
            record.setType(AddressRecord.Type.LOCATION);
        }
        this.locationAddress = locationAddress;
    }

    public Set<AddressRecord> getLocationAddress() {
        return this.locationAddress;
    }




    public static final String DB_FIELD_POSTAL_ADDRESS = "postalAddress";
    public static final String IO_FIELD_POSTAL_ADDRESS = "postadresse";

    @OneToMany(mappedBy = "companyRecord", cascade = CascadeType.ALL)
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

    @OneToMany(mappedBy = "companyRecord", cascade = CascadeType.ALL)
    @JsonProperty(value = "telefonNummer")
    private Set<ContactRecord> phoneNumber;

    public void setPhoneNumber(Set<ContactRecord> phoneNumber) {
        for (ContactRecord record : phoneNumber) {
            record.setType(ContactRecord.Type.TELEFONNUMMER);
        }
        this.phoneNumber = phoneNumber;
    }



    public static final String DB_FIELD_FAX = "faxNumber";
    public static final String IO_FIELD_FAX = "telefaxNummer";

    @OneToMany(mappedBy = "companyRecord", cascade = CascadeType.ALL)
    @JsonProperty(value = "telefaxNummer")
    private Set<ContactRecord> faxNumber;

    public void setFaxNumber(Set<ContactRecord> faxNumber) {
        for (ContactRecord record : faxNumber) {
            record.setType(ContactRecord.Type.TELEFAXNUMMER);
        }
        this.faxNumber = faxNumber;
    }



    public static final String DB_FIELD_EMAIL = "emailAddress";
    public static final String IO_FIELD_EMAIL = "elektroniskPost";

    @OneToMany(mappedBy = "companyRecord", cascade = CascadeType.ALL)
    @JsonProperty(value = "elektroniskPost")
    private Set<ContactRecord> emailAddress;

    public void setEmailAddress(Set<ContactRecord> emailAddress) {
        for (ContactRecord record : emailAddress) {
            record.setType(ContactRecord.Type.EMAILADRESSE);
        }
        this.emailAddress = emailAddress;
    }



    public static final String DB_FIELD_HOMEPAGE = "homepage";
    public static final String IO_FIELD_HOMEPAGE = "hjemmeside";

    @OneToMany(mappedBy = "companyRecord", cascade = CascadeType.ALL)
    @JsonProperty(value = "hjemmeside")
    private Set<ContactRecord> homepage;

    public void setHomepage(Set<ContactRecord> homepage) {
        for (ContactRecord record : homepage) {
            record.setType(ContactRecord.Type.HJEMMESIDE);
        }
        this.homepage = homepage;
    }



    public static final String DB_FIELD_MANDATORY_EMAIL = "mandatoryEmailAddress";
    public static final String IO_FIELD_MANDATORY_EMAIL = "obligatoriskEmail";

    @OneToMany(mappedBy = "companyRecord", cascade = CascadeType.ALL)
    @JsonProperty(value = "obligatoriskEmail")
    private Set<ContactRecord> mandatoryEmailAddress;

    public void setMandatoryEmailAddress(Set<ContactRecord> mandatoryEmailAddress) {
        for (ContactRecord record : mandatoryEmailAddress) {
            record.setType(ContactRecord.Type.OBLIGATORISK_EMAILADRESSE);
        }
        this.mandatoryEmailAddress = mandatoryEmailAddress;
    }



    public static final String DB_FIELD_LIFECYCLE = "lifecycle";
    public static final String IO_FIELD_LIFECYCLE = "livsforloeb";

    @OneToMany(mappedBy = "companyRecord", cascade = CascadeType.ALL)
    @JsonProperty(value = "livsforloeb")
    private Set<LifecycleRecord> lifecycle;

    public Set<LifecycleRecord> getLifecycle() {
        return this.lifecycle;
    }



    public static final String DB_FIELD_PRIMARY_INDUSTRY = "primaryIndustry";
    public static final String IO_FIELD_PRIMARY_INDUSTRY = "hovedbranche";

    @OneToMany(mappedBy = "companyRecord", cascade = CascadeType.ALL)
    @JsonProperty(value = "hovedbranche")
    private Set<CompanyIndustryRecord> primaryIndustry;

    public void setPrimaryIndustry(Set<CompanyIndustryRecord> primaryIndustry) {
        for (CompanyIndustryRecord record : primaryIndustry) {
            record.setIndex(0);
        }
        this.primaryIndustry = primaryIndustry;
    }

    public Set<CompanyIndustryRecord> getPrimaryIndustry() {
        return this.primaryIndustry;
    }


    public static final String DB_FIELD_SECONDARY_INDUSTRY1 = "secondaryIndustry1";
    public static final String IO_FIELD_SECONDARY_INDUSTRY1 = "bibranche1";

    @OneToMany(mappedBy = "companyRecord", cascade = CascadeType.ALL)
    @JsonProperty(value = "bibranche1")
    private Set<CompanyIndustryRecord> secondaryIndustry1;

    public void setSecondaryIndustry1(Set<CompanyIndustryRecord> secondaryIndustryRecords) {
        for (CompanyIndustryRecord record : secondaryIndustryRecords) {
            record.setIndex(1);
        }
        this.secondaryIndustry1 = secondaryIndustryRecords;
    }


    public static final String DB_FIELD_SECONDARY_INDUSTRY2 = "secondaryIndustry2";
    public static final String IO_FIELD_SECONDARY_INDUSTRY2 = "bibranche2";

    @OneToMany(mappedBy = "companyRecord", cascade = CascadeType.ALL)
    @JsonProperty(value = "bibranche2")
    private Set<CompanyIndustryRecord> secondaryIndustry2;

    public void setSecondaryIndustry2(Set<CompanyIndustryRecord> secondaryIndustryRecords) {
        for (CompanyIndustryRecord record : secondaryIndustryRecords) {
            record.setIndex(2);
        }
        this.secondaryIndustry2 = secondaryIndustryRecords;
    }


    public static final String DB_FIELD_SECONDARY_INDUSTRY3 = "secondaryIndustry3";
    public static final String IO_FIELD_SECONDARY_INDUSTRY3 = "bibranche3";

    @OneToMany(mappedBy = "companyRecord", cascade = CascadeType.ALL)
    @JsonProperty(value = "bibranche3")
    private Set<CompanyIndustryRecord> secondaryIndustry3;

    public void setSecondaryIndustry3(Set<CompanyIndustryRecord> secondaryIndustryRecords) {
        for (CompanyIndustryRecord record : secondaryIndustryRecords) {
            record.setIndex(3);
        }
        this.secondaryIndustry3 = secondaryIndustryRecords;
    }


    public static final String DB_FIELD_STATUS = "companyStatus";
    public static final String IO_FIELD_STATUS = "virksomhedsstatus";

    @OneToMany(mappedBy = "companyRecord", cascade = CascadeType.ALL)
    @JsonProperty(value = "virksomhedsstatus")
    private Set<CompanyStatusRecord> companyStatus;

    public Set<CompanyStatusRecord> getCompanyStatus() {
        return this.companyStatus;
    }


    public static final String DB_FIELD_FORM = "companyForm";
    public static final String IO_FIELD_FORM = "virksomhedsform";

    @OneToMany(mappedBy = "companyRecord", cascade = CascadeType.ALL)
    @JsonProperty(value = "virksomhedsform")
    private Set<CompanyFormRecord> companyForm;


    public static final String DB_FIELD_YEARLY_NUMBERS = "yearlyNumbers";
    public static final String IO_FIELD_YEARLY_NUMBERS = "aarsbeskaeftigelse";

    @OneToMany(mappedBy = "companyRecord", cascade = CascadeType.ALL)
    @JsonProperty(value = "aarsbeskaeftigelse")
    private Set<CompanyYearlyNumbersRecord> yearlyNumbers;


    public static final String DB_FIELD_QUARTERLY_NUMBERS = "quarterlyNumbers";
    public static final String IO_FIELD_QUARTERLY_NUMBERS = "kvartalsbeskaeftigelse";

    @OneToMany(mappedBy = "companyRecord", cascade = CascadeType.ALL)
    @JsonProperty(value = "kvartalsbeskaeftigelse")
    private Set<CompanyQuarterlyNumbersRecord> quarterlyNumbers;


    public static final String DB_FIELD_MONTHLY_NUMBERS = "monthlyNumbers";
    public static final String IO_FIELD_MONTHLY_NUMBERS = "maanedsbeskaeftigelse";

    @OneToMany(mappedBy = "companyRecord", cascade = CascadeType.ALL)
    @JsonProperty(value = "maanedsbeskaeftigelse")
    private Set<CompanyMonthlyNumbersRecord> monthlyNumbers;


    public static final String DB_FIELD_ATTRIBUTES = "attributes";
    public static final String IO_FIELD_ATTRIBUTES = "attributter";

    @OneToMany(mappedBy = "companyRecord", cascade = CascadeType.ALL)
    @JsonProperty(value = "attributter")
    private Set<AttributeRecord> attributes;


    public static final String DB_FIELD_P_UNITS = "productionUnits";
    public static final String IO_FIELD_P_UNITS = "penheder";

    @OneToMany(mappedBy = "companyRecord", cascade = CascadeType.ALL)
    @JsonProperty(value = "penheder")
    private Set<CompanyUnitLinkRecord> productionUnits;


    public static final String DB_FIELD_PARTICIPANTS = "participants";
    public static final String IO_FIELD_PARTICIPANTS = "deltagerRelation";

    @OneToMany(mappedBy = "companyRecord", cascade = CascadeType.ALL)
    @JsonProperty(value = "deltagerRelation")
    private Set<CompanyParticipantRelationRecord> participants;


    public static final String DB_FIELD_META = "metadata";
    public static final String IO_FIELD_META = "virksomhedMetadata";

    @OneToOne(mappedBy = "companyRecord", cascade = CascadeType.ALL)
    @JsonProperty(value = "virksomhedMetadata")
    private MetadataRecord metadata;

    public MetadataRecord getMetadata() {
        return this.metadata;
    }


    @Column
    @JsonProperty(value = "samtId")
    private long samtId;

    @Column
    @JsonProperty(value = "fejlRegistreret")
    private boolean registerError;

    @Column
    @JsonProperty(value = "dataAdgang")
    private long dataAccess;


    @Column
    @JsonProperty(value = "fejlVedIndlaesning")
    private boolean loadingError;


                        //"naermesteFremtidigeDato": null,
                        //"fejlBeskrivelse": null,


    @Column
    @JsonProperty(value = "virkningsAktoer")
    private String effectAgent;





    @JsonIgnore
    @Override
    public List<CvrRecord> getAll() {
        ArrayList<CvrRecord> list = new ArrayList<>();
        list.add(this);
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

    public UUID generateUUID() {
        return CompanyEntity.generateUUID(this.cvrNumber);
    }

    @Override
    public void populateBaseData(CompanyBaseData baseData, Session session) {
        baseData.setAdvertProtection(this.advertProtection);
        baseData.setCvrNumber(this.cvrNumber);
    }

}
