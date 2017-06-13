package dk.magenta.datafordeler.cvr.data.company;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.magenta.datafordeler.cvr.data.DetailData;
import dk.magenta.datafordeler.cvr.data.embeddable.CompanyTextEmbed;

import javax.persistence.*;
import java.util.Map;

/**
 * Created by lars on 16-05-17.
 */
@Entity
@Table(name = "cvr_company_text")
public class CompanyTextData extends DetailData {

    private CompanyTextEmbed companyText = new CompanyTextEmbed();

    public CompanyTextData() {
    }

    public CompanyTextData(CompanyTextEmbed.Type type) {
        this.companyText.setType(type);
    }

    @JsonAnyGetter
    public Map<String, Object> asMap() {
        return this.companyText.asMap();
    }

    public String getText() {
        return this.companyText.getText();
    }

    public void setText(String text) {
        this.companyText.setText(text);
    }

    public CompanyTextEmbed.Type getType() {
        return this.companyText.getType();
    }

    public void setType(CompanyTextEmbed.Type type) {
        this.companyText.setType(type);
    }

    /*@JsonAnySetter
    public void setFields(Map<String, Object> fields) {
        this.companyText.setFields(fields);
    }*/

    /**
     * Return a map of attributes, including those from the superclass
     * @return
     */
    //@Override
    @JsonIgnore
    public Map<String, Object> databaseFields() {
        return this.companyText.databaseFields();
    }

}
