package dk.magenta.datafordeler.cvr.data.companyunit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.shared.QuarterlyEmployeeNumbersData;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import static dk.magenta.datafordeler.cvr.data.shared.QuarterlyEmployeeNumbersData.DB_FIELD_QUARTER;
import static dk.magenta.datafordeler.cvr.data.shared.QuarterlyEmployeeNumbersData.DB_FIELD_YEAR;

/**
 * Storage for data on a Company Unit's quarterly employees
 * referenced by {@link dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData}
 */
@Entity
@Table(name = "cvr_companyunit_quarterly_employees", indexes = {
        @Index(name = "companyUnitQuarterlyEmployessYear", columnList = DB_FIELD_YEAR),
        @Index(name = "companyUnitQuarterlyEmployessQuarter", columnList = DB_FIELD_QUARTER + ", " + DB_FIELD_YEAR)
})
public class CompanyUnitQuarterlyEmployeeNumbersData extends QuarterlyEmployeeNumbersData {

    @JsonIgnore
    @ManyToOne(targetEntity = CompanyUnitBaseData.class)
    private CompanyUnitBaseData companyUnitBaseData;

    public CompanyUnitBaseData getCompanyUnitBaseData() {
        return this.companyUnitBaseData;
    }

    public void setCompanyUnitBaseData(CompanyUnitBaseData companyUnitBaseData) {
        this.companyUnitBaseData = companyUnitBaseData;
    }
}
