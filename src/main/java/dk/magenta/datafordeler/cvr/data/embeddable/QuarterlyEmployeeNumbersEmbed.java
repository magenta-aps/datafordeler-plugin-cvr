package dk.magenta.datafordeler.cvr.data.embeddable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by jubk on 03-03-2015.
 */
@Embeddable
public class QuarterlyEmployeeNumbersEmbed extends EmployeeNumbersEmbed {

    @Column(name = "quarterlyEmployeesYear")
    private int year;

    @Column(name = "quarterlyEmployeesQuarter")
    private int quarter;

    @Column(name = "quarterlyEmployeesLow")
    private int quarterlyEmployeesLow;

    @Column(name = "quarterlyEmployeesHigh")
    private int quarterlyEmployeesHigh;

    @Column(name = "quarterlyEmployeesFullTimeEquivalentLow")
    private int quarterlyFullTimeEquivalentLow;

    @Column(name = "quarterlyEmployeesFullTimeEquivalentHigh")
    private int quarterlyFullTimeEquivalentHigh;

    @Column(name = "quarterlyEmployeesIncludingOwnersLow")
    private int quarterlyIncludingOwnersLow;

    @Column(name = "quarterlyEmployeesIncludingOwnersHigh")
    private int quarterlyIncludingOwnersHigh;


    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }

    public int getQuarterlyEmployeesLow() {
        return quarterlyEmployeesLow;
    }

    public void setQuarterlyEmployeesLow(int quarterlyEmployeesLow) {
        if (this.quarterlyEmployeesHigh != 0) {
            this.checkBounds(quarterlyEmployeesLow, this.quarterlyEmployeesHigh);
        }
        this.quarterlyEmployeesLow = quarterlyEmployeesLow;
    }

    public int getQuarterlyEmployeesHigh() {
        return quarterlyEmployeesHigh;
    }

    public void setQuarterlyEmployeesHigh(int quarterlyEmployeesHigh) {
        if (this.quarterlyEmployeesLow != 0) {
            this.checkBounds(this.quarterlyEmployeesLow, quarterlyEmployeesHigh);
        }
        this.quarterlyEmployeesHigh = quarterlyEmployeesHigh;
    }

    public int getQuarterlyFullTimeEquivalentLow() {
        return quarterlyFullTimeEquivalentLow;
    }

    public void setQuarterlyFullTimeEquivalentLow(int quarterlyFullTimeEquivalentLow) {
        if (this.quarterlyFullTimeEquivalentHigh != 0) {
            this.checkBounds(quarterlyFullTimeEquivalentLow, this.quarterlyFullTimeEquivalentHigh);
        }
        this.quarterlyFullTimeEquivalentLow = quarterlyFullTimeEquivalentLow;
    }

    public int getQuarterlyFullTimeEquivalentHigh() {
        return quarterlyFullTimeEquivalentHigh;
    }

    public void setQuarterlyFullTimeEquivalentHigh(int quarterlyFullTimeEquivalentHigh) {
        if (this.quarterlyFullTimeEquivalentLow != 0) {
            this.checkBounds(this.quarterlyFullTimeEquivalentLow, quarterlyFullTimeEquivalentHigh);
        }
        this.quarterlyFullTimeEquivalentHigh = quarterlyFullTimeEquivalentHigh;
    }

    public int getQuarterlyIncludingOwnersLow() {
        return quarterlyIncludingOwnersLow;
    }

    public void setQuarterlyIncludingOwnersLow(int quarterlyIncludingOwnersLow) {
        if (this.quarterlyIncludingOwnersHigh != 0) {
            this.checkBounds(quarterlyIncludingOwnersLow, this.quarterlyIncludingOwnersHigh);
        }
        this.quarterlyIncludingOwnersLow = quarterlyIncludingOwnersLow;
    }

    public int getQuarterlyIncludingOwnersHigh() {
        return quarterlyIncludingOwnersHigh;
    }

    public void setQuarterlyIncludingOwnersHigh(int quarterlyIncludingOwnersHigh) {
        if (this.quarterlyIncludingOwnersLow != 0) {
            this.checkBounds(this.quarterlyIncludingOwnersLow, quarterlyIncludingOwnersHigh);
        }
        this.quarterlyIncludingOwnersHigh = quarterlyIncludingOwnersHigh;
    }

}
