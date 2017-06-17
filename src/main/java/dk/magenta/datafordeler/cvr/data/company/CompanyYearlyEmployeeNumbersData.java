package dk.magenta.datafordeler.cvr.data.company;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 15-06-17.
 */
@Entity
@Table(name = "cvr_company_yearly_employees", indexes = {
        @Index(name = "year", columnList = "year")
})
public class CompanyYearlyEmployeeNumbersData extends CompanyEmployeeNumbersData {


    @Column(name = "year")
    private int year;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }



    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>(super.asMap());
        map.put("year", this.year);
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
        return map;
    }
}
