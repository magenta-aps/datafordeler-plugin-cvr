package dk.magenta.datafordeler.cvr.data.shared;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 15-06-17.
 */
@Entity
@Table(name = "cvr_company_monthly_employees", indexes = {
        @Index(name = "companyMonthlyEmployeesYear", columnList = "year"),
        @Index(name = "companyMonthlyEmployeesMonth", columnList = "month")
})
public class MonthlyEmployeeNumbersData extends EmployeeNumbersData {


    @Column(name = "year")
    @JsonProperty(value = "år")
    @XmlElement(name = "år")
    private int year;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }



    @Column(name = "month")
    @JsonProperty(value = "måned")
    @XmlElement(name = "måned")
    private int month;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }




    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>(super.asMap());
        map.put("year", this.year);
        map.put("month", this.month);
        return map;
    }

    /**
     * Return a map of attributes, including those from the superclass
     * @return
     */
    @JsonIgnore
    public Map<String, Object> databaseFields() {
        HashMap<String, Object> map = new HashMap<>(super.databaseFields());
        map.put("year", this.year);
        map.put("month", this.month);
        return map;
    }
}
