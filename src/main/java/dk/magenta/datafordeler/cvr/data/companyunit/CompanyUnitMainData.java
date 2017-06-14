package dk.magenta.datafordeler.cvr.data.companyunit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.Identification;
import dk.magenta.datafordeler.cvr.data.DetailData;
import dk.magenta.datafordeler.cvr.data.company.CompanyEntity;
import dk.magenta.datafordeler.cvr.data.embeddable.LifeCycleEmbed;
import dk.magenta.datafordeler.cvr.data.embeddable.QuarterlyEmployeeNumbersEmbed;
import dk.magenta.datafordeler.cvr.data.embeddable.YearlyEmployeeNumbersEmbed;
import dk.magenta.datafordeler.cvr.data.unversioned.CompanyForm;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 16-05-17.
 */
@Entity
@Table(name="cvr_companyunit_data")
public class CompanyUnitMainData extends DetailData {

    public CompanyUnitMainData() {
    }




    /**
     * Return a map of attributes, including those from the superclass
     * @return
     */
    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("form", this.form);
        map.put("lifeCycle", this.lifeCycle);
        map.put("advertProtection", this.advertProtection);
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
    @Override
    @JsonIgnore
    public HashMap<String, Identification> getReferences() {
        return new HashMap<String, Identification>();
    }

    /**
     * Update this object from a map of references (omit this method if there are no references in the class)
     * @return
     */
    @Override
    public void updateReferences(HashMap<String, Identification> references) {

    }

    @ManyToOne
    @JsonProperty
    @XmlElement
    private CompanyEntity company;

    @Column
    @JsonProperty
    @XmlElement
    private boolean isPrimary;

    @OneToMany
    @JsonProperty(value = "enheder")
    @XmlElement(name = "enheder")
    private Collection<CompanyUnitEntity> units;

    

    @ManyToOne
    @JsonProperty(value = "form")
    @XmlElement(name = "form")
    private CompanyForm form;

    public CompanyForm obtainForm() {
        if (this.form == null) {
            this.form = new CompanyForm();
        }
        return this.form;
    }

    public void setForm(CompanyForm form) {
        this.form = form;
    }

    // participants



    @Column(name="advertProtection")
    @JsonProperty(value = "advertProtection")
    @XmlElement(name = "advertProtection")
    private boolean advertProtection;

    public boolean hasAdvertProtection() {
        return this.advertProtection;
    }

    public void setAdvertProtection(boolean advertProtection) {
        this.advertProtection = advertProtection;
    }


    private LifeCycleEmbed lifeCycle;

    public LifeCycleEmbed obtainLifeCycle() {
        if (this.lifeCycle == null) {
            this.lifeCycle = new LifeCycleEmbed();
        }
        return this.lifeCycle;
    }

    public void setLifeCycle(LifeCycleEmbed lifeCycle) {
        this.lifeCycle = lifeCycle;
    }







    private YearlyEmployeeNumbersEmbed yearlyEmployeeNumbers;

    public YearlyEmployeeNumbersEmbed obtainYearlyEmployeeNumbers() {
        if (this.yearlyEmployeeNumbers == null) {
            this.yearlyEmployeeNumbers = new YearlyEmployeeNumbersEmbed();
        }
        return this.yearlyEmployeeNumbers;
    }

    public void setYearlyEmployeeNumbers(YearlyEmployeeNumbersEmbed yearlyEmployeeNumbers) {
        this.yearlyEmployeeNumbers = yearlyEmployeeNumbers;
    }


    private QuarterlyEmployeeNumbersEmbed quarterlyEmployeeNumbers;

    public QuarterlyEmployeeNumbersEmbed obtainQuarterlyEmployeeNumbers() {
        if (this.quarterlyEmployeeNumbers == null) {
            this.quarterlyEmployeeNumbers = new QuarterlyEmployeeNumbersEmbed();
        }
        return this.quarterlyEmployeeNumbers;
    }

    public void setQuarterlyEmployeeNumbers(QuarterlyEmployeeNumbersEmbed quarterlyEmployeeNumbers) {
        this.quarterlyEmployeeNumbers = quarterlyEmployeeNumbers;
    }
}
