package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantBaseData;
import org.hibernate.Session;

/**
 * Record for Company, CompanyUnit or Participant attribute values.
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
    public void populateBaseData(CompanyBaseData baseData, Session session) {
        baseData.addAttribute(
                this.parent.getType(),
                this.parent.getValueType(),
                this.value,
                this.parent.getSequenceNumber()
        );
    }

    @Override
    public void populateBaseData(CompanyUnitBaseData baseData, Session session) {
        baseData.addAttribute(
                this.parent.getType(),
                this.parent.getValueType(),
                this.value,
                this.parent.getSequenceNumber()
        );
    }

    @Override
    public void populateBaseData(ParticipantBaseData baseData, Session session) {
        baseData.addAttribute(
                this.parent.getType(),
                this.parent.getValueType(),
                this.value,
                this.parent.getSequenceNumber()
        );
    }
}
