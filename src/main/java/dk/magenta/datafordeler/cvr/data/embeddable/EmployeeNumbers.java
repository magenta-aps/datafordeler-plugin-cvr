package dk.magenta.datafordeler.cvr.data.embeddable;

import javax.persistence.Column;

/**
 * Created by jubk on 03-03-2015.
 */
public abstract class EmployeeNumbers {

    @Column(name = "yearlyEmployeesNumber")
    private int employees;

    @Column(name = "yearlyEmployeesInterval")
    private String employeesInterval;

    @Column(name = "yearlyEmployeesFullTimeEquivalent")
    private int fullTimeEquivalent;
    
    @Column(name = "yearlyEmployeesFullTimeEquivalentInterval")
    private String fullTimeEquivalentInterval;

    @Column(name = "yearlyEmployeesIncludingOwners")
    private int includingOwners;

    @Column(name = "yearlyEmployeesIncludingOwnersInterval")
    private String includingOwnersInterval;

    public int getEmployees() {
        return employees;
    }

    public void setEmployees(int employees) {
        this.employees = employees;
    }

    public String getEmployeesInterval() {
        return employeesInterval;
    }

    public void setEmployeesInterval(String employeesInterval) {
        this.employeesInterval = employeesInterval;
    }

    public int getFullTimeEquivalent() {
        return fullTimeEquivalent;
    }

    public void setFullTimeEquivalent(int fullTimeEquivalent) {
        this.fullTimeEquivalent = fullTimeEquivalent;
    }

    public String getFullTimeEquivalentInterval() {
        return fullTimeEquivalentInterval;
    }

    public void setFullTimeEquivalentInterval(String fullTimeEquivalentInterval) {
        this.fullTimeEquivalentInterval = fullTimeEquivalentInterval;
    }

    public int getIncludingOwners() {
        return includingOwners;
    }

    public void setIncludingOwners(int includingOwners) {
        this.includingOwners = includingOwners;
    }

    public String getIncludingOwnersInterval() {
        return includingOwnersInterval;
    }

    public void setIncludingOwnersInterval(String includingOwnersInterval) {
        this.includingOwnersInterval = includingOwnersInterval;
    }

}
