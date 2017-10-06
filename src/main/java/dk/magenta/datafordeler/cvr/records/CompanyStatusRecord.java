package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.unversioned.CompanyStatus;
import org.hibernate.Session;

/**
 * Created by lars on 26-06-17.
 */
public class CompanyStatusRecord extends CvrBaseRecord {

    @JsonProperty(value = "status")
    private String status;

    @Override
    public void populateBaseData(CompanyBaseData baseData, Session session) {
        baseData.setStatus(CompanyStatus.getStatus(this.status, session));
    }

}
