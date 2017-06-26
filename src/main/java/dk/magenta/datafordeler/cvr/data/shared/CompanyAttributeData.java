package dk.magenta.datafordeler.cvr.data.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.cvr.data.DetailData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 26-06-17.
 */
@Entity
@Table(name = "cvr_company_attributes")
public class CompanyAttributeData extends DetailData {

    @JsonProperty
    @XmlElement
    @Column
    private int sequenceNumber;

    public int getSequenceNumber() {
        return this.sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    @JsonProperty
    @XmlElement
    @Column
    private String type;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty
    @XmlElement
    @Column
    private String valueType;

    public String getValueType() {
        return this.valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    @JsonProperty
    @XmlElement
    @Column
    private String value;

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("sequenceNumber", this.sequenceNumber);
        map.put("type", this.type);
        map.put("valueType", this.valueType);
        map.put("value", this.value);
        return map;
    }
}
