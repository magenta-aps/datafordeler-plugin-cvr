package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.DatabaseEntry;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantBaseData;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantEntity;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantType;
import org.hibernate.Session;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Base record for Participant data, parsed from JSON into a tree of objects
 * with this class at the base.
 */
@Entity
@Table(name="cvr_record_participantrecord")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParticipantRecord extends CvrEntityRecord {

    public static final String DB_FIELD_UNIT_NUMBER = "unitNumber";
    public static final String IO_FIELD_UNIT_NUMBER = "enhedsNummer";

    @Column(name = DB_FIELD_UNIT_NUMBER)
    @JsonProperty(value = IO_FIELD_UNIT_NUMBER)
    private long unitNumber;

    public long getUnitNumber() {
        return this.unitNumber;
    }



    public static final String DB_FIELD_UNIT_TYPE = "unitType";
    public static final String IO_FIELD_UNIT_TYPE = "enhedstype";

    @Column(name = DB_FIELD_UNIT_TYPE)
    @JsonProperty(value = IO_FIELD_UNIT_TYPE)
    private String unitType;

    public String getUnitType() {
        return this.unitType;
    }



    public static final String DB_FIELD_NAMES = "names";
    public static final String IO_FIELD_NAMES = "navne";

    @OneToMany(mappedBy = NameRecord.DB_FIELD_PARTICIPANT, targetEntity = NameRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = IO_FIELD_NAMES)
    public Set<NameRecord> names;

    public void setNames(Set<NameRecord> names) {
        this.names = names;
        for (NameRecord record : names) {
            record.setParticipantRecord(this);
        }
    }



    public static final String DB_FIELD_LOCATION_ADDRESS = "locationAddress";
    public static final String IO_FIELD_LOCATION_ADDRESS = "beliggenhedsadresse";

    @OneToMany(mappedBy = AddressRecord.DB_FIELD_PARTICIPANT, targetEntity = AddressRecord.class, cascade = CascadeType.ALL)
    @Where(clause = AddressRecord.DB_FIELD_TYPE+"="+AddressRecord.TYPE_LOCATION)
    @JsonProperty(value = IO_FIELD_LOCATION_ADDRESS)
    public Set<AddressRecord> locationAddress;

    public void setLocationAddress(Set<AddressRecord> locationAddress) {
        for (AddressRecord record : locationAddress) {
            record.setType(AddressRecord.TYPE_LOCATION);
            record.setParticipantRecord(this);
        }
        this.locationAddress = locationAddress;
    }



    public static final String DB_FIELD_POSTAL_ADDRESS = "postalAddress";
    public static final String IO_FIELD_POSTAL_ADDRESS = "postadresse";

    @OneToMany(mappedBy = AddressRecord.DB_FIELD_PARTICIPANT, targetEntity = AddressRecord.class, cascade = CascadeType.ALL)
    @Where(clause = AddressRecord.DB_FIELD_TYPE+"="+AddressRecord.TYPE_POSTAL)
    @JsonProperty(value = IO_FIELD_POSTAL_ADDRESS)
    public Set<AddressRecord> postalAddress;

    public void setPostalAddress(Set<AddressRecord> postalAddress) {
        for (AddressRecord record : postalAddress) {
            record.setType(AddressRecord.TYPE_POSTAL);
            record.setParticipantRecord(this);
        }
        this.postalAddress = postalAddress;
    }



    public static final String DB_FIELD_BUSINESS_ADDRESS = "businessAddress";
    public static final String IO_FIELD_BUSINESS_ADDRESS = "forretningsadresse";

    @OneToMany(mappedBy = AddressRecord.DB_FIELD_PARTICIPANT, targetEntity = AddressRecord.class, cascade = CascadeType.ALL)
    @Where(clause = AddressRecord.DB_FIELD_TYPE+"="+AddressRecord.TYPE_BUSINESS)
    @JsonProperty(value = IO_FIELD_BUSINESS_ADDRESS)
    public Set<AddressRecord> businessAddress;

    public void setBusinessAddress(Set<AddressRecord> businessAddress) {
        for (AddressRecord record : businessAddress) {
            record.setType(AddressRecord.TYPE_BUSINESS);
            record.setParticipantRecord(this);
        }
        this.businessAddress = businessAddress;
    }



    public static final String DB_FIELD_PHONE = "phoneNumber";
    public static final String IO_FIELD_PHONE = "telefonNummer";

    @OneToMany(mappedBy = ContactRecord.DB_FIELD_PARTICIPANT, targetEntity = ContactRecord.class, cascade = CascadeType.ALL)
    @Where(clause = ContactRecord.DB_FIELD_TYPE+"="+ContactRecord.TYPE_TELEFONNUMMER)
    @JsonProperty(value = IO_FIELD_PHONE)
    public Set<ContactRecord> phoneNumber;

    public void setPhoneNumber(Set<ContactRecord> phoneNumber) {
        for (ContactRecord record : phoneNumber) {
            record.setType(ContactRecord.TYPE_TELEFONNUMMER);
            record.setParticipantRecord(this);
        }
        this.phoneNumber = phoneNumber;
    }



    public static final String DB_FIELD_FAX = "faxNumber";
    public static final String IO_FIELD_FAX = "telefaxNummer";

    @OneToMany(mappedBy = ContactRecord.DB_FIELD_PARTICIPANT, targetEntity = ContactRecord.class, cascade = CascadeType.ALL)
    @Where(clause = ContactRecord.DB_FIELD_TYPE+"="+ContactRecord.TYPE_TELEFAXNUMMER)
    @JsonProperty(value = IO_FIELD_FAX)
    public Set<ContactRecord> faxNumber;

    public void setFaxNumber(Set<ContactRecord> faxNumber) {
        for (ContactRecord record : faxNumber) {
            record.setType(ContactRecord.TYPE_TELEFAXNUMMER);
            record.setParticipantRecord(this);
        }
        this.faxNumber = faxNumber;
    }



    public static final String DB_FIELD_EMAIL = "emailAddress";
    public static final String IO_FIELD_EMAIL = "elektroniskPost";

    @OneToMany(mappedBy = ContactRecord.DB_FIELD_PARTICIPANT, targetEntity = ContactRecord.class, cascade = CascadeType.ALL)
    @Where(clause = ContactRecord.DB_FIELD_TYPE+"="+ContactRecord.TYPE_EMAILADRESSE)
    @JsonProperty(value = IO_FIELD_EMAIL)
    public Set<ContactRecord> emailAddress;

    public void setEmailAddress(Set<ContactRecord> emailAddress) {
        for (ContactRecord record : emailAddress) {
            record.setType(ContactRecord.TYPE_EMAILADRESSE);
            record.setParticipantRecord(this);
        }
        this.emailAddress = emailAddress;
    }



    public static final String DB_FIELD_ATTRIBUTES = "attributes";
    public static final String IO_FIELD_ATTRIBUTES = "attributter";

    @OneToMany(mappedBy = AttributeRecord.DB_FIELD_PARTICIPANT, targetEntity = AttributeRecord.class, cascade = CascadeType.ALL)
    @JsonProperty(value = IO_FIELD_ATTRIBUTES)
    public Set<AttributeRecord> attributes;

    public void setAttributes(Set<AttributeRecord> attributes) {
        this.attributes = attributes;
        for (AttributeRecord attributeRecord : attributes) {
            attributeRecord.setParticipantRecord(this);
        }
    }



    public static final String DB_FIELD_POSITION = "position";
    public static final String IO_FIELD_POSITION = "stilling";

    @Column(name = DB_FIELD_POSITION)
    @JsonProperty(value = IO_FIELD_POSITION)
    public String position;



    public static final String DB_FIELD_BUSINESS_KEY = "businessKey";
    public static final String IO_FIELD_BUSINESS_KEY = "forretningsnoegle";

    @Column(name = DB_FIELD_BUSINESS_KEY)
    @JsonProperty(value = IO_FIELD_BUSINESS_KEY)
    public Long businessKey;



    public static final String DB_FIELD_META = "metadata";
    public static final String IO_FIELD_META = "deltagerpersonMetadata";

    @OneToOne(mappedBy = ParticipantMetadataRecord.DB_FIELD_PARTICIPANT, targetEntity = ParticipantMetadataRecord.class, cascade = CascadeType.ALL)
    @JoinColumn(name = DB_FIELD_META + DatabaseEntry.REF)
    @JsonProperty(value = IO_FIELD_META)
    private ParticipantMetadataRecord metadata;

    public void setMetadata(ParticipantMetadataRecord metadata) {
        this.metadata = metadata;
        this.metadata.setParticipantRecord(this);
    }

    public ParticipantMetadataRecord getMetadata() {
        return this.metadata;
    }





    @JsonIgnore
    public List<CvrRecord> getAll() {
        ArrayList<CvrRecord> list = new ArrayList<>();
        list.add(this);
        if (this.names != null) {
            list.addAll(this.names);
        }
        if (this.locationAddress != null) {
            list.addAll(this.locationAddress);
        }
        if (this.postalAddress != null) {
            list.addAll(this.postalAddress);
        }
        if (this.businessAddress != null) {
            list.addAll(this.businessAddress);
        }
        if (this.phoneNumber != null) {
            list.addAll(this.phoneNumber);
        }
        if (this.faxNumber != null) {
            list.addAll(this.faxNumber);
        }
        if (this.emailAddress != null) {
            list.addAll(this.emailAddress);
        }
        if (this.attributes != null) {
            for (AttributeRecord attributeRecord : this.attributes) {
                list.addAll(attributeRecord.getValues());
            }
        }
        return list;
    }

    public UUID generateUUID() {
        return ParticipantEntity.generateUUID(this.unitType, this.unitNumber);
    }

    @Override
    public void populateBaseData(ParticipantBaseData baseData, Session session) {
        baseData.setUnitNumber(this.unitNumber);
        if (this.unitType != null) {
            baseData.setUnitType(ParticipantType.getType(this.unitType, session));
        }
        if (this.position != null) {
            baseData.setPosition(this.position);
        }
    }



    @Override
    public void save(Session session) {
        for (AddressRecord address : this.locationAddress) {
            address.wire(session);
        }
        for (AddressRecord address : this.postalAddress) {
            address.wire(session);
        }
        for (AddressRecord address : this.businessAddress) {
            address.wire(session);
        }
        this.metadata.wire(session);
        super.save(session);
    }
}
