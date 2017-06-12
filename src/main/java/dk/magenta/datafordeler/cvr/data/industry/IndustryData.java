package dk.magenta.datafordeler.cvr.data.industry;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.cvr.data.CvrData;

import javax.persistence.Column;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 16-05-17.
 */
@javax.persistence.Entity
@Table(name="cvr_industry_data", indexes = {@Index(name="code", columnList = "code"), @Index(name="text", columnList = "text")})
public class IndustryData extends CvrData<IndustryEffect, IndustryData> {

    @Column(unique = true, nullable = false, insertable = true, updatable = false)
    @JsonProperty(value = "brancheKode")
    @XmlElement(name = "brancheKode")
    private String code;

    @Column(unique = true, nullable = false, insertable = true, updatable = false)
    @JsonProperty(value = "brancheTekst")
    @XmlElement(name = "brancheTekst")
    private String text;

    /**
     * Return a map of attributes, including those from the superclass
     * @return
     */
    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", this.code);
        map.put("text", this.text);
        return map;
    }

}
