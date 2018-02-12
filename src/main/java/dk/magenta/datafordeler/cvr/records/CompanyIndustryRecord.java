package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import dk.magenta.datafordeler.cvr.data.unversioned.Industry;
import org.hibernate.Session;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Record for Company and CompanyUnit industry.
 */
@Entity
@Table(name = "cvr_record_industry")
public class CompanyIndustryRecord extends CvrBitemporalDataRecord {

    @JsonIgnore
    private int index;

    @JsonProperty(value = "branchekode")
    private String industryCode;

    @JsonProperty(value = "branchetekst")
    private String industryText;

    public String getIndustryText() {
        return this.industryText;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public void populateBaseData(CompanyBaseData baseData, Session session) {
        Industry industry = Industry.getIndustry(this.industryCode, this.industryText, session);
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
    public void populateBaseData(CompanyUnitBaseData baseData, Session session) {
        Industry industry = Industry.getIndustry(this.industryCode, this.industryText, session);
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
