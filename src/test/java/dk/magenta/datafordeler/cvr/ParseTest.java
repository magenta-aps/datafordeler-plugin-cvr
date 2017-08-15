package dk.magenta.datafordeler.cvr;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.magenta.datafordeler.core.database.Registration;
import dk.magenta.datafordeler.core.exception.ParseException;
import dk.magenta.datafordeler.core.plugin.EntityManager;
import dk.magenta.datafordeler.cvr.data.CvrEntityManager;
import dk.magenta.datafordeler.cvr.data.company.CompanyEntity;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitEntity;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lars on 26-06-17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ParseTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CvrPlugin plugin;

    private static HashMap<String, String> schemaMap = new HashMap<>();
    static {
        schemaMap.put("virksomhed", CompanyEntity.schema);
        schemaMap.put("produktionsenhed", CompanyUnitEntity.schema);
        schemaMap.put("deltager", ParticipantEntity.schema);
    }

    @Test
    public void testParseCompanyFile() throws IOException, ParseException {
        InputStream input = ParseTest.class.getResourceAsStream("/company.json");
        JsonNode root = objectMapper.readTree(input);
        JsonNode itemList = root.get("hits").get("hits");
        Assert.assertTrue(itemList.isArray());
        for (JsonNode item : itemList) {
            String type = item.get("_type").asText();
            EntityManager entityManager = plugin.getRegisterManager().getEntityManager(schemaMap.get(type));
            List<? extends Registration> registrations = entityManager.parseRegistration(item.get("_source").get("Vrvirksomhed"));

            Collections.sort(registrations);
            Assert.assertEquals(OffsetDateTime.parse("1999-11-29T16:33:47+01:00"), registrations.get(0).getRegistrationFrom());
            Assert.assertEquals(OffsetDateTime.parse("1999-11-29T16:33:51+01:00"), registrations.get(0).getRegistrationTo());
            System.out.println(registrations.size());


            //String json1 = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(registrations);
            //System.out.println(json1);
            System.out.println("==========================================================");

            List<? extends Registration> registrations2 = entityManager.parseRegistration(item.get("_source").get("Vrvirksomhed"));
            //String json2 = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(registrations2);

            //Assert.assertEquals(json1, json2);
        }
    }

    @Test
    public void testParseUnitFile() throws IOException, ParseException {
        InputStream input = ParseTest.class.getResourceAsStream("/unit.json");
        JsonNode root = objectMapper.readTree(input);
        JsonNode itemList = root.get("hits").get("hits");
        Assert.assertTrue(itemList.isArray());
        for (JsonNode item : itemList) {
            String type = item.get("_type").asText();
            EntityManager entityManager = plugin.getRegisterManager().getEntityManager(schemaMap.get(type));
            List<? extends Registration> registrations = entityManager.parseRegistration(item.get("_source").get("VrproduktionsEnhed"));
            System.out.println(objectMapper.writeValueAsString(registrations));
        }
    }

    @Test
    public void testParseParticipantFile() throws IOException, ParseException {
        InputStream input = ParseTest.class.getResourceAsStream("/person.json");
        JsonNode root = objectMapper.readTree(input);
        JsonNode itemList = root.get("hits").get("hits");
        Assert.assertTrue(itemList.isArray());
        for (JsonNode item : itemList) {
            String type = item.get("_type").asText();
            EntityManager entityManager = plugin.getRegisterManager().getEntityManager(schemaMap.get(type));
            List<? extends Registration> registrations = entityManager.parseRegistration(item.get("_source").get("Vrdeltagerperson"));
            System.out.println(objectMapper.writeValueAsString(registrations));
        }
    }

}
