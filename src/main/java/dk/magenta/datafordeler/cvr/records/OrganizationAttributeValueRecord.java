package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.DatabaseEntry;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantBaseData;
import org.hibernate.Session;

import javax.persistence.*;

/**
 * Record for Company, CompanyUnit or Participant attribute values.
 */
@Entity
@Table(name = "cvr_record_organization_attribute_value", indexes = {
        @Index(name = "cvr_record_organization_attribute_value_attribute", columnList = OrganizationAttributeValueRecord.DB_FIELD_ATTRIBUTE + DatabaseEntry.REF)
})
public class OrganizationAttributeValueRecord extends BaseAttributeValueRecord {

    public static final String DB_FIELD_ATTRIBUTE = "attribute";
    public static final String IO_FIELD_ATTRIBUTE = "attribut";

    @ManyToOne(targetEntity = OrganizationAttributeRecord.class)
    @JsonIgnore
    private OrganizationAttributeRecord attribute;

    public void setAttribute(OrganizationAttributeRecord attribute) {
        this.attribute = attribute;
    }

}
