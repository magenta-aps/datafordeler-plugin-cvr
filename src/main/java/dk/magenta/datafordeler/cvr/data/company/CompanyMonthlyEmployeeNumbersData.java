package dk.magenta.datafordeler.cvr.data.company;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.magenta.datafordeler.cvr.data.shared.MonthlyEmployeeNumbersData;

import javax.persistence.*;

import static dk.magenta.datafordeler.cvr.data.company.CompanyMonthlyEmployeeNumbersData.DB_FIELD_BASEDATA;
import static dk.magenta.datafordeler.cvr.data.shared.MonthlyEmployeeNumbersData.DB_FIELD_MONTH;
import static dk.magenta.datafordeler.cvr.data.shared.QuarterlyEmployeeNumbersData.DB_FIELD_YEAR;

/**
 * Storage for data on a Company's monthly employees
 * referenced by {@link dk.magenta.datafordeler.cvr.data.company.CompanyBaseData}
 */
@Entity
@Table(name = "cvr_company_monthly_employees", indexes = {
        @Index(name = "cvr_company_monthlyEmployees_year", columnList = DB_FIELD_YEAR),
        @Index(name = "cvr_company_monthlyEmployees_month", columnList = DB_FIELD_MONTH + ", " + MonthlyEmployeeNumbersData.DB_FIELD_YEAR),
        @Index(name = "cvr_company_monthlyEmployees_base", columnList = DB_FIELD_BASEDATA + "_id")
})
public class CompanyMonthlyEmployeeNumbersData extends MonthlyEmployeeNumbersData {

    public static final String DB_FIELD_BASEDATA = "companyBaseData";

    @JsonIgnore
    @ManyToOne(targetEntity = CompanyBaseData.class)
    @JoinColumn(name = DB_FIELD_BASEDATA + "_id")
    private CompanyBaseData companyBaseData;

    public CompanyBaseData getCompanyBaseData() {
        return this.companyBaseData;
    }

    public void setCompanyBaseData(CompanyBaseData companyBaseData) {
        this.companyBaseData = companyBaseData;
    }
}
