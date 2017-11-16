package dk.magenta.datafordeler.cvr.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.magenta.datafordeler.core.database.*;
import dk.magenta.datafordeler.core.exception.DataFordelerException;
import dk.magenta.datafordeler.core.exception.DataStreamException;
import dk.magenta.datafordeler.core.exception.ParseException;
import dk.magenta.datafordeler.core.exception.WrongSubclassException;
import dk.magenta.datafordeler.core.io.ImportMetadata;
import dk.magenta.datafordeler.core.io.Receipt;
import dk.magenta.datafordeler.core.plugin.Communicator;
import dk.magenta.datafordeler.core.plugin.EntityManager;
import dk.magenta.datafordeler.core.plugin.RegisterManager;
import dk.magenta.datafordeler.core.plugin.ScanScrollCommunicator;
import dk.magenta.datafordeler.core.util.ItemInputStream;
import dk.magenta.datafordeler.core.util.ListHashMap;
import dk.magenta.datafordeler.core.util.Stopwatch;
import dk.magenta.datafordeler.cvr.CvrPlugin;
import dk.magenta.datafordeler.cvr.data.unversioned.Industry;
import dk.magenta.datafordeler.cvr.records.CvrBaseRecord;
import dk.magenta.datafordeler.cvr.records.CvrEntityRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.internal.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.*;

/**
 * Created by lars on 29-05-17.
 */
