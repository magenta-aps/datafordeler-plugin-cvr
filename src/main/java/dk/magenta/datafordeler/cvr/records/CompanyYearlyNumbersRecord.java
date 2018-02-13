package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.DatabaseEntry;
import dk.magenta.datafordeler.core.exception.ParseException;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import org.hibernate.Session;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * Record for Company and CompanyUnit yearly employee numbers.
 */
@Entity
@Table(name = "cvr_record_yearly_numbers", indexes = {
        @Index(name = "cvr_record_yearlynumbers_company", columnList = CompanyYearlyNumbersRecord.DB_FIELD_COMPANY + DatabaseEntry.REF),
        @Index(name = "cvr_record_yearlynumbers_companyunit", columnList = CompanyYearlyNumbersRecord.DB_FIELD_COMPANYUNIT + DatabaseEntry.REF),
        @Index(name = "cvr_record_yearlynumbers_year", columnList = CompanyYearlyNumbersRecord.DB_FIELD_YEAR),
})
public class CompanyYearlyNumbersRecord extends CompanyNumbersRecord {

    public static final String DB_FIELD_YEAR = "year";
    public static final String IO_FIELD_YEAR = "aar";

    @Column(name = DB_FIELD_YEAR)
    @JsonProperty(value = IO_FIELD_YEAR)
    private int year;

    @Override
    public void populateBaseData(CompanyBaseData baseData, Session session) throws ParseException {
        baseData.setYearlyEmployeeNumbers(
                this.year,
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
        baseData.setYearlyEmployeeNumbers(
                this.year,
                this.getEmployeeLow(),
                this.getEmployeeHigh(),
                this.getFulltimeEquivalentLow(),
                this.getFulltimeEquivalentHigh(),
                this.getIncludingOwnersLow(),
                this.getIncludingOwnersHigh()
        );
    }
}
