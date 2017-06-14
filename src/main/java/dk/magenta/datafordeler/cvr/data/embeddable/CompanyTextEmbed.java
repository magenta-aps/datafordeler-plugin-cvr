package dk.magenta.datafordeler.cvr.data.embeddable;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 16-05-17.
 */
@Embeddable
public class CompanyTextEmbed {

    public enum Type {
        NAME,
        EMAIL,
        PHONE,
        FAX
    }

    @Column
    @JsonIgnore
    private String text;

    @Column
    @JsonIgnore
    private Type type;

    public String getFieldName() {
        return this.type.name().toLowerCase();
    }

    @JsonAnyGetter
    public Map<String, Object> asMap() {
        HashMap<String, Object> fields = new HashMap<>();
        fields.put(this.getFieldName(), this.text);
        return fields;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Return a map of attributes, including those from the superclass
     * @return
     */
    @JsonIgnore
    public Map<String, Object> databaseFields() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("text", this.text);
        map.put("type", this.type);
        return map;
    }

}
