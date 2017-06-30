package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.Identification;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.cvr.CvrPlugin;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import org.hibernate.Session;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 * Created by lars on 30-06-17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyParticipantRelationRecord extends CompanyBaseRecord {

    @JsonProperty(value = "deltager")
    private ParticipantRelationRecord participant;

    @JsonProperty(value = "organisationer")
    private List<OrganizationRecord> organizations;

    public OffsetDateTime getLastUpdated() {
        return this.participant.getLastUpdated();
    }

    @Override
    public void populateBaseData(CompanyBaseData baseData, QueryManager queryManager, Session session) {
        UUID participantUUID = this.participant.generateUUID();
        Identification participantIdentification = queryManager.getIdentification(session, participantUUID);
        if (participantIdentification == null) {
            participantIdentification = new Identification(participantUUID, CvrPlugin.getDomain());
            session.save(participantIdentification);
        }
        HashSet<Identification> organizationIdentifications = new HashSet<>();
        for (OrganizationRecord organizationRecord : this.organizations) {
            UUID organizationUUID = organizationRecord.generateUUID();
            Identification organizationIdentification = queryManager.getIdentification(session, organizationUUID);
            if (organizationIdentification == null) {
                organizationIdentification = new Identification(organizationUUID, CvrPlugin.getDomain());
                session.save(organizationIdentification);
            }
            organizationIdentifications.add(organizationIdentification);
        }
        baseData.addParticipantRelation(participantIdentification, organizationIdentifications);
    }

    @Override
    public void populateBaseData(CompanyUnitBaseData baseData, QueryManager queryManager, Session session) {

    }
}
