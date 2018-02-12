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
public class OrganizationAttributeValueRecord extends CvrBitemporalRecord {

    @Column
    @JsonProperty(value = "vaerdi")
    private String value;


    public static final String DB_FIELD_ATTRIBUTE = "attribute";
    public static final String IO_FIELD_ATTRIBUTE = "attribut";

    @ManyToOne(targetEntity = AttributeRecord.class)
    @JsonIgnore
    private OrganizationAttributeRecord attribute;

    public void setAttribute(OrganizationAttributeRecord attribute) {
        this.attribute = attribute;
    }

}
