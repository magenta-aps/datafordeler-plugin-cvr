package dk.magenta.datafordeler.cvr;

import dk.magenta.datafordeler.core.Application;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.core.database.SessionManager;
import dk.magenta.datafordeler.core.exception.DataFordelerException;
import dk.magenta.datafordeler.core.io.ImportMetadata;
import dk.magenta.datafordeler.cvr.entitymanager.CvrEntityManager;
import dk.magenta.datafordeler.cvr.query.CompanyRecordQuery;
import dk.magenta.datafordeler.cvr.records.CompanyRecord;
import dk.magenta.datafordeler.cvr.records.CompanyUnitRecord;
import dk.magenta.datafordeler.cvr.records.ParticipantRecord;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * Test that it is possible to load and clear data which is dedicated for demopurpose
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class CvrLoadDemodatasetTest {

    @Autowired
    private SessionManager sessionManager;


    @Autowired
    private CvrRegisterManager registerManager;

    private CvrEntityManager entityManager;

    private static HashMap<String, String> schemaMap = new HashMap<>();
    static {
        schemaMap.put("_doc", CompanyRecord.schema);
        schemaMap.put("produktionsenhed", CompanyUnitRecord.schema);
        schemaMap.put("deltager", ParticipantRecord.schema);
    }

    /**
     * This test is parly used for the generation of information about persons in testdata
     * @throws DataFordelerException
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test
    public void test_A_LoadingOfDemoDataset() throws DataFordelerException, URISyntaxException {
        ImportMetadata importMetadata = new ImportMetadata();

        URL testData = ParseTest.class.getResource("/GLBASETEST.json");
        String testDataPath = testData.toURI().toString();
        registerManager.setCvrDemoFile(testDataPath);

        entityManager = (CvrEntityManager) this.registerManager.getEntityManagers().get(0);
        InputStream stream = this.registerManager.pullRawData(this.registerManager.getEventInterface(entityManager), entityManager, importMetadata);
        entityManager.parseData(stream, importMetadata);

        try(Session session = sessionManager.getSessionFactory().openSession()) {
            CompanyRecordQuery query = new CompanyRecordQuery();
            OffsetDateTime time = OffsetDateTime.now();
            query.setRegistrationFromBefore(time);
            query.setRegistrationToAfter(time);
            query.setEffectToAfter(time);
            query.setEffectFromBefore(time);
            query.applyFilters(session);

            List<CompanyRecord> companyList = QueryManager.getAllEntities(session, query, CompanyRecord.class);

            Assert.assertEquals(4, companyList.size());

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
    public void test_B_ReadingDemoDataset() {
        try (Session session = sessionManager.getSessionFactory().openSession()) {
            CompanyRecordQuery query = new CompanyRecordQuery();
            OffsetDateTime time = OffsetDateTime.now();
            query.setRegistrationFromBefore(time);
            query.setRegistrationToAfter(time);
            query.setEffectToAfter(time);
            query.setEffectFromBefore(time);
            query.applyFilters(session);
            List<CompanyRecord> companyList = QueryManager.getAllEntities(session, query, CompanyRecord.class);
            Assert.assertEquals(4, companyList.size());
        }
    }


    @Test
    public void test_C_ClearingDemoDataset() throws URISyntaxException {
        try (Session session = sessionManager.getSessionFactory().openSession()) {
            entityManager = (CvrEntityManager) this.registerManager.getEntityManagers().get(0);
            entityManager.setCvrDemoList("88888881,88888882,88888883,88888884");
            URL testData = ParseTest.class.getResource("/GLBASETEST.json");
            String testDataPath = testData.toURI().toString();
            registerManager.setCvrDemoFile(testDataPath);
            entityManager.cleanDemoData(session);
        }
    }


    @Test
    public void test_D_ReadingDemoDataset() {
        try (Session session = sessionManager.getSessionFactory().openSession()) {
            CompanyRecordQuery query = new CompanyRecordQuery();
            OffsetDateTime time = OffsetDateTime.now();
            query.setRegistrationFromBefore(time);
            query.setRegistrationToAfter(time);
            query.setEffectToAfter(time);
            query.setEffectFromBefore(time);
            query.applyFilters(session);
            List<CompanyRecord> companyList = QueryManager.getAllEntities(session, query, CompanyRecord.class);
            Assert.assertEquals(0, companyList.size());
        }
    }
}
