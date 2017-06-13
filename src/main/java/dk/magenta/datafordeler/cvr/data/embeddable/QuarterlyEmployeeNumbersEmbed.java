package dk.magenta.datafordeler.cvr.data.embeddable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by jubk on 03-03-2015.
 */
@Embeddable
public class QuarterlyEmployeeNumbersEmbed {

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
        this.quarterlyEmployeesLow = quarterlyEmployeesLow;
    }

    public int getQuarterlyEmployeesHigh() {
        return quarterlyEmployeesHigh;
    }

    public void setQuarterlyEmployeesHigh(int quarterlyEmployeesHigh) {
        this.quarterlyEmployeesHigh = quarterlyEmployeesHigh;
    }

    public int getQuarterlyFullTimeEquivalentLow() {
        return quarterlyFullTimeEquivalentLow;
    }

    public void setQuarterlyFullTimeEquivalentLow(int quarterlyFullTimeEquivalentLow) {
        this.quarterlyFullTimeEquivalentLow = quarterlyFullTimeEquivalentLow;
    }

    public int getQuarterlyFullTimeEquivalentHigh() {
        return quarterlyFullTimeEquivalentHigh;
    }

    public void setQuarterlyFullTimeEquivalentHigh(int quarterlyFullTimeEquivalentHigh) {
        this.quarterlyFullTimeEquivalentHigh = quarterlyFullTimeEquivalentHigh;
    }

    public int getQuarterlyIncludingOwnersLow() {
        return quarterlyIncludingOwnersLow;
    }

    public void setQuarterlyIncludingOwnersLow(int quarterlyIncludingOwnersLow) {
        this.quarterlyIncludingOwnersLow = quarterlyIncludingOwnersLow;
    }

    public int getQuarterlyIncludingOwnersHigh() {
        return quarterlyIncludingOwnersHigh;
    }

    public void setQuarterlyIncludingOwnersHigh(int quarterlyIncludingOwnersHigh) {
        this.quarterlyIncludingOwnersHigh = quarterlyIncludingOwnersHigh;
    }

}
