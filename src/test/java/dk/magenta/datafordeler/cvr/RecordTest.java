package dk.magenta.datafordeler.cvr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dk.magenta.datafordeler.core.Application;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.core.database.SessionManager;
import dk.magenta.datafordeler.core.exception.DataFordelerException;
import dk.magenta.datafordeler.core.io.ImportMetadata;
import dk.magenta.datafordeler.core.user.DafoUserManager;
import dk.magenta.datafordeler.cvr.access.CvrRolesDefinition;
import dk.magenta.datafordeler.cvr.entitymanager.CompanyEntityManager;
import dk.magenta.datafordeler.cvr.query.CompanyRecordQuery;
import dk.magenta.datafordeler.cvr.entitymanager.CompanyUnitEntityManager;
import dk.magenta.datafordeler.cvr.query.CompanyUnitRecordQuery;
import dk.magenta.datafordeler.cvr.entitymanager.ParticipantEntityManager;
import dk.magenta.datafordeler.cvr.query.ParticipantRecordQuery;
import dk.magenta.datafordeler.cvr.records.*;
import dk.magenta.datafordeler.cvr.output.CompanyRecordOutputWrapper;
import org.hibernate.Session;
import org.hibernate.Transaction;
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
import java.time.format.DateTimeParseException;
import java.util.*;

