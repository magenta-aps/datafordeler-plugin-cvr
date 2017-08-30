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
@Table(name = "cvr_company_quarterly_employees", indexes = {
        @Index(name = "companyQuarterlyEmployessYear", columnList = "year"),
        @Index(name = "companyQuarterlyEmployessQuarter", columnList = "quarter, year")
})
public class QuarterlyEmployeeNumbersData extends EmployeeNumbersData implements Comparable<QuarterlyEmployeeNumbersData> {

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



    @Column(name = "quarter")
    @JsonProperty(value = "kvartal")
    @XmlElement(name = "kvartal")
    private int quarter;

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }




    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>(super.asMap());
        map.put("year", this.year);
        map.put("quarter", this.quarter);
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
        map.put("quarter", this.quarter);
        return map;
    }

    @Override
    public int compareTo(QuarterlyEmployeeNumbersData o) {
        if (o == null) return 1;
        if (this.year == o.year) {
            if (this.quarter == o.quarter) return 0;
            return this.quarter < o.quarter ? -1 : 1;
        }
        return this.year < o.year ? -1 : 1;
    }
}
