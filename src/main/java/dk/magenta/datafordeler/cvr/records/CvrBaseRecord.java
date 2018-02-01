package dk.magenta.datafordeler.cvr.records;

import dk.magenta.datafordeler.core.database.DataItem;
import dk.magenta.datafordeler.core.exception.ParseException;
import dk.magenta.datafordeler.cvr.data.Bitemporality;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantBaseData;
import org.hibernate.Session;

import java.time.OffsetDateTime;

/**
 * A superclass for data-holding records, able to populate Cvr DataItems (thus updating the DB)
 */
public abstract class CvrBaseRecord extends CvrRecord {

    public void populateBaseData(DataItem baseData, Session session, OffsetDateTime timestamp, Bitemporality bitemporality) throws ParseException {
        if (baseData instanceof CompanyBaseData) {
            CompanyBaseData companyBaseData = (CompanyBaseData) baseData;
            this.populateBaseData(companyBaseData, session, bitemporality);
        }
        if (baseData instanceof CompanyUnitBaseData) {
            CompanyUnitBaseData companyUnitBaseData = (CompanyUnitBaseData) baseData;
            this.populateBaseData(companyUnitBaseData, session, bitemporality);
        }
        if (baseData instanceof ParticipantBaseData) {
            ParticipantBaseData participantBaseData = (ParticipantBaseData) baseData;
            this.populateBaseData(participantBaseData, session, bitemporality);
        }
    }

    public void populateBaseData(CompanyBaseData baseData, Session session, Bitemporality bitemporality) throws ParseException {}

    public void populateBaseData(CompanyUnitBaseData baseData, Session session, Bitemporality bitemporality) throws ParseException {}

    public void populateBaseData(ParticipantBaseData baseData, Session session, Bitemporality bitemporality) throws ParseException {}
}
