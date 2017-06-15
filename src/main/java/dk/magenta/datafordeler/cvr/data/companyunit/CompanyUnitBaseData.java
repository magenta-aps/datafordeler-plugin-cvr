package dk.magenta.datafordeler.cvr.data.companyunit;

import dk.magenta.datafordeler.core.database.DataItem;
import dk.magenta.datafordeler.core.database.LookupDefinition;
import dk.magenta.datafordeler.cvr.data.company.*;
import dk.magenta.datafordeler.cvr.data.unversioned.Address;
import dk.magenta.datafordeler.cvr.data.unversioned.Industry;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by lars on 12-06-17.
 */
@Entity
@Table(name="cvr_companyunit_basedata")
public class CompanyUnitBaseData extends DataItem<CompanyUnitEffect, CompanyUnitBaseData> {

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyLifecycleData lifecycleData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyBooleanData advertProtectionData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyAddressData locationAddressData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyAddressData postalAddressData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyYearlyEmployeeNumbersData yearlyEmployeeNumbersData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyQuarterlyEmployeeNumbersData quarterlyEmployeeNumbersData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyIndustryData primaryIndustryData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyIndustryData secondaryIndustryData1;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyIndustryData secondaryIndustryData2;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyIndustryData secondaryIndustryData3;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyTextData nameData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyTextData phoneData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyTextData emailData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyTextData faxData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyBooleanData isPrimaryData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyUnitCvrData companyData;

