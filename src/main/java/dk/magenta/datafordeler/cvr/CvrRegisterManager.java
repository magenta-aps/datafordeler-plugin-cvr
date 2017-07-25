package dk.magenta.datafordeler.cvr;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.magenta.datafordeler.core.exception.DataFordelerException;
import dk.magenta.datafordeler.core.exception.DataStreamException;
import dk.magenta.datafordeler.core.exception.HttpStatusException;
import dk.magenta.datafordeler.core.exception.WrongSubclassException;
import dk.magenta.datafordeler.core.io.Event;
import dk.magenta.datafordeler.core.io.PluginSourceData;
import dk.magenta.datafordeler.core.plugin.*;
import dk.magenta.datafordeler.core.util.ItemInputStream;
import dk.magenta.datafordeler.cvr.configuration.CvrConfiguration;
import dk.magenta.datafordeler.cvr.configuration.CvrConfigurationManager;
import dk.magenta.datafordeler.cvr.data.CvrEntityManager;
import dk.magenta.datafordeler.cvr.data.company.CompanyEntity;
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
import java.util.*;

/**
 * Created by lars on 16-05-17.
 */
@Component
public class CvrRegisterManager extends RegisterManager {

    private HttpCommunicator commonFetcher;

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
    protected URI getEventInterface(EntityManager entityManager) {
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

    @Override
    public ItemInputStream<? extends PluginSourceData> pullEvents(final URI eventInterface, EntityManager entityManager) throws DataFordelerException {
        if (!(entityManager instanceof CvrEntityManager)) {
            throw new WrongSubclassException(CvrEntityManager.class, entityManager);
        }
        ScanScrollCommunicator eventCommunicator = (ScanScrollCommunicator) this.getEventFetcher();


        InputStream responseBody = null;
        String schema = entityManager.getSchema();

        try {
            URI repeaterUri = new URI(
                    eventInterface.getScheme(), eventInterface.getHost(),
                    "/_search/scroll", ""
            );
            responseBody = eventCommunicator.fetch(
                    eventInterface,
                    repeaterUri,
                    "{\"query\":{\"match_all\":{}},\"size\":10}"
            );
        } catch (URISyntaxException | DataStreamException | HttpStatusException e) {
            CvrRegisterManager.this.log.error(e);
        }
        return this.parseEventResponse(responseBody, entityManager);
    }

    @Override
    protected ItemInputStream<? extends PluginSourceData> parseEventResponse(InputStream inputStream, EntityManager entityManager) throws DataFordelerException {
        if (!(entityManager instanceof CvrEntityManager)) {
            throw new WrongSubclassException(CvrEntityManager.class, entityManager);
        }
        return this.parseEventResponse(inputStream, entityManager.getSchema());
    }

    private ItemInputStream<? extends PluginSourceData> parseEventResponse(final InputStream responseBody, String schema) {

        PipedInputStream inputStream = new PipedInputStream();
        final PipedOutputStream outputStream;
        try {
            outputStream = new PipedOutputStream(inputStream);
            final ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    if (responseBody != null) {

                        final BufferedReader responseReader = new BufferedReader(new InputStreamReader(responseBody));
                        System.out.println("inputStream: " + inputStream);
                        System.out.println("dataStream: " + responseReader);

                        int count = 0;
                        try {
                            String line;
                            while ((line = responseReader.readLine()) != null) {
                                System.out.println("got a response line");
                                objectOutputStream.writeObject(new CvrSourceData(schema, line));
                                count++;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {

                            System.out.println("Wrote " + count + " events");

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
            e.printStackTrace();
            return null;
        }
    }

}
