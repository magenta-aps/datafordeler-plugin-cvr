package dk.magenta.datafordeler.cvr.data.company;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.magenta.datafordeler.cvr.data.shared.QuarterlyEmployeeNumbersData;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import static dk.magenta.datafordeler.cvr.data.shared.QuarterlyEmployeeNumbersData.DB_FIELD_QUARTER;
import static dk.magenta.datafordeler.cvr.data.shared.QuarterlyEmployeeNumbersData.DB_FIELD_YEAR;

/**
 * Storage for data on a Company's quarterly employees
 * referenced by {@link dk.magenta.datafordeler.cvr.data.company.CompanyBaseData}
 */
@Entity
@Table(name = "cvr_company_quarterly_employees", indexes = {
        @Index(name = "companyQuarterlyEmployessYear", columnList = DB_FIELD_YEAR),
        @Index(name = "companyQuarterlyEmployessQuarter", columnList = DB_FIELD_QUARTER + ", " + DB_FIELD_YEAR)
})
public class CompanyQuarterlyEmployeeNumbersData extends QuarterlyEmployeeNumbersData {

    @JsonIgnore
    @ManyToOne(targetEntity = CompanyBaseData.class)
    private CompanyBaseData companyBaseData;

    public CompanyBaseData getCompanyBaseData() {
        return this.companyBaseData;
    }

    public void setCompanyBaseData(CompanyBaseData companyBaseData) {
        this.companyBaseData = companyBaseData;
    }
}
