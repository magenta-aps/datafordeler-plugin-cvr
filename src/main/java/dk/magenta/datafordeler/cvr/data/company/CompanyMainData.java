package dk.magenta.datafordeler.cvr.data.company;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.Identification;
import dk.magenta.datafordeler.cvr.data.DetailData;
import dk.magenta.datafordeler.cvr.data.embeddable.QuarterlyEmployeeNumbersEmbed;
import dk.magenta.datafordeler.cvr.data.embeddable.YearlyEmployeeNumbersEmbed;
import dk.magenta.datafordeler.cvr.data.unversioned.CompanyForm;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitEntity;
import dk.magenta.datafordeler.cvr.data.embeddable.LifeCycleEmbed;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 16-05-17.
 */
@Entity
@Table(name="cvr_company_data")
public class CompanyMainData extends DetailData {

    public CompanyMainData() {
        //this.companySharedData = new CompanySharedData();
    }

    /**
     * Embed common attributes
     */
    //private CompanySharedData companySharedData;


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


    @Embedded
    private LifeCycleEmbed lifeCycle;

    public LifeCycleEmbed getLifeCycle() {
        return lifeCycle;
    }

    public void setLifeCycle(LifeCycleEmbed lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    // CompanyInfo

    @ManyToOne
    @JsonProperty(value = "form")
    @XmlElement(name = "form")
    private CompanyForm form;

    public CompanyForm getForm() {
        return form;
    }

    public void setForm(CompanyForm form) {
        this.form = form;
    }

    // creditInformation


    // companyParticipantRelations

    // participants

    @Column
    @JsonProperty(value = "advertProtection")
    @XmlElement(name = "advertProtection")
    private boolean advertProtection;


    private YearlyEmployeeNumbersEmbed yearlyEmployeeNumbers;

    public YearlyEmployeeNumbersEmbed obtainYearlyEmployeeNumbers() {
        if (this.yearlyEmployeeNumbers == null) {
            this.yearlyEmployeeNumbers = new YearlyEmployeeNumbersEmbed();
        }
        return this.yearlyEmployeeNumbers;
    }

    private QuarterlyEmployeeNumbersEmbed quarterlyEmployeeNumbers;

    public QuarterlyEmployeeNumbersEmbed obtainQuarterlyEmployeeNumbers() {
        if (this.quarterlyEmployeeNumbers == null) {
            this.quarterlyEmployeeNumbers = new QuarterlyEmployeeNumbersEmbed();
        }
        return this.quarterlyEmployeeNumbers;
    }

    /**
     * Return a map of attributes, including those from the superclass
     * @return
     */
    //@Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("advertProtection", this.advertProtection);
        map.put("lifeCycle", this.lifeCycle);
        map.put("form", this.form);
        if (this.yearlyEmployeeNumbers != null) {
            map.put("yearlyEmployeeNumbers", yearlyEmployeeNumbers);
        }
        if (this.quarterlyEmployeeNumbers != null) {
            map.put("quarterlyEmployeeNumbers", quarterlyEmployeeNumbers);
        }
        return map;
    }

    /**
     * Return a map of references (omit this method if there are no references in the class)
     * @return
     */
    //@Override
    @JsonIgnore
    public HashMap<String, Identification> getReferences() {
        //HashMap<String, Identification> references = super.getReferences();
        //return references;
        return new HashMap<>();
    }

    /**
     * Update this object from a map of references (omit this method if there are no references in the class)
     * @return
     */
    //@Override
    public void updateReferences(HashMap<String, Identification> references) {
        //super.updateReferences(references);
        //this.companySharedData.updateReferences(references);
    }
}
