package dk.magenta.datafordeler.cvr.data.company;

import dk.magenta.datafordeler.cvr.data.shared.YearlyEmployeeNumbersData;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import static dk.magenta.datafordeler.cvr.data.shared.QuarterlyEmployeeNumbersData.DB_FIELD_YEAR;

@Entity
@Table(name = "cvr_company_yearly_employees", indexes = {
        @Index(name = "companyYearlyEmployeesYear", columnList = DB_FIELD_YEAR),
})
public class CompanyYearlyEmployeeNumbersData extends YearlyEmployeeNumbersData {
    @ManyToOne(targetEntity = CompanyBaseData.class)
    private CompanyBaseData companyBaseData;

    public CompanyBaseData getCompanyBaseData() {
        return this.companyBaseData;
    }

    public void setCompanyBaseData(CompanyBaseData companyBaseData) {
        this.companyBaseData = companyBaseData;
    }
}