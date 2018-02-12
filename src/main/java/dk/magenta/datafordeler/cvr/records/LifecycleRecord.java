package dk.magenta.datafordeler.cvr.records;

import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import org.hibernate.Session;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Record for Company and CompanyUnit lifecycle data.
 */
@Entity
@Table(name = "cvr_record_lifecycle")
public class LifecycleRecord extends CvrBitemporalDataRecord {

    /*@Override
    public String getContainerName() {
        return "livsforloeb";
    }*/

    @Override
    public void populateBaseData(CompanyBaseData baseData, Session session) {
        if (this.getValidFrom() != null) {
            baseData.setLivsforloebStart(OffsetDateTime.of(this.getValidFrom(), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        }
        if (this.getValidTo() != null) {
            baseData.setLivsforloebSlut(OffsetDateTime.of(this.getValidTo(), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        }
    }

    @Override
    public void populateBaseData(CompanyUnitBaseData baseData, Session session) {
        if (this.getValidFrom() != null) {
            baseData.setLifecycleStart(OffsetDateTime.of(this.getValidFrom(), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        }
        if (this.getValidTo() != null) {
            baseData.setLifecycleStop(OffsetDateTime.of(this.getValidTo(), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        }
    }
}
