package dk.magenta.datafordeler.cvr;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.magenta.datafordeler.core.exception.DataFordelerException;
import dk.magenta.datafordeler.core.exception.DataStreamException;
import dk.magenta.datafordeler.core.plugin.HttpCommunicator;
import dk.magenta.datafordeler.cvr.configuration.CvrConfiguration;
import dk.magenta.datafordeler.cvr.configuration.CvrConfigurationManager;
import dk.magenta.datafordeler.cvr.records.CompanyRecord;
import dk.magenta.datafordeler.cvr.records.ParticipantRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class DirectLookup {

    @Autowired
    private CvrConfigurationManager configurationManager;

    @Autowired
    private ObjectMapper objectMapper;

    public Collection<CompanyRecord> companyLookup(Collection<String> cvrNumbers) throws DataFordelerException {
        List<CompanyRecord> records = new ArrayList<CompanyRecord>();
        for(String cvrNumber : cvrNumbers) {
            records.add(companyLookup(cvrNumber));
        }
        return records;
    }



    public CompanyRecord companyLookup(String cvrNumber) throws DataFordelerException {
        CvrConfiguration configuration = this.configurationManager.getConfiguration();
        File keystore = new File(configuration.getCompanyRegisterDirectLookupCertificate());
        String keystorePassword = null;
        try {
            keystorePassword = configuration.getCompanyRegisterDirectLookupPassword();
        } catch (GeneralSecurityException | IOException e) {
            throw new DataStreamException(e);
        }
        URI queryUri;
        try {
            queryUri = new URL(configuration.getCompanyRegisterDirectLookupAddress().replace("%{cvr}", cvrNumber)).toURI();
        } catch (URISyntaxException | MalformedURLException e) {
            throw new DataStreamException(e);
        }
        HttpCommunicator httpCommunicator = new HttpCommunicator(keystore, keystorePassword);
        InputStream response = httpCommunicator.fetch(queryUri);
        try {
            return objectMapper.readValue(response, CompanyRecord.class);
        } catch (IOException e) {
            throw new DataStreamException(e);
        } finally {
            try {
                response.close();
            } catch (IOException e) {
            }
        }
    }

    public Collection<ParticipantRecord> participantLookup(Collection<String> unitNumbers) throws DataFordelerException {
        List<ParticipantRecord> records = new ArrayList<ParticipantRecord>();
        for(String unitNumber : unitNumbers) {
            records.add(participantLookup(unitNumber));
        }
        return records;
    }


    public ParticipantRecord participantLookup(String unitNumber) throws DataFordelerException {
        CvrConfiguration configuration = this.configurationManager.getConfiguration();
        File keystore = new File(configuration.getParticipantRegisterDirectLookupCertificate());
        String keystorePassword = null;
        try {
            keystorePassword = configuration.getParticipantRegisterDirectLookupPassword();
        } catch (GeneralSecurityException | IOException e) {
            throw new DataStreamException(e);
        }
        URI queryUri;
        try {
            queryUri = new URL(configuration.getParticipantRegisterDirectLookupAddress().replace("%{unit}", unitNumber)).toURI();
        } catch (URISyntaxException | MalformedURLException e) {
            throw new DataStreamException(e);
        }
        HttpCommunicator httpCommunicator = new HttpCommunicator(keystore, keystorePassword);
        InputStream response = httpCommunicator.fetch(queryUri);
        try {
            return objectMapper.readValue(response, ParticipantRecord.class);
        } catch (IOException e) {
            throw new DataStreamException(e);
        } finally {
            try {
                response.close();
            } catch (IOException e) {
            }
        }
    }

}
