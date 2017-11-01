package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import org.hibernate.Session;

/**
 * Created by lars on 26-06-17.
 */
public class CompanyLinkRecord extends CvrBaseRecord {

    @JsonProperty(value = "cvrNummer")
    private int cvrNumber;

    @Override
    public void populateBaseData(CompanyUnitBaseData baseData, Session session) {
        baseData.addAssociatedCvrNumber(this.cvrNumber);
    }

}
