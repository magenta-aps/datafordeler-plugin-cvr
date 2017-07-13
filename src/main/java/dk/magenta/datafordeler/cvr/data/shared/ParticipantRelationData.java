package dk.magenta.datafordeler.cvr.data.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.Identification;
import dk.magenta.datafordeler.cvr.data.DetailData;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by lars on 30-06-17.
 */
@Entity
@Table(name = "cvr_participantrelation")
public class ParticipantRelationData extends DetailData {

    @ManyToOne
    @JsonProperty(value = "deltager")
    @XmlElement(name = "deltager")
    private Identification deltager;

    public void setDeltager(Identification deltager) {
        this.deltager = deltager;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonProperty(value = "organisationer")
    @XmlElement(name = "organisationer")
    private Set<Identification> organisationer = new HashSet<>();

    public void addOrganization(Identification organization) {
        this.organisationer.add(organization);
    }

    public Identification getDeltager() {
        return deltager;
    }

    public Set<Identification> getOrganisationer() {
        return organisationer;
    }

    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("deltager", this.deltager);
        return map;
    }
}
