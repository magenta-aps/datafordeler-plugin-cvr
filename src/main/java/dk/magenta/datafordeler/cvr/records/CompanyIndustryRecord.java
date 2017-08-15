package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import dk.magenta.datafordeler.cvr.data.unversioned.Industry;
import org.hibernate.Session;

/**
 * Created by lars on 26-06-17.
 */
public class CompanyIndustryRecord extends BaseRecord {

    @JsonIgnore
    private int index;

    @JsonProperty(value = "branchekode")
    private int code;

    @JsonProperty(value = "branchetekst")
    private String text;

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public void populateBaseData(CompanyBaseData baseData, QueryManager queryManager, Session session) {
        Industry industry = Industry.getIndustry(this.code, this.text, queryManager, session);
        switch (this.index) {
            case 0:
                baseData.setPrimaryIndustry(industry);
                break;
            case 1:
                baseData.setSecondaryIndustry1(industry);
                break;
            case 2:
                baseData.setSecondaryIndustry2(industry);
                break;
            case 3:
                baseData.setSecondaryIndustry3(industry);
                break;
        }
    }

    @Override
    public void populateBaseData(CompanyUnitBaseData baseData, QueryManager queryManager, Session session) {
        Industry industry = Industry.getIndustry(this.code, this.text, queryManager, session);
        switch (this.index) {
            case 0:
                baseData.setPrimaryIndustry(industry);
                break;
            case 1:
                baseData.setSecondaryIndustry1(industry);
                break;
            case 2:
                baseData.setSecondaryIndustry2(industry);
                break;
            case 3:
                baseData.setSecondaryIndustry3(industry);
                break;
        }
    }
}
