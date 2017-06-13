package dk.magenta.datafordeler.cvr.data.embeddable;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.cvr.data.industry.IndustryEntity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlElement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 12-06-17.
 */
@Embeddable
public class IndustryEmbed {

    @ManyToOne
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

    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("industry", this.industry);
        map.put("isPrimary", this.isPrimary);
        map.put("index", this.index);
        return map;
    }
}
