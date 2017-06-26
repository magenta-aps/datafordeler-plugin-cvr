package dk.magenta.datafordeler.cvr.data.company;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import dk.magenta.datafordeler.core.database.*;
import dk.magenta.datafordeler.core.exception.ParseException;
import dk.magenta.datafordeler.core.fapi.FapiService;
import dk.magenta.datafordeler.core.util.ListHashMap;
import dk.magenta.datafordeler.cvr.data.CvrEntityManager;
import dk.magenta.datafordeler.cvr.records.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.*;
import java.util.*;

/**
 * Created by lars on 16-05-17.
 */
@Component
public class CompanyEntityManager extends CvrEntityManager {

    @Autowired
    private CompanyEntityService companyEntityService;

    @Autowired
    private QueryManager queryManager;

    @Autowired
    private SessionManager sessionManager;

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
    public List<Registration> parseRegistration(JsonNode jsonNode) throws ParseException {
        Session session = sessionManager.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        System.out.println("Parse jsonNode");
        jsonNode = this.unwrap(jsonNode);

        HashMap<OffsetDateTime, CompanyRegistration> registrations = new HashMap<>();

        CompanyEntity company = new CompanyEntity(UUID.randomUUID(), "cvr", jsonNode.get("cvrNummer").asInt());
        CompanyRecord companyRecord;
        try {
            companyRecord = getObjectMapper().treeToValue(jsonNode, CompanyRecord.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
        List<CompanyBaseRecord> records = companyRecord.getAll();




        ListHashMap<OffsetDateTime, CompanyBaseRecord> ajourRecords = new ListHashMap<>();
        TreeSet<OffsetDateTime> sortedTimestamps = new TreeSet<>();
        for (CompanyBaseRecord record : records) {
            System.out.println("record: "+record);
            OffsetDateTime registrationFrom = record.getLastUpdated();
            ajourRecords.add(registrationFrom, record);
            sortedTimestamps.add(registrationFrom);
        }

        CompanyRegistration lastRegistration = null;
        for (OffsetDateTime registrationFrom : sortedTimestamps) {

            // Get any existing registration that matches this date, or create a new one
            CompanyRegistration registration = company.getRegistration(registrationFrom);
            if (registration == null) {
                registration = new CompanyRegistration();
                registration.setRegistrationFrom(registrationFrom);
                registration.setEntity(company);
            }

            // Copy data over from the previous registration, by cloning all effects and point underlying dataitems to the clones as well as the originals
            if (lastRegistration != null) {
                for (CompanyEffect originalEffect : lastRegistration.getEffects()) {
                    CompanyEffect newEffect = new CompanyEffect(registration, originalEffect.getEffectFrom(), originalEffect.getEffectTo());
                    for (CompanyBaseData originalData : originalEffect.getDataItems()) {
                        originalData.addEffect(newEffect);
                    }
                }
            }

            for (CompanyBaseRecord record : ajourRecords.get(registrationFrom)) {
                //CompanyRegistration registration = registrations.computeIfAbsent(record.getLastUpdated(), k -> new CompanyRegistration());

                CompanyEffect effect = registration.getEffect(record.getValidFrom(), record.getValidTo());
                if (effect == null) {
                    //System.out.println("did not find effect on "+record.getValidFrom()+", "+record.getValidTo()+", creating new");
                    effect = new CompanyEffect(registration, record.getValidFrom(), record.getValidTo());
                } else {
                    //System.out.println("found effect on "+record.getValidFrom()+", "+record.getValidTo()+", reusing");
                }

                //System.out.println("DATAITEMS COUNT: "+effect.getDataItems().size());

                //CompanyBaseData baseData = new CompanyBaseData();
                if (effect.getDataItems().isEmpty()) {
                    CompanyBaseData baseData = new CompanyBaseData();
                    baseData.addEffect(effect);
                }
                for (CompanyBaseData baseData : effect.getDataItems()) {
                    // There really should be only one item for each effect right now
                    record.populateCompanyBaseData(baseData, this.queryManager, session);
                }
            }
            lastRegistration = registration;

            try {
                System.out.println(getObjectMapper().writeValueAsString(registration));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            System.out.println("==========================================================");
        }
        //System.out.println(company.getRegistrations());
        transaction.commit();
        session.close();
        return null;
    }


    private JsonNode unwrap(JsonNode jsonNode) {
        if (jsonNode.has("_source")) {
            jsonNode = jsonNode.get("_source");
        }
        if (jsonNode.has("Vrvirksomhed")) {
            jsonNode = jsonNode.get("Vrvirksomhed");
        }
        return jsonNode;
    }


}
