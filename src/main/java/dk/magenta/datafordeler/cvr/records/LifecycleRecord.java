package dk.magenta.datafordeler.cvr.records;

import dk.magenta.datafordeler.cvr.data.Bitemporality;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import org.hibernate.Session;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Record for Company and CompanyUnit lifecycle data.
 */
public class LifecycleRecord extends CvrBaseRecord {

    /*@Override
    public String getContainerName() {
        return "livsforloeb";
    }*/

    @Override
    public void populateBaseData(CompanyBaseData baseData, Session session, Bitemporality bitemporality) {
        if (this.getValidFrom() != null) {
            baseData.setLivsforloebStart(OffsetDateTime.of(this.getValidFrom(), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        }
        if (this.getValidTo() != null) {
            baseData.setLivsforloebSlut(OffsetDateTime.of(this.getValidTo(), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        }
    }

    @Override
    public void populateBaseData(CompanyUnitBaseData baseData, Session session, Bitemporality bitemporality) {
        if (this.getValidFrom() != null) {
            baseData.setLifecycleStart(OffsetDateTime.of(this.getValidFrom(), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        }
        if (this.getValidTo() != null) {
            baseData.setLifecycleStop(OffsetDateTime.of(this.getValidTo(), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        }
    }
}
