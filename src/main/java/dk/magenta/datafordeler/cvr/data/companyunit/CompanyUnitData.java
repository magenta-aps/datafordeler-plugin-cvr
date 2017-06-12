package dk.magenta.datafordeler.cvr.data.companyunit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.Identification;
import dk.magenta.datafordeler.cvr.data.CvrData;
import dk.magenta.datafordeler.cvr.data.company.CompanyEntity;
import dk.magenta.datafordeler.cvr.data.embeddable.CompanySharedData;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantEntity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 16-05-17.
 */
@javax.persistence.Entity
@Table(name="cvr_companyunit_data", indexes = {
        @Index(name = "company", columnList = "company_id"),
        @Index(name = "pNumber", columnList = "pNumber"),
        @Index(name = "isPrimary", columnList = "isPrimary")})
public class CompanyUnitData extends CvrData<CompanyUnitEffect, CompanyUnitData> {

    /**
     * Add entity-specific attributes here
     */
    @Column
    @JsonProperty
    @XmlElement
    private String pNumber;

    @ManyToOne
    @JsonProperty
    @XmlElement
    private CompanyEntity company;

    @Column
    @JsonProperty
    @XmlElement
    private boolean isPrimary;

    /**
     * Embed common attributes
     */
    //private CompanySharedData companySharedData;

    /**
     * Return a map of attributes, including those from the superclass
     * @return
     */
    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pNumber", this.pNumber);
        map.put("isPrimary", this.isPrimary);
        //map.putAll(this.companySharedData.asMap());
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
        //references.putAll(this.companySharedData.getReferences());
        return references;
    }

    /**
     * Update this object from a map of references (omit this method if there are no references in the class)
     * @return
     */
    @Override
    public void updateReferences(HashMap<String, Identification> references) {
        super.updateReferences(references);
        //this.companySharedData.updateReferences(references);
    }

}
