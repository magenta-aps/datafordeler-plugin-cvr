package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.unversioned.CompanyStatus;
import org.hibernate.Session;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Record for Company status data.
 */
@Entity
@Table(name = "cvr_record_company_status")
public class CompanyStatusRecord extends CvrBitemporalDataRecord {

    @JsonProperty(value = "status")
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
