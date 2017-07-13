package dk.magenta.datafordeler.cvr;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.core.database.SessionManager;
import dk.magenta.datafordeler.core.exception.DataFordelerException;
import dk.magenta.datafordeler.core.exception.ParseException;
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
import java.util.List;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
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

    private CompanyOutputWrapper companyOutputWrapper = new CompanyOutputWrapper();
    private CompanyUnitOutputWrapper companyUnitOutputWrapper = new CompanyUnitOutputWrapper();
    private ParticipantOutputWrapper participantOutputWrapper = new ParticipantOutputWrapper();

    @Test
    public void testQueryCompany() throws IOException, ParseException {
        Session session = null;
        try {
            InputStream testData = QueryTest.class.getResourceAsStream("/company.json");
            JsonNode root = objectMapper.readTree(testData);
            JsonNode itemList = root.get("hits").get("hits");
            Assert.assertTrue(itemList.isArray());
            for (JsonNode item : itemList) {
                companyEntityManager.parseRegistration(item.get("_source").get("Vrvirksomhed"));
            }

            CompanyQuery query = new CompanyQuery();
            query.setEmailadresse("info@magenta.dk");
            session = sessionManager.getSessionFactory().openSession();

            try {
                List<CompanyEntity> entities = queryManager.getAllEntities(session, query, CompanyEntity.class);
                companyOutputWrapper.wrapResults(entities);
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
    public void testQueryCompanyUnit() throws IOException, ParseException {
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
            //query.setTilknyttetVirksomhedsCVRNummer(1020895337L);
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
    public void testQueryParticipant() throws IOException, ParseException {
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
