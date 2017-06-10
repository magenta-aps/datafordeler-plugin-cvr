package dk.magenta.datafordeler.cvr.data.company;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.cvr.data.industry.IndustryEntity;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 09-06-17.
 */
@Table(name="cvr_company_industry")
public class CompanyIndustryData extends CompanyData {

    @Column
    @JsonProperty
    @XmlElement
    private IndustryEntity industry;

    @Column
    @JsonProperty
    @XmlElement
    private boolean isPrimary;

    @Column
    @JsonProperty
    @XmlElement
    private short index;

    /**
     * Return a map of attributes, including those from the superclass
     * @return
     */
    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("industry", this.industry);
        map.put("isPrimary", this.isPrimary);
        map.put("index", this.index);
        return map;
    }

}
