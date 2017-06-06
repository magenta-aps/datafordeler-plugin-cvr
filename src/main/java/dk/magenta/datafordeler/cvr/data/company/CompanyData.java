package dk.magenta.datafordeler.cvr.data.company;

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
@Table(name="cvr_company_data")
public class CompanyData extends CvrData<CompanyEffect, CompanyData> {

    /**
     * Add entity-specific attributes here
     */
    @Column
    @JsonProperty
    @XmlElement
    private String cvrNumber;

    @Column
    @JsonProperty(value = "hovedbranche")
    @XmlElement(name = "hovedbranche")
    private Identification mainIndustry;


    @Column
    @JsonProperty(value = "bibranche1")
    @XmlElement(name = "bibranche1")
    private Identification secondaryIndustry1;


    @Column
    @JsonProperty(value = "bibranche2")
    @XmlElement(name = "bibranche2")
    private Identification secondaryIndustry2;


    @Column
    @JsonProperty(value = "bibranche3")
    @XmlElement(name = "bibranche3")
    private Identification secondaryIndustry3;

    /**
     * Return a map of attributes, including those from the superclass
     * @return
     */
    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>(super.asMap());
        map.put("cvrNumber", this.cvrNumber);
        map.put("mainIndustry", this.mainIndustry);
        map.put("secondaryIndustry1", this.secondaryIndustry1);
        map.put("secondaryIndustry2", this.secondaryIndustry2);
        map.put("secondaryIndustry3", this.secondaryIndustry3);
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
        references.put("mainIndustry", this.mainIndustry);
        references.put("secondaryIndustry1", this.secondaryIndustry1);
        references.put("secondaryIndustry2", this.secondaryIndustry2);
        references.put("secondaryIndustry3", this.secondaryIndustry3);
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
            this.mainIndustry = references.get("mainIndustry");
            this.secondaryIndustry1 = references.get("secondaryIndustry1");
            this.secondaryIndustry2 = references.get("secondaryIndustry2");
            this.secondaryIndustry3 = references.get("secondaryIndustry3");
        }
    }
}
