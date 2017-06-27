package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.util.Pair;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lars on 26-06-17.
 */
public abstract class CompanyNumbersRecord extends CompanyBaseRecord {

    @JsonProperty(value = "antalAnsatte")
    private int employees;

    @JsonIgnore
    private int employeeLow;

    @JsonIgnore
    private int employeeHigh;

    @JsonProperty(value = "intervalKodeAntalAnsatte")
    public void setEmployeeRange(String range) {
        Pair<Integer, Integer> parsed = this.parseRange(range);
        if (parsed != null) {
            this.employeeLow = parsed.getKey();
            this.employeeHigh = parsed.getValue();
        }
    }

    public int getEmployeeLow() {
        return this.employeeLow;
    }

    public int getEmployeeHigh() {
        return this.employeeHigh;
    }



    @JsonProperty(value = "antalAarsvaerk")
    private int fulltimeEquivalent;

    @JsonIgnore
    private int fulltimeEquivalentLow;

    @JsonIgnore
    private int fulltimeEquivalentHigh;

    @JsonProperty(value = "intervalKodeAntalAarsvaerk")
    public void setFulltimeEquivalentRange(String range) {
        Pair<Integer, Integer> parsed = this.parseRange(range);
        if (parsed != null) {
            this.fulltimeEquivalentLow = parsed.getKey();
            this.fulltimeEquivalentHigh = parsed.getValue();
        }
    }

    public int getFulltimeEquivalentLow() {
        return this.fulltimeEquivalentLow;
    }

    public int getFulltimeEquivalentHigh() {
        return this.fulltimeEquivalentHigh;
    }



    @JsonProperty(value = "antalInklusivEjere")
    private int includingOwners;

    @JsonIgnore
    private int includingOwnersLow;

    @JsonIgnore
    private int includingOwnersHigh;

    @JsonProperty(value = "intervalKodeAntalInklusivEjere")
    public void setIncludingOwnersRange(String range) {
        Pair<Integer, Integer> parsed = this.parseRange(range);
        if (parsed != null) {
            this.includingOwnersLow = parsed.getKey();
            this.includingOwnersHigh = parsed.getValue();
        }
    }

    public int getIncludingOwnersLow() {
        return this.includingOwnersLow;
    }

    public int getIncludingOwnersHigh() {
        return this.includingOwnersHigh;
    }

    private static Pattern rangePattern = Pattern.compile("^ANTAL_(\\d+)_(\\d+)$");
    private static Pair<Integer, Integer> parseRange(String range) {
        if (range != null) {
            Matcher m = rangePattern.matcher(range);
            if (m.find()) {
                return new Pair<>(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
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
