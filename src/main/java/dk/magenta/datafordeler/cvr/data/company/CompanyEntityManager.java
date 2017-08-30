package dk.magenta.datafordeler.cvr.data.company;

import dk.magenta.datafordeler.core.database.*;
import dk.magenta.datafordeler.core.fapi.FapiService;
import dk.magenta.datafordeler.cvr.data.CvrEntityManager;
import dk.magenta.datafordeler.cvr.records.CompanyRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.*;

/**
 * Created by lars on 16-05-17.
 */
@Component
public class CompanyEntityManager extends CvrEntityManager<CompanyEntity, CompanyRegistration, CompanyEffect, CompanyBaseData, CompanyRecord> {

    @Autowired
    private CompanyEntityService companyEntityService;

    @Autowired
    private QueryManager queryManager;

    @Autowired
    private SessionManager sessionManager;

    private Logger log = LogManager.getLogger(CompanyEntityManager.class);

    public CompanyEntityManager() {
        this.managedEntityClass = CompanyEntity.class;
        this.managedEntityReferenceClass = CompanyEntityReference.class;
        this.managedRegistrationClass = CompanyRegistration.class;
        this.managedRegistrationReferenceClass = CompanyRegistrationReference.class;
    }

    @Override
    protected String getBaseName() {
        return "company";
    }

    @Override
    public FapiService getEntityService() {
        return this.companyEntityService;
    }

    @Override
    public String getSchema() {
        return CompanyEntity.schema;
    }

    @Override
    protected RegistrationReference createRegistrationReference(URI uri) {
        return new CompanyRegistrationReference(uri);
    }

    @Override
    protected SessionManager getSessionManager() {
        return this.sessionManager;
    }

    @Override
    protected QueryManager getQueryManager() {
        return this.queryManager;
    }

    @Override
    protected String getJsonTypeName() {
        return "Vrvirksomhed";
    }

    @Override
    protected Class getRecordClass() {
        return CompanyRecord.class;
    }

    @Override
    protected Class getEntityClass() {
        return CompanyEntity.class;
    }

    @Override
    protected UUID generateUUID(CompanyRecord record) {
        return CompanyEntity.generateUUID(record.getCvrNumber());
    }

    @Override
    protected CompanyEntity createBasicEntity(CompanyRecord record) {
        CompanyEntity entity = new CompanyEntity();
        entity.setCvrNumber(record.getCvrNumber());
        return entity;
    }

    @Override
    protected CompanyBaseData createDataItem() {
        return new CompanyBaseData();
    }

}
