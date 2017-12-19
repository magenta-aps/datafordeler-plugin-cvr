package dk.magenta.datafordeler.cvr.data.companyunit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.magenta.datafordeler.cvr.data.shared.MonthlyEmployeeNumbersData;

import javax.persistence.*;

import static dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitMonthlyEmployeeNumbersData.DB_FIELD_BASEDATA;
import static dk.magenta.datafordeler.cvr.data.shared.MonthlyEmployeeNumbersData.DB_FIELD_MONTH;
import static dk.magenta.datafordeler.cvr.data.shared.QuarterlyEmployeeNumbersData.DB_FIELD_YEAR;

/**
 * Storage for data on a Company Unit's monthly employees
 * referenced by {@link dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData}
 */
@Entity
@Table(name = "cvr_companyunit_monthly_employees", indexes = {
        @Index(name = "cvr_companyunit_monthlyEmployess_year", columnList = DB_FIELD_YEAR),
        @Index(name = "cvr_companyunit_monthlyEmployees_month", columnList = DB_FIELD_MONTH + ", " + MonthlyEmployeeNumbersData.DB_FIELD_YEAR),
        @Index(name = "cvr_companyunit_monthlyEmployees_base", columnList = DB_FIELD_BASEDATA + "_id")
})
public class CompanyUnitMonthlyEmployeeNumbersData extends MonthlyEmployeeNumbersData {

    public static final String DB_FIELD_BASEDATA = "companyUnitBaseData";

    @JsonIgnore
    @ManyToOne(targetEntity = CompanyUnitBaseData.class)
    @JoinColumn(name = DB_FIELD_BASEDATA + "_id")
    private CompanyUnitBaseData companyUnitBaseData;

    public CompanyUnitBaseData getCompanyUnitBaseData() {
        return this.companyUnitBaseData;
    }

    public void setCompanyUnitBaseData(CompanyUnitBaseData companyUnitBaseData) {
        this.companyUnitBaseData = companyUnitBaseData;
    }
}
