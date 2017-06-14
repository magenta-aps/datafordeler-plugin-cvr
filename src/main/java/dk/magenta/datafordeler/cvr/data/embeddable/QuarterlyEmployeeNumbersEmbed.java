package dk.magenta.datafordeler.cvr.data.embeddable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by jubk on 03-03-2015.
 */
@Embeddable
public class QuarterlyEmployeeNumbersEmbed extends EmployeeNumbersEmbed {

    @Column(name = "quarterlyEmployeesYear")
    private int year;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }



    @Column(name = "quarterlyEmployeesQuarter")
    private int quarter;

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }



    @JsonIgnore
    @XmlTransient
    @Column(name = "quarterlyEmployeesLow")
    private int quarterlyEmployeesLow;

    public int getQuarterlyEmployeesLow() {
        return quarterlyEmployeesLow;
    }

    public void setQuarterlyEmployeesLow(int quarterlyEmployeesLow) {
        if (this.quarterlyEmployeesHigh != 0) {
            this.checkBounds(quarterlyEmployeesLow, this.quarterlyEmployeesHigh);
        }
        this.quarterlyEmployeesLow = quarterlyEmployeesLow;
    }



    @JsonIgnore
    @XmlTransient
    @Column(name = "quarterlyEmployeesHigh")
    private int quarterlyEmployeesHigh;

    public int getQuarterlyEmployeesHigh() {
        return quarterlyEmployeesHigh;
    }

    public void setQuarterlyEmployeesHigh(int quarterlyEmployeesHigh) {
        if (this.quarterlyEmployeesLow != 0) {
            this.checkBounds(this.quarterlyEmployeesLow, quarterlyEmployeesHigh);
        }
        this.quarterlyEmployeesHigh = quarterlyEmployeesHigh;
    }

    @JsonProperty
    public String getYearlyEmployeesInterval() {
        return this.formatInterval(this.quarterlyEmployeesLow, this.quarterlyEmployeesHigh);
    }


    @JsonIgnore
    @XmlTransient
    @Column(name = "quarterlyEmployeesFullTimeEquivalentLow")
    private int quarterlyFullTimeEquivalentLow;

    public int getQuarterlyFullTimeEquivalentLow() {
        return quarterlyFullTimeEquivalentLow;
    }

    public void setQuarterlyFullTimeEquivalentLow(int quarterlyFullTimeEquivalentLow) {
        if (this.quarterlyFullTimeEquivalentHigh != 0) {
            this.checkBounds(quarterlyFullTimeEquivalentLow, this.quarterlyFullTimeEquivalentHigh);
        }
        this.quarterlyFullTimeEquivalentLow = quarterlyFullTimeEquivalentLow;
    }



    @JsonIgnore
    @XmlTransient
    @Column(name = "quarterlyEmployeesFullTimeEquivalentHigh")
    private int quarterlyFullTimeEquivalentHigh;

    public int getQuarterlyFullTimeEquivalentHigh() {
        return quarterlyFullTimeEquivalentHigh;
    }

    public void setQuarterlyFullTimeEquivalentHigh(int quarterlyFullTimeEquivalentHigh) {
        if (this.quarterlyFullTimeEquivalentLow != 0) {
            this.checkBounds(this.quarterlyFullTimeEquivalentLow, quarterlyFullTimeEquivalentHigh);
        }
        this.quarterlyFullTimeEquivalentHigh = quarterlyFullTimeEquivalentHigh;
    }

    @JsonProperty
    public String getYearlyFullTimeEquivalentInterval() {
        return this.formatInterval(this.quarterlyFullTimeEquivalentLow, this.quarterlyFullTimeEquivalentHigh);
    }


    @JsonIgnore
    @XmlTransient
    @Column(name = "quarterlyEmployeesIncludingOwnersLow")
    private int quarterlyIncludingOwnersLow;

    public int getQuarterlyIncludingOwnersLow() {
        return quarterlyIncludingOwnersLow;
    }

    public void setQuarterlyIncludingOwnersLow(int quarterlyIncludingOwnersLow) {
        if (this.quarterlyIncludingOwnersHigh != 0) {
            this.checkBounds(quarterlyIncludingOwnersLow, this.quarterlyIncludingOwnersHigh);
        }
        this.quarterlyIncludingOwnersLow = quarterlyIncludingOwnersLow;
    }



    @JsonIgnore
    @XmlTransient
    @Column(name = "quarterlyEmployeesIncludingOwnersHigh")
    private int quarterlyIncludingOwnersHigh;

    public int getQuarterlyIncludingOwnersHigh() {
        return quarterlyIncludingOwnersHigh;
    }

    public void setQuarterlyIncludingOwnersHigh(int quarterlyIncludingOwnersHigh) {
        if (this.quarterlyIncludingOwnersLow != 0) {
            this.checkBounds(this.quarterlyIncludingOwnersLow, quarterlyIncludingOwnersHigh);
        }
        this.quarterlyIncludingOwnersHigh = quarterlyIncludingOwnersHigh;
    }

    @JsonProperty
    public String getYearlyIncludingOwnersInterval() {
        return this.formatInterval(this.quarterlyIncludingOwnersLow, this.quarterlyIncludingOwnersHigh);
    }

}
