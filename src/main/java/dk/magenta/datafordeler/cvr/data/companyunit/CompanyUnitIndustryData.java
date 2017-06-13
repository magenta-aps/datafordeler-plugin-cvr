package dk.magenta.datafordeler.cvr.data.companyunit;

import dk.magenta.datafordeler.core.database.DatabaseEntry;
import dk.magenta.datafordeler.cvr.data.CvrData;
import dk.magenta.datafordeler.cvr.data.company.CompanyEffect;
import dk.magenta.datafordeler.cvr.data.embeddable.IndustryEmbed;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Map;

/**
 * Created by lars on 09-06-17.
 */
@Entity
@Table(name="cvr_companyunit_industry")
public class CompanyUnitIndustryData extends DatabaseEntry {

    private IndustryEmbed industry;

    /**
     * Return a map of attributes, including those from the superclass
     * @return
     */
    //@Override
    public Map<String, Object> asMap() {
        return this.industry.asMap();
    }

}
