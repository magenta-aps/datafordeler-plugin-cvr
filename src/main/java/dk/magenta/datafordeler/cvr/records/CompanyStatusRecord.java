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

/**
 * Record for Company status data.
 */
@Entity
@Table(name = "cvr_record_companystatus", indexes = {
        @Index(name = "cvr_record_companystatus_company", columnList = CompanyStatusRecord.DB_FIELD_COMPANY + DatabaseEntry.REF),
        @Index(name = "cvr_record_companystatus_companyunit", columnList = CompanyStatusRecord.DB_FIELD_COMPANYUNIT + DatabaseEntry.REF),
})
public class CompanyStatusRecord extends CvrBitemporalDataRecord {

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

}
