package dk.magenta.datafordeler.cvr.records;

import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantBaseData;
import org.hibernate.Session;

/**
 * Created by lars on 26-06-17.
 */
public abstract class CompanyBaseRecord extends BaseRecord {
    public abstract void populateCompanyBaseData(CompanyBaseData baseData, QueryManager queryManager, Session session);
    public void populateParticipantBaseData(ParticipantBaseData baseData, QueryManager queryManager, Session session) {}
}
