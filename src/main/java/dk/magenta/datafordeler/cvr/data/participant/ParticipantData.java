package dk.magenta.datafordeler.cvr.data.participant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.DataItem;
import dk.magenta.datafordeler.core.database.Identification;
import dk.magenta.datafordeler.cvr.data.CvrData;

import javax.persistence.Column;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 16-05-17.
 */
@javax.persistence.Entity
@Table(name="cvr_participant_data", indexes = {
        @Index(name = "cvrNumber", columnList = "cvrNumber")/*,
        @Index(name = "type", columnList = "type"),
        @Index(name = "role", columnList = "role"),
        @Index(name = "status", columnList = "status")*/})
public class ParticipantData extends DataItem<ParticipantEffect, ParticipantData> {

    /**
     * Add entity-specific attributes here
     */
    @Column
    @JsonProperty
    @XmlElement
    private String cvrNumber;

    @ManyToOne
    @JsonProperty
    @XmlElement
    private Identification companyUnit;

/*
    @ManyToOne
    @JsonProperty
    @XmlElement
    private ParticipantType type;

    @ManyToOne
    @JsonProperty
    @XmlElement
    private ParticipantRole role;

    @ManyToOne
    @JsonProperty
    @XmlElement
    private ParticipantStatus status;
*/

    /**
     * Return a map of attributes, including those from the superclass
     * @return
     */
    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cvrNumber", this.cvrNumber);
        return map;
    }

    /**
     * Return a map of references (omit this method if there are no references in the class)
     * @return
     */
    @Override
    @JsonIgnore
    public HashMap<String, Identification> getReferences() {
        HashMap<String, Identification> references = super.getReferences();
        references.put("companyUnit", this.companyUnit);
        return references;
    }

    /**
     * Update this object from a map of references (omit this method if there are no references in the class)
     * @return
     */
    @Override
    public void updateReferences(HashMap<String, Identification> references) {
        super.updateReferences(references);
        if (references.containsKey("companyUnit")) {
            this.companyUnit = references.get("companyUnit");
        }
    }
}
