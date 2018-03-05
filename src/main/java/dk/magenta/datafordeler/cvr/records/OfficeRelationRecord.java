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




    public static final String DB_FIELD_ATTRIBUTES = "attributes";
    public static final String IO_FIELD_ATTRIBUTES = "attributter";

    @OneToMany(targetEntity = AttributeRecord.class, mappedBy = AttributeRecord.DB_FIELD_OFFICE, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty(value = IO_FIELD_ATTRIBUTES)
    private Set<AttributeRecord> attributes = new HashSet<>();

    public void setAttributes(Set<AttributeRecord> attributes) {
        this.attributes = attributes;
        for (AttributeRecord attributeRecord : attributes) {
            attributeRecord.setOfficeRelationRecord(this);
        }
    }

    public void addAttribute(AttributeRecord attribute) {
        if (attribute != null && !this.attributes.contains(attribute)) {
            attribute.setOfficeRelationRecord(this);
            this.attributes.add(attribute);
        }
    }

    public Set<AttributeRecord> getAttributes() {
        return this.attributes;
    }

}
