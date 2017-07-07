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
public class CompanyIndustryRecord extends CompanyBaseRecord {

    @JsonIgnore
    private int index;

    @JsonProperty(value = "branchekode")
    private String branchekode;

    @JsonProperty(value = "branchetekst")
    private String branchetekst;

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public void populateBaseData(CompanyBaseData baseData, QueryManager queryManager, Session session) {
        Industry industry = Industry.getIndustry(this.branchekode, this.branchetekst, queryManager, session);
        switch (this.index) {
            case 0:
                baseData.setHovedbranche(industry);
                break;
            case 1:
                baseData.setbibranche1(industry);
                break;
            case 2:
                baseData.setBibranche2(industry);
                break;
            case 3:
                baseData.setBibranche3(industry);
                break;
        }
    }

    @Override
    public void populateBaseData(CompanyUnitBaseData baseData, QueryManager queryManager, Session session) {
        Industry industry = Industry.getIndustry(this.branchekode, this.branchetekst, queryManager, session);
        switch (this.index) {
            case 0:
                baseData.setHovedbranche(industry);
                break;
            case 1:
                baseData.setBibranche1(industry);
                break;
            case 2:
                baseData.setBibranche2(industry);
                break;
            case 3:
                baseData.setBibranche3(industry);
                break;
        }
    }
}
