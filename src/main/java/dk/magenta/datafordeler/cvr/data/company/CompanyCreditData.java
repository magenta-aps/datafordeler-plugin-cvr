package dk.magenta.datafordeler.cvr.data.company;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.*;

/**
 * Created by lars on 16-05-17.
 */
@Entity
@Table(name = "cvr_company_credit")
public class CompanyCreditData extends CompanySingleData<String> {

    public CompanyCreditData() {
    }

    @ManyToMany
    private Set<CompanyBaseData> companyBases = new HashSet<>();

    @JsonAnyGetter
    public Map<String, Object> asMap() {
        return Collections.singletonMap("text", this.getData());
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
