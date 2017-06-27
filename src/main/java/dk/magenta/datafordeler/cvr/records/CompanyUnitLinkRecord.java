package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.company.CompanyUnitLink;
import org.hibernate.Session;

/**
 * Created by lars on 26-06-17.
 */
public class CompanyUnitLinkRecord extends CompanyBaseRecord {

    @JsonProperty(value = "pNummer")
    private int pNumber;

    @Override
    public void populateCompanyBaseData(CompanyBaseData baseData, QueryManager queryManager, Session session) {
        CompanyUnitLink link = new CompanyUnitLink();
        link.setpNumber(this.pNumber);
        baseData.addCompanyUnit(link);
    }
}
