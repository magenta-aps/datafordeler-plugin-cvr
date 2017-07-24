package dk.magenta.datafordeler.cvr.data.shared;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 16-05-17.
 */
@Entity
@Table(name = "cvr_company_contact", indexes = {
        @Index(name = "companyContactType", columnList = "type"),
        @Index(name = "companyContactData", columnList = "type, data")
})
public class ContactData extends SingleData<String> {

    public enum Type {
        NAME,
        EMAIL,
        PHONE,
        FAX,
        HOMEPAGE,
        MANDATORY_EMAIL
    }

    public ContactData() {
    }

    public ContactData(Type type) {
        this.setType(type);
    }

    @Column
    @JsonIgnore
    @XmlTransient
    private Type type;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Column
    @JsonProperty(value = "kontaktoplysning")
    @XmlElement(name = "kontaktoplysning")
    public String getData() {
        return super.getData();
    }

    @Column
    @JsonProperty("hemmelig")
    @XmlElement(name = "hemmelig")
    private boolean secret;

    public boolean isSecret() {
        return this.secret;
    }

    public void setSecret(boolean secret) {
        this.secret = secret;
    }

    public Map<String, Object> asMap() {
        HashMap<String, Object> fields = new HashMap<>();
        fields.put("data", this.getData());
        fields.put("secret", this.secret);
        fields.put("type", this.type);
        return fields;
    }

    /**
     * Return a map of attributes, including those from the superclass
     * @return
     */
    @JsonIgnore
    public Map<String, Object> databaseFields() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("data", this.getData());
        map.put("secret", this.secret);
        map.put("type", this.type);
        return map;
    }


}
