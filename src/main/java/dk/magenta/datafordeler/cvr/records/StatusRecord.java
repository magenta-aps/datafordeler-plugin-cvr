package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.DatabaseEntry;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.unversioned.CompanyStatus;
import org.hibernate.Session;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Record for Company statusText data.
 */
@Entity
@Table(name = "cvr_record_status", indexes = {
        @Index(name = "cvr_record_status_company", columnList = StatusRecord.DB_FIELD_COMPANY + DatabaseEntry.REF),
        @Index(name = "cvr_record_status_companyunit", columnList = StatusRecord.DB_FIELD_COMPANYUNIT + DatabaseEntry.REF),
})
public class StatusRecord extends CvrBitemporalDataRecord {

    public static final String DB_FIELD_STATUSTEXT = "statusText";
    public static final String IO_FIELD_STATUSTEXT = "statustekst";

    @Column(name = DB_FIELD_STATUSTEXT)
    @JsonProperty(value = IO_FIELD_STATUSTEXT)
    private String statusText;

    public String getStatusText() {
        return this.statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }



    public static final String DB_FIELD_STATUSCODE = "statusCode";
    public static final String IO_FIELD_STATUSCODE = "statuskode";

    @Column(name = DB_FIELD_STATUSCODE)
    @JsonProperty(value = IO_FIELD_STATUSCODE)
    private int statusCode;

    public int getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }



    public static final String DB_FIELD_CREDITDATATEXT = "creditDataText";
    public static final String IO_FIELD_CREDITDATATEXT = "kreditoplysningtekst";

    @Column(name = DB_FIELD_CREDITDATATEXT)
    @JsonProperty(value = IO_FIELD_CREDITDATATEXT)
    private String creditDataText;

    public String getCreditDataText() {
        return this.creditDataText;
    }

    public void setCreditDataText(String creditDataText) {
        this.creditDataText = creditDataText;
    }



    public static final String DB_FIELD_CREDITDATACODE = "creditDataCode";
    public static final String IO_FIELD_CREDITDATACODE = "kreditoplysningkode";

    @Column(name = DB_FIELD_CREDITDATACODE)
    @JsonProperty(value = IO_FIELD_CREDITDATACODE)
    private int creditDataCode;

    public int getCreditDataCode() {
        return this.creditDataCode;
    }

    public void setCreditDataCode(int creditDataCode) {
        this.creditDataCode = creditDataCode;
    }



    @Override
    public void populateBaseData(CompanyBaseData baseData, Session session) {
        baseData.setStatus(CompanyStatus.getStatus(this.statusText, session));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        StatusRecord that = (StatusRecord) o;
        return statusCode == that.statusCode &&
                creditDataCode == that.creditDataCode &&
                Objects.equals(statusText, that.statusText) &&
                Objects.equals(creditDataText, that.creditDataText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), statusText, statusCode, creditDataText, creditDataCode);
    }
}
