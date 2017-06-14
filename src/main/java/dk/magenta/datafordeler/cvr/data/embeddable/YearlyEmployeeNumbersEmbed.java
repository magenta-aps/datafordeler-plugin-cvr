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
public class YearlyEmployeeNumbersEmbed extends EmployeeNumbersEmbed {

    @Column(name = "yearlyEmployeesYear")
    private int year;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }



    @JsonIgnore
    @Column(name = "yearlyEmployeesLow")
    private int yearlyEmployeesLow;

    public int getYearlyEmployeesLow() {
        return yearlyEmployeesLow;
    }

    public void setYearlyEmployeesLow(int yearlyEmployeesLow) {
        if (this.yearlyEmployeesHigh != 0) {
            this.checkBounds(yearlyEmployeesLow, this.yearlyEmployeesHigh);
        }
        this.yearlyEmployeesLow = yearlyEmployeesLow;
    }



    @JsonIgnore
    @Column(name = "yearlyEmployeesHigh")
    private int yearlyEmployeesHigh;

    public int getYearlyEmployeesHigh() {
        return yearlyEmployeesHigh;
    }

    public void setYearlyEmployeesHigh(int yearlyEmployeesHigh) {
        if (this.yearlyEmployeesLow != 0) {
            this.checkBounds(this.yearlyEmployeesLow, yearlyEmployeesHigh);
        }
        this.yearlyEmployeesHigh = yearlyEmployeesHigh;
    }

    @JsonProperty
    public String getYearlyEmployeesInterval() {
        return this.formatInterval(this.yearlyEmployeesLow, this.yearlyEmployeesHigh);
    }



    @JsonIgnore
    @XmlTransient
    @Column(name = "yearlyEmployeesFullTimeEquivalentLow")
    private int yearlyFullTimeEquivalentLow;

    public int getYearlyFullTimeEquivalentLow() {
        return yearlyFullTimeEquivalentLow;
    }

    public void setYearlyFullTimeEquivalentLow(int yearlyFullTimeEquivalentLow) {
        if (this.yearlyFullTimeEquivalentHigh != 0) {
            this.checkBounds(yearlyFullTimeEquivalentLow, this.yearlyFullTimeEquivalentHigh);
        }
        this.yearlyFullTimeEquivalentLow = yearlyFullTimeEquivalentLow;
    }



    @JsonIgnore
    @XmlTransient
    @Column(name = "yearlyEmployeesFullTimeEquivalentHigh")
    private int yearlyFullTimeEquivalentHigh;

    public int getYearlyFullTimeEquivalentHigh() {
        return yearlyFullTimeEquivalentHigh;
    }

    public void setYearlyFullTimeEquivalentHigh(int yearlyFullTimeEquivalentHigh) {
        if (this.yearlyFullTimeEquivalentLow != 0) {
            this.checkBounds(this.yearlyFullTimeEquivalentLow, yearlyFullTimeEquivalentHigh);
        }
        this.yearlyFullTimeEquivalentHigh = yearlyFullTimeEquivalentHigh;
    }

    @JsonProperty
    public String getYearlyFullTimeEquivalentInterval() {
        return this.formatInterval(this.yearlyFullTimeEquivalentLow, this.yearlyFullTimeEquivalentHigh);
    }




    @JsonIgnore
    @XmlTransient
    @Column(name = "yearlyEmployeesIncludingOwnersLow")
    private int yearlyIncludingOwnersLow;

    public int getYearlyIncludingOwnersLow() {
        return yearlyIncludingOwnersLow;
    }

    public void setYearlyIncludingOwnersLow(int yearlyIncludingOwnersLow) {
        if (this.yearlyIncludingOwnersHigh != 0) {
            this.checkBounds(yearlyIncludingOwnersLow, this.yearlyIncludingOwnersHigh);
        }
        this.yearlyIncludingOwnersLow = yearlyIncludingOwnersLow;
    }



    @JsonIgnore
    @XmlTransient
    @Column(name = "yearlyEmployeesIncludingOwnersHigh")
    private int yearlyIncludingOwnersHigh;

    public int getYearlyIncludingOwnersHigh() {
        return yearlyIncludingOwnersHigh;
    }

    public void setYearlyIncludingOwnersHigh(int yearlyIncludingOwnersHigh) {
        if (this.yearlyIncludingOwnersLow != 0) {
            this.checkBounds(this.yearlyIncludingOwnersLow, yearlyIncludingOwnersHigh);
        }
        this.yearlyIncludingOwnersHigh = yearlyIncludingOwnersHigh;
    }

    @JsonProperty
    public String getYearlyIncludingOwnersInterval() {
        return this.formatInterval(this.yearlyIncludingOwnersLow, this.yearlyIncludingOwnersHigh);
    }

}
