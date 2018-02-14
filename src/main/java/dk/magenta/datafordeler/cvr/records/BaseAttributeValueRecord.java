package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseAttributeValueRecord extends CvrBitemporalRecord {

    public static final String DB_FIELD_VALUE = "value";
    public static final String IO_FIELD_VALUE = "vaerdi";

    @Column(name = DB_FIELD_VALUE, length = 8000)
    @JsonProperty(value = IO_FIELD_VALUE)
    protected String value;

    @JsonProperty(value = IO_FIELD_VALUE)
    public void setValue(String value) {
        if (value != null && value.length() > 8000) {
            value = value.substring(0, 7999);
        }
        this.value = value;
    }

}
