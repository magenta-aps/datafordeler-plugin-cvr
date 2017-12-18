package dk.magenta.datafordeler.cvr.data.companyunit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.magenta.datafordeler.cvr.data.shared.MonthlyEmployeeNumbersData;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import static dk.magenta.datafordeler.cvr.data.shared.MonthlyEmployeeNumbersData.DB_FIELD_MONTH;
import static dk.magenta.datafordeler.cvr.data.shared.QuarterlyEmployeeNumbersData.DB_FIELD_YEAR;

/**
 * Storage for data on a Company Unit's monthly employees
 * referenced by {@link dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData}
 */
@Entity
@Table(name = "cvr_companyunit_monthly_employees", indexes = {
        @Index(name = "companyUnitMonthlyEmployessYear", columnList = DB_FIELD_YEAR),
        @Index(name = "companyUnitMonthlyEmployeesMonth", columnList = DB_FIELD_MONTH + ", " + MonthlyEmployeeNumbersData.DB_FIELD_YEAR)
})
public class CompanyUnitMonthlyEmployeeNumbersData extends MonthlyEmployeeNumbersData {

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
