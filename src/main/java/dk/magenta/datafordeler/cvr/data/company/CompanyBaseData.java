package dk.magenta.datafordeler.cvr.data.company;

import dk.magenta.datafordeler.core.database.DataItem;
import dk.magenta.datafordeler.core.database.Identification;
import dk.magenta.datafordeler.core.database.LookupDefinition;
import dk.magenta.datafordeler.core.exception.ParseException;
import dk.magenta.datafordeler.cvr.data.CvrData;
import dk.magenta.datafordeler.cvr.data.DetailData;
import dk.magenta.datafordeler.cvr.data.shared.*;
import dk.magenta.datafordeler.cvr.data.unversioned.Address;
import dk.magenta.datafordeler.cvr.data.unversioned.CompanyForm;
import dk.magenta.datafordeler.cvr.data.unversioned.CompanyStatus;
import dk.magenta.datafordeler.cvr.data.unversioned.Industry;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.annotations.SortComparator;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.time.OffsetDateTime;
import java.util.*;

/**
 * Created by lars on 12-06-17.
 */
@Entity
@Table(name="cvr_company_basedata")
public class CompanyBaseData extends CvrData<CompanyEffect, CompanyBaseData> {

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyFormData formData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyStatusData statusData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private LifecycleData lifecycleData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private BooleanData advertProtectionData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private IntegerData unitNumberData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private AddressData locationAddressData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private AddressData postalAddressData;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy(value = "year asc")
    private List<YearlyEmployeeNumbersData> yearlyEmployeeNumbersData = null;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy(value = "year asc, quarter asc")
    private List<QuarterlyEmployeeNumbersData> quarterlyEmployeeNumbersData = null;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy(value = "year asc, month asc")
    private List<MonthlyEmployeeNumbersData> monthlyEmployeeNumbersData = null;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private IndustryData primaryIndustryData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private IndustryData secondaryIndustryData1;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private IndustryData secondaryIndustryData2;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private IndustryData secondaryIndustryData3;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private TextData nameData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private ContactData phoneData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private ContactData emailData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private ContactData faxData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private ContactData homepageData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private ContactData mandatoryEmailData;

    @ManyToMany(mappedBy = "companyBases")
    private Set<CompanyUnitLink> unitData = new HashSet<>();

