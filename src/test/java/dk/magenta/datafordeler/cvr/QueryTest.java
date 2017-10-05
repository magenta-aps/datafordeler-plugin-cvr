package dk.magenta.datafordeler.cvr;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dk.magenta.datafordeler.core.Application;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.core.database.SessionManager;
import dk.magenta.datafordeler.core.exception.DataFordelerException;
import dk.magenta.datafordeler.core.fapi.ParameterMap;
import dk.magenta.datafordeler.core.io.ImportMetadata;
import dk.magenta.datafordeler.core.user.DafoUserManager;
import dk.magenta.datafordeler.cvr.data.company.CompanyEntity;
import dk.magenta.datafordeler.cvr.data.company.CompanyEntityManager;
import dk.magenta.datafordeler.cvr.data.company.CompanyOutputWrapper;
import dk.magenta.datafordeler.cvr.data.company.CompanyQuery;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitEntity;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitEntityManager;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitOutputWrapper;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitQuery;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantEntity;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantEntityManager;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantOutputWrapper;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantQuery;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QueryTest {

    @Autowired
    private CvrPlugin plugin;

    @Autowired
    private QueryManager queryManager;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private CompanyEntityManager companyEntityManager;

    @Autowired
    private CompanyUnitEntityManager companyUnitEntityManager;

    @Autowired
    private ParticipantEntityManager participantEntityManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestRestTemplate restTemplate;

    @SpyBean
    private DafoUserManager dafoUserManager;

    private CompanyOutputWrapper companyOutputWrapper = new CompanyOutputWrapper();
    private CompanyUnitOutputWrapper companyUnitOutputWrapper = new CompanyUnitOutputWrapper();
    private ParticipantOutputWrapper participantOutputWrapper = new ParticipantOutputWrapper();

    private void loadCompany() throws IOException, DataFordelerException {
        ImportMetadata importMetadata = new ImportMetadata();
        InputStream testData = QueryTest.class.getResourceAsStream("/company_in.json");
        JsonNode root = objectMapper.readTree(testData);
        JsonNode itemList = root.get("hits").get("hits");
        Assert.assertTrue(itemList.isArray());
        for (JsonNode item : itemList) {
            companyEntityManager.parseRegistration(item.get("_source").get("Vrvirksomhed"), importMetadata);
        }
    }

    private void loadUnit() throws IOException, DataFordelerException {
        ImportMetadata importMetadata = new ImportMetadata();
        InputStream testData = QueryTest.class.getResourceAsStream("/unit.json");
        JsonNode root = objectMapper.readTree(testData);
        JsonNode itemList = root.get("hits").get("hits");
        Assert.assertTrue(itemList.isArray());
        for (JsonNode item : itemList) {
            companyUnitEntityManager.parseRegistration(item.get("_source").get("VrproduktionsEnhed"), importMetadata);
        }
    }

    private void loadParticipant() throws IOException, DataFordelerException {
        ImportMetadata importMetadata = new ImportMetadata();
        InputStream testData = QueryTest.class.getResourceAsStream("/person.json");
        JsonNode root = objectMapper.readTree(testData);
        JsonNode itemList = root.get("hits").get("hits");
        Assert.assertTrue(itemList.isArray());
        for (JsonNode item : itemList) {
            participantEntityManager.parseRegistration(item.get("_source").get("Vrdeltagerperson"), importMetadata);
        }
    }

    private void applyAccess(TestUserDetails testUserDetails) {
        when(dafoUserManager.getFallbackUser()).thenReturn(testUserDetails);
    }

    private ResponseEntity<String> restSearch(ParameterMap parameters, String type) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity<String> httpEntity = new HttpEntity<String>("", headers);
        return this.restTemplate.exchange("/cvr/"+type+"/1/rest/search?" + parameters.asUrlParams(), HttpMethod.GET, httpEntity, String.class);
    }

    private static void assertJsonEquality(JsonNode node1, JsonNode node2, boolean ignoreArrayOrdering, boolean printDifference) {
        try {
            Assert.assertEquals(node1.isNull(), node2.isNull());
            Assert.assertEquals(node1.isArray(), node2.isArray());
            Assert.assertEquals(node1.isObject(), node2.isObject());
            Assert.assertEquals(node1.isLong(), node2.isLong());
            Assert.assertEquals(node1.isInt(), node2.isInt());
            Assert.assertEquals(node1.isShort(), node2.isShort());
            Assert.assertEquals(node1.isBoolean(), node2.isBoolean());
            Assert.assertEquals(node1.isTextual(), node2.isTextual());
            if (node1.isArray()) {
                Assert.assertEquals(node1.size(), node2.size());
                if (ignoreArrayOrdering) {
                    for (int i = 0; i < node1.size(); i++) {
                        boolean match = false;
                        for (int j = 0; j < node2.size(); j++) {
                            try {
                                assertJsonEquality(node1.get(i), node2.get(j), true, false);
                                match = true;
                            } catch (AssertionError e) {
                            }
                        }
                        if (!match) {
                            throw new AssertionError();
                        }
                    }
                } else {
                    for (int i = 0; i < node1.size(); i++) {
                        assertJsonEquality(node1.get(i), node2.get(i), false, printDifference);
                    }
                }
            } else if (node1.isObject()) {
                Assert.assertEquals(node1.size(), node2.size());
                Iterator<String> keys = node1.fieldNames();
                while (keys.hasNext()) {
                    String key = keys.next();
                    Assert.assertNotNull(node2.get(key));
                    assertJsonEquality(node1.get(key), node2.get(key), ignoreArrayOrdering, printDifference);
                }
            } else {
                Assert.assertEquals(node1.asText(), node2.asText());
            }
        } catch (AssertionError e) {
            if (printDifference) {
                System.out.println("\n" + node1 + "\n != \n" + node2 + "\n\n\n");
            }
            throw e;
        }
    }

    private static final String RESTRICTION_KOMMUNE_TEST = "TestMunicipality";
    private void addTestMunicipalityAreaRestriction() {
        plugin.getAreaRestrictionDefinition().getAreaRestrictionTypeByName(
                CvrAreaRestrictionDefinition.RESTRICTIONTYPE_KOMMUNEKODER
        ).addChoice(RESTRICTION_KOMMUNE_TEST, "Testing", null, "101");
    }

    /*
        Company tests
     */

    @Test
    public void testCompanyIdempotence() throws IOException, DataFordelerException {
        Session session = sessionManager.getSessionFactory().openSession();
        try {
            loadCompany();

            List<CompanyEntity> entities = queryManager.getAllEntities(session, CompanyEntity.class);
            JsonNode firstImport = objectMapper.valueToTree(entities);

            loadCompany();
            entities = queryManager.getAllEntities(session, CompanyEntity.class);
            JsonNode secondImport = objectMapper.valueToTree(entities);

            assertJsonEquality(firstImport, secondImport, true, true);
        } finally {
            session.close();
        }
    }


    @Test
    public void testCompanyQuery() throws IOException, DataFordelerException {
        Session session = sessionManager.getSessionFactory().openSession();
        try {
            loadCompany();
            UUID expectedUUID = UUID.fromString("2334456b-d2ca-372d-aa60-4a2ba7fed7cd");

            CompanyEntity companyEntity = queryManager.getEntity(session, expectedUUID, CompanyEntity.class);
            Object wrappedEntity = companyOutputWrapper.wrapResult(companyEntity);
            Assert.assertTrue(wrappedEntity instanceof ObjectNode);
            ObjectNode objectNode = (ObjectNode) wrappedEntity;
            Assert.assertEquals(123, objectNode.get("registreringer").size());

            List<CompanyEntity> entities;
            CompanyQuery query;

            query = new CompanyQuery();
            query.setEmailadresse("info@magenta.dk");
            entities = queryManager.getAllEntities(session, query, CompanyEntity.class);
            Assert.assertEquals(1, entities.size());
            Assert.assertEquals(expectedUUID, entities.get(0).getUUID());

            query = new CompanyQuery();
            query.addKommunekode(101);
            entities = queryManager.getAllEntities(session, query, CompanyEntity.class);
            Assert.assertEquals(1, entities.size());
            Assert.assertEquals(expectedUUID, entities.get(0).getUUID());

            query = new CompanyQuery();
            query.setVirksomhedsform(80);
            entities = queryManager.getAllEntities(session, query, CompanyEntity.class);
            Assert.assertEquals(1, entities.size());
            Assert.assertEquals(expectedUUID, entities.get(0).getUUID());

            query = new CompanyQuery();
            query.setCVRNummer("25052943");
            entities = queryManager.getAllEntities(session, query, CompanyEntity.class);
            Assert.assertEquals(1, entities.size());
            Assert.assertEquals(expectedUUID, entities.get(0).getUUID());

            query = new CompanyQuery();
            query.setTelefonnummer("33369696");
            entities = queryManager.getAllEntities(session, query, CompanyEntity.class);
            Assert.assertEquals(1, entities.size());
            Assert.assertEquals(expectedUUID, entities.get(0).getUUID());

            query = new CompanyQuery();
            query.setVirksomhedsnavn("MAGENTA ApS");
            entities = queryManager.getAllEntities(session, query, CompanyEntity.class);
            Assert.assertEquals(1, entities.size());
            Assert.assertEquals(expectedUUID, entities.get(0).getUUID());

            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(companyOutputWrapper.wrapResult(entities.get(0))));

        } finally {
            session.close();
        }
    }


    @Test
    public void testCompanyRestQuery() throws IOException, DataFordelerException {
        loadCompany();
        TestUserDetails testUserDetails = new TestUserDetails();
        testUserDetails.giveAccess(CvrRolesDefinition.READ_CVR_ROLE);
        this.applyAccess(testUserDetails);

        ParameterMap searchParameters = new ParameterMap();
        searchParameters.add("CVRNummer", "25052943");
        searchParameters.add("registrationFrom", "1999-11-30");
        searchParameters.add("registrationTo", "1999-12-01");


        ResponseEntity<String> response = restSearch(searchParameters, "company");
        Assert.assertEquals(200, response.getStatusCode().value());
        JsonNode jsonBody = objectMapper.readTree(response.getBody());

        ObjectNode entity = (ObjectNode) jsonBody.get("results").get(0);
        Assert.assertEquals(25052943, entity.get("CVRNummer").asInt());
        Assert.assertEquals(CompanyEntity.generateUUID(25052943).toString(), entity.get("UUID").asText());
        ArrayNode registrations = (ArrayNode) entity.get("registreringer");
        Assert.assertEquals(1, registrations.size());
        ObjectNode registration = (ObjectNode) registrations.get(0);
        Assert.assertTrue(OffsetDateTime.parse("1999-11-29T16:37:10+01:00").isEqual(OffsetDateTime.parse(registration.get("registreringFra").textValue())));
        Assert.assertTrue(OffsetDateTime.parse("2000-02-29T11:44:42+01:00").isEqual(OffsetDateTime.parse(registration.get("registreringTil").textValue())));

        JsonNode formNodeList = registration.get("virksomhedsform");
        Assert.assertEquals(1, formNodeList.size());
        JsonNode formNode = formNodeList.get(0);
        Assert.assertTrue(OffsetDateTime.parse("1999-11-15T01:00+01:00").isEqual(OffsetDateTime.parse(formNode.get("virkningFra").asText())));
        Assert.assertTrue(formNode.get("virkningTil").isNull());
        Assert.assertEquals("80", formNode.get("formkode").asText());
        Assert.assertEquals("E&S", formNode.get("dataleverandør").asText());


        JsonNode lifecycleNodeList = registration.get("livscyklus");
        Assert.assertEquals(1, formNodeList.size());
        JsonNode lifecycleNode = lifecycleNodeList.get(0);
        Assert.assertTrue(OffsetDateTime.parse("1999-11-15T01:00+01:00").isEqual(OffsetDateTime.parse(lifecycleNode.get("virkningFra").asText())));
        Assert.assertTrue(lifecycleNode.get("virkningTil").isNull());
        Assert.assertEquals("1999-11-15", lifecycleNode.get("virksomhedStartdato").asText());


        JsonNode unitNodeList = registration.get("produktionsEnheder");
        Assert.assertEquals(1, unitNodeList.size());
        JsonNode unitNode = unitNodeList.get(0);
        Assert.assertTrue(OffsetDateTime.parse("1999-11-15T01:00+01:00").isEqual(OffsetDateTime.parse(unitNode.get("virkningFra").asText())));
        Assert.assertTrue(unitNode.get("virkningTil").isNull());
        Assert.assertEquals(1007256945L, unitNode.get("pnummer").asLong());


        // Retrieve what was registered at 2015-01-01, with effect 2015-01-01
        searchParameters = new ParameterMap();
        searchParameters.add("CVRNummer", "25052943");
        searchParameters.add("registrationFrom", "2015-01-01");
        searchParameters.add("registrationTo", "2015-01-01");
        searchParameters.add("effectFrom", "2015-01-01");
        searchParameters.add("effectTo", "2015-01-01");

        response = restSearch(searchParameters, "company");
        Assert.assertEquals(200, response.getStatusCode().value());
        jsonBody = objectMapper.readTree(response.getBody());

        entity = (ObjectNode) jsonBody.get("results").get(0);
        registrations = (ArrayNode) entity.get("registreringer");
        Assert.assertEquals(1, registrations.size());
        registration = (ObjectNode) registrations.get(0);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(registrations));
        Assert.assertNotNull(registration.get("deltagerRelationer"));
        Assert.assertNotNull(registration.get("livscyklus"));
        Assert.assertNotNull(registration.get("virksomhedsform"));
        Assert.assertNotNull(registration.get("produktionsEnheder"));
        Assert.assertNotNull(registration.get("telefonnummer"));
        Assert.assertNotNull(registration.get("virksomhedsnavn"));
        Assert.assertNull(registration.get("emailadresse"));
        Assert.assertNull(registration.get("hovedbranche"));
        Assert.assertNull(registration.get("beliggenhedsadresse"));
    }


    @Test
    public void testCompanyAccess() throws Exception {
        loadCompany();
        addTestMunicipalityAreaRestriction();
        TestUserDetails testUserDetails = new TestUserDetails();

        ParameterMap searchParameters = new ParameterMap();
        ResponseEntity<String> response;// = restSearch(searchParameters, "company");
        //Assert.assertEquals(403, response.getStatusCode().value());

        testUserDetails.giveAccess(CvrRolesDefinition.READ_CVR_ROLE);
        this.applyAccess(testUserDetails);

        searchParameters.add("kommune", "101");
        response = restSearch(searchParameters, "company");
        Assert.assertEquals(200, response.getStatusCode().value());
        JsonNode jsonBody = objectMapper.readTree(response.getBody());
        JsonNode results = jsonBody.get("results");
        Assert.assertTrue(results.isArray());
        Assert.assertEquals(1, results.size());
        Assert.assertEquals(CompanyEntity.generateUUID(25052943).toString(), results.get(0).get("UUID").asText());

        searchParameters = new ParameterMap();
        searchParameters.add("virksomhedsnavn", "MAGENTA ApS");
        response = restSearch(searchParameters, "company");
        Assert.assertEquals(200, response.getStatusCode().value());
        jsonBody = objectMapper.readTree(response.getBody());
        results = jsonBody.get("results");
        Assert.assertTrue(results.isArray());
        Assert.assertEquals(1, results.size());
        Assert.assertEquals(CompanyEntity.generateUUID(25052943).toString(), results.get(0).get("UUID").asText());

        testUserDetails.giveAccess(
                plugin.getAreaRestrictionDefinition().getAreaRestrictionTypeByName(
                        CvrAreaRestrictionDefinition.RESTRICTIONTYPE_KOMMUNEKODER
                ).getRestriction(
                        CvrAreaRestrictionDefinition.RESTRICTION_KOMMUNE_SERMERSOOQ
                )
        );
        this.applyAccess(testUserDetails);

        response = restSearch(searchParameters, "company");
        Assert.assertEquals(200, response.getStatusCode().value());
        jsonBody = objectMapper.readTree(response.getBody());
        results = jsonBody.get("results");
        Assert.assertTrue(results.isArray());
        Assert.assertEquals(0, results.size());

        testUserDetails.giveAccess(
                plugin.getAreaRestrictionDefinition().getAreaRestrictionTypeByName(
                        CvrAreaRestrictionDefinition.RESTRICTIONTYPE_KOMMUNEKODER
                ).getRestriction(
                        RESTRICTION_KOMMUNE_TEST
                )
        );
        this.applyAccess(testUserDetails);

        response = restSearch(searchParameters, "company");
        Assert.assertEquals(200, response.getStatusCode().value());
        jsonBody = objectMapper.readTree(response.getBody());
        results = jsonBody.get("results");
        Assert.assertTrue(results.isArray());


        Assert.assertEquals(1, results.size());
        Assert.assertEquals(CompanyEntity.generateUUID(25052943).toString(), results.get(0).get("UUID").asText());

/*
        String queryString = "SELECT DISTINCT e from dk.magenta.datafordeler.cvr.data.company.CompanyEntity e " +
                "WHERE e.identification.uuid IS NOT null " +
                "AND e in (select e from dk.magenta.datafordeler.cvr.data.company.CompanyBaseData d join d.effects v join v.registration r join r.entity e where d.companyName.value = :d_companyName_value) " +
                "AND e in (select e from dk.magenta.datafordeler.cvr.data.company.CompanyBaseData d join d.effects v join v.registration r join r.entity e where d.locationAddress.address.municipality.code = :d_locationAddress_address_municipality_code)" +
                "";
        Session session = sessionManager.getSessionFactory().openSession();
        Query<CompanyEntity> query = session.createQuery(queryString);
        query.setParameter("d_companyName_value", "MAGENTA ApS");
        query.setParameter("d_locationAddress_address_municipality_code", "101");

        List<CompanyEntity> resultList = query.getResultList();
        System.out.println(resultList);
        session.close();
        */
    }



    /*
        CompanyUnit tests
     */

    public void testCompanyUnitIdempotence() throws Exception {
        Session session = sessionManager.getSessionFactory().openSession();
        try {
            loadUnit();
            List<CompanyUnitEntity> entities = queryManager.getAllEntities(session, CompanyUnitEntity.class);
            Assert.assertFalse(entities.isEmpty());
            JsonNode firstImport = objectMapper.valueToTree(entities);

            loadUnit();
            entities = queryManager.getAllEntities(session, CompanyUnitEntity.class);
            JsonNode secondImport = objectMapper.valueToTree(entities);

            assertJsonEquality(firstImport, secondImport, true, true);
        } finally {
            session.close();
        }
    }


    @Test
    public void testCompanyUnitQuery() throws Exception {
        Session session = sessionManager.getSessionFactory().openSession();
        try {
            loadUnit();
            UUID expectedUUID = UUID.fromString("92ce7d3e-b261-31d6-a401-df408167dd1b");

            CompanyUnitEntity companyUnitEntity = queryManager.getEntity(session, expectedUUID, CompanyUnitEntity.class);
            Object wrapped = companyUnitOutputWrapper.wrapResult(companyUnitEntity);
            Assert.assertTrue(wrapped instanceof ObjectNode);
            ObjectNode objectNode = (ObjectNode) wrapped;
            Assert.assertEquals(1, objectNode.get("registreringer").size());
            System.out.println(objectNode.toString());

            CompanyUnitQuery query = new CompanyUnitQuery();
            query.setPrimaryIndustry("855900");
            List<CompanyUnitEntity> entities = queryManager.getAllEntities(session, query, CompanyUnitEntity.class);
            Assert.assertEquals(1, entities.size());
            Assert.assertEquals(expectedUUID, entities.get(0).getUUID());

            query = new CompanyUnitQuery();
            query.addKommunekode(101);
            entities = queryManager.getAllEntities(session, query, CompanyUnitEntity.class);
            Assert.assertEquals(5, entities.size());
            List<UUID> expected = Arrays.asList(UUID.fromString("cd834835-384b-3026-8fd8-ec24095aa446"),
                    UUID.fromString("ebebf16f-11a8-3276-903b-8d3b1179722b"),
                    UUID.fromString("418c6bf2-e4fe-31d4-bf05-4d2c1e6c0380"),
                    UUID.fromString("7aa1f12b-315a-316f-9e08-f8724d7a09d9"),
                    UUID.fromString("c2a6a3da-c46e-3689-a39a-9727a94bb5c5"));
            for (CompanyUnitEntity e : entities) {
                Assert.assertTrue(expected.contains(e.getUUID()));
            }

            query = new CompanyUnitQuery();
            query.setAssociatedCompanyCvrNumber("36238208");
            entities = queryManager.getAllEntities(session, query, CompanyUnitEntity.class);
            Assert.assertEquals(1, entities.size());

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /*
        Participant tests
     */

    public void testParticipantIdempotence() throws IOException, DataFordelerException {
        Session session = sessionManager.getSessionFactory().openSession();
        try {
            loadParticipant();

            List<ParticipantEntity> entities = queryManager.getAllEntities(session, ParticipantEntity.class);
            JsonNode firstImport = objectMapper.valueToTree(entities);

            loadParticipant();
            entities = queryManager.getAllEntities(session, ParticipantEntity.class);
            JsonNode secondImport = objectMapper.valueToTree(entities);

            assertJsonEquality(firstImport, secondImport, true, true);
        } finally {
            session.close();
        }
    }

    @Test
    public void testParticipantQuery() throws IOException, DataFordelerException {
        Session session = null;
        try {
            loadParticipant();
            session = sessionManager.getSessionFactory().openSession();

            ParticipantQuery query = new ParticipantQuery();
            query.setNavne("Morten Kjærsgaard");

            List<ParticipantEntity> entities = queryManager.getAllEntities(session, query, ParticipantEntity.class);
            List<Object> wrapped = participantOutputWrapper.wrapResults(entities);

            Assert.assertEquals(1, wrapped.size());
            Assert.assertTrue(wrapped.get(0) instanceof ObjectNode);
            ObjectNode objectNode = (ObjectNode) wrapped.get(0);
            Assert.assertEquals(4, objectNode.get("registreringer").size());

            String firstImport = objectMapper.writeValueAsString(wrapped);

            loadParticipant();
            entities = queryManager.getAllEntities(session, query, ParticipantEntity.class);
            wrapped = participantOutputWrapper.wrapResults(entities);
            String secondImport = objectMapper.writeValueAsString(wrapped);

            assertJsonEquality(objectMapper.readTree(firstImport), objectMapper.readTree(secondImport), true, true);

            query = new ParticipantQuery();
            query.addKommunekode(101);
            entities = queryManager.getAllEntities(session, query, ParticipantEntity.class);
            Assert.assertEquals(1, entities.size());

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

}
