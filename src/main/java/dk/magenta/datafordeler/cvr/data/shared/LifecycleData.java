package dk.magenta.datafordeler.cvr.data.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.cvr.data.DetailData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 15-06-17.
 */
@Entity
@Table(name = "cvr_company_lifecycle")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LifecycleData extends DetailData {

    @JsonProperty(value = "startDato")
    @XmlElement(name = "startDato")
    @Column(nullable = true)
    private OffsetDateTime startDate;

    public OffsetDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(OffsetDateTime startDate) {
        this.startDate = startDate;
    }



    @JsonProperty(value = "slutDato")
    @XmlElement(name = "slutDato")
    @Column(nullable = true)
    private OffsetDateTime endDate;

    public OffsetDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(OffsetDateTime endDate) {
        this.endDate = endDate;
    }



    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        if (this.startDate != null) {
            map.put("startDate", this.startDate);
        }
        if (this.endDate != null) {
            map.put("endDate", this.endDate);
        }
        return map;
    }
}
