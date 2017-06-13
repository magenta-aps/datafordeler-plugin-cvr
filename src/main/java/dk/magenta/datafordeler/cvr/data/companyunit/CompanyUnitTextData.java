package dk.magenta.datafordeler.cvr.data.companyunit;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.magenta.datafordeler.core.database.DatabaseEntry;
import dk.magenta.datafordeler.cvr.data.CvrData;
import dk.magenta.datafordeler.cvr.data.company.CompanyEffect;
import dk.magenta.datafordeler.cvr.data.embeddable.CompanyTextEmbed;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Map;

/**
 * Created by lars on 16-05-17.
 */
@Entity
@Table(name = "cvr_companyunit_text")
public class CompanyUnitTextData extends DatabaseEntry {

    private CompanyTextEmbed companyText = new CompanyTextEmbed();

    public CompanyUnitTextData() {
    }

    public CompanyUnitTextData(CompanyTextEmbed.Type type) {
        this.companyText.setType(type);
    }

    @JsonAnyGetter
    public Map<String, Object> asMap() {
        return this.companyText.asMap();
    }

    /*@JsonAnySetter
    public void setFields(Map<String, Object> fields) {
        this.companyText.setFields(fields);
    }*/

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
