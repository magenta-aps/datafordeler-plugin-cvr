package dk.magenta.datafordeler.cvr.data.shared;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 16-05-17.
 */
@Entity
@Table(name = "cvr_company_contact", indexes = {
        @Index(name = "type", columnList = "type"),
        @Index(name = "data", columnList = "type, data")
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
    private Type type;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Column
    @JsonIgnore
    private String data;

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Column
    @JsonIgnore
    private boolean secret;

    public boolean isSecret() {
        return this.secret;
    }

    public void setSecret(boolean secret) {
        this.secret = secret;
    }

    public String getFieldName() {
        return this.type.name().toLowerCase();
    }

    @JsonAnyGetter
    public Map<String, Object> asMap() {
        HashMap<String, Object> fields = new HashMap<>();
        fields.put(this.getFieldName(), this.getData());
        fields.put("secret", this.isSecret());
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
        map.put("type", this.type);
        return map;
    }


}
