package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.Session;

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
@Table(name = "cvr_record_participant_relation_participant")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParticipantRelationRecord extends CvrBitemporalRecord {

    public static final String DB_FIELD_UNITNUMBER = "unitNumber";
    public static final String IO_FIELD_UNITNUMBER = "enhedsNummer";

    @Column(name = DB_FIELD_UNITNUMBER)
    @JsonProperty(value = IO_FIELD_UNITNUMBER)
    private long unitNumber;

    public long getUnitNumber() {
        return this.unitNumber;
    }



    public static final String DB_FIELD_UNITTYPE = "unitType";
    public static final String IO_FIELD_UNITTYPE = "enhedstype";

    @Column(name = DB_FIELD_UNITTYPE)
    @JsonProperty(value = IO_FIELD_UNITTYPE)
    public String unitType;

    public String getUnitType() {
        return this.unitType;
    }



    public static final String DB_FIELD_NAME = "names";
    public static final String IO_FIELD_NAME = "navne";

    @OneToMany(targetEntity = NameRecord.class, mappedBy = NameRecord.DB_FIELD_PARTICIPANT_RELATION, cascade = CascadeType.ALL)
    @JsonProperty(value = IO_FIELD_NAME)
    private Set<NameRecord> names = new HashSet<>();

    public void setNames(Set<NameRecord> names) {
        this.names = names;
        for (NameRecord name : names) {
            name.setSecondary(false);
            name.setParticipantRelationRecord(this);
        }
    }

    public void addName(NameRecord record) {
        if (!this.names.contains(record)) {
            record.setSecondary(false);
            record.setParticipantRelationRecord(this);
            this.names.add(record);
        }
    }

    public Set<NameRecord> getNames() {
        return this.names;
    }



    public static final String DB_FIELD_LOCATION_ADDRESS = "locationAddress";
    public static final String IO_FIELD_LOCATION_ADDRESS = "beliggenhedsadresse";

    @OneToMany(targetEntity = AddressRecord.class, mappedBy = AddressRecord.DB_FIELD_PARTICIPANT_RELATION, cascade = CascadeType.ALL)
    @JsonProperty(value = IO_FIELD_LOCATION_ADDRESS)
    private Set<AddressRecord> locationAddress = new HashSet<>();

    public void setLocationAddress(Set<AddressRecord> locationAddress) {
        this.locationAddress = locationAddress;
        for (AddressRecord name : locationAddress) {
            name.setParticipantRelationRecord(this);
        }
    }

    public void addLocationAddress(AddressRecord record) {
        if (!this.locationAddress.contains(record)) {
            record.setParticipantRelationRecord(this);
            this.locationAddress.add(record);
        }
    }

    public Set<AddressRecord> getLocationAddress() {
        return this.locationAddress;
    }




    @OneToOne(targetEntity = CompanyParticipantRelationRecord.class, mappedBy = CompanyParticipantRelationRecord.DB_FIELD_PARTICIPANT_RELATION)
    @JsonIgnore
    private CompanyParticipantRelationRecord companyParticipantRelationRecord;

    public void setCompanyParticipantRelationRecord(CompanyParticipantRelationRecord companyParticipantRelationRecord) {
        this.companyParticipantRelationRecord = companyParticipantRelationRecord;
    }



    public UUID generateUUID() {
        String uuidInput = "participant:"+this.unitType+"/"+this.unitNumber;
        return UUID.nameUUIDFromBytes(uuidInput.getBytes());
    }

    @Override
    public OffsetDateTime getRegistrationFrom() {
        OffsetDateTime registrationFrom = super.getRegistrationFrom();
        if (registrationFrom == null) {
            registrationFrom = this.getLastUpdated();
        }
        return registrationFrom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ParticipantRelationRecord that = (ParticipantRelationRecord) o;
        return unitNumber == that.unitNumber &&
                Objects.equals(unitType, that.unitType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), unitNumber, unitType);
    }

    public void merge(ParticipantRelationRecord other) {
        if (other != null) {
            for (NameRecord name : other.getNames()) {
                this.addName(name);
            }
            for (AddressRecord address : other.getLocationAddress()) {
                this.addLocationAddress(address);
            }
        }
    }

    public void wire(Session session) {
        for (AddressRecord address : this.locationAddress) {
            address.wire(session);
        }
    }
}
