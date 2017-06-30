package dk.magenta.datafordeler.cvr.data.companyunit;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.magenta.datafordeler.cvr.data.shared.SingleData;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 16-05-17.
 */
@Entity
@Table(name = "cvr_companyunit_company")
public class CompanyUnitCvrData extends SingleData<Integer> {

    public CompanyUnitCvrData() {
    }

    public CompanyUnitCvrData(int cvr) {
        this.setData(cvr);
    }

    @JsonAnyGetter
    public Map<String, Object> asMap() {
        HashMap<String, Object> fields = new HashMap<>();
        fields.put("cvrNumber", this.getData());
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
        return map;
    }

}
