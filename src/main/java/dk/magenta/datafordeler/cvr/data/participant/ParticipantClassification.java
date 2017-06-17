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
public class ParticipantClassification extends UnversionedEntity {

    @Column
    @JsonProperty
    @XmlElement
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return a map of attributes, including those from the superclass
     * @return
     */
    @JsonIgnore
    public Map<String, Object> databaseFields() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", this.getName());
        return map;
    }
}
