package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.shared.CompanyAttributeData;
import org.hibernate.Session;

/**
 * Created by lars on 26-06-17.
 */
public class CompanyAttributeValueRecord extends CompanyBaseRecord {

    @JsonProperty(value = "vaerdi")
    private String value;

    @JsonIgnore
    private CompanyAttributeRecord parent;

    public void setParent(CompanyAttributeRecord parent) {
        this.parent = parent;
    }

    @Override
    public void populateCompanyBaseData(CompanyBaseData baseData, QueryManager queryManager, Session session) {
        baseData.addAttribute(
                this.parent.getType(),
                this.parent.getValueType(),
                this.value,
                this.parent.getSequenceNumber()
        );
    }
}
