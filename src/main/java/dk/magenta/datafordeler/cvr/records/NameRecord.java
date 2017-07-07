package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
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

    public void populateBaseData(CompanyBaseData baseData, QueryManager queryManager, Session session) {
        baseData.setVirksomhedsnavn(this.name);
    }

    @Override
    public void populateBaseData(CompanyUnitBaseData baseData, QueryManager queryManager, Session session) {
        baseData.setVirksomhedsnavn(this.name);
    }

    @Override
    public void populateBaseData(ParticipantBaseData baseData, QueryManager queryManager, Session session) {
        baseData.setNavn(this.name);
    }
}
