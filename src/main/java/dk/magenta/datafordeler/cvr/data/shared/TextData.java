package dk.magenta.datafordeler.cvr.data.shared;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 16-05-17.
 */
@Entity
@Table(name = "cvr_text", indexes = {
        @Index(name = "companyTextType", columnList = "type"),
        @Index(name = "companyTextData", columnList = "type, vaerdi")
})
public class TextData extends SingleData<String> {

    public enum Type {
        NAVN,
        EMAILADRESSE,
        TELEFONNUMMER,
        TELEFAXNUMMER,
        TYPE
    }

    public TextData() {
    }

    public TextData(Type type) {
        this.setType(type);
    }

    @Column
    @JsonIgnore
    private Type type;

    public String getFieldName() {
        return this.type.name().toLowerCase();
    }

    @JsonAnyGetter
    public Map<String, Object> asMap() {
        HashMap<String, Object> fields = new HashMap<>();
        fields.put(this.getFieldName(), this.getVaerdi());
        return fields;
    }

    /**
     * Return a map of attributes, including those from the superclass
     * @return
     */
    @JsonIgnore
    public Map<String, Object> databaseFields() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("vaerdi", this.getVaerdi());
        map.put("type", this.type);
        return map;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

}
