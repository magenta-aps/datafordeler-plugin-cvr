package dk.magenta.datafordeler.cvr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dk.magenta.datafordeler.core.Application;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.core.database.Registration;
import dk.magenta.datafordeler.core.database.SessionManager;
import dk.magenta.datafordeler.core.exception.DataFordelerException;
import dk.magenta.datafordeler.core.io.ImportMetadata;
import dk.magenta.datafordeler.cvr.data.company.CompanyEntity;
import dk.magenta.datafordeler.cvr.data.company.CompanyEntityManager;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitEntity;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantEntity;
import dk.magenta.datafordeler.cvr.records.CompanyRecord;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
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

    @Test
    public void testCompany() throws DataFordelerException, IOException {
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
            transaction.commit();


            HashMap<String, Object> filter = new HashMap<>();
            filter.put("cvrNumber", 25052943);
            CompanyRecord companyRecord = QueryManager.getItem(session, CompanyRecord.class, filter);
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(companyRecord));



        } finally {
            session.close();
            QueryManager.clearCaches();
        }
    }

    /*private void compareJson(JsonNode n1, JsonNode n2, JsonNode parent) throws JsonProcessingException {
        if (n1.isObject() && n2.isObject()) {
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
                    System.out.println("Mismatch: missing field "+field+" in "+);
                }
            }

        } else if (n1.isArray() && n2.isArray()) {

        } else {
            System.out.println("Mismatch: "+n1.asText()+" != "+n2.asText());
            System.out.println("Parent: "+objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(parent));
        }
    }*/
}
