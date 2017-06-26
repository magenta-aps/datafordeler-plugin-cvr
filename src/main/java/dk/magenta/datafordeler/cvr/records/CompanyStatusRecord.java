package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.company.CompanyEntityManager;
import dk.magenta.datafordeler.cvr.data.unversioned.CompanyStatus;
import org.hibernate.Session;

/**
 * Created by lars on 26-06-17.
 */
public class CompanyStatusRecord extends CompanyBaseRecord {

    @JsonProperty(value = "status")
    private String status;

    @Override
    public void populateCompanyBaseData(CompanyBaseData baseData, QueryManager queryManager, Session session) {
        CompanyStatus status = CompanyStatus.getStatus(this.status, queryManager, session);
        baseData.setStatus(status);
    }
}
