package dk.magenta.datafordeler.cvr.data.company;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.magenta.datafordeler.cvr.data.shared.SingleData;

import javax.persistence.*;
import java.util.*;

/**
 * Created by lars on 16-05-17.
 */
@Entity
@Table(name = "cvr_company_credit", indexes = {
        @Index(name = "companyCreditData", columnList = "vaerdi"),
},
uniqueConstraints = {
        @UniqueConstraint(name = "vaerdi", columnNames = "vaerdi")
})
public class CompanyCreditData extends SingleData<String> {

    public CompanyCreditData() {
    }

    @ManyToMany
    private Set<CompanyBaseData> companyBases = new HashSet<>();

    @JsonAnyGetter
    public Map<String, Object> asMap() {
        return Collections.singletonMap("text", this.getVaerdi());
    }

    /**
     * Return a map of attributes, including those from the superclass
     * @return
     */
    @JsonIgnore
    public Map<String, Object> databaseFields() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("data", this.getVaerdi());
        return map;
    }

}
