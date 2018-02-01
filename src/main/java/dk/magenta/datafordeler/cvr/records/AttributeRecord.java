package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.cvr.data.Bitemporality;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantBaseData;
import org.hibernate.Session;

import java.util.List;

/**
 * Record for Company, CompanyUnit or Participant attributes.
 * Attributes with a given name may have more than one value, so values are
 * kept in {@link dk.magenta.datafordeler.cvr.records.AttributeValueRecord}
 */
public class AttributeRecord extends CvrBaseRecord {

    @JsonProperty(value = "sekvensnr")
    private int sequenceNumber;

    public int getSequenceNumber() {
        return this.sequenceNumber;
    }

    @JsonProperty(value = "type")
    private String type;

    public String getType() {
        return this.type;
    }

    @JsonProperty(value = "vaerditype")
    private String valueType;

    public String getValueType() {
        return this.valueType;
    }

    private List<AttributeValueRecord> values;

    @JsonProperty(value = "vaerdier")
    public void setValues(List<AttributeValueRecord> values) {
        for (AttributeValueRecord record : values) {
            record.setParent(this);
        }
        this.values = values;
    }

    public List<AttributeValueRecord> getValues() {
        return this.values;
    }

    @Override
    public void populateBaseData(CompanyBaseData baseData, Session session, Bitemporality bitemporality) {
        for (AttributeValueRecord record : values) {
            record.populateBaseData(baseData, session, );
        }
    }

    @Override
    public void populateBaseData(CompanyUnitBaseData baseData, Session session, Bitemporality bitemporality) {
        for (AttributeValueRecord record : values) {
            record.populateBaseData(baseData, session, );
        }
    }

    @Override
    public void populateBaseData(ParticipantBaseData baseData, Session session, Bitemporality bitemporality) {
        for (AttributeValueRecord record : values) {
            record.populateBaseData(baseData, session, );
        }
    }
}
