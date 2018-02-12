package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDate;

/**
 * The object representation of a “periode” JSON object from the source
 */
@Embeddable
public class CvrRecordPeriod {

    public static final String DB_FIELD_VALID_FROM = "validFrom";

    @Column(name = DB_FIELD_VALID_FROM)
    @JsonProperty(value = "gyldigFra")
    private LocalDate validFrom;

    public LocalDate getValidFrom() {
        return this.validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public static final String DB_FIELD_VALID_TO = "validTo";

    @Column(name = DB_FIELD_VALID_TO)
    @JsonProperty(value = "gyldigTil")
    private LocalDate validTo;

    public LocalDate getValidTo() {
        return this.validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }
}
