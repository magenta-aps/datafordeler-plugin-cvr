package dk.magenta.datafordeler.cvr.data.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;

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
public class ParticipantLink extends SingleData<Integer> {

    @ManyToMany
    private Set<CompanyBaseData> companyBases = new HashSet<>();

    @ManyToMany
    private Set<CompanyUnitBaseData> companyUnitBases = new HashSet<>();

    @JsonProperty
    public int getDeltagernummer() {
        return this.getVaerdi();
    }

    @Override
    public Map<String, Object> asMap() {
        return Collections.singletonMap("vaerdi", this.getVaerdi());
    }
}
