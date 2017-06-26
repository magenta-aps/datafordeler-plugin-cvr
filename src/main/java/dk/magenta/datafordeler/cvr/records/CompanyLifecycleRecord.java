package dk.magenta.datafordeler.cvr.records;

import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import org.hibernate.Session;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Created by lars on 26-06-17.
 */
public class CompanyLifecycleRecord extends CompanyBaseRecord {

    /*@Override
    public String getContainerName() {
        return "livsforloeb";
    }*/

    @Override
    public void populateCompanyBaseData(CompanyBaseData baseData, QueryManager queryManager, Session session) {
        if (this.getValidFrom() != null) {
            baseData.setLifecycleStartDate(OffsetDateTime.of(this.getValidFrom(), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        }
        if (this.getValidTo() != null) {
            baseData.setLifecycleEndDate(OffsetDateTime.of(this.getValidTo(), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        }
    }
}
