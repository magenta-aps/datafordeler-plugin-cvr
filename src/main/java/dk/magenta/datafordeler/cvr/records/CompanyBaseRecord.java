package dk.magenta.datafordeler.cvr.records;

import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import org.hibernate.Session;

/**
 * Created by lars on 26-06-17.
 */
public abstract class CompanyBaseRecord extends CvrRecord {
    public abstract void populateCompanyBaseData(CompanyBaseData baseData, QueryManager queryManager, Session session);
}
