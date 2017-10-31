package dk.magenta.datafordeler.cvr;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.magenta.datafordeler.core.database.SessionManager;
import dk.magenta.datafordeler.core.exception.*;
import dk.magenta.datafordeler.core.io.PluginSourceData;
import dk.magenta.datafordeler.core.plugin.*;
import dk.magenta.datafordeler.core.util.ItemInputStream;
import dk.magenta.datafordeler.cvr.configuration.CvrConfiguration;
import dk.magenta.datafordeler.cvr.configuration.CvrConfigurationManager;
import dk.magenta.datafordeler.cvr.data.CvrEntityManager;
import dk.magenta.datafordeler.cvr.synchronization.CvrSourceData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.IOUtils;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Created by lars on 16-05-17.
 */
@Component
public class CvrRegisterManager extends RegisterManager {

    private ScanScrollCommunicator commonFetcher;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CvrConfigurationManager configurationManager;

    @Autowired
    private CvrPlugin plugin;

    @Autowired
    private SessionManager sessionManager;

    private Logger log = LogManager.getLogger("CvrRegisterManager");

    public CvrRegisterManager() {

    }

    /**
    * RegisterManager initialization; set up configuration, source fetcher and source url
    */
    @PostConstruct
    public void init() {
        this.commonFetcher = new ScanScrollCommunicator();
        this.commonFetcher.setScrollIdJsonKey("_scroll_id");

    }

    @Override
    protected Logger getLog() {
        return this.log;
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public SessionManager getSessionManager() {
        return this.sessionManager;
    }

    @Override
    public URI getBaseEndpoint() {
        CvrConfiguration configuration = configurationManager.getConfiguration();
        try {
            return new URI(configuration.getRegisterAddress());
        } catch (URISyntaxException e) {
            this.log.error(e);
            return null;
        }
    }

    @Override
    protected Communicator getEventFetcher() {
        return this.commonFetcher;
    }

    @Override
    protected ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }


    @Override
    protected Communicator getChecksumFetcher() {
        return this.getEventFetcher();
    }

    @Override
    public URI getListChecksumInterface(String schema, OffsetDateTime from) {
        return null;
    }

    public String getPullCronSchedule() {
        return this.configurationManager.getConfiguration().getPullCronSchedule();
    }


    @Override
    public URI getEventInterface(EntityManager entityManager) {
        return null;
    }

    /**
     * Pull data from the data source denoted by eventInterface, using the 
     * mechanism appropriate for the source.
     * For CVR, this is done using a ScanScrollCommunicator, where we specify the 
     * query in a POST, then get a handle back that we can use in a series of 
     * subsequent GET requests to get all the data.
     * We then package each response in an Event, and feed them into a stream for 
     * returning.
     */
    @Override
    public ItemInputStream<? extends PluginSourceData> pullEvents(URI eventInterface, EntityManager entityManager) throws DataFordelerException {
        if (!(entityManager instanceof CvrEntityManager)) {
            throw new WrongSubclassException(CvrEntityManager.class, entityManager);
        }
        String schema = entityManager.getSchema();
        ScanScrollCommunicator eventCommunicator = (ScanScrollCommunicator) this.getEventFetcher();

        String requestBody;

        Session session = this.sessionManager.getSessionFactory().openSession();
        OffsetDateTime lastUpdateTime = entityManager.getLastUpdated(session);
        session.close();

        CvrConfiguration configuration = this.configurationManager.getConfiguration();
        if (configuration.getRegisterType(schema) == CvrConfiguration.RegisterType.REMOTE_HTTP) {

            if (lastUpdateTime == null) {
                requestBody = configuration.getInitialQuery(schema);
            } else {
                requestBody = String.format(
                        configuration.getUpdateQuery(schema),
                        lastUpdateTime.format(DateTimeFormatter.ISO_LOCAL_DATE)
                );
            }

            eventCommunicator.setUsername(configuration.getUsername(schema));
            eventCommunicator.setPassword(configuration.getPassword(schema));

            InputStream responseBody;
            final ArrayList<Throwable> errors = new ArrayList<>();

            try {
                eventCommunicator.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread t, Throwable e) {
                        errors.add(e);
                    }
                });
                responseBody = eventCommunicator.fetch(
                        new URI(configuration.getStartAddress(schema)),
                        new URI(configuration.getScrollAddress(schema)),
                        requestBody
                );
            } catch (URISyntaxException e) {
                throw new ConfigurationException("Invalid pull URI '"+e.getInput()+"'");
            } catch (IOException e) {
                throw new DataStreamException(e);
            }

            File cacheFile = new File("cache/cvr"+ LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE));

            try {
                cacheFile.createNewFile();
                FileWriter fileWriter = new FileWriter(cacheFile);
                org.apache.commons.io.IOUtils.copy(responseBody, fileWriter);
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            eventCommunicator.wait(responseBody);
            if (!errors.isEmpty()) {
                throw new ParseException("Error while loading data for "+entityManager.getSchema(), errors.get(0));
            }

            try {
                FileInputStream cachedData = new FileInputStream(cacheFile);
                return this.parseEventResponse(cachedData, entityManager);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected ItemInputStream<? extends PluginSourceData> parseEventResponse(final InputStream responseBody, EntityManager entityManager) throws DataFordelerException {
        PipedInputStream inputStream = new PipedInputStream();
        final PipedOutputStream outputStream;
        final String charsetName = "UTF-8";
        try {
            outputStream = new PipedOutputStream(inputStream);
            final ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (responseBody != null) {
                        final int dataBaseId = responseBody.hashCode();
                        BufferedReader responseReader = null;
                        int count = 0;
                        try {
                            responseReader = new BufferedReader(new InputStreamReader(responseBody, charsetName));
                            String line;
                            while ((line = responseReader.readLine()) != null) {
                                objectOutputStream.writeObject(new CvrSourceData(entityManager.getSchema(), line, dataBaseId + ":" + count));
                                count++;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            log.debug("Wrote " + count + " events");
                            try {
                                if (responseReader != null) {
                                    responseReader.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    try {
                        objectOutputStream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            t.start();

            //System.out.println("There are "+entityManagerParseStreams.size()+" streams");
            return new ItemInputStream<>(inputStream);
        } catch (IOException e) {
            throw new DataStreamException(e);
        }
    }
}
