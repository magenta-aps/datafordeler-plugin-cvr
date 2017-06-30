package dk.magenta.datafordeler.cvr.data.companyunit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.core.database.Registration;
import dk.magenta.datafordeler.core.database.RegistrationReference;
import dk.magenta.datafordeler.core.database.SessionManager;
import dk.magenta.datafordeler.core.exception.DataFordelerException;
import dk.magenta.datafordeler.core.exception.ParseException;
import dk.magenta.datafordeler.core.fapi.FapiService;
import dk.magenta.datafordeler.core.plugin.EntityManager;
import dk.magenta.datafordeler.core.util.ListHashMap;
import dk.magenta.datafordeler.cvr.CvrPlugin;
import dk.magenta.datafordeler.cvr.data.CvrEntityManager;
import dk.magenta.datafordeler.cvr.data.company.CompanyEntity;
import dk.magenta.datafordeler.cvr.records.BaseRecord;
import dk.magenta.datafordeler.cvr.records.CompanyUnitRecord;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.UUID;

/**
 * Created by lars on 16-05-17.
 */
@Component
public class CompanyUnitEntityManager extends CvrEntityManager {

    @Autowired
    private CompanyUnitEntityService companyUnitEntityService;

    @Autowired
    private QueryManager queryManager;

    @Autowired
    private SessionManager sessionManager;

    public CompanyUnitEntityManager() {
        this.managedEntityClass = CompanyUnitEntity.class;
        this.managedEntityReferenceClass = CompanyUnitEntityReference.class;
        this.managedRegistrationClass = CompanyUnitRegistration.class;
        this.managedRegistrationReferenceClass = CompanyUnitRegistrationReference.class;
    }

    @Override
    protected String getBaseName() {
        return "companyunit";
    }

    @Override
    public FapiService getEntityService() {
        return this.companyUnitEntityService;
    }

    @Override
    public String getSchema() {
        return CompanyUnitEntity.schema;
    }

    @Override
    protected RegistrationReference createRegistrationReference(URI uri) {
        return new CompanyUnitRegistrationReference(uri);
    }


    @Override
    public List<? extends Registration> parseRegistration(JsonNode jsonNode) throws ParseException {
        ArrayList<Registration> registrations = new ArrayList<>();

        if (jsonNode.has("hits")) {
            jsonNode = jsonNode.get("hits");
            if (jsonNode.has("hits")) {
                jsonNode = jsonNode.get("hits");
            }
            if (jsonNode.isArray()) {
                // We have a list of results
                for (JsonNode item : jsonNode) {
                    registrations.addAll(this.parseRegistration(item));
                }
                return registrations;
            }
        }

        String type = jsonNode.has("_type") ? jsonNode.get("_type").asText() : null;
        if (type != null && !type.equals(CompanyUnitEntity.schema)) {
            // Wrong type. See if we have another EntityManager that can handle it
            EntityManager otherManager = this.getRegisterManager().getEntityManager(type);
            if (otherManager != null) {
                return otherManager.parseRegistration(jsonNode);
            }
            return null;
        }

        if (jsonNode.has("_source")) {
            jsonNode = jsonNode.get("_source");
        }
        if (jsonNode.has("VrproduktionsEnhed")) {
            jsonNode = jsonNode.get("VrproduktionsEnhed");
        }
        Session session = sessionManager.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        CompanyUnitRecord companyUnitRecord;
        try {
            companyUnitRecord = getObjectMapper().treeToValue(jsonNode, CompanyUnitRecord.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
        CompanyUnitEntity companyUnit = new CompanyUnitEntity(UUID.randomUUID(), CvrPlugin.getDomain());
        companyUnit.setPNumber(companyUnitRecord.getpNumber());

        List<BaseRecord> records = companyUnitRecord.getAll();

        ListHashMap<OffsetDateTime, BaseRecord> ajourRecords = new ListHashMap<>();
        TreeSet<OffsetDateTime> sortedTimestamps = new TreeSet<>();
        for (BaseRecord record : records) {
            OffsetDateTime registrationFrom = record.getLastUpdated();
            ajourRecords.add(registrationFrom, record);
            sortedTimestamps.add(registrationFrom);
        }

        CompanyUnitRegistration lastRegistration = null;
        for (OffsetDateTime registrationFrom : sortedTimestamps) {

            // Get any existing registration that matches this date, or create a new one
            CompanyUnitRegistration registration = companyUnit.getRegistration(registrationFrom);
            if (registration == null) {
                registration = new CompanyUnitRegistration();
                registration.setRegistrationFrom(registrationFrom);
                registration.setEntity(companyUnit);
            }

            // Copy data over from the previous registration, by cloning all effects and point underlying dataitems to the clones as well as the originals
            if (lastRegistration != null) {
                for (CompanyUnitEffect originalEffect : lastRegistration.getEffects()) {
                    CompanyUnitEffect newEffect = new CompanyUnitEffect(registration, originalEffect.getEffectFrom(), originalEffect.getEffectTo());
                    for (CompanyUnitBaseData originalData : originalEffect.getDataItems()) {
                        originalData.addEffect(newEffect);
                    }
                }
            }

            for (BaseRecord record : ajourRecords.get(registrationFrom)) {
                CompanyUnitEffect effect = registration.getEffect(record.getValidFrom(), record.getValidTo());
                if (effect == null) {
                    effect = new CompanyUnitEffect(registration, record.getValidFrom(), record.getValidTo());
                }

                if (effect.getDataItems().isEmpty()) {
                    CompanyUnitBaseData baseData = new CompanyUnitBaseData();
                    baseData.addEffect(effect);
                }
                for (CompanyUnitBaseData baseData : effect.getDataItems()) {
                    // There really should be only one item for each effect right now
                    record.populateBaseData(baseData, this.queryManager, session);
                }
            }
            lastRegistration = registration;
            registrations.add(registration);

            try {
                queryManager.saveRegistration(session, companyUnit, registration);
            } catch (DataFordelerException e) {
                e.printStackTrace();
            }
        }
        System.out.println("created " + companyUnit.getRegistrations().size() + " registrations");
        transaction.commit();
        session.close();
        return registrations;
    }

}
