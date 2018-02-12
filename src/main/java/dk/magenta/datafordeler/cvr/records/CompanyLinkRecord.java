package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import org.hibernate.Session;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Record for CompanyUnit associated CVR number.
 */
@Entity
@Table(name = "cvr_record_companyunit_link_company")
public class CompanyLinkRecord extends CvrBitemporalDataRecord {

    @JsonProperty(value = "cvrNummer")
    private int cvrNumber;

    @Override
    public void populateBaseData(CompanyUnitBaseData baseData, Session session) {
        baseData.addAssociatedCvrNumber(this.cvrNumber);
    }

}
