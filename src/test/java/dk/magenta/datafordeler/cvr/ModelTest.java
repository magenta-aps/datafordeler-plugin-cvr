package dk.magenta.datafordeler.cvr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.magenta.datafordeler.core.database.Identification;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.core.database.SessionManager;
import dk.magenta.datafordeler.core.exception.DataFordelerException;
import dk.magenta.datafordeler.cvr.data.company.CompanyEffect;
import dk.magenta.datafordeler.cvr.data.company.CompanyEntity;
import dk.magenta.datafordeler.cvr.data.company.CompanyMainData;
import dk.magenta.datafordeler.cvr.data.company.CompanyRegistration;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.OffsetDateTime;
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
        Identification identification = new Identification(UUID.randomUUID(), "test");
        CompanyEntity company = new CompanyEntity();
        company.setIdentification(identification);

        CompanyRegistration registration = new CompanyRegistration(OffsetDateTime.parse("2017-01-01T00:00:00+00:00"), OffsetDateTime.parse("2018-01-01T00:00:00+00:00"), 1);
        CompanyEffect effect = new CompanyEffect(registration, OffsetDateTime.parse("2017-07-01T00:00:00+00:00"), null);

        CompanyMainData mainData = new CompanyMainData();
        mainData.setCvrNumber("12345678");
        mainData.addEffect(effect);

        Session session = sessionManager.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            queryManager.saveRegistration(session, company, registration);
            transaction.commit();
        } catch (DataFordelerException e) {
            transaction.rollback();
        } finally {
            session.close();
        }


        System.out.println(objectMapper.writeValueAsString(company));
    }
}
