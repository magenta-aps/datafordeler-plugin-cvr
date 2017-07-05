package dk.magenta.datafordeler.cvr.data.participant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.cvr.data.unversioned.UnversionedEntity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlElement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 09-06-17.
 */
@MappedSuperclass
public abstract class ParticipantClassification extends UnversionedEntity {

    @Column(unique = true)
    @JsonProperty
    @XmlElement
    private String navn;

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    /**
     * Return a map of attributes, including those from the superclass
     * @return
     */
    @JsonIgnore
    public Map<String, Object> databaseFields() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("navn", this.getNavn());
        return map;
    }
}
