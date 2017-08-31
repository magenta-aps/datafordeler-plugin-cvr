package dk.magenta.datafordeler.cvr.data.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.Identification;
import dk.magenta.datafordeler.cvr.data.DetailData;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantEntity;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.util.*;

/**
 * Created by lars on 30-06-17.
 */
@Entity
@Table(name = "cvr_participantrelation")
public class ParticipantRelationData extends DetailData implements Comparable<ParticipantRelationData> {

    @Override
    public int compareTo(ParticipantRelationData o) {
        Identification i1 = this.participant;
        Identification i2 = o == null ? null : o.participant;
        if (i1 == null && i2 == null) return 0;
        if (i1 == null) return -1;
        if (i2 == null) return 1;
        return i1.getUuid().compareTo(i2.getUuid());
    }

    public static class Comparator implements java.util.Comparator<ParticipantRelationData> {
        @Override
        public int compare(ParticipantRelationData o1, ParticipantRelationData o2) {
            if (o1 == null && o2 == null) return 0;
            if (o1 == null) return -1;
            return o1.compareTo(o2);
        }
    }

    @ManyToOne
    @JsonProperty(value = "deltager")
    @XmlElement(name = "deltager")
    private Identification participant;

    public void setParticipant(Identification participant) {
        this.participant = participant;
    }

    public Identification getParticipant() {
        return participant;
    }


    @ManyToMany(cascade = CascadeType.ALL)
    @JsonProperty(value = "organisationer")
    @XmlElement(name = "organisationer")
    private Set<Identification> organizations = new HashSet<>();

    public void addOrganization(Identification organization) {
        this.organizations.add(organization);
    }

    public Set<Identification> getOrganizations() {
        return organizations;
    }

    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("participant", this.participant);
        return map;
    }

    public void forceLoad(Session session) {
        Hibernate.initialize(this);
        Hibernate.initialize(this.participant);
        for (Identification organization : this.organizations) {
            Hibernate.initialize(organization);
        }
    }
}
