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
import dk.magenta.datafordeler.core.util.ListHashMap;
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
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
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

    @Test
    public void testQueryCompany() throws IOException, DataFordelerException {
        Session session = null;
        try {
            loadCompany();

            CompanyQuery query = new CompanyQuery();
            query.setEmailadresse("info@magenta.dk");
            session = sessionManager.getSessionFactory().openSession();

            List<CompanyEntity> entities = queryManager.getAllEntities(session, query, CompanyEntity.class);
            long start = Instant.now().toEpochMilli();
            List<Object> wrapped = companyOutputWrapper.wrapResults(entities);
            System.out.println(Instant.now().toEpochMilli() - start + "ms");

            Assert.assertEquals(1, wrapped.size());
            Assert.assertTrue(wrapped.get(0) instanceof ObjectNode);
            ObjectNode objectNode = (ObjectNode) wrapped.get(0);

            Assert.assertEquals(122, objectNode.get("registreringer").size());
            //System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode));

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }




    @Test
    public void testRestQueryCompany() throws IOException, DataFordelerException {
        Session session = null;
        try {
            loadCompany();
            giveAccess(CvrRolesDefinition.READ_CVR_ROLE);

            ParameterMap searchParameters = new ParameterMap();
            searchParameters.add("CVRNummer", "25052943");
            searchParameters.add("registrationFrom", "1999-11-30");
            searchParameters.add("registrationTo", "1999-12-01");

            ResponseEntity<String> response = search(searchParameters);
            Assert.assertEquals(200, response.getStatusCode().value());
            JsonNode jsonBody = objectMapper.readTree(response.getBody());

            ObjectNode entity = (ObjectNode) jsonBody.get("results").get(0);
            Assert.assertEquals(25052943, entity.get("CVRNummer").asInt());
            ArrayNode registrations = (ArrayNode) entity.get("registreringer");
            Assert.assertEquals(1, registrations.size());
            ObjectNode registration = (ObjectNode) registrations.get(0);
            Assert.assertEquals("1999-11-29T16:37:10+01:00", registration.get("registreringFra").textValue());
            Assert.assertEquals("2000-02-29T21:10:50+01:00", registration.get("registreringTil").textValue());

            Assert.assertEquals(1, registration.get("virkninger").size());
            ObjectNode effect = (ObjectNode) registration.get("virkninger").get(0);
            Assert.assertEquals("1999-11-15T01:00+01:00", effect.get("virkningFra").asText());
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

            response = search(searchParameters);
            Assert.assertEquals(200, response.getStatusCode().value());
            jsonBody = objectMapper.readTree(response.getBody());

            entity = (ObjectNode) jsonBody.get("results").get(0);
            registrations = (ArrayNode) entity.get("registreringer");
            registration = (ObjectNode) registrations.get(0);
            Assert.assertEquals(4, registration.get("virkninger").size());

            //System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonBody));

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private ResponseEntity<String> search(ParameterMap parameters) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity<String> httpEntity = new HttpEntity<String>("", headers);
        return this.restTemplate.exchange("/cvr/company/1/rest/search?" + parameters.asUrlParams(), HttpMethod.GET, httpEntity, String.class);
    }


    @Test
    public void testQueryCompanyUnit() throws IOException, DataFordelerException {
        Session session = null;
        try {
            InputStream testData = QueryTest.class.getResourceAsStream("/unit.json");
            JsonNode root = objectMapper.readTree(testData);
            JsonNode itemList = root.get("hits").get("hits");
            Assert.assertTrue(itemList.isArray());
            for (JsonNode item : itemList) {
                companyUnitEntityManager.parseRegistration(item.get("_source").get("VrproduktionsEnhed"));
            }

            CompanyUnitQuery query = new CompanyUnitQuery();
            //query.setAssociatedCompanyCvrNumber(1020895337L);
            session = sessionManager.getSessionFactory().openSession();

            try {
                List<CompanyUnitEntity> entities = queryManager.getAllEntities(session, query, CompanyUnitEntity.class);
                companyUnitOutputWrapper.wrapResults(entities);
            } catch (DataFordelerException e) {
                e.printStackTrace();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Test
    public void testQueryParticipant() throws IOException, DataFordelerException {
        Session session = null;
        try {
            InputStream testData = QueryTest.class.getResourceAsStream("/person.json");
            JsonNode root = objectMapper.readTree(testData);
            JsonNode itemList = root.get("hits").get("hits");
            Assert.assertTrue(itemList.isArray());
            for (JsonNode item : itemList) {
                participantEntityManager.parseRegistration(item.get("_source").get("Vrdeltagerperson"));
            }
            ParticipantQuery query = new ParticipantQuery();
            query.setNavne("Morten Kjærsgaard");
            session = sessionManager.getSessionFactory().openSession();

            try {
                List<ParticipantEntity> entities = queryManager.getAllEntities(session, query, ParticipantEntity.class);
                participantOutputWrapper.wrapResults(entities);
            } catch (DataFordelerException e) {
                e.printStackTrace();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

}
