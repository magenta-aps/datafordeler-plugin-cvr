package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.DatabaseEntry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * Record for Company, CompanyUnit or Participant name.
 */
@Entity
@Table(name = "cvr_record_company_regnumber", indexes = {
        @Index(name = "cvr_record_regnumber_company", columnList = CompanyRegNumberRecord.DB_FIELD_COMPANY + DatabaseEntry.REF),
        @Index(name = "cvr_record_regnumber_data", columnList = CompanyRegNumberRecord.DB_FIELD_REGNUMBER),
})
public class CompanyRegNumberRecord extends CvrBitemporalDataRecord {

    public static final String DB_FIELD_REGNUMBER = "regNumber";
    public static final String IO_FIELD_REGNUMBER = "regNummer";

    @Column(name = DB_FIELD_REGNUMBER)
    @JsonProperty(value = IO_FIELD_REGNUMBER)
    private String regNumber;

    public String getRegNumber() {
        return this.regNumber;
    }

}
