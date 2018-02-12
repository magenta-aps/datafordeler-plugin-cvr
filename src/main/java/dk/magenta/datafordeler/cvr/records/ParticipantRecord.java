package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantBaseData;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantEntity;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantType;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Base record for Participant data, parsed from JSON into a tree of objects
 * with this class at the base.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParticipantRecord extends CvrEntityRecord {

    @JsonProperty(value = "enhedsNummer")
    public long unitNumber;

    @JsonProperty(value = "enhedstype")
    public String unitType;

    @JsonProperty(value = "navne")
    public List<NameRecord> names;

    @JsonProperty(value = "beliggenhedsadresse")
    public List<AddressRecord> locationAddress;

    public void setLocationAddress(List<AddressRecord> locationAddress) {
        for (AddressRecord record : locationAddress) {
            record.setType(AddressRecord.Type.LOCATION);
        }
        this.locationAddress = locationAddress;
    }

    @JsonProperty(value = "postadresse")
    public List<AddressRecord> postalAddress;

    public void setPostalAddress(List<AddressRecord> postalAddress) {
        for (AddressRecord record : postalAddress) {
            record.setType(AddressRecord.Type.POSTAL);
        }
        this.postalAddress = postalAddress;
    }

    @JsonProperty(value = "forretningsadresse")
    public List<AddressRecord> businessAddress;

    public void setBusinessAddress(List<AddressRecord> businessAddress) {
        for (AddressRecord record : businessAddress) {
            record.setType(AddressRecord.Type.BUSINESS);
        }
        this.businessAddress = businessAddress;
    }

    @JsonProperty(value = "telefonNummer")
    public List<ContactRecord> phoneNumber;

    public void setPhoneNumber(List<ContactRecord> phoneNumber) {
        for (ContactRecord record : phoneNumber) {
            record.setType(ContactRecord.Type.TELEFONNUMMER);
        }
        this.phoneNumber = phoneNumber;
    }

    @JsonProperty(value = "telefaxNummer")
    public List<ContactRecord> faxNumber;

    public void setFaxNumber(List<ContactRecord> faxNumber) {
        for (ContactRecord record : faxNumber) {
            record.setType(ContactRecord.Type.TELEFAXNUMMER);
        }
        this.faxNumber = faxNumber;
    }

    @JsonProperty(value = "elektroniskPost")
    public List<ContactRecord> emailAddress;

    public void setEmailAddress(List<ContactRecord> emailAddress) {
        for (ContactRecord record : emailAddress) {
            record.setType(ContactRecord.Type.EMAILADRESSE);
        }
        this.emailAddress = emailAddress;
    }


    @JsonProperty(value = "attributter")
    public List<AttributeRecord> attributes;


    @JsonProperty(value = "stilling")
    public String position;


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
}
