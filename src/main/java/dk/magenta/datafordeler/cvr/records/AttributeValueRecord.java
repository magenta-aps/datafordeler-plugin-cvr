package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantBaseData;
import org.hibernate.Session;

/**
 * Created by lars on 26-06-17.
 */
public class AttributeValueRecord extends CvrBaseRecord {

    @JsonProperty(value = "vaerdi")
    private String value;

    @JsonIgnore
    private AttributeRecord parent;

    public void setParent(AttributeRecord parent) {
        this.parent = parent;
    }

    @Override
    public void populateBaseData(CompanyBaseData baseData, QueryManager queryManager, Session session) {
        baseData.addAttribute(
                this.parent.getType(),
                this.parent.getValueType(),
                this.value,
                this.parent.getSequenceNumber()
        );
    }

    @Override
    public void populateBaseData(CompanyUnitBaseData baseData, QueryManager queryManager, Session session) {
        baseData.addAttribute(
                this.parent.getType(),
                this.parent.getValueType(),
                this.value,
                this.parent.getSequenceNumber()
        );
    }

    @Override
    public void populateBaseData(ParticipantBaseData baseData, QueryManager queryManager, Session session) {
        /*baseData.addAttribute(
                this.parent.getType(),
                this.parent.getValueType(),
                this.value,
                this.parent.getSekvensnummer()
        );*/
    }
}
