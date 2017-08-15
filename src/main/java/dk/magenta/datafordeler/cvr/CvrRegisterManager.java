package dk.magenta.datafordeler.cvr;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.magenta.datafordeler.core.exception.DataFordelerException;
import dk.magenta.datafordeler.core.exception.DataStreamException;
import dk.magenta.datafordeler.core.exception.HttpStatusException;
import dk.magenta.datafordeler.core.io.Event;
import dk.magenta.datafordeler.core.plugin.*;
import dk.magenta.datafordeler.core.util.ItemInputStream;
import dk.magenta.datafordeler.cvr.configuration.CvrConfiguration;
import dk.magenta.datafordeler.cvr.configuration.CvrConfigurationManager;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitEntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.*;

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

    @PostConstruct
    public void init() {
        CvrConfiguration configuration = configurationManager.getConfiguration();
        this.commonFetcher = new ScanScrollCommunicator(
                configuration.getUsername(),
                configuration.getPassword()
        );
        this.commonFetcher.setScrollIdJsonKey("_scroll_id");
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
    protected URI getEventInterface() {
        return null;
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

    public ItemInputStream<Event> pullEvents(URI eventInterface) throws DataFordelerException {
        ScanScrollCommunicator eventCommunicator = (ScanScrollCommunicator) this.getEventFetcher();

        PipedInputStream inputStream = new PipedInputStream();
        final PipedOutputStream outputStream;
        try {
            outputStream = new PipedOutputStream(inputStream);
            final ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            final List<EntityManager> entityManagers = new ArrayList<>(this.entityManagers);
            final URI baseEndpoint = this.baseEndpoint;

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    for (EntityManager entityManager : entityManagers) {
                        String body = "{\"query\":{\"match_all\":{}},\"size\":10}";

                        InputStream responseBody = null;
                        String schema = entityManager.getSchema();

                        try {
                            System.out.println("/cvr-permanent/" + schema + "/_search   "+body);
                            responseBody = eventCommunicator.fetch(
                                    new URI(
                                            baseEndpoint.getScheme(), baseEndpoint.getHost(),
                                            "/cvr-permanent/" + schema + "/_search", ""
                                    ),
                                    new URI(
                                            baseEndpoint.getScheme(), baseEndpoint.getHost(),
                                            "/_search/scroll", ""
                                    ),
                                    body
                            );
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        } catch (HttpStatusException e1) {
                            e1.printStackTrace();
                        } catch (DataStreamException e1) {
                            e1.printStackTrace();
                        }
                        if (responseBody != null) {

                            final BufferedReader responseReader = new BufferedReader(new InputStreamReader(responseBody));

                            int count = 0;
                            try {
                                String line;

                                // One line per event
                                while ((line = responseReader.readLine()) != null) {
                                    objectOutputStream.writeObject(CvrRegisterManager.this.wrap(Collections.singletonList(line), schema));
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
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
            e.printStackTrace();
            return null;
        }
    }

    private Event wrap(List<String> lines, String schema) {
        Event event = new Event();
        event.setEventID(UUID.randomUUID().toString());
        event.setBeskedVersion("1.0");
        StringJoiner s = new StringJoiner("\n");
        for (String line : lines) {
            s.add(line);
        }
        event.setDataskema(schema);
        event.setObjektData(s.toString());
        return event;
    }

}
