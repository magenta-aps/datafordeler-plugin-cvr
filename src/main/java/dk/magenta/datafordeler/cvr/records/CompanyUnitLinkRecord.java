package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.DatabaseEntry;
import dk.magenta.datafordeler.core.database.Identification;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.cvr.CvrPlugin;
import org.hibernate.Session;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

/**
 * Record for Company Unit references.
 */
@Entity
@Table(name = CvrPlugin.DEBUG_TABLE_PREFIX + CompanyUnitLinkRecord.TABLE_NAME, indexes = {
        @Index(name = CvrPlugin.DEBUG_TABLE_PREFIX + CompanyUnitLinkRecord.TABLE_NAME + "__company", columnList = CompanyLinkRecord.DB_FIELD_COMPANY + DatabaseEntry.REF)
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyUnitLinkRecord extends CvrBitemporalDataRecord {

    public static final String TABLE_NAME = "cvr_record_company_unit_relation";

    public static final String DB_FIELD_PNUMBER = "pNumber";
    public static final String IO_FIELD_PNUMBER = "pNummer";

    @Column(name = DB_FIELD_PNUMBER)
    @JsonProperty(value = IO_FIELD_PNUMBER)
    private int pNumber;

    private Identification getUnitIdentification(Session session) {
        UUID unitUUID = CompanyUnitRecord.generateUUID(this.pNumber);
        Identification unitIdentification = QueryManager.getOrCreateIdentification(session, unitUUID, CvrPlugin.getDomain());
        return unitIdentification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CompanyUnitLinkRecord that = (CompanyUnitLinkRecord) o;
        return pNumber == that.pNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), pNumber);
    }

    /*@Override
    public boolean equalData(Object o) {
        if (!super.equalData(o)) return false;
        CompanyUnitLinkRecord that = (CompanyUnitLinkRecord) o;
        return pNumber == that.pNumber;
    }*/
}