    @ManyToMany(mappedBy = "companyUnitBases")
    private Set<CompanyParticipantLink> participantData = new HashSet<>();


    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        if (this.lifecycleData != null) {
            map.put("lifecycle", this.lifecycleData.asMap());
        }
        if (this.advertProtectionData != null) {
            map.put("advertProtection", this.advertProtectionData.getData());
        }
        if (this.locationAddressData != null) {
            map.put("locationAddress", this.locationAddressData.getAddress());
        }
        if (this.postalAddressData != null) {
            map.put("postalAddress", this.postalAddressData.getAddress());
        }
        if (this.yearlyEmployeeNumbersData != null) {
            map.put("yearlyEmployeeNumbers", this.yearlyEmployeeNumbersData);
        }
        if (this.quarterlyEmployeeNumbersData != null) {
            map.put("quarterlyEmployeeNumbers", this.quarterlyEmployeeNumbersData);
        }
        if (this.primaryIndustryData != null) {
            map.put("primaryIndustry", this.primaryIndustryData.getIndustry());
        }
        if (this.secondaryIndustryData1 != null) {
            map.put("secondaryIndustry1", this.secondaryIndustryData1.getIndustry());
        }
        if (this.secondaryIndustryData2 != null) {
            map.put("secondaryIndustry2", this.secondaryIndustryData2.getIndustry());
        }
        if (this.secondaryIndustryData3 != null) {
            map.put("secondaryIndustry3", this.secondaryIndustryData3.getIndustry());
        }
        if (this.nameData != null) {
            map.put("name", this.nameData.getData());
        }
        if (this.phoneData != null) {
            map.put("phone", this.phoneData.getData());
        }
        if (this.emailData != null) {
            map.put("email", this.emailData.getData());
        }
        if (this.faxData != null) {
            map.put("fax", this.faxData.getData());
        }
        if (this.isPrimaryData != null) {
            map.put("isPrimary", this.isPrimaryData.getData());
        }
        if (this.companyData != null) {
            map.put("company", this.companyData);
        }
        if (this.participantData != null && !this.participantData.isEmpty()) {
            map.put("participants", this.participantData);
        }
        return map;
    }

    public void setLifecycleStartDate(OffsetDateTime startDate) {
        if (this.lifecycleData == null) {
            this.lifecycleData = new CompanyLifecycleData();
        }
        this.lifecycleData.setStartDate(startDate);
    }
    public void setLifecycleEndDate(OffsetDateTime endDate) {
        if (this.lifecycleData == null) {
            this.lifecycleData = new CompanyLifecycleData();
        }
        this.lifecycleData.setEndDate(endDate);
    }
    public void setAdvertProtection(boolean advertProtection) {
        if (this.advertProtectionData == null) {
            this.advertProtectionData = new CompanyBooleanData(CompanyBooleanData.Type.ADVERT_PROTECTION);
        }
        this.advertProtectionData.setData(advertProtection);
    }
    public void setLocationAddress(Address address) {
        if (this.locationAddressData == null) {
            this.locationAddressData = new CompanyAddressData();
        }
        this.locationAddressData.setAddress(address);
    }
    public void setPostalAddress(Address address) {
        if (this.postalAddressData == null) {
            this.postalAddressData = new CompanyAddressData();
        }
        this.postalAddressData.setAddress(address);
    }

    public void setYearlyEmployeeNumbers(int year, int employeesLow, int employeesHigh, int fulltimeEquivalentLow, int fulltimeEquivalentHigh, int includingOwnersLow, int includingOwnersHigh) {
        if (this.yearlyEmployeeNumbersData == null) {
            this.yearlyEmployeeNumbersData = new CompanyYearlyEmployeeNumbersData();
        }
        this.yearlyEmployeeNumbersData.setYear(year);
        this.yearlyEmployeeNumbersData.setEmployeesLow(employeesLow);
        this.yearlyEmployeeNumbersData.setEmployeesHigh(employeesHigh);
        this.yearlyEmployeeNumbersData.setFullTimeEquivalentLow(fulltimeEquivalentLow);
        this.yearlyEmployeeNumbersData.setFullTimeEquivalentHigh(fulltimeEquivalentHigh);
        this.yearlyEmployeeNumbersData.setIncludingOwnersLow(includingOwnersLow);
        this.yearlyEmployeeNumbersData.setIncludingOwnersHigh(includingOwnersHigh);
    }
    public void setQuarterlyEmployeeNumbers(int year, int quarter, int employeesLow, int employeesHigh, int fulltimeEquivalentLow, int fulltimeEquivalentHigh, int includingOwnersLow, int includingOwnersHigh) {
        if (this.quarterlyEmployeeNumbersData == null) {
            this.quarterlyEmployeeNumbersData = new CompanyQuarterlyEmployeeNumbersData();
        }
        this.quarterlyEmployeeNumbersData.setYear(year);
        this.quarterlyEmployeeNumbersData.setQuarter(quarter);
        this.quarterlyEmployeeNumbersData.setEmployeesLow(employeesLow);
        this.quarterlyEmployeeNumbersData.setEmployeesHigh(employeesHigh);
        this.quarterlyEmployeeNumbersData.setFullTimeEquivalentLow(fulltimeEquivalentLow);
        this.quarterlyEmployeeNumbersData.setFullTimeEquivalentHigh(fulltimeEquivalentHigh);
        this.quarterlyEmployeeNumbersData.setIncludingOwnersLow(includingOwnersLow);
        this.quarterlyEmployeeNumbersData.setIncludingOwnersHigh(includingOwnersHigh);
    }

    public void setPrimaryIndustry(Industry industry) {
        if (this.primaryIndustryData == null) {
            this.primaryIndustryData = new CompanyIndustryData(true);
        }
        this.primaryIndustryData.setIndustry(industry);
    }
    public void setSecondaryIndustry1(Industry industry) {
        if (this.secondaryIndustryData1 == null) {
            this.secondaryIndustryData1 = new CompanyIndustryData(false);
        }
        this.secondaryIndustryData1.setIndustry(industry);
    }
    public void setSecondaryIndustry2(Industry industry) {
        if (this.secondaryIndustryData2 == null) {
            this.secondaryIndustryData2 = new CompanyIndustryData(false);
        }
        this.secondaryIndustryData2.setIndustry(industry);
    }
    public void setSecondaryIndustry3(Industry industry) {
        if (this.secondaryIndustryData3 == null) {
            this.secondaryIndustryData3 = new CompanyIndustryData(false);
        }
        this.secondaryIndustryData3.setIndustry(industry);
    }

    public void setName(String name) {
        if (this.nameData == null) {
            this.nameData = new CompanyTextData(CompanyTextData.Type.NAME);
        }
        this.nameData.setData(name);
    }
    public void setPhone(String phone) {
        if (this.phoneData == null) {
            this.phoneData = new CompanyTextData(CompanyTextData.Type.PHONE);
        }
        this.phoneData.setData(phone);
    }
    public void setEmail(String email) {
        if (this.emailData == null) {
            this.emailData = new CompanyTextData(CompanyTextData.Type.EMAIL);
        }
        this.emailData.setData(email);
    }
    public void setFax(String fax) {
        if (this.faxData == null) {
            this.faxData = new CompanyTextData(CompanyTextData.Type.FAX);
        }
        this.faxData.setData(fax);
    }

    public CompanyBooleanData getIsPrimaryData() {
        if (this.isPrimaryData == null) {
            this.isPrimaryData = new CompanyBooleanData(CompanyBooleanData.Type.IS_PRIMARY_UNIT);
        }
        return this.isPrimaryData;
    }
    public CompanyUnitCvrData getCompanyData() {
        if (this.companyData == null) {
            this.companyData = new CompanyUnitCvrData();
        }
        return this.companyData;
    }
    public void addParticipant(CompanyParticipantLink participantLink) {
        this.participantData.add(participantLink);
    }

    @Override
    public LookupDefinition getLookupDefinition() {
        LookupDefinition lookupDefinition = new LookupDefinition();
        if (this.lifecycleData != null) {
            lookupDefinition.putAll("lifecycleData", this.lifecycleData.databaseFields());
        }
        if (this.locationAddressData != null) {
            lookupDefinition.putAll("locationAddressData", this.locationAddressData.databaseFields());
        }
        if (this.postalAddressData != null) {
            lookupDefinition.putAll("postalAddressData", this.postalAddressData.databaseFields());
        }
        if (this.yearlyEmployeeNumbersData != null) {
            lookupDefinition.putAll("yearlyEmployeeNumbersData", this.yearlyEmployeeNumbersData.databaseFields());
        }
        if (this.quarterlyEmployeeNumbersData != null) {
            lookupDefinition.putAll("quarterlyEmployeeNumbersData", this.quarterlyEmployeeNumbersData.databaseFields());
        }
        if (this.primaryIndustryData != null) {
            lookupDefinition.putAll("primaryIndustryData", this.primaryIndustryData.databaseFields());
        }
        if (this.secondaryIndustryData1 != null) {
            lookupDefinition.putAll("secondaryIndustryData1", this.secondaryIndustryData1.databaseFields());
        }
        if (this.secondaryIndustryData2 != null) {
            lookupDefinition.putAll("secondaryIndustryData2", this.secondaryIndustryData2.databaseFields());
        }
        if (this.secondaryIndustryData3 != null) {
            lookupDefinition.putAll("secondaryIndustryData3", this.secondaryIndustryData3.databaseFields());
        }
        if (this.nameData != null) {
            lookupDefinition.putAll("nameData", this.nameData.databaseFields());
        }
        if (this.phoneData != null) {
            lookupDefinition.putAll("phoneData", this.phoneData.databaseFields());
        }
        if (this.emailData != null) {
            lookupDefinition.putAll("emailData", this.emailData.databaseFields());
        }
        if (this.faxData != null) {
            lookupDefinition.putAll("faxData", this.faxData.databaseFields());
        }
        if (this.isPrimaryData != null) {
            lookupDefinition.putAll("isPrimaryData", this.isPrimaryData.databaseFields());
        }
        if (this.companyData != null) {
            lookupDefinition.putAll("companyData", this.companyData.databaseFields());
        }
        return lookupDefinition;
    }

}
