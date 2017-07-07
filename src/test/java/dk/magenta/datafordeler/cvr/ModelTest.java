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
import dk.magenta.datafordeler.cvr.data.shared.ParticipantLink;
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
        Identification identifikation = new Identification(UUID.randomUUID(), "test");
        CompanyEntity virksomhedEnhed = new CompanyEntity();
        virksomhedEnhed.setIdentifikation(identifikation);
        virksomhedEnhed.setCVRNummer(78975790);

        CompanyRegistration registrering = new CompanyRegistration(OffsetDateTime.parse("2017-01-01T00:00:00+00:00"), OffsetDateTime.parse("2018-01-01T00:00:00+00:00"), 1);
        CompanyEffect virkning1 = new CompanyEffect(registrering, OffsetDateTime.parse("2017-07-01T00:00:00+00:00"), null);
        CompanyEffect virkning2 = new CompanyEffect(registrering, OffsetDateTime.parse("2017-08-01T00:00:00+00:00"), null);

        CompanyBaseData virksomhed1 = new CompanyBaseData();
        virksomhed1.addVirkning(virkning1);

        CompanyBaseData virksomhed2 = new CompanyBaseData();
        virksomhed2.addVirkning(virkning2);

        virksomhed1.setLivsforloebStart(OffsetDateTime.parse("2000-01-01T00:00:00+00:00"));


        virksomhed1.setReklamebeskyttelse(true);

        virksomhed1.setVirksomhedsnavn("Some company name");



        Municipality kommune = Municipality.getKommune("101", "Copenhagen", queryManager, session);

        HashMap<String, Object> adresse1 = new HashMap<>();
        adresse1.put("vejnavn", "FoobarRoad");
        adresse1.put("vejkode", "1234");
        adresse1.put("husnummerFra", "12");
        adresse1.put("kommune", kommune);
        Address beliggenhedsadresse = queryManager.getItem(session, Address.class, adresse1);
        if (beliggenhedsadresse == null) {
            beliggenhedsadresse = new Address();
            beliggenhedsadresse.setVejnavn((String)adresse1.get("vejnavn"));
            beliggenhedsadresse.setVejkode((String)adresse1.get("vejkode"));
            beliggenhedsadresse.setHusnummerFra((String)adresse1.get("husnummerFra"));
            beliggenhedsadresse.setKommune(kommune);
            session.saveOrUpdate(beliggenhedsadresse);
        }
        virksomhed1.setAdresse(beliggenhedsadresse);



        HashMap<String, Object> adresse2 = new HashMap<>();
        adresse2.put("vejnavn", "HelloWorldRoad");
        adresse2.put("vejkode", "5678");
        adresse2.put("husnummerFra", "34");
        adresse2.put("kommune", kommune);
        Address postadresse = queryManager.getItem(session, Address.class, adresse2);
        if (postadresse == null) {
            postadresse = new Address();
            postadresse.setVejnavn((String)adresse2.get("vejnavn"));
            postadresse.setVejkode((String)adresse2.get("vejkode"));
            postadresse.setHusnummerFra((String)adresse2.get("husnummerFra"));
            postadresse.setKommune(kommune);
            session.saveOrUpdate(postadresse);
        }
        virksomhed1.setPostalAddress(postadresse);


        Industry hovedbranche = queryManager.getItem(session, Industry.class, Collections.singletonMap("branchekode", "123456"));
        if (hovedbranche == null) {
            hovedbranche = new Industry();
            hovedbranche.setBranchekode("123456");
            hovedbranche.setBranchetekst("It company");
            session.saveOrUpdate(hovedbranche);
        }
        virksomhed2.setHovedbranche(hovedbranche);


        Industry bibranche1 = queryManager.getItem(session, Industry.class, Collections.singletonMap("branchekode", "112358"));
        if (bibranche1 == null) {
            bibranche1 = new Industry();
            bibranche1.setBranchekode("112358");
            bibranche1.setBranchetekst("Psychiatric institution");
            session.saveOrUpdate(bibranche1);
        }
        virksomhed2.setbibranche1(bibranche1);

        virksomhed1.setTelefonnummer("87654321", false);
        virksomhed1.setTelefaxnummer("11112222", false);
        virksomhed1.setEmailadresse("test@example.com", false);

        CompanyForm virksomhedsform = queryManager.getItem(session, CompanyForm.class, Collections.singletonMap("virksomhedsformkode", "123"));
        if (virksomhedsform == null) {
            virksomhedsform = new CompanyForm();
            virksomhedsform.setVirksomhedsformkode("123");
            virksomhedsform.setKortBeskrivelse("A/S");
            virksomhedsform.setAnsvarligDataleverandoer("E&S");
            session.saveOrUpdate(virksomhedsform);
        }
        virksomhed1.setVirksomhedsform(virksomhedsform);

        virksomhed1.addAarsbeskaeftigelse(2017,1,2,1,2,1,2);

        virksomhed1.addKvartalsbeskaeftigelse(2017,2,1,2,1,2,1,2);


        ParticipantLink deltagerlink = queryManager.getItem(session, ParticipantLink.class, Collections.singletonMap("vaerdi", 44446666));
        if (deltagerlink == null) {
            deltagerlink = new ParticipantLink();
            deltagerlink.setVaerdi(44446666);
            session.saveOrUpdate(deltagerlink);
        }
        virksomhed1.addDeltager(deltagerlink);


        CompanyUnitLink produktionsenhed = queryManager.getItem(session, CompanyUnitLink.class, Collections.singletonMap("pNummer", 314159265));
        if (produktionsenhed == null) {
            produktionsenhed = new CompanyUnitLink();
            produktionsenhed.setpNummer(314159265);
            session.saveOrUpdate(produktionsenhed);
        }

        CompanyEffect virkning3 = new CompanyEffect(registrering, OffsetDateTime.parse("2017-09-01T00:00:00+00:00"), null);
        CompanyBaseData virksomhed3 = new CompanyBaseData();
        virksomhed3.addVirkning(virkning3);
        virksomhed3.addProduktionsenhed(produktionsenhed);

        Transaction transaction = session.beginTransaction();

        try {
            queryManager.saveRegistrering(session, virksomhedEnhed, registrering);
            transaction.commit();
        } catch (DataFordelerException e) {
            transaction.rollback();
        } finally {
            session.close();
        }

        session = sessionManager.getSessionFactory().openSession();
        CompanyQuery companyQuery = new CompanyQuery();
        companyQuery.setVirksomhedsnavn("Some company name");
        Assert.assertEquals(1, queryManager.getAllEntities(session, companyQuery, CompanyEntity.class).size());

        companyQuery = new CompanyQuery();
        companyQuery.setCVRNummer("78975790");
        Assert.assertEquals(1, queryManager.getAllEntities(session, companyQuery, CompanyEntity.class).size());

        companyQuery = new CompanyQuery();
        companyQuery.setVirksomhedsform("123");
        Assert.assertEquals(1, queryManager.getAllEntities(session, companyQuery, CompanyEntity.class).size());

        companyQuery = new CompanyQuery();
        companyQuery.setTelefonnummer("87654321");
        Assert.assertEquals(1, queryManager.getAllEntities(session, companyQuery, CompanyEntity.class).size());

        companyQuery = new CompanyQuery();
        companyQuery.setTelefaxnummer("11112222");
        Assert.assertEquals(1, queryManager.getAllEntities(session, companyQuery, CompanyEntity.class).size());

        companyQuery = new CompanyQuery();
        companyQuery.setEmailadresse("test@example.com");
        Assert.assertEquals(1, queryManager.getAllEntities(session, companyQuery, CompanyEntity.class).size());


        System.out.println("--------------------------------------");
        System.out.println(objectMapper.writeValueAsString(virksomhedEnhed));
        System.out.println("--------------------------------------");

        transaction = session.beginTransaction();
        session.delete(session.merge(virksomhedEnhed));
        transaction.commit();
        session.close();
    }

    @Test
    public void testCompanyUnit() throws DataFordelerException, JsonProcessingException {
        Session session = sessionManager.getSessionFactory().openSession();
        Identification virksomhedsId = new Identification(UUID.randomUUID(), "test");
        CompanyEntity virksomhedsEnhed = new CompanyEntity();
        virksomhedsEnhed.setIdentifikation(virksomhedsId);
        virksomhedsEnhed.setCVRNummer(24681012);

        Identification produktionsenhedsId = new Identification(UUID.randomUUID(), "test");
        CompanyUnitEntity virksomhed = new CompanyUnitEntity();
        virksomhed.setIdentifikation(produktionsenhedsId);
        virksomhed.setPNumber(11223344);

        CompanyUnitRegistration registrering = new CompanyUnitRegistration(OffsetDateTime.parse("2017-01-01T00:00:00+00:00"), OffsetDateTime.parse("2018-01-01T00:00:00+00:00"), 1);
        CompanyUnitEffect virkning = new CompanyUnitEffect(registrering, OffsetDateTime.parse("2017-07-01T00:00:00+00:00"), null);

        CompanyUnitBaseData produktionsenhed = new CompanyUnitBaseData();
        produktionsenhed.addVirkning(virkning);

        produktionsenhed.setIsPrimaer(true);

        produktionsenhed.setLivsforloebStart(OffsetDateTime.parse("2000-01-01T00:00:00+00:00"));



        produktionsenhed.setVirksomhedsnavn("Some company unit name");
        produktionsenhed.setVirksomhedscvr(24681012);



        Municipality kommune = queryManager.getItem(session, Municipality.class, Collections.singletonMap("kommunekode", "101"));
        if (kommune == null) {
            kommune = new Municipality();
            kommune.setKommunekode("101");
            kommune.setKommunenavn("Copenhagen");
            session.saveOrUpdate(kommune);
        }

        HashMap<String, Object> adresse = new HashMap<>();
        adresse.put("vejnavn", "FoobarRoad");
        adresse.put("vejkode", "1234");
        adresse.put("husnummerFra", "12");
        adresse.put("kommune", kommune);
        Address beliggenhedsadresse = queryManager.getItem(session, Address.class, adresse);
        if (beliggenhedsadresse == null) {
            beliggenhedsadresse = new Address();
            beliggenhedsadresse.setVejnavn((String)adresse.get("vejnavn"));
            beliggenhedsadresse.setVejkode((String)adresse.get("vejkode"));
            beliggenhedsadresse.setHusnummerFra((String)adresse.get("husnummerFra"));
            beliggenhedsadresse.setKommune(kommune);
            session.saveOrUpdate(beliggenhedsadresse);
        }

        produktionsenhed.setBeliggenhedsadresse(beliggenhedsadresse);


        Industry primaryIndustry = queryManager.getItem(session, Industry.class, Collections.singletonMap("branchekode", "123456"));
        if (primaryIndustry == null) {
            primaryIndustry = new Industry();
            primaryIndustry.setBranchekode("123456");
            primaryIndustry.setBranchetekst("It company");
            session.saveOrUpdate(primaryIndustry);
        }
        produktionsenhed.setHovedbranche(primaryIndustry);


        produktionsenhed.setTelefonnummer("87654321", false);
        produktionsenhed.setTelefaxnummer("11112222",true);
        produktionsenhed.setEmailadresse("test@example.com", false);

        produktionsenhed.addAarsbeskaeftigelse(2017,1,2,1,2,1,2);

        produktionsenhed.addKvartalsbeskaeftigelse(2017,2,1,1,1,1,1,1);

        ParticipantLink deltagerlink = queryManager.getItem(session, ParticipantLink.class, Collections.singletonMap("vaerdi", 44446666));
        if (deltagerlink == null) {
            deltagerlink = new ParticipantLink();
            deltagerlink.setVaerdi(44446666);
            session.saveOrUpdate(deltagerlink);
        }
        produktionsenhed.addDeltagere(deltagerlink);

        Transaction transaction = session.beginTransaction();

        session.saveOrUpdate(virksomhedsEnhed);

        try {
            queryManager.saveRegistrering(session, virksomhed, registrering);
            transaction.commit();
        } catch (DataFordelerException e) {
            transaction.rollback();
        } finally {
            session.close();
        }

        System.out.println("--------------------------------------");
        System.out.println(objectMapper.writeValueAsString(virksomhed));
        System.out.println("--------------------------------------");

        session = sessionManager.getSessionFactory().openSession();
        transaction = session.beginTransaction();
        session.delete(virksomhedsEnhed);
        session.delete(virksomhed);
        transaction.commit();
        session.close();
    }

    @Test
    public void testParticipant() throws DataFordelerException, JsonProcessingException {
        Session session = sessionManager.getSessionFactory().openSession();

        ParticipantEntity deltagerEnhed = new ParticipantEntity(UUID.randomUUID(), "test");
        deltagerEnhed.setDeltagernummer(12345);

        ParticipantRegistration registrering = new ParticipantRegistration(OffsetDateTime.parse("2017-07-01T00:00:00Z"), null, 1);

        ParticipantEffect virkning = new ParticipantEffect(registrering, OffsetDateTime.parse("2017-01-01T00:00:00Z"), null);

        ParticipantBaseData deltager = new ParticipantBaseData();
        deltager.addVirkning(virkning);
        deltager.setNavn("Mickey Mouse");

        ParticipantType type = queryManager.getItem(session, ParticipantType.class, Collections.singletonMap("navn", "Person"));
        if (type == null) {
            type = new ParticipantType();
            type.setNavn("Person");
            session.saveOrUpdate(type);
        }

        deltager.setEnhedstype(type);

        ParticipantRole rolle = queryManager.getItem(session, ParticipantRole.class, Collections.singletonMap("navn", "CEO"));
        if (rolle == null) {
            rolle = new ParticipantRole();
            rolle.setNavn("CEO");
            session.saveOrUpdate(rolle);
        }

        deltager.setRolle(rolle);

        ParticipantStatus status = queryManager.getItem(session, ParticipantStatus.class, Collections.singletonMap("navn", "Deceased"));
        if (status == null) {
            status = new ParticipantStatus();
            status.setNavn("Deceased");
            session.saveOrUpdate(status);
        }

        deltager.setStatus(status);

        queryManager.saveRegistrering(session, deltagerEnhed, registrering);

        System.out.println("--------------------------------------");
        System.out.println(objectMapper.writeValueAsString(deltagerEnhed));
        System.out.println("--------------------------------------");

    }
}
