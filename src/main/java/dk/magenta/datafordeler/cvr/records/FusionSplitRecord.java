package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.DatabaseEntry;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Record for Company status data.
 */
@Entity
@Table(name = "cvr_record_fusion", indexes = {
        @Index(name = "cvr_record_fusion_company", columnList = FusionSplitRecord.DB_FIELD_COMPANY + DatabaseEntry.REF + "," + FusionSplitRecord.DB_FIELD_SPLIT),
})
public class FusionSplitRecord extends CvrNontemporalDataRecord {

    public static final String DB_FIELD_SPLIT = "split";

    @Column(name = DB_FIELD_SPLIT)
    @JsonIgnore
    private boolean split;

    public void setSplit(boolean split) {
        this.split = split;
    }



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
            nameRecord.setFusionSplitRecord(this);
        }
    }

    public static final String DB_FIELD_INCOMING = "incoming";
    public static final String IO_FIELD_INCOMING = "indgaaende";

    @OneToMany(mappedBy = OrganizationAttributeRecord.DB_FIELD_FUSION, targetEntity = OrganizationAttributeRecord.class, cascade = CascadeType.ALL)
    @Where(clause = OrganizationAttributeRecord.FUSION_OUTGOING+"=false")
    @JsonProperty(value = IO_FIELD_INCOMING)
    private Set<OrganizationAttributeRecord> incoming = new HashSet<>();

    public void setIncoming(Set<OrganizationAttributeRecord> incoming) {
        this.incoming = incoming;
        for (OrganizationAttributeRecord attributeRecord : incoming) {
            attributeRecord.setFusionSplitRecord(this);
            attributeRecord.setFusionOutgoing(false);
        }
    }

    public void addIncoming(OrganizationAttributeRecord attribute) {
        if (attribute != null && !this.incoming.contains(attribute)) {
            attribute.setFusionSplitRecord(this);
            attribute.setFusionOutgoing(false);
            this.incoming.add(attribute);
        }
    }

    public Set<OrganizationAttributeRecord> getIncoming() {
        return this.incoming;
    }



    public static final String DB_FIELD_OUTGOING = "outgoing";
    public static final String IO_FIELD_OUTGOING = "udgaaende";

    @OneToMany(mappedBy = OrganizationAttributeRecord.DB_FIELD_FUSION, targetEntity = OrganizationAttributeRecord.class, cascade = CascadeType.ALL)
    @Where(clause = OrganizationAttributeRecord.FUSION_OUTGOING+"=true")
    @JsonProperty(value = IO_FIELD_OUTGOING)
    private Set<OrganizationAttributeRecord> outgoing = new HashSet<>();

    public void setOutgoing(Set<OrganizationAttributeRecord> outgoing) {
        this.outgoing = outgoing;
        for (OrganizationAttributeRecord attributeRecord : outgoing) {
            attributeRecord.setFusionSplitRecord(this);
            attributeRecord.setFusionOutgoing(true);
        }
    }

    public void addOutgoing(OrganizationAttributeRecord attribute) {
        if (attribute != null && !this.outgoing.contains(attribute)) {
            attribute.setFusionSplitRecord(this);
            attribute.setFusionOutgoing(true);
            this.outgoing.add(attribute);
        }
    }

    public Set<OrganizationAttributeRecord> getOutgoing() {
        return this.outgoing;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FusionSplitRecord that = (FusionSplitRecord) o;
        return split == that.split &&
                organizationUnitNumber == that.organizationUnitNumber &&
                Objects.equals(name, that.name) &&
                Objects.equals(incoming, that.incoming) &&
                Objects.equals(outgoing, that.outgoing);
    }

    @Override
    public int hashCode() {
        return Objects.hash(split, organizationUnitNumber, name, incoming, outgoing);
    }
}
