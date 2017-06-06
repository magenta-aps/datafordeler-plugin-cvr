package dk.magenta.datafordeler.cvr.data.productionunit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.Identification;
import dk.magenta.datafordeler.cvr.data.CvrData;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 16-05-17.
 */
@javax.persistence.Entity
@Table(name="cvr_productionunit_data")
public class ProductionUnitData extends CvrData<ProductionUnitEffect, ProductionUnitData> {

    /**
     * Add entity-specific attributes here
     */
    @Column
    @JsonProperty
    @XmlElement
    private String cvrNumber;

    @Column
    @JsonProperty
    @XmlElement
    private Identification reference;

    /**
     * Return a map of attributes, including those from the superclass
     * @return
     */
    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>(super.asMap());
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
        references.put("reference", this.reference);
        return references;
    }

    /**
     * Update this object from a map of references (omit this method if there are no references in the class)
     * @return
     */
    @Override
    public void updateReferences(HashMap<String, Identification> references) {
        super.updateReferences(references);
        if (references.containsKey("reference")) {
            this.reference = references.get("reference");
        }
    }
}
