package dk.magenta.datafordeler.cvr.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.DataItem;
import dk.magenta.datafordeler.core.database.Effect;
import dk.magenta.datafordeler.core.database.Identification;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlElement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 19-05-17.
 */
@MappedSuperclass
public class CvrData<V extends Effect, D extends DataItem> extends DataItem<V, D> {

    // Add all common attributes here, annotated by @Column, @JsonProperty and @XmlElement
    // For example:

    @Column
    @JsonProperty
    @XmlElement
    private Identification referenceToOtherObject;

    @Column
    @JsonProperty
    @XmlElement
    private String someCommonData;

    /**
     * Return a Map of all attributes
     * @return
     */
    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("referenceToOtherObject", this.referenceToOtherObject);
        map.put("someCommonData", this.someCommonData);
        return map;
    }

    /**
     * Return a map of all Reference attributes
     * @return
     */
    @Override
    @JsonIgnore
    public HashMap<String, Identification> getReferences() {
        HashMap<String, Identification> references = super.getReferences();
        references.put("referenceToOtherObject", this.referenceToOtherObject);
        return references;
    }

    /**
     * Receive a map of references and update this object
     * @param references
     */
    @Override
    public void updateReferences(HashMap<String, Identification> references) {
        super.updateReferences(references);
        if (references.containsKey("referenceToOtherObject")) {
            this.referenceToOtherObject = references.get("referenceToOtherObject");
        }
    }

}
