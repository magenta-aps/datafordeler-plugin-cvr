package dk.magenta.datafordeler.cvr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.magenta.datafordeler.core.database.Identification;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.core.database.SessionManager;
import dk.magenta.datafordeler.core.exception.DataFordelerException;
import dk.magenta.datafordeler.cvr.data.company.*;
import dk.magenta.datafordeler.cvr.data.company.CompanyMainData;
import dk.magenta.datafordeler.cvr.data.company.CompanyTextData;
import dk.magenta.datafordeler.cvr.data.companyunit.*;
import dk.magenta.datafordeler.cvr.data.embeddable.LifeCycleEmbed;
import dk.magenta.datafordeler.cvr.data.embeddable.QuarterlyEmployeeNumbersEmbed;
import dk.magenta.datafordeler.cvr.data.embeddable.YearlyEmployeeNumbersEmbed;
import dk.magenta.datafordeler.cvr.data.unversioned.CompanyForm;
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
        company.setCvrNumber(12345678);

        CompanyRegistration registration = new CompanyRegistration(OffsetDateTime.parse("2017-01-01T00:00:00+00:00"), OffsetDateTime.parse("2018-01-01T00:00:00+00:00"), 1);
        CompanyEffect effect = new CompanyEffect(registration, OffsetDateTime.parse("2017-07-01T00:00:00+00:00"), null);

        CompanyBaseData companyData = new CompanyBaseData();
        companyData.addEffect(effect);

        CompanyMainData mainData = companyData.getMainData();

        LifeCycleEmbed lifeCycle = mainData.obtainLifeCycle();
        lifeCycle.setStartDate(OffsetDateTime.parse("2000-01-01T00:00:00+00:00"));

        mainData.setAdvertProtection(true);

        CompanyTextData nameData = companyData.getNameData();
        nameData.setText("Some company name");
        nameData.setType(CompanyTextData.Type.NAME);

        CompanyTextData phoneData = companyData.getPhoneData();
        phoneData.setType(CompanyTextData.Type.PHONE);
        phoneData.setText("87654321");

        CompanyTextData faxData = companyData.getFaxData();
        faxData.setType(CompanyTextData.Type.FAX);
        faxData.setText("11112222");

        CompanyTextData emailData = companyData.getEmailData();
        emailData.setType(CompanyTextData.Type.EMAIL);
        emailData.setText("test@example.com");

        CompanyForm companyForm = new CompanyForm();
        companyForm.setCode(111);
        companyForm.setName("A/S");
        companyForm.setResponsibleDatasource("E&S");
        mainData.setForm(companyForm);

        YearlyEmployeeNumbersEmbed yearlyEmployeeNumbers = mainData.obtainYearlyEmployeeNumbers();
        yearlyEmployeeNumbers.setYear(2017);
        yearlyEmployeeNumbers.setYearlyEmployeesLow(1);
        yearlyEmployeeNumbers.setYearlyEmployeesHigh(2);
        yearlyEmployeeNumbers.setYearlyFullTimeEquivalentLow(1);
        yearlyEmployeeNumbers.setYearlyFullTimeEquivalentHigh(2);
        yearlyEmployeeNumbers.setYearlyIncludingOwnersLow(1);
        yearlyEmployeeNumbers.setYearlyIncludingOwnersHigh(2);


        QuarterlyEmployeeNumbersEmbed quarterlyEmployeeNumbers = mainData.obtainQuarterlyEmployeeNumbers();
        quarterlyEmployeeNumbers.setYear(2017);
        quarterlyEmployeeNumbers.setQuarter(2);
        quarterlyEmployeeNumbers.setQuarterlyEmployeesLow(1);
        quarterlyEmployeeNumbers.setQuarterlyEmployeesHigh(2);
        quarterlyEmployeeNumbers.setQuarterlyFullTimeEquivalentLow(1);
        quarterlyEmployeeNumbers.setQuarterlyFullTimeEquivalentHigh(2);
        quarterlyEmployeeNumbers.setQuarterlyIncludingOwnersLow(1);
        quarterlyEmployeeNumbers.setQuarterlyIncludingOwnersHigh(2);


        Session session = sessionManager.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        session.saveOrUpdate(companyForm);

        try {
            queryManager.saveRegistration(session, company, registration);
            transaction.commit();
        } catch (DataFordelerException e) {
            transaction.rollback();
        } finally {
            session.close();
        }

        System.out.println("--------------------------------------");
        System.out.println(objectMapper.writeValueAsString(company));
        System.out.println("--------------------------------------");
    }

    @Test
    public void testCompanyUnit() throws DataFordelerException, JsonProcessingException {
        Identification companyIdentification = new Identification(UUID.randomUUID(), "test");
        CompanyEntity company = new CompanyEntity();
        company.setIdentification(companyIdentification);
        company.setCvrNumber(12345678);

        Identification companyUnitIdentification = new Identification(UUID.randomUUID(), "test");
        CompanyUnitEntity companyUnit = new CompanyUnitEntity();
        companyUnit.setIdentification(companyUnitIdentification);
        companyUnit.setPNumber(11223344);

        CompanyUnitRegistration registration = new CompanyUnitRegistration(OffsetDateTime.parse("2017-01-01T00:00:00+00:00"), OffsetDateTime.parse("2018-01-01T00:00:00+00:00"), 1);
        CompanyUnitEffect effect = new CompanyUnitEffect(registration, OffsetDateTime.parse("2017-07-01T00:00:00+00:00"), null);

        CompanyUnitBaseData companyUnitData = new CompanyUnitBaseData();
        companyUnitData.addEffect(effect);


        CompanyUnitMainData mainData = companyUnitData.getMainData();
        LifeCycleEmbed lifeCycle = new LifeCycleEmbed();
        lifeCycle.setStartDate(OffsetDateTime.parse("2000-01-01T00:00:00+00:00"));
        mainData.setLifeCycle(lifeCycle);

        CompanyUnitTextData nameData = companyUnitData.getNameData();
        nameData.setText("Some company name");
        nameData.setType(CompanyUnitTextData.Type.NAME);

        CompanyUnitTextData phoneData = companyUnitData.getPhoneData();
        phoneData.setType(CompanyUnitTextData.Type.PHONE);
        phoneData.setText("87654321");

        CompanyUnitTextData faxData = companyUnitData.getFaxData();
        faxData.setType(CompanyUnitTextData.Type.FAX);
        faxData.setText("11112222");

        CompanyUnitTextData emailData = companyUnitData.getEmailData();
        emailData.setType(CompanyUnitTextData.Type.EMAIL);
        emailData.setText("test@example.com");

        CompanyForm companyForm = new CompanyForm();
        companyForm.setCode(123);
        companyForm.setName("Ltd.");
        companyForm.setResponsibleDatasource("E&S");
        mainData.setForm(companyForm);

        YearlyEmployeeNumbersEmbed yearlyEmployeeNumbers = mainData.obtainYearlyEmployeeNumbers();
        yearlyEmployeeNumbers.setYear(2017);
        yearlyEmployeeNumbers.setYearlyEmployeesLow(1);
        yearlyEmployeeNumbers.setYearlyEmployeesHigh(2);
        yearlyEmployeeNumbers.setYearlyFullTimeEquivalentLow(1);
        yearlyEmployeeNumbers.setYearlyFullTimeEquivalentHigh(2);
        yearlyEmployeeNumbers.setYearlyIncludingOwnersLow(1);
        yearlyEmployeeNumbers.setYearlyIncludingOwnersHigh(2);

        QuarterlyEmployeeNumbersEmbed quarterlyEmployeeNumbers = mainData.obtainQuarterlyEmployeeNumbers();
        quarterlyEmployeeNumbers.setYear(2017);
        quarterlyEmployeeNumbers.setQuarter(2);
        quarterlyEmployeeNumbers.setQuarterlyEmployeesLow(1);
        quarterlyEmployeeNumbers.setQuarterlyEmployeesHigh(2);
        quarterlyEmployeeNumbers.setQuarterlyFullTimeEquivalentLow(1);
        quarterlyEmployeeNumbers.setQuarterlyFullTimeEquivalentHigh(2);
        quarterlyEmployeeNumbers.setQuarterlyIncludingOwnersLow(1);
        quarterlyEmployeeNumbers.setQuarterlyIncludingOwnersHigh(2);


        Session session = sessionManager.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        session.saveOrUpdate(companyForm);
        session.saveOrUpdate(company);

        try {
            queryManager.saveRegistration(session, companyUnit, registration);
            transaction.commit();
        } catch (DataFordelerException e) {
            transaction.rollback();
        } finally {
            session.close();
        }

        System.out.println("--------------------------------------");
        System.out.println(objectMapper.writeValueAsString(companyUnit));
        System.out.println("--------------------------------------");
    }
}
