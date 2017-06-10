package dk.magenta.datafordeler.cvr.data.company;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.Identification;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitEntity;
import dk.magenta.datafordeler.cvr.data.embeddable.CompanySharedData;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 16-05-17.
 */
@javax.persistence.Entity
@Table(name="cvr_company_data", indexes = {@Index(name = "cvrNumber", columnList = "cvrNumber")})
public class CompanyMainData extends CompanyData {

    /**
     * Add entity-specific attributes here
     */
    @Column
    @JsonProperty
    @XmlElement
    private String cvrNumber;

    /**
     * Embed common attributes
     */
    private CompanySharedData companySharedData;


    @OneToMany
    @JsonProperty(value = "enheder")
    @XmlElement(name = "enheder")
    private Collection<CompanyUnitEntity> units;

/*
    @Column
    @JsonProperty(value = "hovedenhed")
    @XmlElement(name = "hovedenhed")
    private CompanyUnitEntity primaryUnit;
    */

    // CompanyInfo

    @ManyToOne
    @JsonProperty(value = "form")
    @XmlElement(name = "form")
    private CompanyForm form;


    // creditInformation


    // companyParticipantRelations





    /**
     * Return a map of attributes, including those from the superclass
     * @return
     */
    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cvrNumber", this.cvrNumber);
        map.putAll(this.companySharedData.asMap());
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
        references.putAll(this.companySharedData.getReferences());
        return references;
    }

    /**
     * Update this object from a map of references (omit this method if there are no references in the class)
     * @return
     */
    @Override
    public void updateReferences(HashMap<String, Identification> references) {
        super.updateReferences(references);
        this.companySharedData.updateReferences(references);
    }
}
