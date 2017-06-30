package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantBaseData;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lars on 26-06-17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyUnitRecord extends BaseRecord {

    @JsonProperty(value = "pNummer")
    private int pNumber;

    public int getpNumber() {
        return this.pNumber;
    }

    @JsonProperty(value = "reklamebeskyttet")
    private boolean advertProtected;

    @JsonProperty(value = "enhedsNummer")
    private int unitNumber;

    @JsonProperty(value = "navne")
    public List<NameRecord> names;

    @JsonProperty(value = "beliggenhedsadresse")
    public List<AddressRecord> locationAddresses;

    public void setLocationAddresses(List<AddressRecord> locationAddresses) {
        for (AddressRecord record : locationAddresses) {
            record.setType(AddressRecord.Type.LOCATION);
        }
        this.locationAddresses = locationAddresses;
    }

    @JsonProperty(value = "postadresse")
    public List<AddressRecord> postalAddresses;

    public void setPostalAddresses(List<AddressRecord> postalAddresses) {
        for (AddressRecord record : postalAddresses) {
            record.setType(AddressRecord.Type.POSTAL);
        }
        this.postalAddresses = postalAddresses;
    }

    @JsonProperty(value = "telefonNummer")
    public List<ContactRecord> phoneRecords;

    public void setPhoneRecords(List<ContactRecord> phoneRecords) {
        for (ContactRecord record : phoneRecords) {
            record.setType(ContactRecord.Type.PHONE);
        }
        this.phoneRecords = phoneRecords;
    }

    @JsonProperty(value = "telefaxNummer")
    public List<ContactRecord> faxRecords;

    public void setFaxRecords(List<ContactRecord> faxRecords) {
        for (ContactRecord record : faxRecords) {
            record.setType(ContactRecord.Type.FAX);
        }
        this.faxRecords = faxRecords;
    }

    @JsonProperty(value = "elektroniskPost")
    public List<ContactRecord> emailRecords;

    public void setEmailRecords(List<ContactRecord> emailRecords) {
        for (ContactRecord record : emailRecords) {
            record.setType(ContactRecord.Type.EMAIL);
        }
        this.emailRecords = emailRecords;
    }

    @JsonProperty(value = "livsforloeb")
    public List<LifecycleRecord> lifecycleRecords;


    @JsonProperty(value = "hovedbranche")
    public List<CompanyIndustryRecord> mainIndustryRecords;

    public void setMainIndustryRecords(List<CompanyIndustryRecord> mainIndustryRecords) {
        for (CompanyIndustryRecord record : mainIndustryRecords) {
            record.setIndex(0);
        }
        this.mainIndustryRecords = mainIndustryRecords;
    }

    @JsonProperty(value = "bibranche1")
    public List<CompanyIndustryRecord> secondaryIndustryRecords1;

    public void setSecondaryIndustryRecords1(List<CompanyIndustryRecord> secondaryIndustryRecords) {
        for (CompanyIndustryRecord record : secondaryIndustryRecords) {
            record.setIndex(1);
        }
        this.secondaryIndustryRecords1 = secondaryIndustryRecords;
    }

    @JsonProperty(value = "bibranche2")
    public List<CompanyIndustryRecord> secondaryIndustryRecords2;

    public void setSecondaryIndustryRecords2(List<CompanyIndustryRecord> secondaryIndustryRecords) {
        for (CompanyIndustryRecord record : secondaryIndustryRecords) {
            record.setIndex(2);
        }
        this.secondaryIndustryRecords2 = secondaryIndustryRecords;
    }

    @JsonProperty(value = "bibranche3")
    public List<CompanyIndustryRecord> secondaryIndustryRecords3;

    public void setSecondaryIndustryRecords3(List<CompanyIndustryRecord> secondaryIndustryRecords) {
        for (CompanyIndustryRecord record : secondaryIndustryRecords) {
            record.setIndex(3);
        }
        this.secondaryIndustryRecords3 = secondaryIndustryRecords;
    }

    @JsonProperty(value = "aarsbeskaeftigelse")
    public List<CompanyYearlyNumbersRecord> yearlyNumbersRecords;

    @JsonProperty(value = "kvartalsbeskaeftigelse")
    public List<CompanyQuarterlyNumbersRecord> quarterlyNumbersRecords;

    //@JsonProperty(value = "maanedsbeskaeftigelse")
    //public List<CompanyMonthlyNumbersRecord> monthlyNumbersRecords;

    @JsonProperty(value = "attributter")
    public List<AttributeRecord> attributeRecords;


    // virksomhedsrelation
    // enhedstype
    // dataAdgang
    // enhedsNummer
    // reklamebeskyttet
    // deltagerRelation
    // virkningsAktoer
    // brancheAnsvarskode
    // naermesteFremtidigeDato
    // samtId

    public List<BaseRecord> getAll() {
        ArrayList<BaseRecord> list = new ArrayList<>();
        list.addAll(this.names);
        list.addAll(this.locationAddresses);
        list.addAll(this.postalAddresses);
        list.addAll(this.phoneRecords);
        list.addAll(this.faxRecords);
        list.addAll(this.emailRecords);
        list.addAll(this.lifecycleRecords);
        list.addAll(this.mainIndustryRecords);
        list.addAll(this.secondaryIndustryRecords1);
        list.addAll(this.secondaryIndustryRecords2);
        list.addAll(this.secondaryIndustryRecords3);
        list.addAll(this.yearlyNumbersRecords);
        list.addAll(this.quarterlyNumbersRecords);
        for (AttributeRecord attributeRecord : this.attributeRecords) {
            list.addAll(attributeRecord.getValues());
        }
        return list;
    }


    @Override
    public void populateBaseData(CompanyBaseData baseData, QueryManager queryManager, Session session) {
        // noop
    }

    @Override
    public void populateBaseData(CompanyUnitBaseData baseData, QueryManager queryManager, Session session) {
        baseData.setAdvertProtection(this.advertProtected);
        baseData.setUnitNumber(this.unitNumber);
    }

    @Override
    public void populateBaseData(ParticipantBaseData baseData, QueryManager queryManager, Session session) {
        // noop
    }
}
