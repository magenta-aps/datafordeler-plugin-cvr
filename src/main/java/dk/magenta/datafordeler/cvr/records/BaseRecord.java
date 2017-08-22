package dk.magenta.datafordeler.cvr.records;

import dk.magenta.datafordeler.core.database.DataItem;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.core.exception.ParseException;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantBaseData;
import org.hibernate.Session;

/**
 * Created by lars on 26-06-17.
 */
public abstract class BaseRecord extends CvrRecord {

    public void populateBaseData(DataItem baseData, QueryManager queryManager, Session session) throws ParseException {
        if (baseData instanceof CompanyBaseData) {
            CompanyBaseData companyBaseData = (CompanyBaseData) baseData;
            this.populateBaseData(companyBaseData, queryManager, session);
        }
        if (baseData instanceof CompanyUnitBaseData) {
            CompanyUnitBaseData companyUnitBaseData = (CompanyUnitBaseData) baseData;
            this.populateBaseData(companyUnitBaseData, queryManager, session);
        }
        if (baseData instanceof ParticipantBaseData) {
            ParticipantBaseData participantBaseData = (ParticipantBaseData) baseData;
            this.populateBaseData(participantBaseData, queryManager, session);
        }
    }

    public void populateBaseData(CompanyBaseData baseData, QueryManager queryManager, Session session) throws ParseException {};
    public void populateBaseData(CompanyUnitBaseData baseData, QueryManager queryManager, Session session) throws ParseException {};
    public void populateBaseData(ParticipantBaseData baseData, QueryManager queryManager, Session session) throws ParseException {};
}
