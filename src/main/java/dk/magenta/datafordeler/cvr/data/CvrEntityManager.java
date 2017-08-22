package dk.magenta.datafordeler.cvr.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.magenta.datafordeler.core.database.*;
import dk.magenta.datafordeler.core.exception.DataFordelerException;
import dk.magenta.datafordeler.core.exception.ParseException;
import dk.magenta.datafordeler.core.exception.WrongSubclassException;
import dk.magenta.datafordeler.core.io.Receipt;
import dk.magenta.datafordeler.core.plugin.*;
import dk.magenta.datafordeler.core.util.ItemInputStream;
import dk.magenta.datafordeler.core.util.ListHashMap;
import dk.magenta.datafordeler.cvr.CvrPlugin;
import dk.magenta.datafordeler.cvr.records.BaseRecord;
import dk.magenta.datafordeler.cvr.records.CvrRecord;
import dk.magenta.datafordeler.cvr.records.EntityRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;

/**
 * Created by lars on 29-05-17.
 */
@Component
public abstract class CvrEntityManager<T extends EntityRecord, E extends Entity<E, R>, R extends Registration<E, R, V>, V extends Effect, D extends DataItem<V, D>> extends EntityManager {

    @Autowired
    private ObjectMapper objectMapper;

    private ScanScrollCommunicator commonFetcher;

    protected Logger log = LogManager.getLogger(this.getClass().getSimpleName());

    private Collection<String> handledURISubstrings;

    protected abstract String getBaseName();

    protected OffsetDateTime fallbackRegistrationFrom = OffsetDateTime.MIN;

    public CvrEntityManager() {
        this.commonFetcher = new ScanScrollCommunicator("username", "password");
        this.handledURISubstrings = new ArrayList<>();
    }

    @Override
    public void setRegisterManager(RegisterManager registerManager) {
        super.setRegisterManager(registerManager);
        this.handledURISubstrings.add(expandBaseURI(this.getBaseEndpoint(), "/" + this.getBaseName(), null, null).toString());
        this.handledURISubstrings.add(expandBaseURI(this.getBaseEndpoint(), "/get/" + this.getBaseName(), null, null).toString());
    }

    @Override
    public Collection<String> getHandledURISubstrings() {
        return this.handledURISubstrings;
    }

