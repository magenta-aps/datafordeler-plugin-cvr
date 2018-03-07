package dk.magenta.datafordeler.cvr.records;

import dk.magenta.datafordeler.core.database.DatabaseEntry;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import org.hibernate.Session;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

/**
 * Record for Company and CompanyUnit lifecycle data.
 */
@Entity
@Table(name = LifecycleRecord.TABLE_NAME, indexes = {
        @Index(name = LifecycleRecord.TABLE_NAME + "__company", columnList = LifecycleRecord.DB_FIELD_COMPANY + DatabaseEntry.REF),
        @Index(name = LifecycleRecord.TABLE_NAME + "__companyunit", columnList = LifecycleRecord.DB_FIELD_COMPANYUNIT + DatabaseEntry.REF),
        @Index(name = LifecycleRecord.TABLE_NAME + "__participant", columnList = LifecycleRecord.DB_FIELD_PARTICIPANT + DatabaseEntry.REF),
})
public class LifecycleRecord extends CvrBitemporalDataRecord {

    public static final String TABLE_NAME = "cvr_record_lifecycle";

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
