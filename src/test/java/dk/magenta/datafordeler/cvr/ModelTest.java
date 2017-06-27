package dk.magenta.datafordeler.cvr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.magenta.datafordeler.core.database.Identification;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.core.database.SessionManager;
import dk.magenta.datafordeler.core.exception.DataFordelerException;
import dk.magenta.datafordeler.cvr.data.company.*;
import dk.magenta.datafordeler.cvr.data.companyunit.*;
import dk.magenta.datafordeler.cvr.data.participant.*;
import dk.magenta.datafordeler.cvr.data.shared.CompanyParticipantLink;
import dk.magenta.datafordeler.cvr.data.unversioned.Address;
import dk.magenta.datafordeler.cvr.data.unversioned.CompanyForm;
import dk.magenta.datafordeler.cvr.data.unversioned.Industry;
import dk.magenta.datafordeler.cvr.data.unversioned.Municipality;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Assert;
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
        company.setCvrNumber(78975790);

        CompanyRegistration registration = new CompanyRegistration(OffsetDateTime.parse("2017-01-01T00:00:00+00:00"), OffsetDateTime.parse("2018-01-01T00:00:00+00:00"), 1);
        CompanyEffect effect1 = new CompanyEffect(registration, OffsetDateTime.parse("2017-07-01T00:00:00+00:00"), null);
        CompanyEffect effect2 = new CompanyEffect(registration, OffsetDateTime.parse("2017-08-01T00:00:00+00:00"), null);

        CompanyBaseData companyData1 = new CompanyBaseData();
        companyData1.addEffect(effect1);

        CompanyBaseData companyData2 = new CompanyBaseData();
        companyData2.addEffect(effect2);

        companyData1.setLifecycleStartDate(OffsetDateTime.parse("2000-01-01T00:00:00+00:00"));


        companyData1.setAdvertProtection(true);

        companyData1.setName("Some company name");



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
        companyData1.setLocationAddress(locationAddress);



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
        companyData1.setPostalAddress(postalAddress);


        Industry primaryIndustry = queryManager.getItem(session, Industry.class, Collections.singletonMap("code", 123456));
        if (primaryIndustry == null) {
            primaryIndustry = new Industry();
            primaryIndustry.setCode(123456);
            primaryIndustry.setText("It company");
            session.saveOrUpdate(primaryIndustry);
        }
        companyData2.setPrimaryIndustry(primaryIndustry);


        Industry secondaryIndustry1 = queryManager.getItem(session, Industry.class, Collections.singletonMap("code", 112358));
        if (secondaryIndustry1 == null) {
            secondaryIndustry1 = new Industry();
            secondaryIndustry1.setCode(112358);
            secondaryIndustry1.setText("Psychiatric institution");
            session.saveOrUpdate(secondaryIndustry1);
        }
        companyData2.setSecondaryIndustry1(secondaryIndustry1);

        companyData1.setPhone("87654321", false);
        companyData1.setFax("11112222", false);
        companyData1.setEmail("test@example.com", false);

        CompanyForm companyForm = queryManager.getItem(session, CompanyForm.class, Collections.singletonMap("code", 123));
        if (companyForm == null) {
            companyForm = new CompanyForm();
            companyForm.setCode(123);
            companyForm.setShortDescription("A/S");
            companyForm.setResponsibleDatasource("E&S");
            session.saveOrUpdate(companyForm);
        }
        companyData1.setForm(companyForm);

        companyData1.addYearlyEmployeeNumbers(2017,1,2,1,2,1,2);

        companyData1.addQuarterlyEmployeeNumbers(2017,2,1,2,1,2,1,2);


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

        session = sessionManager.getSessionFactory().openSession();
        CompanyQuery companyQuery = new CompanyQuery();
        companyQuery.setName("Some company name");
        Assert.assertEquals(1, queryManager.getAllEntities(session, companyQuery, CompanyEntity.class).size());

        companyQuery = new CompanyQuery();
        companyQuery.setCvrNumber("78975790");
        Assert.assertEquals(1, queryManager.getAllEntities(session, companyQuery, CompanyEntity.class).size());

        companyQuery = new CompanyQuery();
        companyQuery.setFormCode(123);
        Assert.assertEquals(1, queryManager.getAllEntities(session, companyQuery, CompanyEntity.class).size());

        companyQuery = new CompanyQuery();
        companyQuery.setPhone("87654321");
        Assert.assertEquals(1, queryManager.getAllEntities(session, companyQuery, CompanyEntity.class).size());

        companyQuery = new CompanyQuery();
        companyQuery.setFax("11112222");
        Assert.assertEquals(1, queryManager.getAllEntities(session, companyQuery, CompanyEntity.class).size());

        companyQuery = new CompanyQuery();
        companyQuery.setEmail("test@example.com");
        Assert.assertEquals(1, queryManager.getAllEntities(session, companyQuery, CompanyEntity.class).size());


        System.out.println("--------------------------------------");
        System.out.println(objectMapper.writeValueAsString(company));
        System.out.println("--------------------------------------");

        transaction = session.beginTransaction();
        session.delete(session.merge(company));
        transaction.commit();
        session.close();
    }

    @Test
    public void testCompanyUnit() throws DataFordelerException, JsonProcessingException {
        Session session = sessionManager.getSessionFactory().openSession();
        Identification companyIdentification = new Identification(UUID.randomUUID(), "test");
        CompanyEntity company = new CompanyEntity();
        company.setIdentification(companyIdentification);
        company.setCvrNumber(24681012);

        Identification companyUnitIdentification = new Identification(UUID.randomUUID(), "test");
        CompanyUnitEntity companyUnit = new CompanyUnitEntity();
        companyUnit.setIdentification(companyUnitIdentification);
        companyUnit.setPNumber(11223344);

        CompanyUnitRegistration registration = new CompanyUnitRegistration(OffsetDateTime.parse("2017-01-01T00:00:00+00:00"), OffsetDateTime.parse("2018-01-01T00:00:00+00:00"), 1);
        CompanyUnitEffect effect = new CompanyUnitEffect(registration, OffsetDateTime.parse("2017-07-01T00:00:00+00:00"), null);

        CompanyUnitBaseData companyUnitData = new CompanyUnitBaseData();
        companyUnitData.addEffect(effect);

        companyUnitData.setIsPrimary(true);

        companyUnitData.setLifecycleStartDate(OffsetDateTime.parse("2000-01-01T00:00:00+00:00"));



        companyUnitData.setName("Some company unit name");
        companyUnitData.setCompanyCvr(24681012);



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

        companyUnitData.setLocationAddress(locationAddress);


        Industry primaryIndustry = queryManager.getItem(session, Industry.class, Collections.singletonMap("code", 123456));
        if (primaryIndustry == null) {
            primaryIndustry = new Industry();
            primaryIndustry.setCode(123456);
            primaryIndustry.setText("It company");
            session.saveOrUpdate(primaryIndustry);
        }
        companyUnitData.setPrimaryIndustry(primaryIndustry);


        companyUnitData.setPhone("87654321");
        companyUnitData.setFax("11112222");
        companyUnitData.setEmail("test@example.com");

        companyUnitData.setYearlyEmployeeNumbers(2017,1,2,1,2,1,2);

        companyUnitData.setQuarterlyEmployeeNumbers(2017,2,1,1,1,1,1,1);

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

        session = sessionManager.getSessionFactory().openSession();
        transaction = session.beginTransaction();
        session.delete(company);
        session.delete(companyUnit);
        transaction.commit();
        session.close();
    }

    @Test
    public void testParticipant() throws DataFordelerException, JsonProcessingException {
        Session session = sessionManager.getSessionFactory().openSession();

        ParticipantEntity participant = new ParticipantEntity(UUID.randomUUID(), "test", 12345);

        ParticipantRegistration registration = new ParticipantRegistration(OffsetDateTime.parse("2017-07-01T00:00:00Z"), null, 1);

        ParticipantEffect effect = new ParticipantEffect(registration, OffsetDateTime.parse("2017-01-01T00:00:00Z"), null);

        ParticipantBaseData participantBaseData = new ParticipantBaseData();
        participantBaseData.addEffect(effect);
        participantBaseData.setCvrNumber(3691218);
        participantBaseData.setName("Mickey Mouse");

        ParticipantType type = queryManager.getItem(session, ParticipantType.class, Collections.singletonMap("name", "Person"));
        if (type == null) {
            type = new ParticipantType();
            type.setName("Person");
            session.saveOrUpdate(type);
        }

        participantBaseData.setType(type);

        ParticipantRole role = queryManager.getItem(session, ParticipantRole.class, Collections.singletonMap("name", "CEO"));
        if (role == null) {
            role = new ParticipantRole();
            role.setName("CEO");
            session.saveOrUpdate(role);
        }

        participantBaseData.setRole(role);

        ParticipantStatus status = queryManager.getItem(session, ParticipantStatus.class, Collections.singletonMap("name", "Deceased"));
        if (status == null) {
            status = new ParticipantStatus();
            status.setName("Deceased");
            session.saveOrUpdate(status);
        }

        participantBaseData.setStatus(status);

        queryManager.saveRegistration(session, participant, registration);

        System.out.println("--------------------------------------");
        System.out.println(objectMapper.writeValueAsString(participant));
        System.out.println("--------------------------------------");

    }
}
