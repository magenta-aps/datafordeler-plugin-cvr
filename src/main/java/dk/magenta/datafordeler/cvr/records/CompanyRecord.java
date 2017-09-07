package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.company.CompanyEntity;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by lars on 26-06-17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyRecord extends CvrEntityRecord {

    @JsonProperty(value = "cvrNummer")
    private int cvrNumber;

    public int getCvrNumber() {
        return this.cvrNumber;
    }

    @JsonProperty(value = "reklamebeskyttet")
    private boolean advertProtection;

    @JsonProperty(value = "enhedsNummer")
    private int unitNumber;

    @JsonProperty(value = "enhedstype")
    private String unitType;

    @JsonProperty(value = "names")
    private List<NameRecord> names;

    @JsonProperty(value = "beliggenhedsadresse")
    private List<AddressRecord> locationAddress;

    public void setLocationAddress(List<AddressRecord> locationAddress) {
        for (AddressRecord record : locationAddress) {
            record.setType(AddressRecord.Type.LOCATION);
        }
        this.locationAddress = locationAddress;
    }

    @JsonProperty(value = "postadresse")
    private List<AddressRecord> postalAddress;

    public void setPostalAddress(List<AddressRecord> postalAddress) {
        for (AddressRecord record : postalAddress) {
            record.setType(AddressRecord.Type.POSTAL);
        }
        this.postalAddress = postalAddress;
    }

    @JsonProperty(value = "telefonNummer")
    private List<ContactRecord> phoneNumber;

    public void setPhoneNumber(List<ContactRecord> phoneNumber) {
        for (ContactRecord record : phoneNumber) {
            record.setType(ContactRecord.Type.TELEFONNUMMER);
        }
        this.phoneNumber = phoneNumber;
    }

    @JsonProperty(value = "telefaxNummer")
    private List<ContactRecord> faxNumber;

    public void setFaxNumber(List<ContactRecord> faxNumber) {
        for (ContactRecord record : faxNumber) {
            record.setType(ContactRecord.Type.TELEFAXNUMMER);
        }
        this.faxNumber = faxNumber;
    }

    @JsonProperty(value = "elektroniskPost")
    private List<ContactRecord> emailAddress;

    public void setEmailAddress(List<ContactRecord> emailAddress) {
        for (ContactRecord record : emailAddress) {
            record.setType(ContactRecord.Type.EMAILADRESSE);
        }
        this.emailAddress = emailAddress;
    }

    @JsonProperty(value = "hjemmeside")
    private List<ContactRecord> homepage;

    public void setHomepage(List<ContactRecord> homepage) {
        for (ContactRecord record : homepage) {
            record.setType(ContactRecord.Type.HJEMMESIDE);
        }
        this.homepage = homepage;
    }

    @JsonProperty(value = "obligatoriskEmail")
    private List<ContactRecord> mandatoryEmailAddress;

    public void setMandatoryEmailAddress(List<ContactRecord> mandatoryEmailAddress) {
        for (ContactRecord record : mandatoryEmailAddress) {
            record.setType(ContactRecord.Type.OBLIGATORISK_EMAILADRESSE);
        }
        this.mandatoryEmailAddress = mandatoryEmailAddress;
    }

    @JsonProperty(value = "livsforloeb")
    private List<LifecycleRecord> lifecycle;


    @JsonProperty(value = "hovedbranche")
    private List<CompanyIndustryRecord> primaryIndustry;

    public void setPrimaryIndustry(List<CompanyIndustryRecord> primaryIndustry) {
        for (CompanyIndustryRecord record : primaryIndustry) {
            record.setIndex(0);
        }
        this.primaryIndustry = primaryIndustry;
    }

    @JsonProperty(value = "bibranche1")
    private List<CompanyIndustryRecord> secondaryIndustry1;

    public void setSecondaryIndustry1(List<CompanyIndustryRecord> secondaryIndustryRecords) {
        for (CompanyIndustryRecord record : secondaryIndustryRecords) {
            record.setIndex(1);
        }
        this.secondaryIndustry1 = secondaryIndustryRecords;
    }

    @JsonProperty(value = "bibranche2")
    private List<CompanyIndustryRecord> secondaryIndustry2;

    public void setSecondaryIndustry2(List<CompanyIndustryRecord> secondaryIndustryRecords) {
        for (CompanyIndustryRecord record : secondaryIndustryRecords) {
            record.setIndex(2);
        }
        this.secondaryIndustry2 = secondaryIndustryRecords;
    }

    @JsonProperty(value = "bibranche3")
    private List<CompanyIndustryRecord> secondaryIndustry3;

    public void setSecondaryIndustry3(List<CompanyIndustryRecord> secondaryIndustryRecords) {
        for (CompanyIndustryRecord record : secondaryIndustryRecords) {
            record.setIndex(3);
        }
        this.secondaryIndustry3 = secondaryIndustryRecords;
    }

    @JsonProperty(value = "virksomhedsstatus")
    private List<CompanyStatusRecord> companyStatus;


    @JsonProperty(value = "virksomhedsform")
    private List<CompanyFormRecord> companyForm;

    @JsonProperty(value = "aarsbeskaeftigelse")
    private List<CompanyYearlyNumbersRecord> yearlyNumbers;

    @JsonProperty(value = "kvartalsbeskaeftigelse")
    private List<CompanyQuarterlyNumbersRecord> quarterlyNumbers;

    @JsonProperty(value = "maanedsbeskaeftigelse")
    private List<CompanyMonthlyNumbersRecord> monthlyNumbers;

    @JsonProperty(value = "attributter")
    private List<AttributeRecord> attributes;

    @JsonProperty(value = "penheder")
    private List<CompanyUnitLinkRecord> productionUnits;

    @JsonProperty(value = "deltagerRelation")
    private List<CompanyParticipantRelationRecord> participants;

    @Override
    public List<CvrBaseRecord> getAll() {
        ArrayList<CvrBaseRecord> list = new ArrayList<>();
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
        return list;
    }

    public UUID generateUUID() {
        return CompanyEntity.generateUUID(this.cvrNumber);
    }

    @Override
    public void populateBaseData(CompanyBaseData baseData, QueryManager queryManager, Session session) {
        baseData.setAdvertProtection(this.advertProtection);
        baseData.setCvrNumber(this.cvrNumber);
    }

}
