package dk.magenta.datafordeler.cvr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dk.magenta.datafordeler.core.Application;
import dk.magenta.datafordeler.core.database.Effect;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.core.database.Registration;
import dk.magenta.datafordeler.core.database.SessionManager;
import dk.magenta.datafordeler.core.exception.DataFordelerException;
import dk.magenta.datafordeler.core.io.ImportMetadata;
import dk.magenta.datafordeler.cvr.data.company.CompanyEntity;
import dk.magenta.datafordeler.cvr.data.company.CompanyEntityManager;
import dk.magenta.datafordeler.cvr.data.company.CompanyRecordQuery;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitEntity;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitEntityManager;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitRecordQuery;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantEntity;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantEntityManager;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantRecordQuery;
import dk.magenta.datafordeler.cvr.data.unversioned.Municipality;
import dk.magenta.datafordeler.cvr.records.*;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
public class RecordTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private CvrPlugin plugin;


    private static HashMap<String, String> schemaMap = new HashMap<>();
    static {
        schemaMap.put("virksomhed", CompanyEntity.schema);
        schemaMap.put("produktionsenhed", CompanyUnitEntity.schema);
        schemaMap.put("deltager", ParticipantEntity.schema);
    }

    private HashMap<Integer, JsonNode> loadCompany() throws IOException, DataFordelerException {
        ImportMetadata importMetadata = new ImportMetadata();
        Session session = sessionManager.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        InputStream input = ParseTest.class.getResourceAsStream("/company_in.json");
        boolean linedFile = false;
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


    private HashMap<Integer, JsonNode> loadUnits() throws IOException, DataFordelerException {
        ImportMetadata importMetadata = new ImportMetadata();
        Session session = sessionManager.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        InputStream input = ParseTest.class.getResourceAsStream("/unit.json");
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
        this.loadUnits();
        this.loadUnits();
        HashMap<Integer, JsonNode> units = this.loadUnits();
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


    private HashMap<Long, JsonNode> loadParticipant() throws IOException, DataFordelerException {
        ImportMetadata importMetadata = new ImportMetadata();
        Session session = sessionManager.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        InputStream input = ParseTest.class.getResourceAsStream("/person.json");
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
        this.loadParticipant();
        this.loadParticipant();
        HashMap<Long, JsonNode> persons = this.loadParticipant();
        Session session = sessionManager.getSessionFactory().openSession();
        try {
            for (long participantNumber : persons.keySet()) {
                HashMap<String, Object> filter = new HashMap<>();
                filter.put("unitNumber", participantNumber);
                ParticipantRecord companyUnitRecord = QueryManager.getItem(session, ParticipantRecord.class, filter);
                if (companyUnitRecord == null) {
                    System.out.println("Didn't find participant number "+participantNumber);
                } else {
                    compareJson(persons.get(participantNumber), objectMapper.valueToTree(companyUnitRecord), Collections.singletonList("root"));
                }
            }

            ParticipantRecordQuery query = new ParticipantRecordQuery();
            OffsetDateTime time = OffsetDateTime.now();
            query.setRegistrationTo(time);
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
            query.setKommuneKode((String) null);



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
