package dk.magenta.datafordeler.cvr;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.magenta.datafordeler.core.Application;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.core.database.SessionManager;
import dk.magenta.datafordeler.core.exception.DataFordelerException;
import dk.magenta.datafordeler.core.io.ImportMetadata;
import dk.magenta.datafordeler.cvr.entitymanager.CompanyEntityManager;
import dk.magenta.datafordeler.cvr.entitymanager.CompanyUnitEntityManager;
import dk.magenta.datafordeler.cvr.entitymanager.ParticipantEntityManager;
import dk.magenta.datafordeler.cvr.query.CompanyRecordQuery;
import dk.magenta.datafordeler.cvr.records.CompanyRecord;
import dk.magenta.datafordeler.cvr.records.CompanyUnitRecord;
import dk.magenta.datafordeler.cvr.records.ParticipantRecord;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ParseTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private CvrPlugin plugin;

    private static HashMap<String, String> schemaMap = new HashMap<>();
    static {
        schemaMap.put("_doc", CompanyRecord.schema);
        schemaMap.put("produktionsenhed", CompanyUnitRecord.schema);
        schemaMap.put("deltager", ParticipantRecord.schema);
    }

    @Test
    public void testParseCompanyFile() throws DataFordelerException, IOException {
        ImportMetadata importMetadata = new ImportMetadata();
        Session session = sessionManager.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            importMetadata.setSession(session);
            InputStream input = ParseTest.class.getResourceAsStream("/company_in.json");
            JsonNode root = objectMapper.readTree(input);
            JsonNode itemList = root.get("hits").get("hits");
            Assert.assertTrue(itemList.isArray());
            for (JsonNode item : itemList) {
                String type = item.get("_type").asText();
                CompanyEntityManager entityManager = (CompanyEntityManager) plugin.getRegisterManager().getEntityManager(schemaMap.get(type));
                entityManager.parseData(item.get("_source").get("Vrvirksomhed"), importMetadata, session);
            }
        } finally {
            transaction.rollback();
            session.close();
            QueryManager.clearCaches();
        }
    }

    @Test
    public void testParseCompanyDemoFile() throws DataFordelerException, IOException {
        ImportMetadata importMetadata = new ImportMetadata();

        try(Session session = sessionManager.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            importMetadata.setSession(session);
            InputStream input = ParseTest.class.getResourceAsStream("/GLBASETEST.json");
            JsonNode root = objectMapper.readTree(input);
            JsonNode itemList = root.get("hits").get("hits");
            Assert.assertTrue(itemList.isArray());
            for (JsonNode item : itemList) {
                String type = item.get("_type").asText();
                CompanyEntityManager entityManager = (CompanyEntityManager) plugin.getRegisterManager().getEntityManager(schemaMap.get(type));
                entityManager.parseData(item.get("_source").get("Vrvirksomhed"), importMetadata, session);
            }
            transaction.commit();
        }

        try(Session session = sessionManager.getSessionFactory().openSession()) {

            CompanyRecordQuery query = new CompanyRecordQuery();
            OffsetDateTime time = OffsetDateTime.now();
            query.setRegistrationFromBefore(time);
            query.setRegistrationToAfter(time);
            query.setEffectToAfter(time);
            query.setEffectFromBefore(time);
            query.applyFilters(session);

            List<CompanyRecord> companyList = QueryManager.getAllEntities(session, query, CompanyRecord.class);

            for(CompanyRecord company : companyList) {

                System.out.println("CVR "+company.getCvrNumber());
                System.out.println("METANAME "+company.getMetadata().getNewestName().iterator().next().getName());
                System.out.println("NAME "+company.getNames().iterator().next().getName());

                if(company.getCompanyStatus().size()>0) {
                    System.out.println(company.getCompanyStatus().iterator().next().getStatus());
                }
                if(company.getPostalAddress().size()>0) {
                    System.out.println(company.getPostalAddress().iterator().next().getMunicipality().getMunicipalityCode());
                }
                System.out.println();
            }
        }
    }


    @Test
    public void testParseUnitFile() throws IOException, DataFordelerException {
        ImportMetadata importMetadata = new ImportMetadata();
        Session session = sessionManager.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            importMetadata.setSession(session);
            InputStream input = ParseTest.class.getResourceAsStream("/unit.json");
            JsonNode root = objectMapper.readTree(input);
            JsonNode itemList = root.get("hits").get("hits");
            Assert.assertTrue(itemList.isArray());
            for (JsonNode item : itemList) {
                String type = item.get("_type").asText();
                CompanyUnitEntityManager entityManager = (CompanyUnitEntityManager) plugin.getRegisterManager().getEntityManager(schemaMap.get(type));
                entityManager.parseData(item.get("_source").get("VrproduktionsEnhed"), importMetadata, session);
            }
        } finally {
            transaction.rollback();
            session.close();
            QueryManager.clearCaches();
        }
    }

    @Test
    public void testParseParticipantFile() throws IOException, DataFordelerException {
        ImportMetadata importMetadata = new ImportMetadata();
        Session session = sessionManager.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            importMetadata.setSession(session);
            InputStream input = ParseTest.class.getResourceAsStream("/person.json");
            JsonNode root = objectMapper.readTree(input);
            JsonNode itemList = root.get("hits").get("hits");
            Assert.assertTrue(itemList.isArray());
            Assert.assertEquals(1, itemList.size());
            for (JsonNode item : itemList) {
                String type = item.get("_type").asText();
                ParticipantEntityManager entityManager = (ParticipantEntityManager) plugin.getRegisterManager().getEntityManager(schemaMap.get(type));
                entityManager.parseData(item.get("_source").get("Vrdeltagerperson"), importMetadata, session);
            }
        } finally {
            transaction.rollback();
            session.close();
            QueryManager.clearCaches();
        }
    }

}
