package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.DatabaseEntry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Record for Company and CompanyUnit yearly employee numbers.
 */
@Entity
@Table(name = CompanyYearlyNumbersRecord.TABLE_NAME, indexes = {
        @Index(name = CompanyYearlyNumbersRecord.TABLE_NAME + "__company", columnList = CompanyYearlyNumbersRecord.DB_FIELD_COMPANY + DatabaseEntry.REF),
        @Index(name = CompanyYearlyNumbersRecord.TABLE_NAME + "__unit", columnList = CompanyYearlyNumbersRecord.DB_FIELD_COMPANYUNIT + DatabaseEntry.REF),
        @Index(name = CompanyYearlyNumbersRecord.TABLE_NAME + "__year", columnList = CompanyYearlyNumbersRecord.DB_FIELD_YEAR),
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyYearlyNumbersRecord extends CompanyNumbersRecord {

    public static final String TABLE_NAME = "cvr_record_yearly_numbers";

    public static final String DB_FIELD_YEAR = "year";
    public static final String IO_FIELD_YEAR = "aar";

    @Column(name = DB_FIELD_YEAR)
    @JsonProperty(value = IO_FIELD_YEAR)
    private int year;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CompanyYearlyNumbersRecord that = (CompanyYearlyNumbersRecord) o;
        return year == that.year;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), year);
    }

    /*@Override
    public boolean equalData(Object o) {
        if (!super.equalData(o)) return false;
        CompanyYearlyNumbersRecord that = (CompanyYearlyNumbersRecord) o;
        return year == that.year;
    }*/
}
