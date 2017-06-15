package dk.magenta.datafordeler.cvr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.magenta.datafordeler.core.database.Identification;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.core.database.SessionManager;
import dk.magenta.datafordeler.core.exception.DataFordelerException;
import dk.magenta.datafordeler.cvr.data.company.*;
import dk.magenta.datafordeler.cvr.data.company.CompanyTextData;
import dk.magenta.datafordeler.cvr.data.companyunit.*;
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

        CompanyYearlyEmployeeNumbersData yearlyEmployeeNumbers = companyData1.obtainYearlyEmployeeNumbersData();
        yearlyEmployeeNumbers.setYear(2017);
        yearlyEmployeeNumbers.setEmployeesLow(1);
        yearlyEmployeeNumbers.setEmployeesHigh(2);
        yearlyEmployeeNumbers.setFullTimeEquivalentLow(1);
        yearlyEmployeeNumbers.setFullTimeEquivalentHigh(2);
        yearlyEmployeeNumbers.setIncludingOwnersLow(1);
        yearlyEmployeeNumbers.setIncludingOwnersHigh(2);


        CompanyQuarterlyEmployeeNumbersData quarterlyEmployeeNumbers = companyData1.obtainQuarterlyEmployeeNumbersData();
        quarterlyEmployeeNumbers.setYear(2017);
        quarterlyEmployeeNumbers.setQuarter(2);
        quarterlyEmployeeNumbers.setEmployeesLow(1);
        quarterlyEmployeeNumbers.setEmployeesHigh(2);
        quarterlyEmployeeNumbers.setFullTimeEquivalentLow(1);
        quarterlyEmployeeNumbers.setFullTimeEquivalentHigh(2);
        quarterlyEmployeeNumbers.setIncludingOwnersLow(1);
        quarterlyEmployeeNumbers.setIncludingOwnersHigh(2);


        CompanyParticipantLink participantLink = queryManager.getItem(session, CompanyParticipantLink.class, Collections.singletonMap("data", 44446666));
        if (participantLink == null) {
            participantLink = new CompanyParticipantLink();
            participantLink.setData(44446666);
            session.saveOrUpdate(participantLink);
        }
        companyData1.addParticipant(participantLink);


        CompanyUnitLink unitLink = queryManager.getItem(session, CompanyUnitLink.class, Collections.singletonMap("pNumber", 314159265));
        if (unitLink == null) {
            unitLink = new CompanyUnitLink();
            unitLink.setpNumber(314159265);
            session.saveOrUpdate(unitLink);
        }



        CompanyCreditData creditData = queryManager.getItem(session, CompanyCreditData.class, Collections.singletonMap("data", "Creditcategory: 3"));
        if (creditData == null) {
            creditData = new CompanyCreditData();
            creditData.setData("Creditcategory: 3");
            session.saveOrUpdate(creditData);
        }
        companyData1.addCreditData(creditData);

        CompanyEffect effect3 = new CompanyEffect(registration, OffsetDateTime.parse("2017-09-01T00:00:00+00:00"), null);
        CompanyBaseData companyData3 = new CompanyBaseData();
        companyData3.addEffect(effect3);
        companyData3.addCompanyUnit(unitLink);


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



        CompanyTextData nameData = companyUnitData.getNameData();
        nameData.setData("Some company unit name");
        nameData.setType(CompanyTextData.Type.NAME);


        CompanyUnitCvrData companyData = companyUnitData.getCompanyData();
        companyData.setData(12345678);



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

        CompanyYearlyEmployeeNumbersData yearlyEmployeeNumbers = companyUnitData.obtainYearlyEmployeeNumbersData();
        yearlyEmployeeNumbers.setYear(2017);
        yearlyEmployeeNumbers.setEmployeesLow(1);
        yearlyEmployeeNumbers.setEmployeesHigh(2);
        yearlyEmployeeNumbers.setFullTimeEquivalentLow(1);
        yearlyEmployeeNumbers.setFullTimeEquivalentHigh(2);
        yearlyEmployeeNumbers.setIncludingOwnersLow(1);
        yearlyEmployeeNumbers.setIncludingOwnersHigh(2);

        CompanyQuarterlyEmployeeNumbersData quarterlyEmployeeNumbers = companyUnitData.obtainQuarterlyEmployeeNumbersData();
        quarterlyEmployeeNumbers.setYear(2017);
        quarterlyEmployeeNumbers.setQuarter(2);
        quarterlyEmployeeNumbers.setEmployeesLow(1);
        quarterlyEmployeeNumbers.setEmployeesHigh(1);
        quarterlyEmployeeNumbers.setFullTimeEquivalentLow(1);
        quarterlyEmployeeNumbers.setFullTimeEquivalentHigh(1);
        quarterlyEmployeeNumbers.setIncludingOwnersLow(1);
        quarterlyEmployeeNumbers.setIncludingOwnersHigh(1);

        CompanyBooleanData isPrimaryData = companyUnitData.getIsPrimaryData();
        isPrimaryData.setData(true);

        CompanyParticipantLink participantLink = queryManager.getItem(session, CompanyParticipantLink.class, Collections.singletonMap("data", 44446666));
        if (participantLink == null) {
            participantLink = new CompanyParticipantLink();
            participantLink.setData(44446666);
            session.saveOrUpdate(participantLink);
        }
        companyUnitData.addParticipant(participantLink);

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