import static org.mockito.Mockito.when;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecordTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private CompanyRecordOutputWrapper companyRecordOutputWrapper;

    @Autowired
    private CvrPlugin plugin;

    @Autowired
    private TestRestTemplate restTemplate;

    private static HashMap<String, String> schemaMap = new HashMap<>();
    static {
        schemaMap.put("virksomhed", CompanyRecord.schema);
        schemaMap.put("produktionsenhed", CompanyUnitRecord.schema);
        schemaMap.put("deltager", ParticipantRecord.schema);
    }

    @SpyBean
    private DafoUserManager dafoUserManager;

    private void applyAccess(TestUserDetails testUserDetails) {
        when(dafoUserManager.getFallbackUser()).thenReturn(testUserDetails);
    }

    private void whitelistLocalhost() {
        when(dafoUserManager.getIpWhitelist()).thenReturn(Collections.singleton("127.0.0.1"));
    }

    private HashMap<Integer, JsonNode> loadCompany() throws IOException, DataFordelerException {
        return loadCompany("/company_in.json");
    }

    private HashMap<Integer, JsonNode> loadCompany(String resource) throws IOException, DataFordelerException {
        InputStream input = RecordTest.class.getResourceAsStream(resource);
        if (input == null) {
            throw new MissingResourceException("Missing resource \""+resource+"\"", resource, "key");
        }
        return loadCompany(input, false);
    }

    private HashMap<Integer, JsonNode> loadCompany(InputStream input, boolean linedFile) throws IOException, DataFordelerException {
        ImportMetadata importMetadata = new ImportMetadata();
        Session session = sessionManager.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        HashMap<Integer, JsonNode> companies = new HashMap<>();
        try {
            importMetadata.setSession(session);

            if (linedFile) {
                int lineNumber = 0;
                Scanner lineScanner = new Scanner(input, "UTF-8").useDelimiter("\n");
                while (lineScanner.hasNext()) {
                    String data = lineScanner.next();

                    JsonNode root = objectMapper.readTree(data);
                    JsonNode itemList = root.get("hits").get("hits");
                    Assert.assertTrue(itemList.isArray());
                    for (JsonNode item : itemList) {
                        String type = item.get("_type").asText();
                        CompanyEntityManager entityManager = (CompanyEntityManager) plugin.getRegisterManager().getEntityManager(schemaMap.get(type));
                        JsonNode companyInputNode = item.get("_source").get("Vrvirksomhed");
                        entityManager.parseData(companyInputNode, importMetadata, session);
                        companies.put(companyInputNode.get("cvrNummer").asInt(), companyInputNode);
                    }
                    lineNumber++;
                    if (lineNumber % 100 == 0) {
                        System.out.println("loaded line " + lineNumber);
                    }
                }
            } else {
                JsonNode root = objectMapper.readTree(input);
                JsonNode itemList = root.get("hits").get("hits");
                Assert.assertTrue(itemList.isArray());
                for (JsonNode item : itemList) {
                    String type = item.get("_type").asText();
                    CompanyEntityManager entityManager = (CompanyEntityManager) plugin.getRegisterManager().getEntityManager(schemaMap.get(type));
                    JsonNode companyInputNode = item.get("_source").get("Vrvirksomhed");
                    entityManager.parseData(companyInputNode, importMetadata, session);
                    companies.put(companyInputNode.get("cvrNummer").asInt(), companyInputNode);
                }
            }
            transaction.commit();
        } finally {
            session.close();
            QueryManager.clearCaches();
            input.close();
        }
        return companies;
    }

    @Test
    public void testCompany() throws DataFordelerException, IOException {
        this.loadCompany();
        this.loadCompany();
        HashMap<Integer, JsonNode> companies = this.loadCompany();
        Session session = sessionManager.getSessionFactory().openSession();
        try {
            for (int cvrNumber : companies.keySet()) {
                HashMap<String, Object> filter = new HashMap<>();
                filter.put("cvrNumber", cvrNumber);
                CompanyRecord companyRecord = QueryManager.getItem(session, CompanyRecord.class, filter);
                if (companyRecord == null) {
                    System.out.println("Didn't find cvr number "+cvrNumber);
                } else {
                    compareJson(companies.get(cvrNumber), objectMapper.valueToTree(companyRecord), Collections.singletonList("root"));
                }
            }

            CompanyRecordQuery query = new CompanyRecordQuery();
            OffsetDateTime time = OffsetDateTime.now();
            query.setRegistrationTo(time);
            query.setEffectFrom(time);
            query.setEffectTo(time);
            query.applyFilters(session);

            query.setKommuneKode(101);
            Assert.assertEquals(1, QueryManager.getAllEntities(session, query, CompanyRecord.class).size());
            query.clearKommuneKoder();
            query.setTelefonnummer("33369696");
            Assert.assertEquals(1, QueryManager.getAllEntities(session, query, CompanyRecord.class).size());
            query.clearTelefonnummer();
            query.setEmailadresse("info@magenta.dk");
            Assert.assertEquals(1, QueryManager.getAllEntities(session, query, CompanyRecord.class).size());
            query.clearEmailadresse();
            query.setReklamebeskyttelse("true");
            Assert.assertEquals(1, QueryManager.getAllEntities(session, query, CompanyRecord.class).size());
            query.clearReklamebeskyttelse();
            query.setVirksomhedsform(80);
            Assert.assertEquals(1, QueryManager.getAllEntities(session, query, CompanyRecord.class).size());
            query.clearVirksomhedsform();
            query.setVirksomhedsnavn("MAGENTA ApS");
            Assert.assertEquals(1, QueryManager.getAllEntities(session, query, CompanyRecord.class).size());

            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this.companyRecordOutputWrapper.wrapResult(QueryManager.getAllEntities(session, query, CompanyRecord.class).get(0), query)));
            query.clearKommuneKoder();




            time = OffsetDateTime.parse("1998-01-01T00:00:00Z");
            query.setRegistrationTo(time);
            query.setEffectFrom(time);
            query.setEffectTo(time);
            query.applyFilters(session);


            query.setKommuneKode(101);
            Assert.assertEquals(0, QueryManager.getAllEntities(session, query, CompanyRecord.class).size());
            query.clearKommuneKoder();
            query.setTelefonnummer("33369696");
            Assert.assertEquals(0, QueryManager.getAllEntities(session, query, CompanyRecord.class).size());
            query.clearTelefonnummer();
            query.setEmailadresse("info@magenta.dk");
            Assert.assertEquals(0, QueryManager.getAllEntities(session, query, CompanyRecord.class).size());
            query.clearEmailadresse();
            query.setReklamebeskyttelse("true");
            Assert.assertEquals(0, QueryManager.getAllEntities(session, query, CompanyRecord.class).size());
            query.setReklamebeskyttelse(null);
            query.setVirksomhedsform(80);
            Assert.assertEquals(0, QueryManager.getAllEntities(session, query, CompanyRecord.class).size());
            query.clearVirksomhedsform();
            query.setVirksomhedsnavn("MAGENTA ApS");
            Assert.assertEquals(0, QueryManager.getAllEntities(session, query, CompanyRecord.class).size());
            query.clearVirksomhedsnavn();


        } finally {
            session.close();
        }
    }

    @Test
    public void testUpdateCompany() throws IOException, DataFordelerException {
        loadCompany("/company_in.json");
        loadCompany("/company_in2.json");
        Session session = sessionManager.getSessionFactory().openSession();
        try {
            CompanyRecordQuery query = new CompanyRecordQuery();
            query.setCvrNumre("25052943");
            List<CompanyRecord> records = QueryManager.getAllEntities(session, query, CompanyRecord.class);
            CompanyRecord companyRecord = records.get(0);

            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(companyRecord));

            Assert.assertEquals(3, companyRecord.getNames().size());
            Assert.assertEquals(1, companyRecord.getSecondaryNames().size());
            Assert.assertEquals(1, companyRecord.getPostalAddress().size());
            Assert.assertEquals(5, companyRecord.getLocationAddress().size());
            Assert.assertEquals(2, companyRecord.getPhoneNumber().size());
            Assert.assertEquals(0, companyRecord.getFaxNumber().size());
            Assert.assertEquals(2, companyRecord.getEmailAddress().size());
            Assert.assertEquals(2, companyRecord.getLifecycle().size());
            Assert.assertEquals(5, companyRecord.getPrimaryIndustry().size());
            Assert.assertEquals(1, companyRecord.getSecondaryIndustry1().size());
            Assert.assertEquals(0, companyRecord.getSecondaryIndustry2().size());
            Assert.assertEquals(0, companyRecord.getSecondaryIndustry3().size());
            Assert.assertEquals(0, companyRecord.getStatus().size());
            Assert.assertEquals(2, companyRecord.getCompanyStatus().size());
            Assert.assertEquals(16, companyRecord.getYearlyNumbers().size());
            Assert.assertEquals(64, companyRecord.getQuarterlyNumbers().size());
            Assert.assertEquals(14, companyRecord.getAttributes().size());
            Assert.assertEquals(3, companyRecord.getProductionUnits().size());
            Assert.assertEquals(12, companyRecord.getParticipants().size());
            Assert.assertEquals(1, companyRecord.getFusions().size());

            Assert.assertEquals(2, companyRecord.getFusions().iterator().next().getName().size());
            Assert.assertEquals(1, companyRecord.getFusions().iterator().next().getIncoming().size());
            Assert.assertEquals(2, companyRecord.getFusions().iterator().next().getIncoming().iterator().next().getValues().size());



            Assert.assertEquals(1, companyRecord.getSplits().size());
            Assert.assertEquals(2, companyRecord.getMetadata().getNewestName().size());
            Assert.assertEquals(2, companyRecord.getMetadata().getNewestForm().size());
            Assert.assertEquals(2, companyRecord.getMetadata().getNewestLocation().size());
            Assert.assertEquals(2, companyRecord.getMetadata().getNewestPrimaryIndustry().size());
            Assert.assertEquals(1, companyRecord.getMetadata().getNewestSecondaryIndustry1().size());

            boolean foundParticipantData = false;
            for (CompanyParticipantRelationRecord participantRelationRecord : companyRecord.getParticipants()) {
                if (participantRelationRecord.getRelationParticipantRecord().getUnitNumber() == 4000004988L) {
                    foundParticipantData = true;

                    Assert.assertEquals(2, participantRelationRecord.getRelationParticipantRecord().getNames().size());
                    Assert.assertEquals(5, participantRelationRecord.getRelationParticipantRecord().getLocationAddress().size());

                    Assert.assertEquals(1, participantRelationRecord.getOffices().size());
                    OfficeRelationRecord officeRelationRecord = participantRelationRecord.getOffices().iterator().next();
                    Assert.assertEquals(1, officeRelationRecord.getAttributes().size());
                    Assert.assertEquals(2, officeRelationRecord.getAttributes().iterator().next().getValues().size());
                    Assert.assertEquals(2, officeRelationRecord.getOfficeRelationUnitRecord().getNames().size());
                    Assert.assertEquals(2, officeRelationRecord.getOfficeRelationUnitRecord().getLocationAddress().size());

                    boolean foundOrganization1 = false;
                    boolean foundOrganization2 = false;
                    for (OrganizationRecord organizationRecord : participantRelationRecord.getOrganizations()) {
                        if (organizationRecord.getUnitNumber() == 4004733975L) {
                            foundOrganization1 = true;
                            Assert.assertEquals(2, organizationRecord.getNames().size());
                            Assert.assertEquals(1, organizationRecord.getAttributes().size());
                            Assert.assertEquals(2, organizationRecord.getAttributes().iterator().next().getValues().size());
                        }
                        if (organizationRecord.getUnitNumber() == 4004733976L) {
                            foundOrganization2 = true;
                            Assert.assertEquals(2, organizationRecord.getMemberData().size());
                            for (OrganizationMemberdataRecord organizationMemberdataRecord : organizationRecord.getMemberData()) {
                                if (organizationMemberdataRecord.getIndex() == 0) {
                                    Assert.assertEquals(1, organizationMemberdataRecord.getAttributes().size());
                                }
                                if (organizationMemberdataRecord.getIndex() == 1) {
                                    Assert.assertEquals(3, organizationMemberdataRecord.getAttributes().size());
                                    for (AttributeRecord attributeRecord : organizationMemberdataRecord.getAttributes()) {
                                        if (attributeRecord.getType().equals("FUNKTION")) {
                                            Assert.assertEquals(2, attributeRecord.getValues().size());
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Assert.assertTrue(foundOrganization1);
                    Assert.assertTrue(foundOrganization2);
                }
            }
            Assert.assertTrue(foundParticipantData);

        } finally {
            session.close();
        }
    }

    @Test
    public void testRestCompany() throws IOException, DataFordelerException {
        loadCompany("/company_in.json");
        whitelistLocalhost();
        TestUserDetails testUserDetails = new TestUserDetails();
        HttpEntity<String> httpEntity = new HttpEntity<String>("", new HttpHeaders());
        ResponseEntity<String> resp = restTemplate.exchange("/cvr/company/1/rest/search?cvrNummer=25052943", HttpMethod.GET, httpEntity, String.class);
        Assert.assertEquals(403, resp.getStatusCodeValue());

        testUserDetails.giveAccess(CvrRolesDefinition.READ_CVR_ROLE);
        this.applyAccess(testUserDetails);

        resp = restTemplate.exchange("/cvr/company/1/rest/search?cvrnummer=25052943&virkningFra=2000-01-01&virkningTil=2000-01-01&registrationFrom=2000-01-01&fmt=legacy", HttpMethod.GET, httpEntity, String.class);
        String body = resp.getBody();
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.readTree(body)));
        Assert.assertEquals(200, resp.getStatusCodeValue());
    }

    private HashMap<Integer, JsonNode> loadUnit(String resource) throws IOException, DataFordelerException {
        ImportMetadata importMetadata = new ImportMetadata();
        Session session = sessionManager.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        InputStream input = RecordTest.class.getResourceAsStream(resource);
        if (input == null) {
            throw new MissingResourceException("Missing resource \""+resource+"\"", resource, "key");
        }
        boolean linedFile = false;
        HashMap<Integer, JsonNode> units = new HashMap<>();
        try {
            importMetadata.setSession(session);

            if (linedFile) {
                int lineNumber = 0;
                Scanner lineScanner = new Scanner(input, "UTF-8").useDelimiter("\n");
                while (lineScanner.hasNext()) {
                    String data = lineScanner.next();

                    JsonNode root = objectMapper.readTree(data);
                    JsonNode itemList = root.get("hits").get("hits");
                    Assert.assertTrue(itemList.isArray());
                    for (JsonNode item : itemList) {
                        String type = item.get("_type").asText();
                        CompanyUnitEntityManager entityManager = (CompanyUnitEntityManager) plugin.getRegisterManager().getEntityManager(schemaMap.get(type));
                        JsonNode unitInputNode = item.get("_source").get("VrproduktionsEnhed");
                        entityManager.parseData(unitInputNode, importMetadata, session);
                        units.put(unitInputNode.get("pNummer").asInt(), unitInputNode);
                    }
                    lineNumber++;
                    System.out.println("loaded line " + lineNumber);
                    if (lineNumber >= 10) {
                        break;
                    }
                }
            } else {
                JsonNode root = objectMapper.readTree(input);
                JsonNode itemList = root.get("hits").get("hits");
                Assert.assertTrue(itemList.isArray());
                for (JsonNode item : itemList) {
                    String type = item.get("_type").asText();
                    CompanyUnitEntityManager entityManager = (CompanyUnitEntityManager) plugin.getRegisterManager().getEntityManager(schemaMap.get(type));
                    JsonNode unitInputNode = item.get("_source").get("VrproduktionsEnhed");
                    entityManager.parseData(unitInputNode, importMetadata, session);
                    units.put(unitInputNode.get("pNummer").asInt(), unitInputNode);
                }
            }
            transaction.commit();
        } finally {
            session.close();
            QueryManager.clearCaches();
            input.close();
        }
        return units;
    }

    @Test
    public void testCompanyUnit() throws DataFordelerException, IOException {
        this.loadUnit("/unit.json");
        this.loadUnit("/unit.json");
        HashMap<Integer, JsonNode> units = this.loadUnit("/unit.json");
        Session session = sessionManager.getSessionFactory().openSession();
        try {
            for (int pNumber : units.keySet()) {
                HashMap<String, Object> filter = new HashMap<>();
                filter.put("pNumber", pNumber);
                CompanyUnitRecord companyUnitRecord = QueryManager.getItem(session, CompanyUnitRecord.class, filter);
                if (companyUnitRecord == null) {
                    System.out.println("Didn't find p number "+pNumber);
                } else {
                    compareJson(units.get(pNumber), objectMapper.valueToTree(companyUnitRecord), Collections.singletonList("root"));
                }
            }


            CompanyUnitRecordQuery query = new CompanyUnitRecordQuery();
            OffsetDateTime time = OffsetDateTime.parse("2017-01-01T00:00:00Z");
            //query.setRegistrationTo(time);
            query.setEffectFrom(time);
            query.setEffectTo(time);
            query.applyFilters(session);

            query.setPrimaryIndustry("478900");
            Assert.assertEquals(1, QueryManager.getAllEntities(session, query, CompanyUnitRecord.class).size());
            query.clearPrimaryIndustry();
            query.setAssociatedCompanyCvrNummer("37952273");
            Assert.assertEquals(1, QueryManager.getAllEntities(session, query, CompanyUnitRecord.class).size());
            query.clearAssociatedCompanyCvrNummer();
            query.setPNummer("1021686405");
            Assert.assertEquals(1, QueryManager.getAllEntities(session, query, CompanyUnitRecord.class).size());
            query.clearPNummer();
            query.setKommuneKode("561");
            Assert.assertEquals(1, QueryManager.getAllEntities(session, query, CompanyUnitRecord.class).size());
            query.clearKommuneKode();


            time = OffsetDateTime.parse("1900-01-01T00:00:00Z");
            query.setRegistrationTo(time);
            query.setEffectFrom(time);
            query.setEffectTo(time);
            query.applyFilters(session);


            query.setPrimaryIndustry("478900");
            Assert.assertEquals(0, QueryManager.getAllEntities(session, query, CompanyUnitRecord.class).size());
            query.clearPrimaryIndustry();
            query.setAssociatedCompanyCvrNummer("37952273");
            Assert.assertEquals(0, QueryManager.getAllEntities(session, query, CompanyUnitRecord.class).size());
            query.clearAssociatedCompanyCvrNummer();
            query.setPNummer("1021686405");
            Assert.assertEquals(1, QueryManager.getAllEntities(session, query, CompanyUnitRecord.class).size());
            query.clearPNummer();
            query.setKommuneKode("101");
            Assert.assertEquals(0, QueryManager.getAllEntities(session, query, CompanyUnitRecord.class).size());
            query.clearKommuneKode();

        } finally {
            session.close();
        }
    }


    @Test
    public void testUpdateCompanyUnit() throws IOException, DataFordelerException {
        loadUnit("/unit.json");
        loadUnit("/unit2.json");
        Session session = sessionManager.getSessionFactory().openSession();
        try {
            CompanyUnitRecordQuery query = new CompanyUnitRecordQuery();
            query.setPNummer("1020895337");
            List<CompanyUnitRecord> records = QueryManager.getAllEntities(session, query, CompanyUnitRecord.class);
            Assert.assertEquals(1, records.size());
            CompanyUnitRecord companyUnitRecord = records.get(0);
            Assert.assertEquals(2, companyUnitRecord.getNames().size());
            Assert.assertEquals(1, companyUnitRecord.getPostalAddress().size());
            Assert.assertEquals(2, companyUnitRecord.getLocationAddress().size());
            Assert.assertEquals(1, companyUnitRecord.getPhoneNumber().size());
            Assert.assertEquals(0, companyUnitRecord.getFaxNumber().size());
            Assert.assertEquals(2, companyUnitRecord.getEmailAddress().size());
            Assert.assertEquals(2, companyUnitRecord.getLifecycle().size());
            Assert.assertEquals(2, companyUnitRecord.getPrimaryIndustry().size());
            Assert.assertEquals(1, companyUnitRecord.getSecondaryIndustry1().size());
            Assert.assertEquals(0, companyUnitRecord.getSecondaryIndustry2().size());
            Assert.assertEquals(0, companyUnitRecord.getSecondaryIndustry3().size());
            Assert.assertEquals(1, companyUnitRecord.getYearlyNumbers().size());
            Assert.assertEquals(4, companyUnitRecord.getQuarterlyNumbers().size());
            Assert.assertEquals(1, companyUnitRecord.getAttributes().size());
            Assert.assertEquals(0, companyUnitRecord.getParticipants().size());
            Assert.assertEquals(2, companyUnitRecord.getMetadata().getNewestName().size());
            Assert.assertEquals(2, companyUnitRecord.getMetadata().getNewestLocation().size());
            Assert.assertEquals(2, companyUnitRecord.getMetadata().getNewestPrimaryIndustry().size());
            Assert.assertEquals(1, companyUnitRecord.getMetadata().getNewestSecondaryIndustry1().size());
        } finally {
            session.close();
        }
    }

    @Test
    public void testRestCompanyUnit() throws IOException, DataFordelerException {
        loadUnit("/unit.json");
        whitelistLocalhost();
        TestUserDetails testUserDetails = new TestUserDetails();
        HttpEntity<String> httpEntity = new HttpEntity<String>("", new HttpHeaders());
        ResponseEntity<String> resp = restTemplate.exchange("/cvr/unit/1/rest/search?pnummer=1020895337", HttpMethod.GET, httpEntity, String.class);
        Assert.assertEquals(403, resp.getStatusCodeValue());

        testUserDetails.giveAccess(CvrRolesDefinition.READ_CVR_ROLE);
        this.applyAccess(testUserDetails);

        resp = restTemplate.exchange("/cvr/unit/1/rest/search?pnummer=1020895337&virkningFra=2016-01-01&virkningTil=2016-01-01", HttpMethod.GET, httpEntity, String.class);
        String body = resp.getBody();
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.readTree(body)));
        Assert.assertEquals(200, resp.getStatusCodeValue());
    }

    private HashMap<Long, JsonNode> loadParticipant(String resource) throws IOException, DataFordelerException {
        ImportMetadata importMetadata = new ImportMetadata();
        Session session = sessionManager.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        InputStream input = ParseTest.class.getResourceAsStream(resource);
        if (input == null) {
            throw new MissingResourceException("Missing resource \""+resource+"\"", resource, "key");
        }
        boolean linedFile = false;
        HashMap<Long, JsonNode> persons = new HashMap<>();
        try {
            importMetadata.setSession(session);

            if (linedFile) {
                int lineNumber = 0;
                Scanner lineScanner = new Scanner(input, "UTF-8").useDelimiter("\n");
                while (lineScanner.hasNext()) {
                    String data = lineScanner.next();

                    JsonNode root = objectMapper.readTree(data);
                    JsonNode itemList = root.get("hits").get("hits");
                    Assert.assertTrue(itemList.isArray());
                    for (JsonNode item : itemList) {
                        String type = item.get("_type").asText();
                        ParticipantEntityManager entityManager = (ParticipantEntityManager) plugin.getRegisterManager().getEntityManager(schemaMap.get(type));
                        JsonNode participantInputNode = item.get("_source").get("Vrdeltagerperson");
                        entityManager.parseData(participantInputNode, importMetadata, session);
                        persons.put(participantInputNode.get("enhedsNummer").asLong(), participantInputNode);
                    }
                    lineNumber++;
                    System.out.println("loaded line " + lineNumber);
                    if (lineNumber >= 10) {
                        break;
                    }
                }
            } else {
                JsonNode root = objectMapper.readTree(input);
                JsonNode itemList = root.get("hits").get("hits");
                Assert.assertTrue(itemList.isArray());
                for (JsonNode item : itemList) {
                    String type = item.get("_type").asText();
                    ParticipantEntityManager entityManager = (ParticipantEntityManager) plugin.getRegisterManager().getEntityManager(schemaMap.get(type));
                    JsonNode unitInputNode = item.get("_source").get("Vrdeltagerperson");
                    entityManager.parseData(unitInputNode, importMetadata, session);
                    persons.put(unitInputNode.get("enhedsNummer").asLong(), unitInputNode);
                }
            }
            transaction.commit();
        } finally {
            session.close();
            QueryManager.clearCaches();
            input.close();
        }
        return persons;
    }

    @Test
    public void testParticipant() throws DataFordelerException, IOException {
        loadParticipant("/person.json");
        loadParticipant("/person.json");
        HashMap<Long, JsonNode> persons = loadParticipant("/person.json");
        Session session = sessionManager.getSessionFactory().openSession();
        try {
            for (long participantNumber : persons.keySet()) {
                HashMap<String, Object> filter = new HashMap<>();
                filter.put("unitNumber", participantNumber);
                ParticipantRecord participantRecord = QueryManager.getItem(session, ParticipantRecord.class, filter);
                if (participantRecord == null) {
                    System.out.println("Didn't find participant number "+participantNumber);
                } else {
                    compareJson(persons.get(participantNumber), objectMapper.valueToTree(participantRecord), Collections.singletonList("root"));
                }
            }

            ParticipantRecordQuery query = new ParticipantRecordQuery();
            OffsetDateTime time = OffsetDateTime.now();
            //query.setRegistrationTo(time);
            query.setEffectFrom(time);
            query.setEffectTo(time);
            query.applyFilters(session);

            query.setEnhedsNummer("4000004988");
            Assert.assertEquals(1, QueryManager.getAllEntities(session, query, ParticipantRecord.class).size());
            query.clearEnhedsNummer();

            query.setNavn("Morten*");
            Assert.assertEquals(1, QueryManager.getAllEntities(session, query, ParticipantRecord.class).size());
            query.clearNavn();

            query.setKommuneKode("101");
            Assert.assertEquals(1, QueryManager.getAllEntities(session, query, ParticipantRecord.class).size());
            query.clearKommuneKode();



            time = OffsetDateTime.parse("1900-01-01T00:00:00Z");
            //query.setRegistrationTo(time);
            query.setEffectFrom(time);
            query.setEffectTo(time);
            query.applyFilters(session);



            query.setEnhedsNummer("4000004988");
            Assert.assertEquals(1, QueryManager.getAllEntities(session, query, ParticipantRecord.class).size());
            query.clearEnhedsNummer();
            query.setNavn("Morten*");
            Assert.assertEquals(1, QueryManager.getAllEntities(session, query, ParticipantRecord.class).size());
            query.clearNavn();
            query.setKommuneKode("101");
            Assert.assertEquals(0, QueryManager.getAllEntities(session, query, ParticipantRecord.class).size());
            query.setKommuneKode((String) null);

        } finally {
            session.close();
        }
    }

    @Test
    public void testUpdateParticipant() throws IOException, DataFordelerException {
        loadParticipant("/person.json");
        loadParticipant("/person2.json");
        Session session = sessionManager.getSessionFactory().openSession();
        try {
            ParticipantRecordQuery query = new ParticipantRecordQuery();
            query.setEnhedsNummer("4000004988");
            List<ParticipantRecord> records = QueryManager.getAllEntities(session, query, ParticipantRecord.class);
            Assert.assertEquals(1, records.size());
            ParticipantRecord participantRecord = records.get(0);
            Assert.assertEquals(2, participantRecord.getNames().size());
            Assert.assertEquals(1, participantRecord.getPostalAddress().size());
            Assert.assertEquals(5, participantRecord.getLocationAddress().size());
            Assert.assertEquals(1, participantRecord.getBusinessAddress().size());
            Assert.assertEquals(1, participantRecord.getPhoneNumber().size());
            Assert.assertEquals(0, participantRecord.getFaxNumber().size());
            Assert.assertEquals(1, participantRecord.getEmailAddress().size());
            Assert.assertEquals(5, participantRecord.getCompanyRelation().size());
            Assert.assertEquals(0, participantRecord.getAttributes().size());
            Assert.assertEquals(1, participantRecord.getMetadata().getMetadataContactData().size());

            boolean foundCompanyData = false;
            for (CompanyParticipantRelationRecord relationRecord : participantRecord.getCompanyRelation()) {
                if (relationRecord.getCompanyUnitNumber() == 4001248508L) {
                    foundCompanyData = true;
                    Assert.assertEquals(3, relationRecord.getRelationCompanyRecord().getNames().size());
                    Assert.assertEquals(0, relationRecord.getRelationCompanyRecord().getStatus().size());
                    Assert.assertEquals(2, relationRecord.getRelationCompanyRecord().getCompanyStatus().size());
                    Assert.assertEquals(2, relationRecord.getRelationCompanyRecord().getForm().size());
                }
            }
            Assert.assertTrue(foundCompanyData);

        } finally {
            session.close();
        }
    }


    @Test
    public void testRestParticipant() throws IOException, DataFordelerException {
        loadParticipant("/person.json");
        whitelistLocalhost();
        TestUserDetails testUserDetails = new TestUserDetails();
        HttpEntity<String> httpEntity = new HttpEntity<String>("", new HttpHeaders());
        ResponseEntity<String> resp = restTemplate.exchange("/cvr/participant/1/rest/search?enhedsNummer=4000004988", HttpMethod.GET, httpEntity, String.class);
        Assert.assertEquals(403, resp.getStatusCodeValue());

        testUserDetails.giveAccess(CvrRolesDefinition.READ_CVR_ROLE);
        this.applyAccess(testUserDetails);

        resp = restTemplate.exchange("/cvr/participant/1/rest/search?enhedsNummer=4000004988&virkningFra=2001-01-01&virkningTil=2001-01-01", HttpMethod.GET, httpEntity, String.class);
        String body = resp.getBody();
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.readTree(body)));
        Assert.assertEquals(200, resp.getStatusCodeValue());
    }

    /**
     * Checks that all items in n1 are also present in n2
     * @param n1
     * @param n2
     * @param path
     * @throws JsonProcessingException
     */
    private void compareJson(JsonNode n1, JsonNode n2, List<String> path) throws JsonProcessingException {
        if (n1 == null && n2 != null) {
            System.out.println("Mismatch: "+n1+" != "+n2+" at "+path);
        } else if (n1 != null && n2 == null) {
            System.out.println("Mismatch: "+n1+" != "+n2+" at "+path);
        } else if (n1.isObject() && n2.isObject()) {
            ObjectNode o1 = (ObjectNode) n1;
            ObjectNode o2 = (ObjectNode) n2;
            Set<String> f2 = new HashSet<>();
            Iterator<String> o2Fields = o2.fieldNames();
            while (o2Fields.hasNext()) {
                f2.add(o2Fields.next());
            }

            Iterator<String> o1Fields = o1.fieldNames();
            while (o1Fields.hasNext()) {
                String field = o1Fields.next();
                if (!f2.contains(field)) {
                    System.out.println("Mismatch: missing field "+field+" at "+path);
                } else {
                    ArrayList<String> subpath = new ArrayList<>(path);
                    subpath.add(field);
                    compareJson(o1.get(field), o2.get(field), subpath);
                }
            }

        } else if (n1.isArray() && n2.isArray()) {
            ArrayNode a1 = (ArrayNode) n1;
            ArrayNode a2 = (ArrayNode) n2;

            if (a1.size() != a2.size()) {
                System.out.println("Mismatch: Array["+a1.size()+"] != Array["+a2.size()+"] at "+path);
                System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(a2));
            } else {

                for (int i = 0; i < a1.size(); i++) {
                    boolean found = false;
                    for (int j=0; j<a2.size(); j++) {
                        if (a1.get(i).asText().equals(a2.get(j).asText())) {
                            found = true;
                        }
                    }
                    if (!found) {
                        System.out.println("Mismatch: Didn't find item "+a1.get(i)+" in "+objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(a2)+" at "+path);
                    }
                }
            }


        } else if (!n1.asText().equals(n2.asText())){
            boolean skip = false;
            try {
                if (OffsetDateTime.parse(n1.asText()).isEqual(OffsetDateTime.parse(n2.asText()))) {
                    skip = true;
                }
            } catch (DateTimeParseException e) {}
            if (!skip) {
                System.out.println("Mismatch: " + n1.asText() + " (" + n1.getNodeType().name() + ") != " + n2.asText() + " (" + n2.getNodeType().name() + ") at " + path);
            }
        }
    }
}
