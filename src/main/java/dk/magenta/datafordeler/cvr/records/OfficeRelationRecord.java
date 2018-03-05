package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.Identification;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Record for one participant on a Company or CompanyUnit
 */
@Entity
@Table(name = "cvr_record_participant_relation_office")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OfficeRelationRecord extends CvrBitemporalRecord {

    public static final String DB_FIELD_UNITNUMBER = "unitNumber";
    public static final String IO_FIELD_UNITNUMBER = "enhedsNummer";

    @Column(name = DB_FIELD_UNITNUMBER)
    @JsonProperty(value = IO_FIELD_UNITNUMBER)
    public long unitNumber;






    @OneToMany(targetEntity = OrganizationAttributeRecord.class, mappedBy = OrganizationAttributeRecord.DB_FIELD_OFFICE, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrganizationAttributeRecord> attributes = new HashSet<>();

    public void addAttribute(OrganizationAttributeRecord attribute) {
        if (attribute != null && !this.attributes.contains(attribute)) {
            attribute.setOfficeRelationRecord(this);
            this.attributes.add(attribute);
        }
    }

    public Set<OrganizationAttributeRecord> getAttributes() {
        return this.attributes;
    }

}
