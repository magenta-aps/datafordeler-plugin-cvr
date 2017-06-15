package dk.magenta.datafordeler.cvr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.magenta.datafordeler.core.database.Identification;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.core.database.SessionManager;
import dk.magenta.datafordeler.core.exception.DataFordelerException;
import dk.magenta.datafordeler.cvr.data.company.*;
import dk.magenta.datafordeler.cvr.data.company.CompanyMainData;
import dk.magenta.datafordeler.cvr.data.company.CompanyTextData;
import dk.magenta.datafordeler.cvr.data.companyunit.*;
import dk.magenta.datafordeler.cvr.data.embeddable.LifeCycleEmbed;
import dk.magenta.datafordeler.cvr.data.embeddable.QuarterlyEmployeeNumbersEmbed;
import dk.magenta.datafordeler.cvr.data.embeddable.YearlyEmployeeNumbersEmbed;
import dk.magenta.datafordeler.cvr.data.unversioned.Address;
import dk.magenta.datafordeler.cvr.data.unversioned.CompanyForm;
import dk.magenta.datafordeler.cvr.data.unversioned.Industry;
import dk.magenta.datafordeler.cvr.data.unversioned.Municipality;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by lars on 10-06-17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ModelTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private QueryManager queryManager;

    @Autowired
    private SessionManager sessionManager;

    @Test
    public void testCompany() throws DataFordelerException, JsonProcessingException {
        Session session = sessionManager.getSessionFactory().openSession();
        Identification identification = new Identification(UUID.randomUUID(), "test");
        CompanyEntity company = new CompanyEntity();
        company.setIdentification(identification);
        company.setCvrNumber(12345678);

        CompanyRegistration registration = new CompanyRegistration(OffsetDateTime.parse("2017-01-01T00:00:00+00:00"), OffsetDateTime.parse("2018-01-01T00:00:00+00:00"), 1);
        CompanyEffect effect1 = new CompanyEffect(registration, OffsetDateTime.parse("2017-07-01T00:00:00+00:00"), null);
        CompanyEffect effect2 = new CompanyEffect(registration, OffsetDateTime.parse("2017-08-01T00:00:00+00:00"), null);

        CompanyBaseData companyData1 = new CompanyBaseData();
        companyData1.addEffect(effect1);

        CompanyBaseData companyData2 = new CompanyBaseData();
        companyData2.addEffect(effect2);

        CompanyMainData mainData = companyData1.getMainData();

        CompanyLifecycleData lifecycleData = companyData1.obtainLifecycleData();
        lifecycleData.setStartDate(OffsetDateTime.parse("2000-01-01T00:00:00+00:00"));


        CompanyBooleanData advertProtectionData = companyData1.obtainAdvertProtectionData();
        advertProtectionData.setData(true);

        CompanyTextData nameData = companyData1.obtainNameData();
        nameData.setData("Some company name");


        CompanyAddressData locationAddressData = companyData1.obtainLocationAddressData();

        Municipality municipality = queryManager.getItem(session, Municipality.class, Collections.singletonMap("code", 101));
        if (municipality == null) {
            municipality = new Municipality();
            municipality.setCode(101);
            municipality.setText("Copenhagen");
            session.saveOrUpdate(municipality);
        }

        HashMap<String, Object> addressData1 = new HashMap<>();
        addressData1.put("roadName", "FoobarRoad");
        addressData1.put("roadCode", 1234);
        addressData1.put("houseNumberFrom", 12);
        addressData1.put("municipality", municipality);
        Address locationAddress = queryManager.getItem(session, Address.class, addressData1);
        if (locationAddress == null) {
            locationAddress = new Address();
            locationAddress.setRoadName((String)addressData1.get("roadName"));
            locationAddress.setRoadCode((int)addressData1.get("roadCode"));
            locationAddress.setHouseNumberFrom((int)addressData1.get("houseNumberFrom"));
            locationAddress.setMunicipality(municipality);
            session.saveOrUpdate(locationAddress);
        }
        locationAddressData.setAddress(locationAddress);



        CompanyAddressData postalAddressData = companyData1.obtainPostalAddressData();
        HashMap<String, Object> addressData2 = new HashMap<>();
        addressData2.put("roadName", "HelloWorldRoad");
        addressData2.put("roadCode", 5678);
        addressData2.put("houseNumberFrom", 34);
        addressData2.put("municipality", municipality);
        Address postalAddress = queryManager.getItem(session, Address.class, addressData2);
        if (postalAddress == null) {
            postalAddress = new Address();
            postalAddress.setRoadName((String)addressData2.get("roadName"));
            postalAddress.setRoadCode((int)addressData2.get("roadCode"));
            postalAddress.setHouseNumberFrom((int)addressData2.get("houseNumberFrom"));
            postalAddress.setMunicipality(municipality);
            session.saveOrUpdate(postalAddress);
        }
        postalAddressData.setAddress(postalAddress);


        CompanyIndustryData primaryIndustryData = companyData2.obtainPrimaryIndustryData();
        Industry primaryIndustry = queryManager.getItem(session, Industry.class, Collections.singletonMap("code", 123456));
        if (primaryIndustry == null) {
            primaryIndustry = new Industry();
            primaryIndustry.setCode(123456);
            primaryIndustry.setText("It company");
            session.saveOrUpdate(primaryIndustry);
        }
        primaryIndustryData.setIndustry(primaryIndustry);


        CompanyIndustryData secondaryIndustryData1 = companyData2.obtainSecondaryIndustryData1();
        Industry secondaryIndustry1 = queryManager.getItem(session, Industry.class, Collections.singletonMap("code", 112358));
        if (secondaryIndustry1 == null) {
            secondaryIndustry1 = new Industry();
            secondaryIndustry1.setCode(112358);
            secondaryIndustry1.setText("Psychiatric institution");
            session.saveOrUpdate(secondaryIndustry1);
        }
        secondaryIndustryData1.setIndustry(secondaryIndustry1);



        CompanyTextData phoneData = companyData1.obtainPhoneData();
        phoneData.setType(CompanyTextData.Type.PHONE);
        phoneData.setData("87654321");

        CompanyTextData faxData = companyData1.obtainFaxData();
        faxData.setType(CompanyTextData.Type.FAX);
        faxData.setData("11112222");

        CompanyTextData emailData = companyData1.obtainEmailData();
        emailData.setType(CompanyTextData.Type.EMAIL);
        emailData.setData("test@example.com");

        CompanyForm companyForm = queryManager.getItem(session, CompanyForm.class, Collections.singletonMap("code", 123));
        if (companyForm == null) {
            companyForm = new CompanyForm();
            companyForm.setCode(123);
            companyForm.setName("A/S");
            companyForm.setResponsibleDatasource("E&S");
            session.saveOrUpdate(companyForm);
        }
        CompanyFormData formData = companyData1.obtainFormData();
        formData.setForm(companyForm);

        YearlyEmployeeNumbersEmbed yearlyEmployeeNumbers = mainData.obtainYearlyEmployeeNumbers();
        yearlyEmployeeNumbers.setYear(2017);
        yearlyEmployeeNumbers.setYearlyEmployeesLow(1);
        yearlyEmployeeNumbers.setYearlyEmployeesHigh(2);
        yearlyEmployeeNumbers.setYearlyFullTimeEquivalentLow(1);
        yearlyEmployeeNumbers.setYearlyFullTimeEquivalentHigh(2);
        yearlyEmployeeNumbers.setYearlyIncludingOwnersLow(1);
        yearlyEmployeeNumbers.setYearlyIncludingOwnersHigh(2);


        QuarterlyEmployeeNumbersEmbed quarterlyEmployeeNumbers = mainData.obtainQuarterlyEmployeeNumbers();
        quarterlyEmployeeNumbers.setYear(2017);
        quarterlyEmployeeNumbers.setQuarter(2);
        quarterlyEmployeeNumbers.setQuarterlyEmployeesLow(1);
        quarterlyEmployeeNumbers.setQuarterlyEmployeesHigh(2);
        quarterlyEmployeeNumbers.setQuarterlyFullTimeEquivalentLow(1);
        quarterlyEmployeeNumbers.setQuarterlyFullTimeEquivalentHigh(2);
        quarterlyEmployeeNumbers.setQuarterlyIncludingOwnersLow(1);
        quarterlyEmployeeNumbers.setQuarterlyIncludingOwnersHigh(2);


        Transaction transaction = session.beginTransaction();


        try {
            queryManager.saveRegistration(session, company, registration);
            transaction.commit();
        } catch (DataFordelerException e) {
            transaction.rollback();
        } finally {
            session.close();
        }

        System.out.println("--------------------------------------");
        System.out.println(objectMapper.writeValueAsString(company));
        System.out.println("--------------------------------------");
    }

    @Test
    public void testCompanyUnit() throws DataFordelerException, JsonProcessingException {
        Session session = sessionManager.getSessionFactory().openSession();
        Identification companyIdentification = new Identification(UUID.randomUUID(), "test");
        CompanyEntity company = new CompanyEntity();
        company.setIdentification(companyIdentification);
        company.setCvrNumber(12345678);

        Identification companyUnitIdentification = new Identification(UUID.randomUUID(), "test");
        CompanyUnitEntity companyUnit = new CompanyUnitEntity();
        companyUnit.setIdentification(companyUnitIdentification);
        companyUnit.setPNumber(11223344);

        CompanyUnitRegistration registration = new CompanyUnitRegistration(OffsetDateTime.parse("2017-01-01T00:00:00+00:00"), OffsetDateTime.parse("2018-01-01T00:00:00+00:00"), 1);
        CompanyUnitEffect effect = new CompanyUnitEffect(registration, OffsetDateTime.parse("2017-07-01T00:00:00+00:00"), null);

        CompanyUnitBaseData companyUnitData = new CompanyUnitBaseData();
        companyUnitData.addEffect(effect);

        CompanyBooleanData companyUnitIsPrimaryData = companyUnitData.getIsPrimaryData();
        companyUnitIsPrimaryData.setData(true);


        CompanyLifecycleData lifecycleData = companyUnitData.obtainLifecycleData();
        lifecycleData.setStartDate(OffsetDateTime.parse("2000-01-01T00:00:00+00:00"));


        CompanyUnitMainData mainData = companyUnitData.getMainData();

        


        CompanyTextData nameData = companyUnitData.getNameData();
        nameData.setData("Some company name");
        nameData.setType(CompanyTextData.Type.NAME);

        Municipality municipality = queryManager.getItem(session, Municipality.class, Collections.singletonMap("code", 101));
        if (municipality == null) {
            municipality = new Municipality();
            municipality.setCode(101);
            municipality.setText("Copenhagen");
            session.saveOrUpdate(municipality);
        }

        HashMap<String, Object> addressData1 = new HashMap<>();
        addressData1.put("roadName", "FoobarRoad");
        addressData1.put("roadCode", 1234);
        addressData1.put("houseNumberFrom", 12);
        addressData1.put("municipality", municipality);
        Address locationAddress = queryManager.getItem(session, Address.class, addressData1);
        if (locationAddress == null) {
            locationAddress = new Address();
            locationAddress.setRoadName((String)addressData1.get("roadName"));
            locationAddress.setRoadCode((int)addressData1.get("roadCode"));
            locationAddress.setHouseNumberFrom((int)addressData1.get("houseNumberFrom"));
            locationAddress.setMunicipality(municipality);
            session.saveOrUpdate(locationAddress);
        }

        CompanyAddressData locationAddressData = companyUnitData.obtainLocationAddressData();
        locationAddressData.setAddress(locationAddress);



        CompanyIndustryData primaryIndustryData = companyUnitData.obtainPrimaryIndustryData();
        Industry primaryIndustry = queryManager.getItem(session, Industry.class, Collections.singletonMap("code", 123456));
        if (primaryIndustry == null) {
            primaryIndustry = new Industry();
            primaryIndustry.setCode(123456);
            primaryIndustry.setText("It company");
            session.saveOrUpdate(primaryIndustry);
        }
        primaryIndustryData.setIndustry(primaryIndustry);

        CompanyTextData phoneData = companyUnitData.getPhoneData();
        phoneData.setType(CompanyTextData.Type.PHONE);
        phoneData.setData("87654321");

        CompanyTextData faxData = companyUnitData.getFaxData();
        faxData.setType(CompanyTextData.Type.FAX);
        faxData.setData("11112222");

        CompanyTextData emailData = companyUnitData.getEmailData();
        emailData.setType(CompanyTextData.Type.EMAIL);
        emailData.setData("test@example.com");

        YearlyEmployeeNumbersEmbed yearlyEmployeeNumbers = mainData.obtainYearlyEmployeeNumbers();
        yearlyEmployeeNumbers.setYear(2017);
        yearlyEmployeeNumbers.setYearlyEmployeesLow(1);
        yearlyEmployeeNumbers.setYearlyEmployeesHigh(2);
        yearlyEmployeeNumbers.setYearlyFullTimeEquivalentLow(1);
        yearlyEmployeeNumbers.setYearlyFullTimeEquivalentHigh(2);
        yearlyEmployeeNumbers.setYearlyIncludingOwnersLow(1);
        yearlyEmployeeNumbers.setYearlyIncludingOwnersHigh(2);

        QuarterlyEmployeeNumbersEmbed quarterlyEmployeeNumbers = mainData.obtainQuarterlyEmployeeNumbers();
        quarterlyEmployeeNumbers.setYear(2017);
        quarterlyEmployeeNumbers.setQuarter(2);
        quarterlyEmployeeNumbers.setQuarterlyEmployeesLow(1);
        quarterlyEmployeeNumbers.setQuarterlyEmployeesHigh(2);
        quarterlyEmployeeNumbers.setQuarterlyFullTimeEquivalentLow(1);
        quarterlyEmployeeNumbers.setQuarterlyFullTimeEquivalentHigh(2);
        quarterlyEmployeeNumbers.setQuarterlyIncludingOwnersLow(1);
        quarterlyEmployeeNumbers.setQuarterlyIncludingOwnersHigh(2);

        CompanyBooleanData isPrimaryData = companyUnitData.getIsPrimaryData();
        isPrimaryData.setData(true);


        Transaction transaction = session.beginTransaction();

        session.saveOrUpdate(company);

        try {
            queryManager.saveRegistration(session, companyUnit, registration);
            transaction.commit();
        } catch (DataFordelerException e) {
            transaction.rollback();
        } finally {
            session.close();
        }

        System.out.println("--------------------------------------");
        System.out.println(objectMapper.writeValueAsString(companyUnit));
        System.out.println("--------------------------------------");
    }
}
