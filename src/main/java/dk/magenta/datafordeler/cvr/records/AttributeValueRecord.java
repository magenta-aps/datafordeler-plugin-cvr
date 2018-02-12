package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantBaseData;
import org.hibernate.Session;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Record for Company, CompanyUnit or Participant attribute values.
 */
@Entity
@Table(name = "cvr_record_attribute_value")
public class AttributeValueRecord extends CvrBitemporalRecord {

    @Column
    @JsonProperty(value = "vaerdi")
    private String value;


    public static final String DB_FIELD_ATTRIBUTE = "attribute";
    public static final String IO_FIELD_ATTRIBUTE = "attribut";

    @ManyToOne(targetEntity = AttributeRecord.class)
    @JsonIgnore
    private AttributeRecord attribute;

    public void setAttribute(AttributeRecord attribute) {
        this.attribute = attribute;
    }



    @Override
    public void populateBaseData(CompanyBaseData baseData, Session session) {
        baseData.addAttribute(
                this.attribute.getType(),
                this.attribute.getValueType(),
                this.value,
                this.attribute.getSequenceNumber()
        );
    }

    @Override
    public void populateBaseData(CompanyUnitBaseData baseData, Session session) {
        baseData.addAttribute(
                this.attribute.getType(),
                this.attribute.getValueType(),
                this.value,
                this.attribute.getSequenceNumber()
        );
    }

    @Override
    public void populateBaseData(ParticipantBaseData baseData, Session session) {
        baseData.addAttribute(
                this.attribute.getType(),
                this.attribute.getValueType(),
                this.value,
                this.attribute.getSequenceNumber()
        );
    }
}
