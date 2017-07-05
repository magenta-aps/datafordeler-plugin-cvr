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
@Table(
    name = "cvr_company_boolean",
    indexes = {@Index(name = "companyTypeBoolean", columnList = "type")}
)
public class BooleanData extends SingleData<Boolean> {

    public enum Type {
        REKLAME_BESKYTTELSE,
        ER_PRIMAER_ENHED,
    }

    public BooleanData() {
    }

    public BooleanData(Type type) {
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
        fields.put(this.getFieldName(), this.getData());
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

}
