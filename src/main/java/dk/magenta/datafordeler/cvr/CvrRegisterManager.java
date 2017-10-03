package dk.magenta.datafordeler.cvr;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.magenta.datafordeler.core.exception.DataFordelerException;
import dk.magenta.datafordeler.core.exception.DataStreamException;
import dk.magenta.datafordeler.core.exception.HttpStatusException;
import dk.magenta.datafordeler.core.exception.WrongSubclassException;
import dk.magenta.datafordeler.core.io.PluginSourceData;
import dk.magenta.datafordeler.core.plugin.*;
import dk.magenta.datafordeler.core.util.ItemInputStream;
import dk.magenta.datafordeler.cvr.configuration.CvrConfiguration;
import dk.magenta.datafordeler.cvr.configuration.CvrConfigurationManager;
import dk.magenta.datafordeler.cvr.data.CvrEntityManager;
import dk.magenta.datafordeler.cvr.synchronization.CvrSourceData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;

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

    private Logger log = LogManager.getLogger("CvrRegisterManager");

    public CvrRegisterManager() {

    }

    /**
    * RegisterManager initialization; set up configuration, source fetcher and source url
    */
    @PostConstruct
    public void init() {
        CvrConfiguration configuration = configurationManager.getConfiguration();
        this.commonFetcher = new ScanScrollCommunicator(
                configuration.getUsername(),
                configuration.getPassword()
        );
        this.commonFetcher.setScrollIdJsonKey("_scroll_id");
        this.commonFetcher.setThrottle(2000);
        try {
            this.baseEndpoint = new URI(configuration.getRegisterAddress());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Logger getLog() {
        return this.log;
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    private URI baseEndpoint;

    @Override
    public URI getBaseEndpoint() {
        return this.baseEndpoint;
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
        return this.commonFetcher;
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
        URI base = this.getBaseEndpoint();
        try {
            return new URI(
                    base.getScheme(), base.getHost(),
                    "/cvr-permanent/" + entityManager.getSchema() + "/_search", ""
            );
        } catch (URISyntaxException e) {
            this.log.error(e);
            return null;
        }
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
    protected ItemInputStream<? extends PluginSourceData> pullEvents(URI eventInterface, EntityManager entityManager) throws DataFordelerException {
        if (!(entityManager instanceof CvrEntityManager)) {
            throw new WrongSubclassException(CvrEntityManager.class, entityManager);
        }
        ScanScrollCommunicator eventCommunicator = (ScanScrollCommunicator) this.getEventFetcher();

        URI baseEndpoint = this.baseEndpoint;

        //String requestBody = "{\"query\":{\"match_all\":{}},\"size\":10}";
        String requestBody = "{\"query\":{\"bool\":{\n" +
                "\"must\":[{\n" +
                "\"range\":{\n" +
                "\"virksomhed.Vrvirksomhed.beliggenhedsadresse.kommune.kommuneKode\": {\n" +
                "\"from\":950\n" +
                "}\n" +
                "}\n" +
                "}]\n" +
                "}},\"size\":10}";

        InputStream responseBody = null;
        String schema = entityManager.getSchema();

        try {
            responseBody = eventCommunicator.fetch(
                    new URI(
                            baseEndpoint.getScheme(), baseEndpoint.getHost(),
                            "/cvr-permanent/" + schema + "/_search", ""
                    ),
                    new URI(
                            baseEndpoint.getScheme(), baseEndpoint.getHost(),
                            "/_search/scroll", ""
                    ),
                    requestBody
            );
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (HttpStatusException e1) {
            e1.printStackTrace();
        } catch (DataStreamException e1) {
            e1.printStackTrace();
        }
        return this.parseEventResponse(responseBody, entityManager);

    }
    @Override
    protected ItemInputStream<? extends PluginSourceData> parseEventResponse(final InputStream responseBody, EntityManager entityManager) throws DataFordelerException {
        PipedInputStream inputStream = new PipedInputStream();
        final PipedOutputStream outputStream;
        try {
            outputStream = new PipedOutputStream(inputStream);
            final ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (responseBody != null) {
                        final int dataBaseId = responseBody.hashCode();

                        final BufferedReader responseReader = new BufferedReader(new InputStreamReader(responseBody));

                        int count = 0;
                        try {
                            String line;

                            // One line per event
                            while ((line = responseReader.readLine()) != null) {
                                objectOutputStream.writeObject(new CvrSourceData(entityManager.getSchema(), line, dataBaseId + ":" + count));
                                count++;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            log.debug("Wrote " + count + " events");
                            try {
                                responseReader.close();
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
