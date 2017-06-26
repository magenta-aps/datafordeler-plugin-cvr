package dk.magenta.datafordeler.cvr.data.shared;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.exception.InvalidClientInputException;
import dk.magenta.datafordeler.core.exception.ParseException;
import dk.magenta.datafordeler.cvr.data.DetailData;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlTransient;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lars on 15-06-17.
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class CompanyEmployeeNumbersData extends DetailData {


    @JsonIgnore
    @XmlTransient
    @Column(name = "employeesLow")
    private Integer employeesLow;

    public int getEmployeesLow() {
        return employeesLow;
    }

    public void setEmployeesLow(Integer employeesLow) {
        if (employeesLow != null) {
            if (this.employeesHigh != null) {
                this.checkBounds(employeesLow, this.employeesHigh);
            }
            this.employeesLow = employeesLow;
        }
    }



    @JsonIgnore
    @XmlTransient
    @Column(name = "employeesHigh")
    private Integer employeesHigh;

    public int getEmployeesHigh() {
        return employeesHigh;
    }

    public void setEmployeesHigh(Integer employeesHigh) {
        if (employeesHigh != null) {
            if (this.employeesLow != null) {
                this.checkBounds(this.employeesLow, employeesHigh);
            }
            this.employeesHigh = employeesHigh;
        }
    }

    @JsonProperty
    public String getEmployeesInterval() {
        return this.formatInterval(this.employeesLow, this.employeesHigh);
    }


    @JsonIgnore
    @XmlTransient
    @Column(name = "fullTimeEquivalentLow")
    private Integer fullTimeEquivalentLow;

    public int getFullTimeEquivalentLow() {
        return fullTimeEquivalentLow;
    }

    public void setFullTimeEquivalentLow(Integer fullTimeEquivalentLow) {
        if (fullTimeEquivalentLow != null) {
            if (this.fullTimeEquivalentHigh != null) {
                this.checkBounds(fullTimeEquivalentLow, this.fullTimeEquivalentHigh);
            }
            this.fullTimeEquivalentLow = fullTimeEquivalentLow;
        }
    }



    @JsonIgnore
    @XmlTransient
    @Column(name = "fullTimeEquivalentHigh")
    private Integer fullTimeEquivalentHigh;

    public int getFullTimeEquivalentHigh() {
        return fullTimeEquivalentHigh;
    }

    public void setFullTimeEquivalentHigh(Integer fullTimeEquivalentHigh) {
        if (fullTimeEquivalentHigh != null) {
            if (this.fullTimeEquivalentLow != null) {
                this.checkBounds(this.fullTimeEquivalentLow, fullTimeEquivalentHigh);
            }
            this.fullTimeEquivalentHigh = fullTimeEquivalentHigh;
        }
    }

    @JsonProperty
    public String getFullTimeEquivalentInterval() {
        return this.formatInterval(this.fullTimeEquivalentLow, this.fullTimeEquivalentHigh);
    }


    @JsonIgnore
    @XmlTransient
    @Column(name = "includingOwnersLow")
    private Integer includingOwnersLow;

    public int getIncludingOwnersLow() {
        return includingOwnersLow;
    }

    public void setIncludingOwnersLow(Integer includingOwnersLow) {
        if (includingOwnersLow != null) {
            if (this.includingOwnersHigh != null) {
                this.checkBounds(includingOwnersLow, this.includingOwnersHigh);
            }
            this.includingOwnersLow = includingOwnersLow;
        }
    }



    @JsonIgnore
    @XmlTransient
    @Column(name = "includingOwnersHigh")
    private Integer includingOwnersHigh;

    public int getIncludingOwnersHigh() {
        return includingOwnersHigh;
    }

    public void setIncludingOwnersHigh(Integer includingOwnersHigh) {
        if (includingOwnersHigh != null) {
            if (this.includingOwnersLow != null) {
                this.checkBounds(this.includingOwnersLow, includingOwnersHigh);
            }
            this.includingOwnersHigh = includingOwnersHigh;
        }
    }

    @JsonProperty
    public String getIncludingOwnersInterval() {
        return this.formatInterval(this.includingOwnersLow, this.includingOwnersHigh);
    }


    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("employeesInterval", this.getEmployeesInterval());
        map.put("fullTimeEquivalentInterval", this.getFullTimeEquivalentInterval());
        map.put("includingOwnersInterval", this.getIncludingOwnersInterval());
        return map;
    }

    /**
     * Return a map of attributes, including those from the superclass
     * @return
     */
    @JsonIgnore
    public Map<String, Object> databaseFields() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("employeesLow", this.employeesLow);
        map.put("employeesHigh", this.employeesHigh);
        map.put("fullTimeEquivalentLow", this.fullTimeEquivalentLow);
        map.put("fullTimeEquivalentHigh", this.fullTimeEquivalentHigh);
        map.put("includingOwnersLow", this.includingOwnersLow);
        map.put("includingOwnersHigh", this.includingOwnersHigh);
        return map;
    }



    protected void checkBounds(int low, int high) throws InvalidClientInputException {
        if (high < low) {
            throw new InvalidClientInputException("Upper bound must not be less than lower bound (low: "+low+", high: "+high+")");
        }
        if (low < 0) {
            throw new InvalidClientInputException("Lower bound must not be less than zero (low: "+low+")");
        }
    }

    protected String formatInterval(Integer low, Integer high) {
        if (low == null || high == null) {
            return null;
        }
        if (low == high) {
            return Integer.toString(low);
        }
        return low + " - " + high;
    }

    private static Pattern intervalPattern = Pattern.compile("^(\\d+)\\s*-\\s*(\\d+)$");

    protected int parseLow(String interval) throws ParseException {
        try {
            return Integer.parseInt(interval);
        } catch (NumberFormatException e) {
            Matcher m = intervalPattern.matcher(interval);
            if (m.matches()) {
                return Integer.parseInt(m.group(1));
            }
            throw new ParseException("Cannot parse interval "+interval+", needing to match "+intervalPattern.toString());
        }
    }

    protected int parseHigh(String interval) throws ParseException {
        Matcher m = intervalPattern.matcher(interval);
        if (m.matches()) {
            return Integer.parseInt(m.group(2));
        }
        throw new ParseException("Cannot parse interval "+interval+", needing to match "+intervalPattern.toString());
    }

}
