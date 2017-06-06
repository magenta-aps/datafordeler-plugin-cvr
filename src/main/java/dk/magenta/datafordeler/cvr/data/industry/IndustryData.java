package dk.magenta.datafordeler.cvr.data.industry;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.Identification;
import dk.magenta.datafordeler.cvr.data.CvrData;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 16-05-17.
 */
@javax.persistence.Entity
@Table(name="cvr_industry_data")
public class IndustryData extends CvrData<IndustryEffect, IndustryData> {

    /**
     * Add entity-specific attributes here
     */
    @Column
    @JsonProperty(value = "brancheKode")
    @XmlElement(name = "brancheKode")
    private String industryCode;

    @Column
    @JsonProperty(value = "brancheTekst")
    @XmlElement(name = "brancheTekst")
    private String industryText;


    /**
     * Return a map of attributes, including those from the superclass
     * @return
     */
    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>(super.asMap());
        map.put("industryCode", this.industryCode);
        map.put("industryText", this.industryText);
        return map;
    }

}
