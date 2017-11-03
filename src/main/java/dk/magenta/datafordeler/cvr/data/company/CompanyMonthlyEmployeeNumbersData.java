package dk.magenta.datafordeler.cvr.data.company;

import dk.magenta.datafordeler.cvr.data.shared.MonthlyEmployeeNumbersData;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import static dk.magenta.datafordeler.cvr.data.shared.MonthlyEmployeeNumbersData.DB_FIELD_MONTH;
import static dk.magenta.datafordeler.cvr.data.shared.QuarterlyEmployeeNumbersData.DB_FIELD_YEAR;

@Entity
@Table(name = "cvr_company_monthly_employees", indexes = {
        @Index(name = "companyMonthlyEmployessYear", columnList = DB_FIELD_YEAR),
        @Index(name = "companyMonthlyEmployeesMonth", columnList = DB_FIELD_MONTH + ", " + MonthlyEmployeeNumbersData.DB_FIELD_YEAR)
})
public class CompanyMonthlyEmployeeNumbersData extends MonthlyEmployeeNumbersData {
    @ManyToOne(targetEntity = CompanyBaseData.class)
    private CompanyBaseData companyBaseData;

    public CompanyBaseData getCompanyBaseData() {
        return this.companyBaseData;
    }

    public void setCompanyBaseData(CompanyBaseData companyBaseData) {
        this.companyBaseData = companyBaseData;
    }
}