    @ManyToMany(mappedBy = "companyBases")
    private Set<ParticipantLink> participantData = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy(value = "type")
    private SortedSet<AttributeData> attributeData = new TreeSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @SortComparator(ParticipantRelationData.Comparator.class)
    private SortedSet<ParticipantRelationData> participantRelationData = new TreeSet<>();



    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        if (this.formData != null) {
            map.put("virksomhedsform", this.formData.getForm());
        }
        if (this.statusData != null) {
            map.put("status", this.statusData.getStatus());
        }
        if (this.lifecycleData != null) {
            map.put("livsforløb", this.lifecycleData.asMap());
        }
        if (this.advertProtectionData != null) {
            map.put("reklamebeskyttelse", this.advertProtectionData.getData());
        }
        if (this.unitNumberData != null) {
            map.put("enhedsNummer", this.unitNumberData.getData());
        }
        if (this.locationAddressData != null) {
            map.put("beliggenhedsadresse", this.locationAddressData.getAddress());
        }
        if (this.postalAddressData != null) {
            map.put("postadresse", this.postalAddressData.getAddress());
        }
        if (this.yearlyEmployeeNumbersData != null) {
            map.put("årsbeskæftigelse", this.yearlyEmployeeNumbersData);
        }
        if (this.quarterlyEmployeeNumbersData != null) {
            map.put("kvartalsbeskæftigelse", this.quarterlyEmployeeNumbersData);
        }
        if (this.monthlyEmployeeNumbersData != null) {
            map.put("månedsbeskæftigelse", this.monthlyEmployeeNumbersData);
        }
        if (this.primaryIndustryData != null) {
            map.put("hovedbranche", this.primaryIndustryData.getIndustry());
        }
        if (this.secondaryIndustryData1 != null) {
            map.put("bibranche1", this.secondaryIndustryData1.getIndustry());
        }
        if (this.secondaryIndustryData2 != null) {
            map.put("bibranche2", this.secondaryIndustryData2.getIndustry());
        }
        if (this.secondaryIndustryData3 != null) {
            map.put("bibranche3", this.secondaryIndustryData3.getIndustry());
        }
        if (this.nameData != null) {
            map.put("navn", this.nameData.getData());
        }
        if (this.phoneData != null) {
            map.put("telefon", this.phoneData);
        }
        if (this.emailData != null) {
            map.put("email", this.emailData);
        }
        if (this.faxData != null) {
            map.put("fax", this.faxData);
        }
        if (this.homepageData != null) {
            map.put("hjemmeside", this.homepageData);
        }
        if (this.mandatoryEmailData != null) {
            map.put("obligatoriskEmail", this.mandatoryEmailData);
        }
        if (this.unitData != null && !this.unitData.isEmpty()) {
            map.put("enheder", this.unitData);
        }
        if (this.participantData != null && !this.participantData.isEmpty()) {
            map.put("deltagere", this.participantData);
        }
        if (this.attributeData != null && !this.attributeData.isEmpty()) {
            map.put("attributter", this.attributeData);
        }
        if (this.participantRelationData != null && !this.participantRelationData.isEmpty()) {
            map.put("deltagerRelation", this.participantRelationData);
        }
        return map;
    }

    public void setForm(CompanyForm form) {
        if (this.formData == null) {
            this.formData = new CompanyFormData();
        }
        this.formData.setForm(form);
    }
    public boolean hasForm() {
        return this.formData != null;
    }

    public void setStatus(CompanyStatus status) {
        if (this.statusData == null) {
            this.statusData = new CompanyStatusData();
        }
        this.statusData.setStatus(status);
    }
    public void setLifecycleStartDate(OffsetDateTime startDate) {
        if (this.lifecycleData == null) {
            this.lifecycleData = new LifecycleData();
        }
        this.lifecycleData.setStartDate(startDate);
    }
    public void setLifecycleEndDate(OffsetDateTime endDate) {
        if (this.lifecycleData == null) {
            this.lifecycleData = new LifecycleData();
        }
        this.lifecycleData.setEndDate(endDate);
    }
    public void setAdvertProtection(boolean advertProtection) {
        if (this.advertProtectionData == null) {
            this.advertProtectionData = new BooleanData(BooleanData.Type.ADVERT_PROTECTION);
        }
        this.advertProtectionData.setData(advertProtection);
    }
    public void setUnitNumber(long unitNumber) {
        if (this.unitNumberData == null) {
            this.unitNumberData = new IntegerData();
        }
        this.unitNumberData.setData(unitNumber);
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

    public void setYearlyEmployeeNumbers(int year, Integer employeesLow, Integer employeesHigh, Integer fulltimeEquivalentLow, Integer fulltimeEquivalentHigh, Integer includingOwnersLow, Integer includingOwnersHigh) throws ParseException {
        if (this.yearlyEmployeeNumbersData == null) {
            this.yearlyEmployeeNumbersData = new ArrayList<>();
        }
        YearlyEmployeeNumbersData yearlyEmployeeNumbersData = null;
        for (YearlyEmployeeNumbersData data : this.yearlyEmployeeNumbersData) {
            if (data.getYear() == year) {
                yearlyEmployeeNumbersData = data;
            }
        }
        if (yearlyEmployeeNumbersData == null) {
            yearlyEmployeeNumbersData = new YearlyEmployeeNumbersData();
            this.yearlyEmployeeNumbersData.add(yearlyEmployeeNumbersData);
        }
        yearlyEmployeeNumbersData.setYear(year);
        yearlyEmployeeNumbersData.setEmployeesLow(employeesLow);
        yearlyEmployeeNumbersData.setEmployeesHigh(employeesHigh);
        yearlyEmployeeNumbersData.setFullTimeEquivalentLow(fulltimeEquivalentLow);
        yearlyEmployeeNumbersData.setFullTimeEquivalentHigh(fulltimeEquivalentHigh);
        yearlyEmployeeNumbersData.setIncludingOwnersLow(includingOwnersLow);
        yearlyEmployeeNumbersData.setIncludingOwnersHigh(includingOwnersHigh);
    }
    public void setQuarterlyEmployeeNumbers(int year, int quarter, Integer employeesLow, Integer employeesHigh, Integer fulltimeEquivalentLow, Integer fulltimeEquivalentHigh, Integer includingOwnersLow, Integer includingOwnersHigh) throws ParseException {
        if (this.quarterlyEmployeeNumbersData == null) {
            this.quarterlyEmployeeNumbersData = new ArrayList<>();
        }
        QuarterlyEmployeeNumbersData quarterlyEmployeeNumbersData = null;
        for (QuarterlyEmployeeNumbersData data : this.quarterlyEmployeeNumbersData) {
            if (data.getYear() == year && data.getQuarter() == quarter) {
                quarterlyEmployeeNumbersData = data;
            }
        }
        if (quarterlyEmployeeNumbersData == null) {
            quarterlyEmployeeNumbersData = new QuarterlyEmployeeNumbersData();
            this.quarterlyEmployeeNumbersData.add(quarterlyEmployeeNumbersData);
        }
        quarterlyEmployeeNumbersData.setYear(year);
        quarterlyEmployeeNumbersData.setQuarter(quarter);
        quarterlyEmployeeNumbersData.setEmployeesLow(employeesLow);
        quarterlyEmployeeNumbersData.setEmployeesHigh(employeesHigh);
        quarterlyEmployeeNumbersData.setFullTimeEquivalentLow(fulltimeEquivalentLow);
        quarterlyEmployeeNumbersData.setFullTimeEquivalentHigh(fulltimeEquivalentHigh);
        quarterlyEmployeeNumbersData.setIncludingOwnersLow(includingOwnersLow);
        quarterlyEmployeeNumbersData.setIncludingOwnersHigh(includingOwnersHigh);
    }
    public void setMonthlyEmployeeNumbers(int year, int month, Integer employeesLow, Integer employeesHigh, Integer fulltimeEquivalentLow, Integer fulltimeEquivalentHigh, Integer includingOwnersLow, Integer includingOwnersHigh) throws ParseException {
        if (this.monthlyEmployeeNumbersData == null) {
            this.monthlyEmployeeNumbersData = new ArrayList<>();
        }
        MonthlyEmployeeNumbersData monthlyEmployeeNumbersData = null;
        for (MonthlyEmployeeNumbersData data : this.monthlyEmployeeNumbersData) {
            if (data.getYear() == year && data.getMonth() == month) {
                monthlyEmployeeNumbersData = data;
            }
        }
        if (monthlyEmployeeNumbersData == null) {
            monthlyEmployeeNumbersData = new MonthlyEmployeeNumbersData();
            this.monthlyEmployeeNumbersData.add(monthlyEmployeeNumbersData);
        }
        monthlyEmployeeNumbersData.setYear(year);
        monthlyEmployeeNumbersData.setMonth(month);
        monthlyEmployeeNumbersData.setEmployeesLow(employeesLow);
        monthlyEmployeeNumbersData.setEmployeesHigh(employeesHigh);
        monthlyEmployeeNumbersData.setFullTimeEquivalentLow(fulltimeEquivalentLow);
        monthlyEmployeeNumbersData.setFullTimeEquivalentHigh(fulltimeEquivalentHigh);
        monthlyEmployeeNumbersData.setIncludingOwnersLow(includingOwnersLow);
        monthlyEmployeeNumbersData.setIncludingOwnersHigh(includingOwnersHigh);
    }

    public void setPrimaryIndustry(Industry industry) {
        if (this.primaryIndustryData == null) {
            this.primaryIndustryData = new IndustryData(true);
        }
        this.primaryIndustryData.setIndustry(industry);
    }
    public void setSecondaryIndustry1(Industry industry) {
        if (this.secondaryIndustryData1 == null) {
            this.secondaryIndustryData1 = new IndustryData(false);
        }
        this.secondaryIndustryData1.setIndustry(industry);
    }
    public void setSecondaryIndustry2(Industry industry) {
        if (this.secondaryIndustryData2 == null) {
            this.secondaryIndustryData2 = new IndustryData(false);
        }
        this.secondaryIndustryData2.setIndustry(industry);
    }
    public void setSecondaryIndustry3(Industry industry) {
        if (this.secondaryIndustryData3 == null) {
            this.secondaryIndustryData3 = new IndustryData(false);
        }
        this.secondaryIndustryData3.setIndustry(industry);
    }

    public void setName(String name) {
        if (this.nameData == null) {
            this.nameData = new TextData(TextData.Type.NAME);
        }
        this.nameData.setData(name);
    }
    public void setPhone(String phone, boolean secret) {
        if (this.phoneData == null) {
            this.phoneData = new ContactData(ContactData.Type.PHONE);
        }
        this.phoneData.setData(phone);
        this.phoneData.setSecret(secret);
    }
    public void setEmail(String email, boolean secret) {
        if (this.emailData == null) {
            this.emailData = new ContactData(ContactData.Type.EMAIL);
        }
        this.emailData.setData(email);
        this.emailData.setSecret(secret);
    }
    public void setFax(String fax, boolean secret) {
        if (this.faxData == null) {
            this.faxData = new ContactData(ContactData.Type.FAX);
        }
        this.faxData.setData(fax);
        this.faxData.setSecret(secret);
    }
    public void setHomepage(String email, boolean secret) {
        if (this.homepageData == null) {
            this.homepageData = new ContactData(ContactData.Type.HOMEPAGE);
        }
        this.homepageData.setData(email);
        this.homepageData.setSecret(secret);
    }
    public void setMandatoryEmail(String fax, boolean secret) {
        if (this.mandatoryEmailData == null) {
            this.mandatoryEmailData = new ContactData(ContactData.Type.MANDATORY_EMAIL);
        }
        this.mandatoryEmailData.setData(fax);
        this.mandatoryEmailData.setSecret(secret);
    }

    public void addCompanyUnit(int pNumber) {
        for (CompanyUnitLink link : this.unitData) {
            if (link.getpNumber() == pNumber) {
                return;
            }
        }
        CompanyUnitLink link = new CompanyUnitLink();
        link.setpNumber(pNumber);
        this.unitData.add(link);
    }
    public void addParticipant(ParticipantLink participantLink) {
        this.participantData.add(participantLink);
    }
    public void addAttribute(String type, String valueType, String value, int sequenceNumber) {
        AttributeData attributeData = null;
        for (AttributeData data : this.attributeData) {
            if (data.getType().equals(type) && data.getSequenceNumber() == sequenceNumber && data.getValueType().equals(valueType) && data.getValue().equals(value)) {
                attributeData = data;
            }
        }
        if (attributeData == null) {
            attributeData = new AttributeData();
            this.addAttribute(attributeData);
        }
        attributeData.setType(type);
        attributeData.setValueType(valueType);
        attributeData.setValue(value);
        attributeData.setSequenceNumber(sequenceNumber);
    }
    public void addAttribute(AttributeData attributeData) {
        this.attributeData.add(attributeData);
    }

    public void addParticipantRelation(Identification participant, Set<Identification> organizations) {
        ParticipantRelationData participantRelationData = null;
        for (ParticipantRelationData data : this.participantRelationData) {
            if (participant != null && data.getParticipant().equals(participant)) {
                participantRelationData = data;
            }
        }
        if (participantRelationData == null) {
            participantRelationData = new ParticipantRelationData();
            this.participantRelationData.add(participantRelationData);
        }
        if (participant != null) {
            participantRelationData.setParticipant(participant);
        }
        for (Identification organization : organizations) {
            participantRelationData.addOrganization(organization);
        }
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
        if (this.unitNumberData != null) {
            lookupDefinition.putAll("unitNumberData", this.unitNumberData.databaseFields());
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
        if (this.participantRelationData != null) {
            lookupDefinition.putAll("participantRelationData", DetailData.listDatabaseFields(this.participantRelationData));
        }
        return lookupDefinition;
    }

    public void forceLoad(Session session) {
        for (Field field : this.getClass().getDeclaredFields()) {
            try {
                Hibernate.initialize(field.get(this));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        Hibernate.initialize(this.yearlyEmployeeNumbersData);
        Hibernate.initialize(this.quarterlyEmployeeNumbersData);
        Hibernate.initialize(this.monthlyEmployeeNumbersData);
        Hibernate.initialize(this.unitData);
        Hibernate.initialize(this.participantData);
        Hibernate.initialize(this.attributeData);
        for (ParticipantRelationData participantRelationData : this.participantRelationData) {
            participantRelationData.forceLoad(session);
        }
    }
}
