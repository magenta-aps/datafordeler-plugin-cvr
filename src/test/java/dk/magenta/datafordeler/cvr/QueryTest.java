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
import dk.magenta.datafordeler.core.role.SystemRole;
import dk.magenta.datafordeler.core.user.DafoUserManager;
import dk.magenta.datafordeler.core.user.UserProfile;
import dk.magenta.datafordeler.cvr.data.company.CompanyEntity;
import dk.magenta.datafordeler.cvr.data.company.CompanyEntityManager;
import dk.magenta.datafordeler.cvr.data.company.CompanyOutputWrapper;
import dk.magenta.datafordeler.cvr.data.company.CompanyQuery;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitOutputWrapper;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantOutputWrapper;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitEntity;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitEntityManager;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitQuery;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantEntity;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantEntityManager;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantQuery;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.hibernate.Session;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QueryTest {

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
        InputStream testData = QueryTest.class.getResourceAsStream("/company_in.json");
        JsonNode root = objectMapper.readTree(testData);
        JsonNode itemList = root.get("hits").get("hits");
        Assert.assertTrue(itemList.isArray());
        for (JsonNode item : itemList) {
            companyEntityManager.parseRegistration(item.get("_source").get("Vrvirksomhed"));
        }
    }

    private void loadUnit() throws IOException, DataFordelerException {
        InputStream testData = QueryTest.class.getResourceAsStream("/unit.json");
        JsonNode root = objectMapper.readTree(testData);
        JsonNode itemList = root.get("hits").get("hits");
        Assert.assertTrue(itemList.isArray());
        for (JsonNode item : itemList) {
            companyUnitEntityManager.parseRegistration(item.get("_source").get("VrproduktionsEnhed"));
        }
    }

    private void loadParticipant() throws IOException, DataFordelerException {
        InputStream testData = QueryTest.class.getResourceAsStream("/person.json");
        JsonNode root = objectMapper.readTree(testData);
        JsonNode itemList = root.get("hits").get("hits");
        Assert.assertTrue(itemList.isArray());
        for (JsonNode item : itemList) {
            participantEntityManager.parseRegistration(item.get("_source").get("Vrdeltagerperson"));
        }
    }

    private void giveAccess(SystemRole... rolesDefinitions) {
        ArrayList<String> roleNames = new ArrayList<>();
        for (SystemRole role : rolesDefinitions) {
            roleNames.add(role.getRoleName());
        }
        UserProfile testUserProfile = new UserProfile("TestProfile", roleNames);

        TestUserDetails testUserDetails = new TestUserDetails();
        testUserDetails.addUserProfile(testUserProfile);
        when(dafoUserManager.getFallbackUser()).thenReturn(testUserDetails);
    }

    private ResponseEntity<String> restSearch(ParameterMap parameters, String type) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity<String> httpEntity = new HttpEntity<String>("", headers);
        return this.restTemplate.exchange("/cvr/"+type+"/1/rest/search?" + parameters.asUrlParams(), HttpMethod.GET, httpEntity, String.class);
    }

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
            query.setKommunekode(101);
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

        } finally {
            session.close();
        }
    }


    @Test
    public void testCompanyRestQuery() throws IOException, DataFordelerException {
        Session session = null;
        try {
            loadCompany();
            giveAccess(CvrRolesDefinition.READ_CVR_ROLE);

            ParameterMap searchParameters = new ParameterMap();
            searchParameters.add("CVRNummer", "25052943");
            searchParameters.add("registrationFrom", "1999-11-30");
            searchParameters.add("registrationTo", "1999-12-01");

            ResponseEntity<String> response = restSearch(searchParameters, "company");
            Assert.assertEquals(200, response.getStatusCode().value());
            JsonNode jsonBody = objectMapper.readTree(response.getBody());

            ObjectNode entity = (ObjectNode) jsonBody.get("results").get(0);
            Assert.assertEquals(25052943, entity.get("CVRNummer").asInt());
            ArrayNode registrations = (ArrayNode) entity.get("registreringer");
            Assert.assertEquals(1, registrations.size());
            ObjectNode registration = (ObjectNode) registrations.get(0);
            Assert.assertTrue(OffsetDateTime.parse("1999-11-29T16:37:10+01:00").isEqual(OffsetDateTime.parse(registration.get("registreringFra").textValue())));
            Assert.assertTrue(OffsetDateTime.parse("2000-02-29T11:44:42+01:00").isEqual(OffsetDateTime.parse(registration.get("registreringTil").textValue())));

            Assert.assertEquals(2, registration.get("virkninger").size());
            ObjectNode effect = (ObjectNode) registration.get("virkninger").get(0);
            Assert.assertTrue(OffsetDateTime.parse("1999-11-15T01:00+01:00").isEqual(OffsetDateTime.parse(effect.get("virkningFra").asText())));
            Assert.assertTrue(effect.get("virkningTil").isNull());
            Assert.assertEquals("80", effect.get("virksomhedsform").asText());
            Assert.assertEquals("E&S", effect.get("dataleverandør").asText());
            Assert.assertEquals("1999-11-15", effect.get("virksomhedStartdato").asText());
            Assert.assertEquals(1, effect.get("penheder").size());
            Assert.assertEquals(1007256945, effect.get("penheder").get(0).asInt());


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
            registration = (ObjectNode) registrations.get(0);
            Assert.assertEquals(5, registration.get("virkninger").size());

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


    @Test
    public void testCompanyUnitIdempotence() throws Exception {
        Session session = sessionManager.getSessionFactory().openSession();
        try {
            loadUnit();
            List<CompanyUnitEntity> entities = queryManager.getAllEntities(session, CompanyUnitEntity.class);
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

            CompanyUnitQuery query = new CompanyUnitQuery();
            query.setPrimaryIndustry("620200");
            List<CompanyUnitEntity> entities = queryManager.getAllEntities(session, query, CompanyUnitEntity.class);
            List<Object> wrapped = companyUnitOutputWrapper.wrapResults(entities);
            Assert.assertEquals(1, wrapped.size());
            Assert.assertTrue(wrapped.get(0) instanceof ObjectNode);
            ObjectNode objectNode = (ObjectNode) wrapped.get(0);
            Assert.assertEquals(3, objectNode.get("registreringer").size());

            query = new CompanyUnitQuery();
            query.setKommunekode(101);
            entities = queryManager.getAllEntities(session, query, CompanyUnitEntity.class);
            Assert.assertEquals(5, entities.size());



        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Test
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
            query.setKommunekode(101);
            entities = queryManager.getAllEntities(session, query, ParticipantEntity.class);
            Assert.assertEquals(1, entities.size());

        } finally {
            if (session != null) {
                session.close();
            }
        }
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

}
