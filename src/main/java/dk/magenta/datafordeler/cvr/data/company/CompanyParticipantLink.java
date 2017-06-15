package dk.magenta.datafordeler.cvr.data.company;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.cvr.data.DetailData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by lars on 15-06-17.
 */
@Entity
@Table(name = "cvr_company_participantlink")
public class CompanyParticipantLink extends CompanySingleData<Integer> {

    @ManyToMany
    private Set<CompanyBaseData> companyBases = new HashSet<>();

    @ManyToMany
    private Set<CompanyUnitBaseData> companyUnitBases = new HashSet<>();

    @JsonProperty
    public int getParticipantNumber() {
        return this.getData();
    }

    @Override
    public Map<String, Object> asMap() {
        return Collections.singletonMap("participantNumber", this.getData());
    }
}
