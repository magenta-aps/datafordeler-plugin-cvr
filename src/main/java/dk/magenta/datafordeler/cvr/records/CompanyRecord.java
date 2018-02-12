package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.company.CompanyEntity;
import dk.magenta.datafordeler.cvr.data.company.CompanyUnitLink;
import dk.magenta.datafordeler.cvr.data.shared.QuarterlyEmployeeNumbersData;
import dk.magenta.datafordeler.cvr.data.unversioned.CompanyForm;
import dk.magenta.datafordeler.cvr.data.unversioned.CompanyStatus;
import org.hibernate.Session;
import org.hibernate.annotations.Where;

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

    @OneToMany(mappedBy = NameRecord.DB_FIELD_COMPANY, targetEntity = NameRecord.class, cascade = CascadeType.ALL)
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

    @OneToMany(mappedBy = AddressRecord.DB_FIELD_COMPANY, targetEntity = AddressRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = "beliggenhedsadresse")
    private Set<AddressRecord> locationAddress;

    public void setLocationAddress(Set<AddressRecord> locationAddress) {
        for (AddressRecord record : locationAddress) {
            record.setType(AddressRecord.Type.LOCATION);
            record.setCompanyRecord(this);
        }
        this.locationAddress = locationAddress;
    }

    public Set<AddressRecord> getLocationAddress() {
        return this.locationAddress;
    }




    public static final String DB_FIELD_POSTAL_ADDRESS = "postalAddress";
    public static final String IO_FIELD_POSTAL_ADDRESS = "postadresse";

    @OneToMany(mappedBy = AddressRecord.DB_FIELD_COMPANY, targetEntity = AddressRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = "postadresse")
    private Set<AddressRecord> postalAddress;

    public void setPostalAddress(Set<AddressRecord> postalAddress) {
        for (AddressRecord record : postalAddress) {
            record.setType(AddressRecord.Type.POSTAL);
            record.setCompanyRecord(this);
        }
        this.postalAddress = postalAddress;
    }

    public Set<AddressRecord> getPostalAddress() {
        return this.postalAddress;
    }



    public static final String DB_FIELD_PHONE = "phoneNumber";
    public static final String IO_FIELD_PHONE = "telefonNummer";

    @OneToMany(mappedBy = ContactRecord.DB_FIELD_COMPANY, targetEntity = ContactRecord.class, cascade = CascadeType.ALL)
    @Where(clause = ContactRecord.DB_FIELD_TYPE+"="+ContactRecord.TYPE_TELEFONNUMMER)
    @JsonProperty(value = "telefonNummer")
    private Set<ContactRecord> phoneNumber;

    public void setPhoneNumber(Set<ContactRecord> phoneNumber) {
        for (ContactRecord record : phoneNumber) {
            record.setType(ContactRecord.TYPE_TELEFONNUMMER);
            record.setCompanyRecord(this);
        }
        this.phoneNumber = phoneNumber;
    }



    public static final String DB_FIELD_FAX = "faxNumber";
    public static final String IO_FIELD_FAX = "telefaxNummer";

    @OneToMany(mappedBy = ContactRecord.DB_FIELD_COMPANY, targetEntity = ContactRecord.class, cascade = CascadeType.ALL)
    @Where(clause = ContactRecord.DB_FIELD_TYPE+"="+ContactRecord.TYPE_TELEFAXNUMMER)
    @JsonProperty(value = "telefaxNummer")
    private Set<ContactRecord> faxNumber;

    public void setFaxNumber(Set<ContactRecord> faxNumber) {
        for (ContactRecord record : faxNumber) {
            record.setType(ContactRecord.TYPE_TELEFAXNUMMER);
            record.setCompanyRecord(this);
        }
        this.faxNumber = faxNumber;
    }



    public static final String DB_FIELD_EMAIL = "emailAddress";
    public static final String IO_FIELD_EMAIL = "elektroniskPost";

    @OneToMany(mappedBy = ContactRecord.DB_FIELD_COMPANY, targetEntity = ContactRecord.class, cascade = CascadeType.ALL)
    @Where(clause = ContactRecord.DB_FIELD_TYPE+"="+ContactRecord.TYPE_EMAILADRESSE)
    @JsonProperty(value = "elektroniskPost")
    private Set<ContactRecord> emailAddress;

    public void setEmailAddress(Set<ContactRecord> emailAddress) {
        for (ContactRecord record : emailAddress) {
            record.setType(ContactRecord.TYPE_EMAILADRESSE);
            record.setCompanyRecord(this);
        }
        this.emailAddress = emailAddress;
    }



    public static final String DB_FIELD_HOMEPAGE = "homepage";
    public static final String IO_FIELD_HOMEPAGE = "hjemmeside";

    @OneToMany(mappedBy = ContactRecord.DB_FIELD_COMPANY, targetEntity = ContactRecord.class, cascade = CascadeType.ALL)
    @Where(clause = ContactRecord.DB_FIELD_TYPE+"="+ContactRecord.TYPE_HJEMMESIDE)
    @JsonProperty(value = "hjemmeside")
    private Set<ContactRecord> homepage;

    public void setHomepage(Set<ContactRecord> homepage) {
        for (ContactRecord record : homepage) {
            record.setType(ContactRecord.TYPE_HJEMMESIDE);
            record.setCompanyRecord(this);
        }
        this.homepage = homepage;
    }



    public static final String DB_FIELD_MANDATORY_EMAIL = "mandatoryEmailAddress";
    public static final String IO_FIELD_MANDATORY_EMAIL = "obligatoriskEmail";

    @OneToMany(mappedBy = ContactRecord.DB_FIELD_COMPANY, targetEntity = ContactRecord.class, cascade = CascadeType.ALL)
    @Where(clause = ContactRecord.DB_FIELD_TYPE+"="+ContactRecord.TYPE_OBLIGATORISK_EMAILADRESSE)
    @JsonProperty(value = "obligatoriskEmail")
    private Set<ContactRecord> mandatoryEmailAddress;

    public void setMandatoryEmailAddress(Set<ContactRecord> mandatoryEmailAddress) {
        for (ContactRecord record : mandatoryEmailAddress) {
            record.setType(ContactRecord.TYPE_OBLIGATORISK_EMAILADRESSE);
            record.setCompanyRecord(this);
        }
        this.mandatoryEmailAddress = mandatoryEmailAddress;
    }



    public static final String DB_FIELD_LIFECYCLE = "lifecycle";
    public static final String IO_FIELD_LIFECYCLE = "livsforloeb";

    @OneToMany(mappedBy = LifecycleRecord.DB_FIELD_COMPANY, targetEntity = LifecycleRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = "livsforloeb")
    private Set<LifecycleRecord> lifecycle;

    public Set<LifecycleRecord> getLifecycle() {
        return this.lifecycle;
    }

    public void setLifecycle(Set<LifecycleRecord> lifecycle) {
        this.lifecycle = lifecycle;
        for (LifecycleRecord lifecycleRecord : lifecycle) {
            lifecycleRecord.setCompanyRecord(this);
        }
    }



    public static final String DB_FIELD_PRIMARY_INDUSTRY = "primaryIndustry";
    public static final String IO_FIELD_PRIMARY_INDUSTRY = "hovedbranche";

    @OneToMany(mappedBy = CompanyIndustryRecord.DB_FIELD_COMPANY, targetEntity = CompanyIndustryRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = "hovedbranche")
    private Set<CompanyIndustryRecord> primaryIndustry;

    public void setPrimaryIndustry(Set<CompanyIndustryRecord> primaryIndustry) {
        for (CompanyIndustryRecord record : primaryIndustry) {
            record.setIndex(0);
            record.setCompanyRecord(this);
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
            record.setCompanyRecord(this);
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
            record.setCompanyRecord(this);
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
            record.setCompanyRecord(this);
        }
        this.secondaryIndustry3 = secondaryIndustryRecords;
    }


    public static final String DB_FIELD_STATUS = "companyStatus";
    public static final String IO_FIELD_STATUS = "virksomhedsstatus";

    @OneToMany(mappedBy = CompanyStatusRecord.DB_FIELD_COMPANY, targetEntity = CompanyStatusRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = "virksomhedsstatus")
    private Set<CompanyStatusRecord> companyStatus;

    public Set<CompanyStatusRecord> getCompanyStatus() {
        return this.companyStatus;
    }

    public void setCompanyStatus(Set<CompanyStatusRecord> companyStatus) {
        this.companyStatus = companyStatus;
        for (CompanyStatusRecord statusRecord : companyStatus) {
            statusRecord.setCompanyRecord(this);
        }
    }



    public static final String DB_FIELD_FORM = "companyForm";
    public static final String IO_FIELD_FORM = "virksomhedsform";

    @OneToMany(mappedBy = CompanyFormRecord.DB_FIELD_COMPANY, targetEntity = CompanyFormRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = "virksomhedsform")
    private Set<CompanyFormRecord> companyForm;


    public void setCompanyForm(Set<CompanyFormRecord> companyForm) {
        this.companyForm = companyForm;
        for (CompanyFormRecord formRecord : companyForm) {
            formRecord.setCompanyRecord(this);
        }
    }



    public static final String DB_FIELD_YEARLY_NUMBERS = "yearlyNumbers";
    public static final String IO_FIELD_YEARLY_NUMBERS = "aarsbeskaeftigelse";

    @OneToMany(mappedBy = CompanyYearlyNumbersRecord.DB_FIELD_COMPANY, targetEntity = CompanyYearlyNumbersRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = "aarsbeskaeftigelse")
    private Set<CompanyYearlyNumbersRecord> yearlyNumbers;

    public void setYearlyNumbers(Set<CompanyYearlyNumbersRecord> yearlyNumbers) {
        this.yearlyNumbers = yearlyNumbers;
        for (CompanyYearlyNumbersRecord yearlyNumbersRecord : yearlyNumbers) {
            yearlyNumbersRecord.setCompanyRecord(this);
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
            quarterlyNumbersRecord.setCompanyRecord(this);
        }
    }



    public static final String DB_FIELD_MONTHLY_NUMBERS = "monthlyNumbers";
    public static final String IO_FIELD_MONTHLY_NUMBERS = "maanedsbeskaeftigelse";

    @OneToMany(mappedBy = CompanyMonthlyNumbersRecord.DB_FIELD_COMPANY, targetEntity = CompanyMonthlyNumbersRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = "maanedsbeskaeftigelse")
    private Set<CompanyMonthlyNumbersRecord> monthlyNumbers;

    public void setMonthlyNumbers(Set<CompanyMonthlyNumbersRecord> monthlyNumbers) {
        this.monthlyNumbers = monthlyNumbers;
        for (CompanyMonthlyNumbersRecord monthlyNumbersRecord : monthlyNumbers) {
            monthlyNumbersRecord.setCompanyRecord(this);
        }
    }



    public static final String DB_FIELD_ATTRIBUTES = "attributes";
    public static final String IO_FIELD_ATTRIBUTES = "attributter";

    @OneToMany(mappedBy = AttributeRecord.DB_FIELD_COMPANY, targetEntity = AttributeRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = "attributter")
    private Set<AttributeRecord> attributes;

    public void setAttributes(Set<AttributeRecord> attributes) {
        this.attributes = attributes;
        for (AttributeRecord attributeRecord : attributes) {
            attributeRecord.setCompanyRecord(this);
        }
    }



    public static final String DB_FIELD_P_UNITS = "productionUnits";
    public static final String IO_FIELD_P_UNITS = "penheder";

    @OneToMany(mappedBy = CompanyUnitLinkRecord.DB_FIELD_COMPANY, targetEntity = CompanyUnitLinkRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = "penheder")
    private Set<CompanyUnitLinkRecord> productionUnits;

    public void setProductionUnits(Set<CompanyUnitLinkRecord> productionUnits) {
        this.productionUnits = productionUnits;
        for (CompanyUnitLinkRecord unitLinkRecord : productionUnits) {
            unitLinkRecord.setCompanyRecord(this);
        }
    }



    public static final String DB_FIELD_PARTICIPANTS = "participants";
    public static final String IO_FIELD_PARTICIPANTS = "deltagerRelation";

    @OneToMany(mappedBy = CompanyParticipantRelationRecord.DB_FIELD_COMPANY, targetEntity = CompanyParticipantRelationRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = "deltagerRelation")
    private Set<CompanyParticipantRelationRecord> participants;

    public void setParticipants(Set<CompanyParticipantRelationRecord> participants) {
        this.participants = participants;
        for (CompanyParticipantRelationRecord participantRelationRecord : participants) {
            participantRelationRecord.setCompanyRecord(this);
        }
    }



    public static final String DB_FIELD_META = "metadata";
    public static final String IO_FIELD_META = "virksomhedMetadata";

    @OneToOne(mappedBy = MetadataRecord.DB_FIELD_COMPANY, targetEntity = MetadataRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = "virksomhedMetadata")
    private MetadataRecord metadata;

    public void setMetadata(MetadataRecord metadata) {
        this.metadata = metadata;
        this.metadata.setCompanyRecord(this);
    }

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
