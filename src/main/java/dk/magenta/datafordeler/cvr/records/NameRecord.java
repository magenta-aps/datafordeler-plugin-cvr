package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.cvr.data.Bitemporality;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantBaseData;
import org.hibernate.Session;

/**
 * Record for Company, CompanyUnit or Participant name.
 */
public class NameRecord extends CvrBaseRecord {

    @JsonProperty(value = "navn")
    private String name;

    public String getName() {
        return this.name;
    }

    @Override
    public void populateBaseData(CompanyBaseData baseData, Session session, Bitemporality bitemporality) {
        baseData.setCompanyName(this.name);
    }

    @Override
    public void populateBaseData(CompanyUnitBaseData baseData, Session session, Bitemporality bitemporality) {
        baseData.setName(this.name);
    }

    @Override
    public void populateBaseData(ParticipantBaseData baseData, Session session, Bitemporality bitemporality) {
        baseData.addName(this.name);
    }
}
