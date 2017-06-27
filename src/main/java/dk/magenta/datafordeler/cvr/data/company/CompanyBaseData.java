package dk.magenta.datafordeler.cvr.data.company;

import dk.magenta.datafordeler.core.database.DataItem;
import dk.magenta.datafordeler.core.database.LookupDefinition;
import dk.magenta.datafordeler.cvr.data.shared.*;
import dk.magenta.datafordeler.cvr.data.unversioned.Address;
import dk.magenta.datafordeler.cvr.data.unversioned.CompanyForm;
import dk.magenta.datafordeler.cvr.data.unversioned.CompanyStatus;
import dk.magenta.datafordeler.cvr.data.unversioned.Industry;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.*;

/**
 * Created by lars on 12-06-17.
 */
@Entity
@Table(name="cvr_company_basedata")
public class CompanyBaseData extends DataItem<CompanyEffect, CompanyBaseData> {

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyFormData formData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyStatusData statusData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyLifecycleData lifecycleData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyBooleanData advertProtectionData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private AddressData locationAddressData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private AddressData postalAddressData;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy(value = "year asc")
    private List<CompanyYearlyEmployeeNumbersData> yearlyEmployeeNumbersData;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy(value = "year asc, quarter asc")
    private List<CompanyQuarterlyEmployeeNumbersData> quarterlyEmployeeNumbersData;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy(value = "year asc, month asc")
    private List<CompanyMonthlyEmployeeNumbersData> monthlyEmployeeNumbersData;

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
    private CompanyContactData phoneData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyContactData emailData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyContactData faxData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyContactData homepageData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyContactData mandatoryEmailData;

    @ManyToMany(mappedBy = "companyBases")
    private Set<CompanyUnitLink> unitData = new HashSet<>();

    @ManyToMany(mappedBy = "companyBases")
    private Set<CompanyParticipantLink> participantData = new HashSet<>();

    @ManyToMany(mappedBy = "companyBases")
    private Set<CompanyCreditData> creditData = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    private Set<AttributeData> attributeData = new HashSet<>();


    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        if (this.formData != null) {
            map.put("form", this.formData.getForm());
        }
        if (this.statusData != null) {
            map.put("status", this.statusData.getStatus());
        }
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
        if (this.monthlyEmployeeNumbersData != null) {
            map.put("monthlyEmployeeNumbers", this.monthlyEmployeeNumbersData);
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
        if (this.homepageData != null) {
            map.put("homepage", this.homepageData.getData());
        }
        if (this.mandatoryEmailData != null) {
            map.put("fax", this.mandatoryEmailData.getData());
        }
        if (this.unitData != null && !this.unitData.isEmpty()) {
            map.put("units", this.unitData);
        }
        if (this.participantData != null && !this.participantData.isEmpty()) {
            map.put("participants", this.participantData);
        }
        if (this.creditData != null && !this.creditData.isEmpty()) {
            map.put("credit", this.creditData);
        }
        if (this.attributeData != null && !this.attributeData.isEmpty()) {
            map.put("attributes", this.attributeData);
        }
        return map;
    }


