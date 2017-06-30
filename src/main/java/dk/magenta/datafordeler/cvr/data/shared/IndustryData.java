package dk.magenta.datafordeler.cvr.data.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.cvr.data.DetailData;
import dk.magenta.datafordeler.cvr.data.unversioned.Industry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 09-06-17.
 */
@Entity
@Table(name="cvr_industrylink")
public class IndustryData extends DetailData {

    public IndustryData() {
        this(false);
    }
    public IndustryData(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }



    @ManyToOne
    private Industry industry;

    public Industry getIndustry() {
        return this.industry;
    }

    public void setIndustry(Industry industry) {
        this.industry = industry;
    }



    @Column
    private boolean isPrimary;


    public boolean isPrimary() {
        return this.isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }



    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("industry", this.industry);
        map.put("isPrimary", this.isPrimary);
        return map;
    }

}
