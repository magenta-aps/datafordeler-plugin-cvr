package dk.magenta.datafordeler.cvr.data.shared;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 16-05-17.
 */
@Entity
@Table(name = "cvr_integer", indexes = {
        @Index(name = "companyIntegerData", columnList = "vaerdi")
})
public class IntegerData extends SingleData<Long> {

    public IntegerData() {
    }

    public Map<String, Object> asMap() {
        return null;
    }

    /**
     * Return a map of attributes, including those from the superclass
     * @return
     */
    @JsonIgnore
    public Map<String, Object> databaseFields() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("vaerdi", this.getVaerdi());
        return map;
    }

}
