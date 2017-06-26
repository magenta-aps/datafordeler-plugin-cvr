package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import org.hibernate.Session;

import java.util.List;

/**
 * Created by lars on 26-06-17.
 */
public class CompanyAttributeRecord extends CompanyBaseRecord {

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

    private List<CompanyAttributeValueRecord> values;

    @JsonProperty(value = "vaerdier")
    public void setValues(List<CompanyAttributeValueRecord> values) {
        for (CompanyAttributeValueRecord record : values) {
            record.setParent(this);
        }
        this.values = values;
    }

    public List<CompanyAttributeValueRecord> getValues() {
        return this.values;
    }

    @Override
    public void populateCompanyBaseData(CompanyBaseData baseData, QueryManager queryManager, Session session) {
        for (CompanyAttributeValueRecord record : values) {
            record.populateCompanyBaseData(baseData, queryManager, session);
        }
    }
}
