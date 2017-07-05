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
        @Index(name = "companyQuarterlyEmployessYear", columnList = "aar"),
        @Index(name = "companyQuarterlyEmployessQuarter", columnList = "kvartal, aar")
})
public class QuarterlyEmployeeNumbersData extends EmployeeNumbersData {


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



    @Column(name = "kvartal")
    @JsonProperty(value = "kvartal")
    @XmlElement(name = "kvartal")
    private int kvartal;

    public int getKvartal() {
        return kvartal;
    }

    public void setKvartal(int kvartal) {
        this.kvartal = kvartal;
    }




    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>(super.asMap());
        map.put("aar", this.aar);
        map.put("kvartal", this.kvartal);
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
        map.put("kvartal", this.kvartal);
        return map;
    }
}
