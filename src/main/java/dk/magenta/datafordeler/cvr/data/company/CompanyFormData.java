package dk.magenta.datafordeler.cvr.data.company;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.cvr.data.DetailData;
import dk.magenta.datafordeler.cvr.data.unversioned.CompanyForm;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 16-05-17.
 */
@Entity
@Table(name="cvr_company_form")
public class CompanyFormData extends DetailData {

    /**
     * Return a map of attributes, including those from the superclass
     * @return
     */
    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("form", this.form);
        return map;
    }


    @ManyToOne
    @JsonProperty(value = "form")
    @XmlElement(name = "form")
    private CompanyForm form;

    public CompanyForm getForm() {
        return this.form;
    }

    public void setForm(CompanyForm form) {
        this.form = form;
    }

}
