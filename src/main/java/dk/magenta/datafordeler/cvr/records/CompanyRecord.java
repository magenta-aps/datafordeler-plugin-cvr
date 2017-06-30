package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.company.CompanyEntity;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantBaseData;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lars on 26-06-17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyRecord extends BaseRecord {

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

    public List<BaseRecord> getAll() {
        ArrayList<BaseRecord> list = new ArrayList<>();
        list.add(this);
        list.addAll(this.names);
        list.addAll(this.locationAddresses);
        list.addAll(this.postalAddresses);
        list.addAll(this.phoneRecords);
        list.addAll(this.faxRecords);
        list.addAll(this.emailRecords);
        list.addAll(this.homepageRecords);
        list.addAll(this.mandatoryEmailRecords);
        list.addAll(this.lifecycleRecords);
        list.addAll(this.mainIndustryRecords);
        list.addAll(this.secondaryIndustryRecords1);
        list.addAll(this.secondaryIndustryRecords2);
        list.addAll(this.secondaryIndustryRecords3);
        list.addAll(this.statusRecords);
        list.addAll(this.formRecords);
        list.addAll(this.yearlyNumbersRecords);
        list.addAll(this.quarterlyNumbersRecords);
        list.addAll(this.monthlyNumbersRecords);
        for (AttributeRecord attributeRecord : this.attributeRecords) {
            list.addAll(attributeRecord.getValues());
        }
        list.addAll(this.unitLinkRecords);
        return list;
    }

    @Override
    public void populateBaseData(CompanyBaseData data, QueryManager queryManager, Session session) {
        data.setAdvertProtection(this.advertProtected);
        data.setUnitNumber(this.unitNumber);
        data.setUnitType(this.unitType);
    }

    @Override
    public void populateBaseData(CompanyUnitBaseData baseData, QueryManager queryManager, Session session) {
        // noop
    }

    @Override
    public void populateBaseData(ParticipantBaseData baseData, QueryManager queryManager, Session session) {
        // noop
    }
}
