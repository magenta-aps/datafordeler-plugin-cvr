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
        @Index(name = "companyCreditData", columnList = "value"),
},
uniqueConstraints = {
        @UniqueConstraint(name = "value", columnNames = "value")
})
public class CompanyCreditData extends SingleData<String> {

    public CompanyCreditData() {
    }

    @ManyToMany
    private Set<CompanyBaseData> companyBases = new HashSet<>();

    @JsonAnyGetter
    public Map<String, Object> asMap() {
        return Collections.singletonMap("text", this.getValue());
    }

}
