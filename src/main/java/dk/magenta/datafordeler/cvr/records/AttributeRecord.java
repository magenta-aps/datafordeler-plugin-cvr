package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantBaseData;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Record for Company, CompanyUnit or Participant attributes.
 * Attributes with a given name may have more than one value, so values are
 * kept in {@link dk.magenta.datafordeler.cvr.records.AttributeValueRecord}
 */
@Entity
@Table(name = "cvr_record_attribute")
public class AttributeRecord extends CvrNontemporalDataRecord {


    @Column
    @JsonProperty(value = "sekvensnr")
    private int sequenceNumber;

    public int getSequenceNumber() {
        return this.sequenceNumber;
    }


    @Column
    @JsonProperty(value = "type")
    private String type;

    public String getType() {
        return this.type;
    }


    @Column
    @JsonProperty(value = "vaerditype")
    private String valueType;

    public String getValueType() {
        return this.valueType;
    }


    @OneToMany(mappedBy = AttributeValueRecord.DB_FIELD_ATTRIBUTE, targetEntity = AttributeValueRecord.class, cascade = CascadeType.ALL)
    private Set<AttributeValueRecord> values;

    @JsonProperty(value = "vaerdier")
    public void setValues(Collection<AttributeValueRecord> values) {
        for (AttributeValueRecord record : values) {
            record.setAttribute(this);
        }
        this.values = new HashSet<>(values);
    }

    public Set<AttributeValueRecord> getValues() {
        return this.values;
    }



    @Override
    public void populateBaseData(CompanyBaseData baseData, Session session) {
        for (AttributeValueRecord record : values) {
            record.populateBaseData(baseData, session);
        }
    }

    @Override
    public void populateBaseData(CompanyUnitBaseData baseData, Session session) {
        for (AttributeValueRecord record : values) {
            record.populateBaseData(baseData, session);
        }
    }

    @Override
    public void populateBaseData(ParticipantBaseData baseData, Session session) {
        for (AttributeValueRecord record : values) {
            record.populateBaseData(baseData, session);
        }
    }
}
