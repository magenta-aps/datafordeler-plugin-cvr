package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.exception.ParseException;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import org.hibernate.Session;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Record for Company and CompanyUnit quarterly employee numbers.
 */
@Entity
@Table(name = "cvr_record_quarterly_numbers")
public class CompanyQuarterlyNumbersRecord extends CompanyNumbersRecord {

    @JsonProperty(value = "aar")
    private int year;

    @JsonProperty(value = "kvartal")
    private int quarter;

    @Override
    public void populateBaseData(CompanyBaseData baseData, Session session) throws ParseException {
        baseData.setQuarterlyEmployeeNumbers(
                this.year,
                this.quarter,
                this.getEmployeeLow(),
                this.getEmployeeHigh(),
                this.getFulltimeEquivalentLow(),
                this.getFulltimeEquivalentHigh(),
                this.getIncludingOwnersLow(),
                this.getIncludingOwnersHigh()
        );
    }

    @Override
    public void populateBaseData(CompanyUnitBaseData baseData, Session session) throws ParseException {
        baseData.setQuarterlyEmployeeNumbers(
                this.year,
                this.quarter,
                this.getEmployeeLow(),
                this.getEmployeeHigh(),
                this.getFulltimeEquivalentLow(),
                this.getFulltimeEquivalentHigh(),
                this.getIncludingOwnersLow(),
                this.getIncludingOwnersHigh()
        );
    }
}
