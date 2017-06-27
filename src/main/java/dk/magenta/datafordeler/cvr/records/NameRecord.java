package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantBaseData;
import org.hibernate.Session;

/**
 * Created by lars on 26-06-17.
 */
public class NameRecord extends BaseRecord {

    @JsonProperty(value = "navn")
    private String name;

    public static String getContainerName() {
        return "navne";
    }

    public void populateCompanyBaseData(CompanyBaseData baseData, QueryManager queryManager, Session session) {
        baseData.setName(this.name);
    }

    @Override
    public void populateParticipantBaseData(ParticipantBaseData baseData, QueryManager queryManager, Session session) {
        baseData.setName(this.name);
    }
}
