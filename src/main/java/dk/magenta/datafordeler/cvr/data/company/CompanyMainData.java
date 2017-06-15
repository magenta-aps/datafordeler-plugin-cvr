package dk.magenta.datafordeler.cvr.data.company;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.Identification;
import dk.magenta.datafordeler.cvr.data.DetailData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitEntity;
import dk.magenta.datafordeler.cvr.data.embeddable.LifeCycleEmbed;
import dk.magenta.datafordeler.cvr.data.embeddable.QuarterlyEmployeeNumbersEmbed;
import dk.magenta.datafordeler.cvr.data.embeddable.YearlyEmployeeNumbersEmbed;

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

    }




    /**
     * Return a map of attributes, including those from the superclass
     * @return
     */
    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
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



    @OneToMany
    @JsonProperty(value = "enheder")
    @XmlElement(name = "enheder")
    private Collection<CompanyUnitEntity> units;






    @Embedded
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



    @Embedded
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
