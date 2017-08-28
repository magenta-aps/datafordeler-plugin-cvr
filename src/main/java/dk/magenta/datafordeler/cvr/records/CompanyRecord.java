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
    private boolean advertProtected;

    @JsonProperty(value = "enhedsNummer")
    private int unitNumber;

    @JsonProperty(value = "enhedstype")
    private String unitType;

    @JsonProperty(value = "navne")
    private List<NameRecord> names;

    @JsonProperty(value = "beliggenhedsadresse")
    private List<AddressRecord> locationAddresses;

    public void setLocationAddresses(List<AddressRecord> locationAddresses) {
        for (AddressRecord record : locationAddresses) {
            record.setType(AddressRecord.Type.LOCATION);
        }
        this.locationAddresses = locationAddresses;
    }

    @JsonProperty(value = "postadresse")
    private List<AddressRecord> postalAddresses;

    public void setPostalAddresses(List<AddressRecord> postalAddresses) {
        for (AddressRecord record : postalAddresses) {
            record.setType(AddressRecord.Type.POSTAL);
        }
        this.postalAddresses = postalAddresses;
    }

    @JsonProperty(value = "telefonNummer")
    private List<ContactRecord> phoneRecords;

    public void setPhoneRecords(List<ContactRecord> phoneRecords) {
        for (ContactRecord record : phoneRecords) {
            record.setType(ContactRecord.Type.PHONE);
        }
        this.phoneRecords = phoneRecords;
    }

    @JsonProperty(value = "telefaxNummer")
    private List<ContactRecord> faxRecords;

    public void setFaxRecords(List<ContactRecord> faxRecords) {
        for (ContactRecord record : faxRecords) {
            record.setType(ContactRecord.Type.FAX);
        }
        this.faxRecords = faxRecords;
    }

    @JsonProperty(value = "elektroniskPost")
    private List<ContactRecord> emailRecords;

    public void setEmailRecords(List<ContactRecord> emailRecords) {
        for (ContactRecord record : emailRecords) {
            record.setType(ContactRecord.Type.EMAIL);
        }
        this.emailRecords = emailRecords;
    }

    @JsonProperty(value = "hjemmeside")
    private List<ContactRecord> homepageRecords;

    public void setHomepageRecords(List<ContactRecord> homepageRecords) {
        for (ContactRecord record : homepageRecords) {
            record.setType(ContactRecord.Type.HOMEPAGE);
        }
        this.homepageRecords = homepageRecords;
    }

    @JsonProperty(value = "obligatoriskEmail")
    private List<ContactRecord> mandatoryEmailRecords;

    public void setMandatoryEmailRecords(List<ContactRecord> mandatoryEmailRecords) {
        for (ContactRecord record : mandatoryEmailRecords) {
            record.setType(ContactRecord.Type.MANDATORY_EMAIL);
        }
        this.mandatoryEmailRecords = mandatoryEmailRecords;
    }

    @JsonProperty(value = "livsforloeb")
    private List<LifecycleRecord> lifecycleRecords;


    @JsonProperty(value = "hovedbranche")
    private List<CompanyIndustryRecord> mainIndustryRecords;

    public void setMainIndustryRecords(List<CompanyIndustryRecord> mainIndustryRecords) {
        for (CompanyIndustryRecord record : mainIndustryRecords) {
            record.setIndex(0);
        }
        this.mainIndustryRecords = mainIndustryRecords;
    }

    @JsonProperty(value = "bibranche1")
    private List<CompanyIndustryRecord> secondaryIndustryRecords1;

    public void setSecondaryIndustryRecords1(List<CompanyIndustryRecord> secondaryIndustryRecords) {
        for (CompanyIndustryRecord record : secondaryIndustryRecords) {
            record.setIndex(1);
        }
        this.secondaryIndustryRecords1 = secondaryIndustryRecords;
    }

    @JsonProperty(value = "bibranche2")
    private List<CompanyIndustryRecord> secondaryIndustryRecords2;

    public void setSecondaryIndustryRecords2(List<CompanyIndustryRecord> secondaryIndustryRecords) {
        for (CompanyIndustryRecord record : secondaryIndustryRecords) {
            record.setIndex(2);
        }
        this.secondaryIndustryRecords2 = secondaryIndustryRecords;
    }

    @JsonProperty(value = "bibranche3")
    private List<CompanyIndustryRecord> secondaryIndustryRecords3;

    public void setSecondaryIndustryRecords3(List<CompanyIndustryRecord> secondaryIndustryRecords) {
        for (CompanyIndustryRecord record : secondaryIndustryRecords) {
            record.setIndex(3);
        }
        this.secondaryIndustryRecords3 = secondaryIndustryRecords;
    }

    @JsonProperty(value = "virksomhedsstatus")
    private List<CompanyStatusRecord> statusRecords;


    @JsonProperty(value = "virksomhedsform")
    private List<CompanyFormRecord> formRecords;

    @JsonProperty(value = "aarsbeskaeftigelse")
    private List<CompanyYearlyNumbersRecord> yearlyNumbersRecords;

    @JsonProperty(value = "kvartalsbeskaeftigelse")
    private List<CompanyQuarterlyNumbersRecord> quarterlyNumbersRecords;

    @JsonProperty(value = "maanedsbeskaeftigelse")
    private List<CompanyMonthlyNumbersRecord> monthlyNumbersRecords;

    @JsonProperty(value = "attributter")
    private List<AttributeRecord> attributeRecords;

    @JsonProperty(value = "penheder")
    private List<CompanyUnitLinkRecord> unitLinkRecords;

    @JsonProperty(value = "deltagerRelation")
    private List<CompanyParticipantRelationRecord> participantRelationRecords;

    @Override
    public List<CvrBaseRecord> getAll() {
        ArrayList<CvrBaseRecord> list = new ArrayList<>();
        list.add(this);
        if (this.names != null) {
            list.addAll(this.names);
        }
        if (this.locationAddresses != null) {
            list.addAll(this.locationAddresses);
        }
        if (this.postalAddresses != null) {
            list.addAll(this.postalAddresses);
        }
        if (this.phoneRecords != null) {
            list.addAll(this.phoneRecords);
        }
        if (this.faxRecords != null) {
            list.addAll(this.faxRecords);
        }
        if (this.emailRecords != null) {
            list.addAll(this.emailRecords);
        }
        if (this.homepageRecords != null) {
            list.addAll(this.homepageRecords);
        }
        if (this.mandatoryEmailRecords != null) {
            list.addAll(this.mandatoryEmailRecords);
        }
        if (this.lifecycleRecords != null) {
            list.addAll(this.lifecycleRecords);
        }
        if (this.mainIndustryRecords != null) {
            list.addAll(this.mainIndustryRecords);
        }
        if (this.secondaryIndustryRecords1 != null) {
            list.addAll(this.secondaryIndustryRecords1);
        }
        if (this.secondaryIndustryRecords2 != null) {
            list.addAll(this.secondaryIndustryRecords2);
        }
        if (this.secondaryIndustryRecords3 != null) {
            list.addAll(this.secondaryIndustryRecords3);
        }
        if (this.statusRecords != null) {
            list.addAll(this.statusRecords);
        }
        if (this.formRecords != null) {
            list.addAll(this.formRecords);
        }
        if (this.yearlyNumbersRecords != null) {
            list.addAll(this.yearlyNumbersRecords);
        }
        if (this.quarterlyNumbersRecords != null) {
            list.addAll(this.quarterlyNumbersRecords);
        }
        if (this.monthlyNumbersRecords != null) {
            list.addAll(this.monthlyNumbersRecords);
        }
        if (this.attributeRecords != null) {
            for (AttributeRecord attributeRecord : this.attributeRecords) {
                list.addAll(attributeRecord.getValues());
            }
        }
        if (this.unitLinkRecords != null) {
            list.addAll(this.unitLinkRecords);
        }
        if (this.participantRelationRecords != null) {
            list.addAll(this.participantRelationRecords);
        }
        return list;
    }

    public UUID generateUUID() {
        return CompanyEntity.generateUUID(this.cvrNumber);
    }

    @Override
    public void populateBaseData(CompanyBaseData baseData, QueryManager queryManager, Session session) {
        baseData.setAdvertProtection(this.advertProtected);
        baseData.setUnitNumber(this.unitNumber);
    }

}
