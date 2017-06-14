package dk.magenta.datafordeler.cvr.data.embeddable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.Identification;
import dk.magenta.datafordeler.cvr.data.CvrData;
import dk.magenta.datafordeler.cvr.data.company.CompanyEffect;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitEntity;
import dk.magenta.datafordeler.cvr.data.unversioned.CompanyForm;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 16-05-17.
 */
@Embeddable
public class CompanyMainEmbed {

    public CompanyMainEmbed() {
    }

    /**
     * Embed common attributes
     */
    //private CompanySharedData companySharedData;






    /**
     * Return a map of attributes, including those from the superclass
     * @return
     */
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        //map.put("advertProtection", this.advertProtection);
        //map.put("lifeCycle", this.lifeCycle);
        //map.put("form", this.form);
//        if (this.yearlyEmployeeNumbers != null) {
//            map.put("yearlyEmployeeNumbers", yearlyEmployeeNumbers);
//        }
//        if (this.quarterlyEmployeeNumbers != null) {
//            map.put("quarterlyEmployeeNumbers", quarterlyEmployeeNumbers);
//        }
        return map;
    }

    /**
     * Return a map of references (omit this method if there are no references in the class)
     * @return
     */
    @JsonIgnore
    public HashMap<String, Identification> getReferences() {
        return new HashMap<String, Identification>();
    }

    /**
     * Update this object from a map of references (omit this method if there are no references in the class)
     * @return
     */
    public void updateReferences(HashMap<String, Identification> references) {
    }
}
