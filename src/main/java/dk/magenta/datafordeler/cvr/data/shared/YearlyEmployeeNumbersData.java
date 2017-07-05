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
@Table(name = "cvr_company_yearly_employees", indexes = {
        @Index(name = "companyYearlyEmployeesYear", columnList = "aar")
})
public class YearlyEmployeeNumbersData extends EmployeeNumbersData {


    @Column(name = "aar")
    @JsonProperty(value = "aar")
    @XmlElement(name = "aar")
    private int aar;

    public int getAar() {
        return aar;
    }

    public void setAar(int aar) {
        this.aar = aar;
    }



    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>(super.asMap());
        map.put("aar", this.aar);
        return map;
    }

    /**
     * Return a map of attributes, including those from the superclass
     * @return
     */
    @JsonIgnore
    public Map<String, Object> databaseFields() {
        HashMap<String, Object> map = new HashMap<>(super.databaseFields());
        map.put("aar", this.aar);
        return map;
    }
}
