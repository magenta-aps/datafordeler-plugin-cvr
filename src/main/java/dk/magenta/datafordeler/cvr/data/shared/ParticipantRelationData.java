package dk.magenta.datafordeler.cvr.data.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.Identification;
import dk.magenta.datafordeler.cvr.data.DetailData;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantEntity;

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
    private Identification participant;

    public void setParticipant(Identification participant) {
        this.participant = participant;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonProperty(value = "organisationer")
    @XmlElement(name = "organisationer")
    private Set<Identification> organizations = new HashSet<>();

    public void addOrganization(Identification organization) {
        this.organizations.add(organization);
    }


    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("participant", this.participant);
        return map;
    }
}
