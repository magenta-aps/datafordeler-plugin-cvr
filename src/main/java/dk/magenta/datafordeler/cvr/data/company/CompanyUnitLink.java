package dk.magenta.datafordeler.cvr.data.company;

import dk.magenta.datafordeler.cvr.data.DetailData;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by lars on 15-06-17.
 */
@Entity
@Table(name = "cvr_company_unitlink")
public class CompanyUnitLink extends DetailData {

    @Column(insertable = true, updatable = false, nullable = false)
    private int pNummer;

    public int getpNummer() {
        return this.pNummer;
    }

    public void setpNummer(int pNummer) {
        this.pNummer = pNummer;
    }

    @ManyToMany
    private Set<CompanyBaseData> companyBases = new HashSet<>();

    @Override
    public Map<String, Object> asMap() {
        return Collections.singletonMap("pNummer", this.pNummer);
    }
}