    public void setForm(CompanyForm form) {
        if (this.formData == null) {
            this.formData = new CompanyFormData();
        }
        this.formData.setForm(form);
    }
    public void setStatus(CompanyStatus status) {
        if (this.statusData == null) {
            this.statusData = new CompanyStatusData();
        }
        this.statusData.setStatus(status);
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
            this.locationAddressData = new AddressData();
        }
        this.locationAddressData.setAddress(address);
    }
    public void setPostalAddress(Address address) {
        if (this.postalAddressData == null) {
            this.postalAddressData = new AddressData();
        }
        this.postalAddressData.setAddress(address);
    }

    public void addYearlyEmployeeNumbers(int year, Integer employeesLow, Integer employeesHigh, Integer fulltimeEquivalentLow, Integer fulltimeEquivalentHigh, Integer includingOwnersLow, Integer includingOwnersHigh) {
        if (this.yearlyEmployeeNumbersData == null) {
            //this.yearlyEmployeeNumbersData = new CompanyYearlyEmployeeNumbersData();
            this.yearlyEmployeeNumbersData = new ArrayList<>();
        }
        CompanyYearlyEmployeeNumbersData yearlyEmployeeNumbersData = new CompanyYearlyEmployeeNumbersData();
        yearlyEmployeeNumbersData.setYear(year);
        yearlyEmployeeNumbersData.setEmployeesLow(employeesLow);
        yearlyEmployeeNumbersData.setEmployeesHigh(employeesHigh);
        yearlyEmployeeNumbersData.setFullTimeEquivalentLow(fulltimeEquivalentLow);
        yearlyEmployeeNumbersData.setFullTimeEquivalentHigh(fulltimeEquivalentHigh);
        yearlyEmployeeNumbersData.setIncludingOwnersLow(includingOwnersLow);
        yearlyEmployeeNumbersData.setIncludingOwnersHigh(includingOwnersHigh);
        this.yearlyEmployeeNumbersData.add(yearlyEmployeeNumbersData);
    }
    public void addQuarterlyEmployeeNumbers(int year, int quarter, Integer employeesLow, Integer employeesHigh, Integer fulltimeEquivalentLow, Integer fulltimeEquivalentHigh, Integer includingOwnersLow, Integer includingOwnersHigh) {
        if (this.quarterlyEmployeeNumbersData == null) {
            //this.quarterlyEmployeeNumbersData = new CompanyQuarterlyEmployeeNumbersData();
            this.quarterlyEmployeeNumbersData = new ArrayList<>();
        }
        CompanyQuarterlyEmployeeNumbersData quarterlyEmployeeNumbersData = new CompanyQuarterlyEmployeeNumbersData();
        quarterlyEmployeeNumbersData.setYear(year);
        quarterlyEmployeeNumbersData.setQuarter(quarter);
        quarterlyEmployeeNumbersData.setEmployeesLow(employeesLow);
        quarterlyEmployeeNumbersData.setEmployeesHigh(employeesHigh);
        quarterlyEmployeeNumbersData.setFullTimeEquivalentLow(fulltimeEquivalentLow);
        quarterlyEmployeeNumbersData.setFullTimeEquivalentHigh(fulltimeEquivalentHigh);
        quarterlyEmployeeNumbersData.setIncludingOwnersLow(includingOwnersLow);
        quarterlyEmployeeNumbersData.setIncludingOwnersHigh(includingOwnersHigh);
        this.quarterlyEmployeeNumbersData.add(quarterlyEmployeeNumbersData);
    }
    public void addMonthlyEmployeeNumbers(int year, int month, Integer employeesLow, Integer employeesHigh, Integer fulltimeEquivalentLow, Integer fulltimeEquivalentHigh, Integer includingOwnersLow, Integer includingOwnersHigh) {
        if (this.monthlyEmployeeNumbersData == null) {
            //this.monthlyEmployeeNumbersData = new CompanyMonthlyEmployeeNumbersData();
            this.monthlyEmployeeNumbersData = new ArrayList<>();
        }
        CompanyMonthlyEmployeeNumbersData monthlyEmployeeNumbersData = new CompanyMonthlyEmployeeNumbersData();
        monthlyEmployeeNumbersData.setYear(year);
        monthlyEmployeeNumbersData.setMonth(month);
        monthlyEmployeeNumbersData.setEmployeesLow(employeesLow);
        monthlyEmployeeNumbersData.setEmployeesHigh(employeesHigh);
        monthlyEmployeeNumbersData.setFullTimeEquivalentLow(fulltimeEquivalentLow);
        monthlyEmployeeNumbersData.setFullTimeEquivalentHigh(fulltimeEquivalentHigh);
        monthlyEmployeeNumbersData.setIncludingOwnersLow(includingOwnersLow);
        monthlyEmployeeNumbersData.setIncludingOwnersHigh(includingOwnersHigh);
        this.monthlyEmployeeNumbersData.add(monthlyEmployeeNumbersData);
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
    public void setPhone(String phone, boolean secret) {
        if (this.phoneData == null) {
            this.phoneData = new CompanyContactData(CompanyContactData.Type.PHONE);
        }
        this.phoneData.setData(phone);
        this.phoneData.setSecret(secret);
    }
    public void setEmail(String email, boolean secret) {
        if (this.emailData == null) {
            this.emailData = new CompanyContactData(CompanyContactData.Type.EMAIL);
        }
        this.emailData.setData(email);
        this.emailData.setSecret(secret);
    }
    public void setFax(String fax, boolean secret) {
        if (this.faxData == null) {
            this.faxData = new CompanyContactData(CompanyContactData.Type.FAX);
        }
        this.faxData.setData(fax);
        this.faxData.setSecret(secret);
    }
    public void setHomepage(String email, boolean secret) {
        if (this.homepageData == null) {
            this.homepageData = new CompanyContactData(CompanyContactData.Type.HOMEPAGE);
        }
        this.homepageData.setData(email);
        this.homepageData.setSecret(secret);
    }
    public void setMandatoryEmail(String fax, boolean secret) {
        if (this.mandatoryEmailData == null) {
            this.mandatoryEmailData = new CompanyContactData(CompanyContactData.Type.MANDATORY_EMAIL);
        }
        this.mandatoryEmailData.setData(fax);
        this.mandatoryEmailData.setSecret(secret);
    }

    public void addCompanyUnit(CompanyUnitLink unitLink) {
        this.unitData.add(unitLink);
    }
    public void addParticipant(CompanyParticipantLink participantLink) {
        this.participantData.add(participantLink);
    }
    public void addCreditData(CompanyCreditData creditData) {
        this.creditData.add(creditData);
    }
    public void addAttribute(String type, String valueType, String value, int sequenceNumber) {
        AttributeData attributeData = new AttributeData();
        attributeData.setType(type);
        attributeData.setValueType(valueType);
        attributeData.setValue(value);
        attributeData.setSequenceNumber(sequenceNumber);
        this.addAttribute(attributeData);
    }
    public void addAttribute(AttributeData attributeData) {
        this.attributeData.add(attributeData);
    }

    @Override
    public LookupDefinition getLookupDefinition() {
        LookupDefinition lookupDefinition = new LookupDefinition();
        lookupDefinition.setMatchNulls(true);

        if (this.lifecycleData != null) {
            lookupDefinition.putAll("lifecycleData", this.lifecycleData.databaseFields());
        }
        if (this.formData != null) {
            lookupDefinition.putAll("formData", this.formData.databaseFields());
        }
        if (this.statusData != null) {
            lookupDefinition.putAll("statusData", this.statusData.databaseFields());
        }
        if (this.advertProtectionData != null) {
            lookupDefinition.putAll("advertProtectionData", this.advertProtectionData.databaseFields());
        }
        if (this.locationAddressData != null) {
            lookupDefinition.putAll("locationAddressData", this.locationAddressData.databaseFields());
        }
        if (this.postalAddressData != null) {
            lookupDefinition.putAll("postalAddressData", this.postalAddressData.databaseFields());
        }
        /*if (this.yearlyEmployeeNumbersData != null) {
            lookupDefinition.putAll("yearlyEmployeeNumbersData", this.yearlyEmployeeNumbersData.databaseFields());
        }*/
        /*if (this.quarterlyEmployeeNumbersData != null) {
            lookupDefinition.putAll("quarterlyEmployeeNumbersData", this.quarterlyEmployeeNumbersData.databaseFields());
        }*/
        /*if (this.monthlyEmployeeNumbersData != null) {
            lookupDefinition.putAll("monthlyEmployeeNumbersData", this.monthlyEmployeeNumbersData.databaseFields());
        }*/
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
        return lookupDefinition;
    }

}
