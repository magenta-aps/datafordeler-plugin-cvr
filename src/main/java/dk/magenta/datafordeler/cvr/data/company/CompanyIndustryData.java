package dk.magenta.datafordeler.cvr.data.company;

import dk.magenta.datafordeler.cvr.data.DetailData;
import dk.magenta.datafordeler.cvr.data.embeddable.IndustryEmbed;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Map;

/**
 * Created by lars on 09-06-17.
 */
@Entity
@Table(name="cvr_company_industry")
public class CompanyIndustryData extends DetailData {

    private IndustryEmbed industry;

    /**
     * Return a map of attributes, including those from the superclass
     * @return
     */
    @Override
    public Map<String, Object> asMap() {
        return this.industry.asMap();
    }

}
