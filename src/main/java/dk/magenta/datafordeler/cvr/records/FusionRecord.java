package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.DatabaseEntry;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.unversioned.CompanyStatus;
import org.hibernate.Session;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Set;

/**
 * Record for Company status data.
 */
@Entity
@Table(name = "cvr_record_fusion", indexes = {
        @Index(name = "cvr_record_fusion_company", columnList = FusionRecord.DB_FIELD_COMPANY + DatabaseEntry.REF),
})
public class FusionRecord extends CvrNontemporalDataRecord {

    public static final String DB_FIELD_ORGANIZATION_UNITNUMBER = "organizationUnitNumber";
    public static final String IO_FIELD_ORGANIZATION_UNITNUMBER = "enhedsNummerOrganisation";

    @Column(name = DB_FIELD_ORGANIZATION_UNITNUMBER)
    @JsonProperty(value = IO_FIELD_ORGANIZATION_UNITNUMBER)
    private long organizationUnitNumber;

    public long getOrganizationUnitNumber() {
        return this.organizationUnitNumber;
    }

    public void setOrganizationUnitNumber(long organizationUnitNumber) {
        this.organizationUnitNumber = organizationUnitNumber;
    }



    public static final String DB_FIELD_ORGANIZATION_NAME = "name";
    public static final String IO_FIELD_ORGANIZATION_NAME = "organisationsNavn";


    @OneToMany(mappedBy = OrganizationNameRecord.DB_FIELD_FUSION, targetEntity = OrganizationNameRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = IO_FIELD_ORGANIZATION_NAME)
    private Set<OrganizationNameRecord> name;

    public Set<OrganizationNameRecord> getName() {
        return this.name;
    }

    public void setName(Set<OrganizationNameRecord> name) {
        this.name = name;
        for (OrganizationNameRecord nameRecord : name) {
            nameRecord.setFusionRecord(this);
        }
    }

    public static final String DB_FIELD_INCOMING = "incoming";
    public static final String IO_FIELD_INCOMING = "indgaaende";

    @OneToMany(mappedBy = OrganizationAttributeRecord.DB_FIELD_FUSION, targetEntity = OrganizationAttributeRecord.class, cascade = CascadeType.ALL)
    @Where(clause = OrganizationAttributeRecord.FUSION_OUTGOING+"=false")
    @JsonProperty(value = IO_FIELD_INCOMING)
    private Set<OrganizationAttributeRecord> incoming;

    public void setIncoming(Set<OrganizationAttributeRecord> incoming) {
        this.incoming = incoming;
        for (OrganizationAttributeRecord attributeRecord : incoming) {
            attributeRecord.setFusionRecord(this);
            attributeRecord.setFusionOutgoing(false);
        }
    }



    public static final String DB_FIELD_OUTGOING = "outgoing";
    public static final String IO_FIELD_OUTGOING = "udgaaende";

    @OneToMany(mappedBy = OrganizationAttributeRecord.DB_FIELD_FUSION, targetEntity = OrganizationAttributeRecord.class, cascade = CascadeType.ALL)
    @Where(clause = OrganizationAttributeRecord.FUSION_OUTGOING+"=true")
    @JsonProperty(value = IO_FIELD_OUTGOING)
    private Set<OrganizationAttributeRecord> outgoing;

    public void setOutgoing(Set<OrganizationAttributeRecord> outgoing) {
        this.outgoing = outgoing;
        for (OrganizationAttributeRecord attributeRecord : outgoing) {
            attributeRecord.setFusionRecord(this);
            attributeRecord.setFusionOutgoing(true);
        }
    }
}
