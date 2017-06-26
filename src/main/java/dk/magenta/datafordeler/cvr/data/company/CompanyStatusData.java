package dk.magenta.datafordeler.cvr.data.company;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.cvr.data.DetailData;
import dk.magenta.datafordeler.cvr.data.unversioned.CompanyStatus;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 16-05-17.
 */
@Entity
@Table(name="cvr_company_status")
public class CompanyStatusData extends DetailData {

    /**
     * Return a map of attributes, including those from the superclass
     * @return
     */
    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", this.status);
        return map;
    }


    @ManyToOne
    @JsonProperty(value = "status")
    @XmlElement(name = "status")
    private CompanyStatus status;

    public CompanyStatus getStatus() {
        return this.status;
    }

    public void setStatus(CompanyStatus status) {
        this.status = status;
    }
}
