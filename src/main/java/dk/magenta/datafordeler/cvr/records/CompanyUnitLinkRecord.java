package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.DatabaseEntry;
import dk.magenta.datafordeler.core.database.Identification;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.cvr.CvrPlugin;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitEntity;
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
@Table(name = "cvr_record_productionunit_link", indexes = {
        @Index(name = "cvr_record_productionunit_company", columnList = CompanyLinkRecord.DB_FIELD_COMPANY + DatabaseEntry.REF)
})
public class CompanyUnitLinkRecord extends CvrBitemporalDataRecord {

    public static final String DB_FIELD_PNUMBER = "pNumber";
    public static final String IO_FIELD_PNUMBER = "pNummer";

    @Column(name = DB_FIELD_PNUMBER)
    @JsonProperty(value = IO_FIELD_PNUMBER)
    private int pNumber;

    @Override
    public void populateBaseData(CompanyBaseData baseData, Session session) {
        baseData.addCompanyUnit(this.pNumber, this.getUnitIdentification(session));
    }

    private Identification getUnitIdentification(Session session) {
        UUID unitUUID = CompanyUnitEntity.generateUUID(this.pNumber);
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
}
