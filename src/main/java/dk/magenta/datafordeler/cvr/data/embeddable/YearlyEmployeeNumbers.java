package dk.magenta.datafordeler.cvr.data.embeddable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by jubk on 03-03-2015.
 */
@Embeddable
public class YearlyEmployeeNumbers extends EmployeeNumbers {

    @Column(name = "yearlyEmployeesYear")
    private int year;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}
