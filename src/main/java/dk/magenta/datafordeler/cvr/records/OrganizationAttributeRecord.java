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
 * kept in {@link AttributeValueRecord}
 */
@Entity
@Table(name = "cvr_record_organization_attribute")
public class OrganizationAttributeRecord extends CvrNontemporalRecord {


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


    @OneToMany(mappedBy = OrganizationAttributeValueRecord.DB_FIELD_ATTRIBUTE, targetEntity = OrganizationAttributeValueRecord.class, cascade = CascadeType.ALL)
    private Set<OrganizationAttributeValueRecord> values;

    @JsonProperty(value = "vaerdier")
    public void setValues(Collection<OrganizationAttributeValueRecord> values) {
        for (OrganizationAttributeValueRecord record : values) {
            record.setAttribute(this);
        }
        this.values = new HashSet<>(values);
    }

    public Set<OrganizationAttributeValueRecord> getValues() {
        return this.values;
    }



    public static final String DB_FIELD_ORGANIZATION = "organizationRecord";
    public static final String IO_FIELD_ORGANIZATION = "organisation";

    @JsonIgnore
    @ManyToOne(targetEntity = OrganizationRecord.class)
    private OrganizationRecord organizationRecord;

    public OrganizationRecord getOrganizationRecord() {
        return this.organizationRecord;
    }

    public void setOrganizationRecord(OrganizationRecord organizationRecord) {
        this.organizationRecord = organizationRecord;
    }



    public static final String DB_FIELD_ORGANIZATION_MEMBERDATA = "organizationMemberdataRecord";
    public static final String IO_FIELD_ORGANIZATION_MEMBERDATA = "organisationMember";

    @JsonIgnore
    @ManyToOne(targetEntity = OrganizationMemberdataRecord.class)
    private OrganizationMemberdataRecord organizationMemberdataRecord;

    public void setOrganizationMemberdataRecord(OrganizationMemberdataRecord organizationMemberdataRecord) {
        this.organizationMemberdataRecord = organizationMemberdataRecord;
    }
}