    @Override
    protected ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }

    @Override
    protected Communicator getRegistrationFetcher() {
        return this.commonFetcher;
    }

    @Override
    protected Communicator getReceiptSender() {
        return this.commonFetcher;
    }

    @Override
    public URI getBaseEndpoint() {
        return this.getRegisterManager().getBaseEndpoint();
    }

    @Override
    protected URI getReceiptEndpoint(Receipt receipt) {
        return null;
    }

    @Override
    public RegistrationReference parseReference(InputStream referenceData) throws IOException {
        return this.getObjectMapper().readValue(referenceData, this.managedRegistrationReferenceClass);
    }

    @Override
    public RegistrationReference parseReference(String referenceData, String charsetName) throws IOException {
        return this.getObjectMapper().readValue(referenceData.getBytes(charsetName), this.managedRegistrationReferenceClass);
    }

    protected abstract RegistrationReference createRegistrationReference(URI uri);

    @Override
    public RegistrationReference parseReference(URI uri) {
        return this.createRegistrationReference(uri);
    }

    @Override
    public URI getRegistrationInterface(RegistrationReference reference) throws WrongSubclassException {
        if (!this.managedRegistrationReferenceClass.isInstance(reference)) {
            throw new WrongSubclassException(this.managedRegistrationReferenceClass, reference);
        }
        if (reference.getURI() != null) {
            return reference.getURI();
        }
        return EntityManager.expandBaseURI(this.getBaseEndpoint(), "/get/"+this.getBaseName()+"/"+reference.getChecksum());
    }

    @Override
    protected URI getListChecksumInterface(OffsetDateTime fromDate) {
        return this.getRegisterManager().getListChecksumInterface(this.getSchema(), fromDate);
    }

    @Override
    protected ItemInputStream<? extends EntityReference> parseChecksumResponse(InputStream responseContent) throws DataFordelerException {
        return ItemInputStream.parseJsonStream(responseContent, this.managedEntityReferenceClass, "items", this.getObjectMapper());
    }

    @Override
    protected Logger getLog() {
        return this.log;
    }

    @Override
    public List<? extends Registration> parseRegistration(InputStream registrationData) throws ParseException, IOException {
        String dataChunk = new Scanner(registrationData, "UTF-8").useDelimiter(new String(this.commonFetcher.getDelimiter())).next();
        return this.parseRegistration(this.getObjectMapper().readTree(dataChunk));
    }

    protected <R extends Registration, E extends Entity<E, R>> Collection<R> buildRegistrations(E entity, List<BaseRecord> records) {
        // Create a list of registrations, sorted by date and made so that each registration ends when the next begins
        HashMap<OffsetDateTime, R> registrationMap = new HashMap<>();
        for (R registration : entity.getRegistrations()) {
            registrationMap.put(registration.getRegistrationFrom(), registration);
        }
        for (BaseRecord record : records) {
            //System.out.println(record.getClass().getSimpleName()+": "+record.getLastUpdated());
            OffsetDateTime registrationStart = record.getRegistrationFrom();
            if (!registrationMap.containsKey(registrationStart)) {
                log.debug("Create new registration "+registrationStart);
                R registration = entity.createRegistration();
                registration.setRegistrationFrom(registrationStart);
                registrationMap.put(registrationStart, registration);
            }
        }

        HashSet<OffsetDateTime> startTimeSet = new HashSet<>(registrationMap.keySet());
        boolean hadNull = startTimeSet.remove(null);
        ArrayList<OffsetDateTime> startTimes = new ArrayList<>(startTimeSet);
        Collections.sort(startTimes);
        R last = null;
        if (hadNull) {
            startTimes.add(0, null);
        }
        for (OffsetDateTime startTime : startTimes) {
            R registration = registrationMap.get(startTime);
            if (last != null) {
                if (last.getRegistrationTo() == null) {
                    last.setRegistrationTo(startTime);
                    registration.setSequenceNumber(last.getSequenceNumber() + 1);
                } else if (!last.getRegistrationTo().isEqual(startTime)) {
                    log.error("Registration time mismatch: "+last.getRegistrationTo()+" != "+startTime);
                }
            }
            last = registration;
        }
        return registrationMap.values();
    }

    protected abstract SessionManager getSessionManager();
    protected abstract QueryManager getQueryManager();
    protected abstract String getJsonTypeName(); // VrproduktionsEnhed
    protected abstract Class<T> getRecordClass();
    protected abstract Class<E> getEntityClass();
    protected abstract UUID generateUUID(T record);
    protected abstract E createBasicEntity(T record);
    protected abstract D createDataItem();


    /**
     * Parse an incoming JsonNode into registrations (and save them)
     * Must be idempotent
     * @param jsonNode
     * @return
     * @throws ParseException
     */
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
        if (type != null && !type.equals(this.getSchema())) {
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
        String jsonTypeName = this.getJsonTypeName();
        if (jsonNode.has(jsonTypeName)) {
            jsonNode = jsonNode.get(jsonTypeName);
        }

        Session session = this.getSessionManager().getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        T toplevelRecord;

        try {
            toplevelRecord = getObjectMapper().treeToValue(jsonNode, this.getRecordClass());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

        UUID uuid = this.generateUUID(toplevelRecord);
        E entity = this.getQueryManager().getEntity(session, uuid, this.getEntityClass());
        if (entity == null) {
            log.debug("Creating new Entity");
            entity = this.createBasicEntity(toplevelRecord);
            entity.setDomain(CvrPlugin.getDomain());
            entity.setUUID(uuid);
        } else {
            log.debug("Using existing entity");
        }

        List<BaseRecord> records = toplevelRecord.getAll();

        Collection<R> entityRegistrations = this.buildRegistrations(entity, records);


        // Sort the records into groups that share bitemporality
        ListHashMap<String, BaseRecord> recordGroups = CvrRecord.sortIntoGroups(records);

        // Loop over the groups, creating/updating one CompanyBaseData item for each group
        for (List<BaseRecord> group : recordGroups.values()) {
            for (BaseRecord baseRecord : group) {
                log.debug("Handling record "+baseRecord.getClass().getSimpleName()+" with bitemporality "+
                        baseRecord.getRegistrationFrom()+"|"+
                        baseRecord.getRegistrationTo()+"|"+
                        baseRecord.getValidFrom()+"|"+
                        baseRecord.getValidTo()
                );
            }
            BaseRecord firstRecord = group.get(0);
            OffsetDateTime registrationFrom = firstRecord.getRegistrationFrom();
            OffsetDateTime registrationTo = firstRecord.getRegistrationTo();
            LocalDate effectFrom = firstRecord.getValidFrom();
            LocalDate effectTo = firstRecord.getValidTo();

            // Find the appropriate registration objects
            ArrayList<R> applicableRegistrations = new ArrayList<>();
            for (R r : entityRegistrations) {
                // Eliglible when our record registrationFrom is before or equal to the tested registration, AND our record registrationTo is after or equal to the tested registration
                if (
                        (registrationFrom == null || (r.getRegistrationFrom() != null && registrationFrom.compareTo(r.getRegistrationFrom()) < 1)) &&
                        (registrationTo == null || (r.getRegistrationTo() != null && (registrationTo.compareTo(r.getRegistrationTo())) >= 0))
                        ) {
                    applicableRegistrations.add(r);
                }
            }
            ArrayList<V> effects = new ArrayList<>();

            // Find the appropriate effect objects
            for (R registration : applicableRegistrations) {
                V effect = registration.getEffect(effectFrom, effectTo);
                if (effect == null) {
                    log.debug("Create new effect "+effectFrom+"|"+effectTo);
                    effect = registration.createEffect(effectFrom, effectTo);
                    session.saveOrUpdate(registration);
                    session.saveOrUpdate(effect);
                }
                effects.add(effect);
            }

            // R-V-D scenario
            // Every DataItem that we locate for population must match the given effects exactly,
            // or we risk assigning data to an item that shouldn't be assigned to
            D baseData = null;
            HashSet<D> searchPool = new HashSet<>();
            for (V effect : effects) {
                searchPool.addAll(effect.getDataItems());
            }
            for (D data : searchPool) {
                if (data.getEffects().containsAll(effects) && effects.containsAll(data.getEffects())) {
                    baseData = data;
                    log.debug("Reuse existing basedata");
                }
            }
            if (baseData == null) {
                log.debug("Creating new basedata");
                baseData = this.createDataItem();
                for (V effect : effects) {
                    log.debug("Wire basedata to effect "+effect.getRegistration().getRegistrationFrom()+"|"+effect.getRegistration().getRegistrationTo()+"|"+effect.getEffectFrom()+"|"+effect.getEffectTo());
                    baseData.addEffect(effect);
                }
            }
            for (BaseRecord record : group) {
                record.populateBaseData(baseData, this.getQueryManager(), session);
            }

        }
        registrations.addAll(entityRegistrations);

        log.info("Entity "+entity.getUUID()+" now has "+entity.getRegistrations().size()+" registrations");
        transaction.commit();
        session.close();
        return registrations;
    }

}
