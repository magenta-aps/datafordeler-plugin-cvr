package dk.magenta.datafordeler.cvr.data.companyunit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.magenta.datafordeler.cvr.data.shared.YearlyEmployeeNumbersData;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import static dk.magenta.datafordeler.cvr.data.shared.QuarterlyEmployeeNumbersData.DB_FIELD_YEAR;

/**
 * Storage for data on a Company Unit's yearly employees
 * referenced by {@link dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData}
 */
@Entity
@Table(name = "cvr_companyunit_yearly_employees", indexes = {
        @Index(name = "companyUnitYearlyEmployeesYear", columnList = DB_FIELD_YEAR),
})
public class CompanyUnitYearlyEmployeeNumbersData extends YearlyEmployeeNumbersData {

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
