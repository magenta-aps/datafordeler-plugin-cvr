package dk.magenta.datafordeler.cvr.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.magenta.datafordeler.core.database.*;
import dk.magenta.datafordeler.core.exception.*;
import dk.magenta.datafordeler.core.io.ImportInputStream;
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
import dk.magenta.datafordeler.cvr.data.unversioned.*;
import dk.magenta.datafordeler.cvr.records.CvrBaseRecord;
import dk.magenta.datafordeler.cvr.records.CvrEntityRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

    private static final String TASK_PARSE = "CvrParse";
    private static final String TASK_FIND_ENTITY = "CvrFindEntity";
    private static final String TASK_FIND_REGISTRATIONS = "CvrFindRegistrations";
    private static final String TASK_FIND_ITEMS = "CvrFindItems";
    private static final String TASK_POPULATE_DATA = "CvrPopulateData";
    private static final String TASK_SAVE = "CvrSave";
    private static final String TASK_COMMIT = "Transaction commit";

    private static boolean IMPORT_ONLY_CURRENT = false;
    private static boolean DONT_IMPORT_CURRENT = false;

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

        Session session = importMetadata.getSession();
        if (session != null) {
            Industry.initializeCache(session);
            CompanyForm.initializeCache(session);
            CompanyStatus.initializeCache(session);
            Municipality.initializeCache(session);
            PostCode.initializeCache(session);
        }
        List<File> cacheFiles = null;
        if (registrationData instanceof ImportInputStream) {
            cacheFiles = ((ImportInputStream) registrationData).getCacheFiles();
        }

        Scanner scanner = new Scanner(registrationData, "UTF-8").useDelimiter(String.valueOf(this.commonFetcher.delimiter));
        boolean wrappedInTransaction = importMetadata.isTransactionInProgress();
        long chunkCount = 0;
        try {
            while (scanner.hasNext()) {
                System.out.println("running thread id: " + Thread.currentThread().getId());
                try {
                    String data = scanner.next();

                    if (session == null) {
                        session = this.getSessionManager().getSessionFactory().openSession();
                    }

                    if (!wrappedInTransaction) {
                        session.beginTransaction();
                        importMetadata.setTransactionInProgress(true);
                    }
                    try {
                        this.parseRegistration(this.getObjectMapper().readTree(data), importMetadata, session);

                        timer.start(TASK_COMMIT);
                        session.flush();
                        if (!wrappedInTransaction) {
                            session.getTransaction().commit();
                            importMetadata.setTransactionInProgress(false);
                            session.clear();
                        }
                        timer.measure(TASK_COMMIT);

                    } catch (ImportInterruptedException e) {
                        session.getTransaction().rollback();
                        importMetadata.setTransactionInProgress(false);
                        session.clear();
                        throw e;
                    }

                    log.info("Chunk " + chunkCount + ":\n" + timer.formatAllTotal());

                    chunkCount++;
                } catch (IOException e) {
                    throw new DataStreamException(e);
                }
            }
        } catch (ImportInterruptedException e) {
            log.info("Import aborted in chunk " + chunkCount);
            e.setChunk(chunkCount);
            if (cacheFiles != null) {
                log.info("Files are:");
                for (File file : cacheFiles) {
                    log.info(file.getAbsolutePath());
                }
            }
            e.setFiles(cacheFiles);
            // Write importMetadata.getCurrentURI and chunkCount to the database somehow
            throw e;
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

    /**
     * Parse an incoming JsonNode into registrations (and save them)
     * Must be idempotent: Running a second time with the same input should not result in new data
     * @param jsonNode JSON object containing one or more parseable entities from the CVR data source
     * @return A list of registrations that have been saved to the database
     * @throws ParseException
     */
    public List<? extends Registration> parseRegistration(JsonNode jsonNode, ImportMetadata importMetadata, Session session) throws DataFordelerException {
        ArrayList<Registration> registrations = new ArrayList<>();

        if (jsonNode.has("hits")) {
            jsonNode = jsonNode.get("hits");
            if (jsonNode.has("hits")) {
                jsonNode = jsonNode.get("hits");
            }
            if (jsonNode.isArray()) {
                if (jsonNode.size() == 0) {
                    throw new DataStreamException("No input data");
                }
                log.info("Node contains "+jsonNode.size()+" subnodes");
                // We have a list of results

                for (JsonNode item : jsonNode) {
                    registrations.addAll(this.parseRegistration(item, importMetadata, session));
                    //this.parseRegistration(item, importMetadata, session);
                }

                return registrations;
            }
        }


        timer.start(TASK_PARSE);
        this.checkInterrupt(importMetadata);
        if (jsonNode.has("_source")) {
            jsonNode = jsonNode.get("_source");
        }
        String jsonTypeName = this.getJsonTypeName();
        if (jsonNode.has(jsonTypeName)) {
            jsonNode = jsonNode.get(jsonTypeName);
        }
        T toplevelRecord;
        try {
            toplevelRecord = getObjectMapper().treeToValue(jsonNode, this.getRecordClass());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
        timer.measure(TASK_PARSE);


        timer.start(TASK_FIND_ENTITY);
        this.checkInterrupt(importMetadata);
        UUID uuid = this.generateUUID(toplevelRecord);
        E entity = null;
        String domain = CvrPlugin.getDomain();
        if (QueryManager.hasIdentification(session, uuid, domain)) {
            entity = QueryManager.getEntity(session, uuid, this.getEntityClass());
        }
        if (entity == null) {
            log.info("Creating new Entity");
            entity = this.createBasicEntity(toplevelRecord);
            entity.setIdentifikation(
                    QueryManager.getOrCreateIdentification(session, uuid, domain)
            );
        } else {
            log.info("Using existing entity");
        }
        timer.measure(TASK_FIND_ENTITY);



        this.checkInterrupt(importMetadata);
        HashSet<R> entityRegistrations = new HashSet<>();
        ListHashMap<Bitemporality, CvrBaseRecord> groups = this.sortIntoGroups(toplevelRecord.getAll());
        OffsetDateTime timestamp = OffsetDateTime.now();

        for (Bitemporality bitemporality : groups.keySet()) {

            timer.start(TASK_FIND_REGISTRATIONS);
            List<CvrBaseRecord> group = groups.get(bitemporality);
            List<R> entityRegistrationList = entity.findRegistrations(bitemporality.registrationFrom, bitemporality.registrationTo);
            ArrayList<V> effects = new ArrayList<>();
            for (R registration : entityRegistrationList) {
                V effect = registration.getEffect(bitemporality);
                if (effect == null) {
                    effect = registration.createEffect(bitemporality);
                }
                effects.add(effect);
            }
            entityRegistrations.addAll(entityRegistrationList);
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

            for (CvrBaseRecord record : group) {
                timer.start(TASK_POPULATE_DATA+" "+record.getClass().getSimpleName());
                record.populateBaseData(baseData, session, timestamp);
                //RecordData recordData = new RecordData(timestamp);
                //recordData.setSourceData(objectMapper.valueToTree(record).toString());
                //baseData.addRecordData(recordData);
                timer.measure(TASK_POPULATE_DATA+" "+record.getClass().getSimpleName());
            }
        }


        timer.start(TASK_SAVE);
        for (R registration : entityRegistrations) {
            registration.setLastImportTime(importMetadata.getImportTime());
            session.saveOrUpdate(registration);
        }
        session.saveOrUpdate(entity);
        timer.measure(TASK_SAVE);


        registrations.addAll(entityRegistrations);

        this.checkInterrupt(importMetadata);

        return registrations;
    }


    public ListHashMap<Bitemporality, CvrBaseRecord> sortIntoGroups(Collection<CvrBaseRecord> records) {
        // Sort the records into groups that share bitemporality
        ListHashMap<Bitemporality, CvrBaseRecord> recordGroups = new ListHashMap<>();
        for (CvrBaseRecord record : records) {
            // Find the appropriate registration object
            if (IMPORT_ONLY_CURRENT) {
                if (record.getRegistrationTo() == null && record.getValidTo() == null) {
                    recordGroups.add(new Bitemporality(roundTime(record.getRegistrationFrom()), roundTime(record.getRegistrationTo()), record.getValidFrom(), record.getValidTo()), record);
                }
            } else if (DONT_IMPORT_CURRENT) {
                if (record.getRegistrationTo() != null || record.getValidTo() != null) {
                    recordGroups.add(new Bitemporality(roundTime(record.getRegistrationFrom()), roundTime(record.getRegistrationTo()), record.getValidFrom(), record.getValidTo()), record);
                }
            } else {
                recordGroups.add(new Bitemporality(roundTime(record.getRegistrationFrom()), roundTime(record.getRegistrationTo()), record.getValidFrom(), record.getValidTo()), record);
            }
        }
        return recordGroups;
    }

    /*
    How far apart must two data points be for us to consider them separate registrations?
    For now, we say that if they are in the same minute, they're the same registration
     */
    private static OffsetDateTime roundTime(OffsetDateTime in) {
        if (in != null) {
            //return in.withHour(0).withMinute(0).withSecond(0).withNano(0);
            //return in.withMinute(0).withSecond(0).withNano(0);
            return in.withSecond(0).withNano(0);
        }
        return null;
    }

    @Override
    public boolean handlesOwnSaves() {
        return true;
    }

    private void checkInterrupt(ImportMetadata importMetadata) throws ImportInterruptedException {
        if (importMetadata.getStop()) {
            throw new ImportInterruptedException(new InterruptedException());
        }
    }
}
