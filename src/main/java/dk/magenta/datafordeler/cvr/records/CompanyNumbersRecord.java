package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Abstract record for Company and CompanyUnit employee numbers.
 */
@MappedSuperclass
public abstract class CompanyNumbersRecord extends CvrBitemporalDataRecord {

    private static class Range {
        public int low;
        public int high;

        public Range(int low, int high) {
            this.low = low;
            this.high = high;
        }
    }

    @JsonProperty(value = "antalAnsatte")
    private int employees;

    @JsonProperty(value = "antalAnsatteMin")
    private int employeeLow;

    @JsonProperty(value = "antalAnsatteMax")
    private int employeeHigh;

    @JsonProperty(value = "intervalKodeAntalAnsatte")
    public void setEmployeeRange(String range) {
        Range parsed = parseRange(range);
        if (parsed != null) {
            this.employeeLow = parsed.low;
            this.employeeHigh = parsed.high;
        }
    }

    @JsonProperty(value = "intervalKodeAntalAnsatte")
    public String getEmployeeRange() {
        if (this.employeeLow != 0 && this.employeeHigh != 0) {
            return "ANTAL_" + this.employeeLow + "_" + this.employeeHigh;
        }
        return null;
    }

    public int getEmployeeLow() {
        return this.employeeLow;
    }

    public int getEmployeeHigh() {
        return this.employeeHigh;
    }



    @JsonProperty(value = "antalAarsvaerk")
    private int fulltimeEquivalent;

    @JsonProperty(value = "antalAarsvaerkMin")
    private int fulltimeEquivalentLow;

    @JsonProperty(value = "antalAarsvaerkMax")
    private int fulltimeEquivalentHigh;

    @JsonProperty(value = "intervalKodeAntalAarsvaerk")
    public void setFulltimeEquivalentRange(String range) {
        Range parsed = parseRange(range);
        if (parsed != null) {
            this.fulltimeEquivalentLow = parsed.low;
            this.fulltimeEquivalentHigh = parsed.high;
        }
    }

    @JsonProperty(value = "intervalKodeAntalAarsvaerk")
    public String getFulltimeEquivalentRange() {
        if (this.fulltimeEquivalentLow != 0 && this.fulltimeEquivalentHigh != 0) {
            return "ANTAL_" + this.fulltimeEquivalentLow + "_" + this.fulltimeEquivalentHigh;
        }
        return null;
    }

    public int getFulltimeEquivalentLow() {
        return this.fulltimeEquivalentLow;
    }

    public int getFulltimeEquivalentHigh() {
        return this.fulltimeEquivalentHigh;
    }



    @JsonProperty(value = "antalInklusivEjere")
    private int includingOwners;

    @JsonProperty(value = "antalInklusivEjereMin")
    private int includingOwnersLow;

    @JsonProperty(value = "antalInklusivEjereMax")
    private int includingOwnersHigh;

    @JsonProperty(value = "intervalKodeAntalInklusivEjere")
    public void setIncludingOwnersRange(String range) {
        Range parsed = parseRange(range);
        if (parsed != null) {
            this.includingOwnersLow = parsed.low;
            this.includingOwnersHigh = parsed.high;
        }
    }

    @JsonProperty(value = "intervalKodeAntalInklusivEjere")
    public String getIncludingOwnersRange() {
        if (this.includingOwnersLow != 0 && this.includingOwnersHigh != 0) {
            return "ANTAL_" + this.includingOwnersLow + "_" + this.includingOwnersHigh;
        }
        return null;
    }

    public int getIncludingOwnersLow() {
        return this.includingOwnersLow;
    }

    public int getIncludingOwnersHigh() {
        return this.includingOwnersHigh;
    }

    private static Pattern rangePattern = Pattern.compile("^ANTAL_(\\d+)_(\\d+)$");
    private static Range parseRange(String range) {
        if (range != null) {
            Matcher m = rangePattern.matcher(range);
            if (m.find()) {
                return new Range(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
            }
        }
        return null;
    }


    public LocalDate getValidFrom() {
        return null;
    }

    public LocalDate getValidTo() {
        return null;
    }
}
