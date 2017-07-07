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
    private OffsetDateTime startDato;

    public OffsetDateTime getStartDato() {
        return startDato;
    }

    public void setStartDato(OffsetDateTime startDato) {
        this.startDato = startDato;
    }



    @JsonProperty(value = "slutDato")
    @XmlElement(name = "slutDato")
    @Column(nullable = true)
    private OffsetDateTime slutDato;

    public OffsetDateTime getSlutDato() {
        return slutDato;
    }

    public void setSlutDato(OffsetDateTime slutDato) {
        this.slutDato = slutDato;
    }



    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        if (this.startDato != null) {
            map.put("startDato", this.startDato);
        }
        if (this.slutDato != null) {
            map.put("slutDato", this.slutDato);
        }
        return map;
    }
}
