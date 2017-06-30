package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 * Created by lars on 26-06-17.
 */
public abstract class CvrRecord {

    public CvrRecord() {
    }

    @JsonProperty(value = "sidstOpdateret")
    private OffsetDateTime lastUpdated;

    public OffsetDateTime getLastUpdated() {
        return this.lastUpdated;
    }

    public void setLastUpdated(OffsetDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @JsonProperty(value = "periode")
    private CvrRecordPeriod validity;

    public CvrRecordPeriod getValidity() {
        return this.validity;
    }

    public void setValidity(CvrRecordPeriod validity) {
        this.validity = validity;
    }

    public LocalDate getValidFrom() {
        if (this.validity != null) {
            return this.validity.getValidFrom();
        } else {
            return null;
        }
    }

    public LocalDate getValidTo() {
        if (this.validity != null){
            return this.validity.getValidTo();
        } else {
            return null;
        }
    }
}
