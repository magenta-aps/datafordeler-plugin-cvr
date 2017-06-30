package dk.magenta.datafordeler.cvr;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.magenta.datafordeler.core.exception.DataFordelerException;
import dk.magenta.datafordeler.core.exception.DataStreamException;
import dk.magenta.datafordeler.core.io.Event;
import dk.magenta.datafordeler.core.plugin.*;
import dk.magenta.datafordeler.core.util.ItemInputStream;
import dk.magenta.datafordeler.core.util.ListHashMap;
import dk.magenta.datafordeler.cvr.configuration.CvrConfigurationManager;
import dk.magenta.datafordeler.cvr.data.company.CompanyEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

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
        this.commonFetcher = new ScanScrollCommunicator("Magenta_CVR_I_SKYEN", "20ce0f61-3f04-43ec-8119-3a67384e269c");
    }

    @PostConstruct
    public void init() {
        try {
            this.baseEndpoint = new URI(this.configurationManager.getConfiguration().getRegisterAddress());
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
        return expandBaseURI(this.getBaseEndpoint(), "/getNewEvents");
    }

    @Override
    protected Communicator getChecksumFetcher() {
        return this.commonFetcher;
    }

    @Override
    public URI getListChecksumInterface(String schema, OffsetDateTime from) {
        ListHashMap<String, String> parameters = new ListHashMap<>();
        if (schema != null) {
            parameters.add("objectType", schema);
        }
        if (from != null) {
            parameters.add("timestamp", from.format(DateTimeFormatter.ISO_DATE_TIME));
        }
        return expandBaseURI(this.getBaseEndpoint(), "/listChecksums", RegisterManager.joinQueryString(parameters), null);
    }

    public String getPullCronSchedule() {
        return this.configurationManager.getConfiguration().getPullCronSchedule();
    }

    public ItemInputStream<Event> pullEvents(URI eventInterface) throws DataFordelerException {
        //this.getLog().info("Pulling events from "+eventInterface);
        ScanScrollCommunicator eventCommunicator = (ScanScrollCommunicator) this.getEventFetcher();
        //InputStream responseBody = eventCommunicator.fetch(eventInterface);
        InputStream responseBody = null;
        try {
            responseBody = eventCommunicator.fetch(new URI(
                            "http://distribution.virk.dk/cvr-permanent/virksomhed/_search"),
                    new URI("http://distribution.virk.dk/_search/scroll"),
                    //"{\"query\":{\"match_all\":{}},\"size\":1}"
                    "{\"query\":{\"term\":{\"Vrvirksomhed.cvrNummer\":\"25052943\"}},\"size\":1}"
            );
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return this.parseEventResponse(responseBody);
    }




    @Override
    protected ItemInputStream<Event> parseEventResponse(InputStream responseContent) throws DataFordelerException {
        PipedInputStream inputStream = new PipedInputStream();
        final BufferedReader dataStream = new BufferedReader(new InputStreamReader(responseContent));
        try {
            final PipedOutputStream outputStream = new PipedOutputStream(inputStream);
            final ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String line;

                        // Scenario 1: one line per event
                        while ((line = dataStream.readLine()) != null) {
                            objectOutputStream.writeObject(CvrRegisterManager.this.parseLines(Collections.singletonList(line)));
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            objectOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            dataStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            t.start();
            return new ItemInputStream<Event>(inputStream);
        } catch (IOException e) {
            throw new DataStreamException(e);
        }
    }

    private Event parseLines(List<String> lines) {
        Event event = new Event();
        event.setEventID(UUID.randomUUID().toString());
        event.setBeskedVersion("1.0");
        StringJoiner s = new StringJoiner("\n");
        for (String line : lines) {
            s.add(line);
        }
        // Find the relevant class and parse the line into it
        String skema = CompanyEntity.schema;
        String data = s.toString();

        event.setDataskema(skema);
        event.setObjektData(data);
        return event;
    }

}
