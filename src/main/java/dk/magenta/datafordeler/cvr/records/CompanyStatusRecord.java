package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.DatabaseEntry;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.unversioned.CompanyStatus;
import org.hibernate.Session;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Record for Company status data.
 */
@Entity
@Table(name = CompanyStatusRecord.TABLE_NAME, indexes = {
        @Index(name = CompanyStatusRecord.TABLE_NAME + "__company", columnList = CompanyStatusRecord.DB_FIELD_COMPANY + DatabaseEntry.REF),
        @Index(name = CompanyStatusRecord.TABLE_NAME + "__unit", columnList = CompanyStatusRecord.DB_FIELD_COMPANYUNIT + DatabaseEntry.REF),
})
public class CompanyStatusRecord extends CvrBitemporalDataRecord {

    public static final String TABLE_NAME = "cvr_record_status";

    public static final String DB_FIELD_STATUS = "status";
    public static final String IO_FIELD_STATUS = "status";

    @Column(name = DB_FIELD_STATUS)
    @JsonProperty(value = IO_FIELD_STATUS)
    private String status;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void populateBaseData(CompanyBaseData baseData, Session session) {
        baseData.setStatus(CompanyStatus.getStatus(this.status, session));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CompanyStatusRecord that = (CompanyStatusRecord) o;
        return Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), status);
    }
}
