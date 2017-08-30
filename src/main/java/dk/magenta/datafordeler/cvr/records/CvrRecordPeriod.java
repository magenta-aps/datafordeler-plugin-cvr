package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

/**
 * Created by lars on 26-06-17.
 * The object representation of a “periode” JSON object from the source
 */
public class CvrRecordPeriod {

    @JsonProperty(value = "gyldigFra")
    private LocalDate validFrom;

    public LocalDate getValidFrom() {
        return this.validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    @JsonProperty(value = "gyldigTil")
    private LocalDate validTo;

    public LocalDate getValidTo() {
        return this.validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }
}
