package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.exception.ParseException;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitEntity;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Base record for CompanyUnit data, parsed from JSON into a tree of objects
 * with this class at the base.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyUnitRecord extends CvrEntityRecord {

    @JsonProperty(value = "pNummer")
    private int pNumber;

    public int getpNumber() {
        return this.pNumber;
    }

    @JsonProperty(value = "reklamebeskyttet")
    private boolean advertProtection;

    @JsonProperty(value = "enhedsNummer")
    private int unitNumber;

    @JsonProperty(value = "navne")
    public List<NameRecord> names;

    @JsonProperty(value = "beliggenhedsadresse")
    public List<AddressRecord> locationAddress;

    public void setLocationAddress(List<AddressRecord> locationAddress) {
        for (AddressRecord record : locationAddress) {
            record.setType(AddressRecord.Type.LOCATION);
        }
        this.locationAddress = locationAddress;
    }

    @JsonProperty(value = "postadresse")
    public List<AddressRecord> postalAddress;

    public void setPostalAddress(List<AddressRecord> postalAddress) {
        for (AddressRecord record : postalAddress) {
            record.setType(AddressRecord.Type.POSTAL);
        }
        this.postalAddress = postalAddress;
    }

    @JsonProperty(value = "telefonNummer")
    public List<ContactRecord> phoneNumber;

    public void setPhoneNumber(List<ContactRecord> phoneNumber) {
        for (ContactRecord record : phoneNumber) {
            record.setType(ContactRecord.Type.TELEFONNUMMER);
        }
        this.phoneNumber = phoneNumber;
    }

    @JsonProperty(value = "telefaxNummer")
    public List<ContactRecord> faxNumber;

    public void setFaxNumber(List<ContactRecord> faxNumber) {
        for (ContactRecord record : faxNumber) {
            record.setType(ContactRecord.Type.TELEFAXNUMMER);
        }
        this.faxNumber = faxNumber;
    }

    @JsonProperty(value = "elektroniskPost")
    public List<ContactRecord> emailAddress;

    public void setEmailAddress(List<ContactRecord> emailAddress) {
        for (ContactRecord record : emailAddress) {
            record.setType(ContactRecord.Type.EMAILADRESSE);
        }
        this.emailAddress = emailAddress;
    }

    @JsonProperty(value = "livsforloeb")
    public List<LifecycleRecord> lifecycle;


    @JsonProperty(value = "hovedbranche")
    public List<CompanyIndustryRecord> primaryIndustry;

    public void setPrimaryIndustry(List<CompanyIndustryRecord> primaryIndustry) {
        for (CompanyIndustryRecord record : primaryIndustry) {
            record.setIndex(0);
        }
        this.primaryIndustry = primaryIndustry;
    }

    @JsonProperty(value = "bibranche1")
    public List<CompanyIndustryRecord> secondaryIndustry1;

    public void setSecondaryIndustry1(List<CompanyIndustryRecord> secondaryIndustryRecords) {
        for (CompanyIndustryRecord record : secondaryIndustryRecords) {
            record.setIndex(1);
        }
        this.secondaryIndustry1 = secondaryIndustryRecords;
    }

    @JsonProperty(value = "bibranche2")
    public List<CompanyIndustryRecord> secondaryIndustry2;

    public void setSecondaryIndustry2(List<CompanyIndustryRecord> secondaryIndustryRecords) {
        for (CompanyIndustryRecord record : secondaryIndustryRecords) {
            record.setIndex(2);
        }
        this.secondaryIndustry2 = secondaryIndustryRecords;
    }

    @JsonProperty(value = "bibranche3")
    public List<CompanyIndustryRecord> secondaryIndustry3;

    public void setSecondaryIndustry3(List<CompanyIndustryRecord> secondaryIndustryRecords) {
        for (CompanyIndustryRecord record : secondaryIndustryRecords) {
            record.setIndex(3);
        }
        this.secondaryIndustry3 = secondaryIndustryRecords;
    }

    @JsonProperty(value = "aarsbeskaeftigelse")
    public List<CompanyYearlyNumbersRecord> yearlyNumbersRecords;

    @JsonProperty(value = "kvartalsbeskaeftigelse")
    public List<CompanyQuarterlyNumbersRecord> quarterlyNumbersRecords;

    //@JsonProperty(value = "maanedsbeskaeftigelse")
    //public List<CompanyMonthlyNumbersRecord> monthlyNumbersRecords;

    @JsonProperty(value = "attributter")
    public List<AttributeRecord> attributes;

    @JsonProperty(value = "deltagerRelation")
    private List<CompanyParticipantRelationRecord> participantRelations;

    @JsonProperty(value = "virksomhedsrelation")
    private List<CompanyLinkRecord> companyLinkRecords;


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
        if (this.yearlyNumbersRecords != null) {
            list.addAll(this.yearlyNumbersRecords);
        }
        if (this.quarterlyNumbersRecords != null) {
            list.addAll(this.quarterlyNumbersRecords);
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
