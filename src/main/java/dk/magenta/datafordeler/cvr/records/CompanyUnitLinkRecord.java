package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.company.CompanyUnitLink;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import org.hibernate.Session;

/**
 * Created by lars on 26-06-17.
 */
public class CompanyUnitLinkRecord extends CompanyBaseRecord {

    @JsonProperty(value = "pNummer")
    private int pNumber;

    @Override
    public void populateBaseData(CompanyBaseData baseData, QueryManager queryManager, Session session) {
        CompanyUnitLink link = new CompanyUnitLink();
        link.setpNummer(this.pNumber);
        baseData.addProduktionsenhed(link);
    }

    @Override
    public void populateBaseData(CompanyUnitBaseData baseData, QueryManager queryManager, Session session) {
        // noop
    }
}