@Component
public abstract class CvrEntityManager<E extends CvrEntity<E, R>, R extends CvrRegistration<E, R, V>, V extends CvrEffect, D extends CvrData<V, D>, T extends CvrEntityRecord>
        extends EntityManager {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Stopwatch timer;

    private ScanScrollCommunicator commonFetcher;

    protected Logger log = LogManager.getLogger(this.getClass().getSimpleName());

    private Collection<String> handledURISubstrings;

    protected abstract String getBaseName();

    public CvrEntityManager() {
        this.commonFetcher = new ScanScrollCommunicator();
        this.handledURISubstrings = new ArrayList<>();
    }

    /**
     * Set the associated RegisterManager (called as part of Plugin initialization), setting up
     * the service paths listened on in the process
     */
    @Override
    public void setRegisterManager(RegisterManager registerManager) {
        super.setRegisterManager(registerManager);
        //this.handledURISubstrings.add(expandBaseURI(this.getBaseEndpoint(), "/" + this.getBaseName(), null, null).toString());
        //this.handledURISubstrings.add(expandBaseURI(this.getBaseEndpoint(), "/get/" + this.getBaseName(), null, null).toString());
    }

    /**
     * Return the URI substrings that are listened on in the service
     */
    @Override
    public Collection<String> getHandledURISubstrings() {
        return this.handledURISubstrings;
    }

    @Override
    protected ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }

    @Override
    protected ScanScrollCommunicator getRegistrationFetcher() {
        return this.commonFetcher;
    }

    @Override
    protected Communicator getReceiptSender() {
        return this.commonFetcher;
    }

    /**
     * Return the base endpoint, which points to the external source
     */
    @Override
    public URI getBaseEndpoint() {
        return this.getRegisterManager().getBaseEndpoint();
    }

    @Override
    protected URI getReceiptEndpoint(Receipt receipt) {
        return null;
    }

    /**
     * Parse a raw reference data stream into a RegistrationReference object
     */
    @Override
    public RegistrationReference parseReference(InputStream referenceData) throws IOException {
        return this.getObjectMapper().readValue(referenceData, this.managedRegistrationReferenceClass);
    }

    /**
     * Parse a raw reference data string into a RegistrationReference object
     */
    @Override
    public RegistrationReference parseReference(String referenceData, String charsetName) throws IOException {
        return this.getObjectMapper().readValue(referenceData.getBytes(charsetName), this.managedRegistrationReferenceClass);
    }

    protected abstract RegistrationReference createRegistrationReference(URI uri);

    /**
     * Create a RegistrationReference object that encapsulates the given URI in the proper object
     */
    @Override
    public RegistrationReference parseReference(URI uri) {
        return this.createRegistrationReference(uri);
    }

    /**
     * Returns the reference URI based on a RegistrationReference, basically unpacking it
     */
    @Override
    public URI getRegistrationInterface(RegistrationReference reference) throws WrongSubclassException {
        if (!this.managedRegistrationReferenceClass.isInstance(reference)) {
            throw new WrongSubclassException(this.managedRegistrationReferenceClass, reference);
        }
        if (reference.getURI() != null) {
            return reference.getURI();
        }
        return null;
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
    public List<? extends Registration> parseRegistration(InputStream registrationData, ImportMetadata importMetadata) throws DataFordelerException {
/*
        BufferedReader responseReader = null;
        try {
            responseReader = new BufferedReader(new InputStreamReader(registrationData, "UTF-8"));
            String line;
            while ((line = responseReader.readLine()) != null) {
                log.info("reading line");
                this.parseRegistration(this.objectMapper.readTree(line), importMetadata);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
*/
        if (importMetadata.getSession() != null) {
            Industry.prepopulateIndustryCache(importMetadata.getSession());
        }

        Scanner scanner = new Scanner(registrationData, "UTF-8").useDelimiter(String.valueOf(this.commonFetcher.delimiter));
        long count = 0;
        while (scanner.hasNext()) {
            boolean inter = Thread.currentThread().isInterrupted();
            System.out.println("running thread id: "+Thread.currentThread().getId());
            if (inter) break;
            try {
                String data = scanner.next();
                this.parseRegistration(this.getObjectMapper().readTree(data), importMetadata);
                this.log.info("chunk "+count);
                count++;
            } catch (IOException e) {
                throw new DataStreamException(e);
            }
        }
        return null;
    }

    protected abstract SessionManager getSessionManager();
    protected abstract String getJsonTypeName();
    protected abstract Class<T> getRecordClass();
    protected abstract Class<E> getEntityClass();
    protected abstract UUID generateUUID(T record);
    protected abstract E createBasicEntity(T record);
    protected abstract D createDataItem();


    private static final String TASK_PARSE = "CvrParse";
    private static final String TASK_FIND_ENTITY = "CvrFindEntity";
    private static final String TASK_FIND_REGISTRATIONS = "CvrFindRegistrations";
    private static final String TASK_FIND_ITEMS = "CvrFindItems";
    private static final String TASK_POPULATE_DATA = "CvrPopulateData";
    private static final String ADD_RECORD_DATA = "CvrAddRecordData";
    private static final String TASK_SAVE = "CvrSave";
    /**
     * Parse an incoming JsonNode into registrations (and save them)
     * Must be idempotent: Running a second time with the same input should not result in new data
     * @param jsonNode JSON object containing one or more parseable entities from the CVR data source
     * @return A list of registrations that have been saved to the database
     * @throws ParseException
     */
    public List<? extends Registration> parseRegistration(JsonNode jsonNode, ImportMetadata importMetadata) throws DataFordelerException {

        /*timer.reset(TASK_PARSE);
        timer.reset(TASK_FIND_ENTITY);
        timer.reset(TASK_FIND_REGISTRATIONS);
        timer.reset(TASK_FIND_ITEMS);
        timer.reset(TASK_POPULATE_DATA);
        timer.reset(TASK_SAVE);*/
        ArrayList<Registration> registrations = new ArrayList<>();
        Session session = importMetadata.getSession();
        if (session == null) {
            session = this.getSessionManager().getSessionFactory().openSession();
            session.beginTransaction();
        } else {
            //System.out.println("session already exists");
        }


        if (jsonNode.has("hits")) {
            jsonNode = jsonNode.get("hits");
            if (jsonNode.has("hits")) {
                jsonNode = jsonNode.get("hits");
            }
            if (jsonNode.isArray()) {
                if (jsonNode.size() == 0) {
                    throw new DataStreamException("No input data");
                }
                System.out.println("Node contains "+jsonNode.size()+" subnodes");
                // We have a list of results

                //int i = 0;
                for (JsonNode item : jsonNode) {
                    //System.out.println("Handling node "+i);
                    //registrations.addAll(
                            this.parseRegistration(item, importMetadata);
                    //);
                    //System.out.println("    flushing "+i);
                    //System.out.println("    clearing "+i);
                    //i++;

                    session.flush();
                    session.clear();

                }
                session.flush();
                session.getTransaction().commit();
                session.beginTransaction();
                session.clear();
                log.info(timer.formatAllTotal());

                return registrations;
            }
        }

        /*String type = jsonNode.has("_type") ? jsonNode.get("_type").asText() : null;
        if (type != null && !type.equals(this.getSchema())) {
            // Wrong type. See if we have another EntityManager that can handle it
            CvrEntityManager otherManager = (CvrEntityManager) this.getRegisterManager().getEntityManager(type);
            if (otherManager != null) {
                return otherManager.parseRegistration(jsonNode, importMetadata);
            }
            return null;
        }*/

        if (jsonNode.has("_source")) {
            jsonNode = jsonNode.get("_source");
        }
        String jsonTypeName = this.getJsonTypeName();
        if (jsonNode.has(jsonTypeName)) {
            jsonNode = jsonNode.get(jsonTypeName);
        }



        timer.start(TASK_PARSE);
        T toplevelRecord;
        try {
            toplevelRecord = getObjectMapper().treeToValue(jsonNode, this.getRecordClass());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
        timer.measure(TASK_PARSE);


        timer.start(TASK_FIND_ENTITY);
        UUID uuid = this.generateUUID(toplevelRecord);
        //E entity = QueryManager.getEntity(session, uuid, this.getEntityClass());
        E entity = null;
        String domain = CvrPlugin.getDomain();
//        if (QueryManager.hasIdentification(uuid, domain)) {
//            entity = QueryManager.getEntity(session, uuid, this.getEntityClass());
//        }
        if (entity == null) {
            log.debug("Creating new Entity");
            entity = this.createBasicEntity(toplevelRecord);
            entity.setIdentifikation(
                    QueryManager.getOrCreateIdentification(session, uuid, CvrPlugin.getDomain())
            );
        } else {
            log.debug("Using existing entity");
        }
        timer.measure(TASK_FIND_ENTITY);

        Collection<R> entityRegistrations = this.parseRegistration(entity, toplevelRecord.getAll(), session, importMetadata);

        // log.info("Entity " + entity.getUUID() + " now has " + entity.getRegistrations().size() + " registrations");
        /*if (transaction != null) {
            transaction.commit();
            session.close();
            System.out.println("ending session");
            System.out.println(timer.formatAllTotal());
            System.gc();
        }*/

        //registrations.addAll(entityRegistrations);


//        log.info(timer.formatTotal(TASK_PARSE));
//        log.info(timer.formatTotal(TASK_FIND_ENTITY));
//        log.info(timer.formatTotal(TASK_FIND_REGISTRATIONS));
//        log.info(timer.formatTotal(TASK_FIND_ITEMS));
//        log.info(timer.formatTotal(TASK_POPULATE_DATA));
//        log.info(timer.formatTotal(TASK_SAVE));

        return registrations;
    }



    private Collection<R> parseRegistration(E entity, List<CvrBaseRecord> records, Session session, ImportMetadata importMetadata) throws ParseException {

        HashSet<R> entityRegistrations = new HashSet<>();
        ListHashMap<Bitemporality, CvrBaseRecord> groups = this.sortIntoGroups(records);
        OffsetDateTime timestamp = OffsetDateTime.now();

        for (Bitemporality bitemporality : groups.keySet()) {

            timer.start(TASK_FIND_REGISTRATIONS);
            List<CvrBaseRecord> group = groups.get(bitemporality);
            List<R> registrations = entity.findRegistrations(bitemporality.registrationFrom, bitemporality.registrationTo);
            ArrayList<V> effects = new ArrayList<>();
            for (R registration : registrations) {
                V effect = registration.getEffect(bitemporality);
                if (effect == null) {
                    effect = registration.createEffect(bitemporality);
                }
                effects.add(effect);
            }
            entityRegistrations.addAll(registrations);
            timer.measure(TASK_FIND_REGISTRATIONS);


            timer.start(TASK_FIND_ITEMS);
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
                    log.debug("Wire basedata to effect " + effect.getRegistration().getRegistrationFrom() + "|" + effect.getRegistration().getRegistrationTo() + "|" + effect.getEffectFrom() + "|" + effect.getEffectTo());
                    baseData.addEffect(effect);
                }
            }
            timer.measure(TASK_FIND_ITEMS);

            //timer.start(TASK_POPULATE_DATA);
            for (CvrBaseRecord record : group) {
                timer.start(TASK_POPULATE_DATA+" "+record.getClass().getSimpleName());
                record.populateBaseData(baseData, session, timestamp);
                RecordData recordData = new RecordData(timestamp);
                // recordData.setSourceData(objectMapper.valueToTree(record).toString());
                baseData.addRecordData(recordData);

                timer.measure(TASK_POPULATE_DATA+" "+record.getClass().getSimpleName());
            }
            //timer.measure(TASK_POPULATE_DATA);
        }
        timer.start(TASK_SAVE);
        //ArrayList<R> registrationList = new ArrayList<>(entityRegistrations);
        //Collections.sort(registrationList);
        for (R registration : entityRegistrations) {
            registration.setLastImportTime(importMetadata.getImportTime());
            session.saveOrUpdate(registration);
        }
        session.saveOrUpdate(entity);
        timer.measure(TASK_SAVE);
        //return registrationList;
        return entityRegistrations;
    }


    public ListHashMap<Bitemporality, CvrBaseRecord> sortIntoGroups(Collection<CvrBaseRecord> records) {
        // Sort the records into groups that share bitemporality
        ListHashMap<Bitemporality, CvrBaseRecord> recordGroups = new ListHashMap<>();
        for (CvrBaseRecord record : records) {
            // Find the appropriate registration object
            Bitemporality bitemporality = new Bitemporality(record.getRegistrationFrom(), record.getRegistrationTo(), record.getValidFrom(), record.getValidTo());
            recordGroups.add(bitemporality, record);
        }
        return recordGroups;
    }

    @Override
    public boolean handlesOwnSaves() {
        return true;
    }
}
