package dk.magenta.datafordeler.cvr.records;

import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.core.exception.ParseException;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantBaseData;
import org.hibernate.Session;

/**
 * Created by lars on 26-06-17.
 */
public abstract class CompanyBaseRecord extends BaseRecord {
    public abstract void populateBaseData(CompanyBaseData baseData, QueryManager queryManager, Session session) throws ParseException;
    public void populateBaseData(ParticipantBaseData baseData, QueryManager queryManager, Session session) {}
}
