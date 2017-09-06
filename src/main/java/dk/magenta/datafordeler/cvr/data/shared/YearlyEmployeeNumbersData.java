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

import static dk.magenta.datafordeler.cvr.data.shared.YearlyEmployeeNumbersData.DB_FIELD_YEAR;

/**
 * Created by lars on 15-06-17.
 */
@Entity
@Table(name = "cvr_company_yearly_employees", indexes = {
        @Index(name = "companyYearlyEmployeesYear", columnList = DB_FIELD_YEAR)
})
public class YearlyEmployeeNumbersData extends EmployeeNumbersData implements Comparable<YearlyEmployeeNumbersData> {

    public static final String DB_FIELD_YEAR = "year";
    public static final String IO_FIELD_YEAR = "aar";

    @Column
    @JsonProperty(value = IO_FIELD_YEAR)
    @XmlElement(name = IO_FIELD_YEAR)
    private int year;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    //--------------------------------------------------

    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>(super.asMap());
        map.put(DB_FIELD_YEAR, this.year);
        return map;
    }

    /**
     * Return a map of attributes, including those from the superclass
     * @return
     */
    @JsonIgnore
    public Map<String, Object> databaseFields() {
        HashMap<String, Object> map = new HashMap<>(super.databaseFields());
        map.put(DB_FIELD_YEAR, this.year);
        return map;
    }

    @Override
    public int compareTo(YearlyEmployeeNumbersData o) {
        if (o == null) return 1;
        if (this.year == o.year) return 0;
        return this.year < o.year ? -1 : 1;
    }
}
