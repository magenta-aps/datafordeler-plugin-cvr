package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.Effect;
import dk.magenta.datafordeler.core.database.Registration;
import org.hibernate.Session;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Record for one participant on a Company or CompanyUnit
 */
@Entity
@Table(name = "cvr_record_participant_relation_office_unit")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OfficeRelationUnitRecord extends CvrBitemporalRecord {

    public static final String DB_FIELD_UNITNUMBER = "unitNumber";
    public static final String IO_FIELD_UNITNUMBER = "enhedsNummer";

    @Column(name = DB_FIELD_UNITNUMBER)
    @JsonProperty(value = IO_FIELD_UNITNUMBER)
    public long unitNumber;



    public static final String DB_FIELD_UNITTYPE = "unitType";
    public static final String IO_FIELD_UNITTYPE = "enhedsType";

    @Column(name = DB_FIELD_UNITTYPE)
    @JsonProperty(value = IO_FIELD_UNITTYPE)
    public String unitType;



    public static final String DB_FIELD_BUSINESS_KEY = "businessKey";
    public static final String IO_FIELD_BUSINESS_KEY = "forretningsnoegle";

    @Column(name = DB_FIELD_BUSINESS_KEY)
    @JsonProperty(value = IO_FIELD_BUSINESS_KEY)
    public long businessKey;



    public static final String IO_FIELD_NAME = "navne";

    @OneToMany(mappedBy = OrganizationNameRecord.DB_FIELD_OFFICE_UNIT, targetEntity = OrganizationNameRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = IO_FIELD_NAME)
    public Set<OrganizationNameRecord> names = new HashSet<>();

    public Set<OrganizationNameRecord> getNames() {
        return this.names;
    }

    public void setNames(Set<OrganizationNameRecord> names) {
        this.names = names;
        for (OrganizationNameRecord nameRecord : names) {
            nameRecord.setOfficeUnitRecord(this);
        }
    }

    public void addName(OrganizationNameRecord record) {
        if (record != null && !this.names.contains(record)) {
            record.setOfficeUnitRecord(this);
            this.names.add(record);
        }
    }



    public static final String DB_FIELD_LOCATION_ADDRESS = "locationAddress";
    public static final String IO_FIELD_LOCATION_ADDRESS = "beliggenhedsadresse";

    @OneToMany(mappedBy = AddressRecord.DB_FIELD_OFFICE_UNIT, targetEntity = AddressRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = IO_FIELD_LOCATION_ADDRESS)
    public Set<AddressRecord> locationAddress;

    public void setLocationAddress(Set<AddressRecord> locationAddress) {
        for (AddressRecord record : locationAddress) {
            record.setType(AddressRecord.TYPE_LOCATION);
            record.setOfficeUnitRecord(this);
        }
        this.locationAddress = locationAddress;
    }

    public void addLocationAddress(AddressRecord record) {
        if (record != null && !this.locationAddress.contains(record)) {
            record.setType(AddressRecord.TYPE_LOCATION);
            record.setOfficeUnitRecord(this);
            this.locationAddress.add(record);
        }
    }

    public Set<AddressRecord> getLocationAddress() {
        return this.locationAddress;
    }

    public void wire(Session session) {
        for (AddressRecord address : this.locationAddress) {
            address.wire(session);
        }
    }

}
