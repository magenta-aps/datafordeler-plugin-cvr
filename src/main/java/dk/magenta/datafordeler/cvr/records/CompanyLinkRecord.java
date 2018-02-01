package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.cvr.data.Bitemporality;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import org.hibernate.Session;

/**
 * Record for CompanyUnit associated CVR number.
 */
public class CompanyLinkRecord extends CvrBaseRecord {

    @JsonProperty(value = "cvrNummer")
    private int cvrNumber;

    @Override
    public void populateBaseData(CompanyUnitBaseData baseData, Session session, Bitemporality bitemporality) {
        baseData.addAssociatedCvrNumber(this.cvrNumber);
    }

}
