package dk.magenta.datafordeler.cvr.data.company;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.Identification;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitEntity;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantEntity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 16-05-17.
 */
@Table(name = "cvr_company_text")
public class CompanyTextData extends CompanyData {

    public enum Type {
        NAME,
        EMAIL,
        PHONE,
        FAX
    }

    @Column
    private String text;

    @Column
    private Type type;

    public String getFieldName() {
        return this.type.name();
    }

    @JsonAnyGetter
    public Map<String, Object> getFields() {
        HashMap<String, Object> fields = new HashMap<>();
        fields.put(this.getFieldName(), this.text);
        return fields;
    }

    @JsonAnySetter
    public void setFields(Map<String, Object> fields) {
        this.text = (String) fields.get(this.getFieldName());
    }

    /**
     * Return a map of attributes, including those from the superclass
     * @return
     */
    @Override
    public Map<String, Object> asMap() {
        return this.getFields();
    }

}
