package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lars on 26-06-17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParticipantRecord {

    @JsonProperty(value = "navne")
    public List<NameRecord> names;

    @JsonProperty(value = "beliggenhedsadresse")
    public List<AddressRecord> locationAddresses;

    public void setLocationAddresses(List<AddressRecord> locationAddresses) {
        for (AddressRecord record : locationAddresses) {
            record.setType(AddressRecord.Type.LOCATION);
        }
        this.locationAddresses = locationAddresses;
    }

    @JsonProperty(value = "postadresse")
    public List<AddressRecord> postalAddresses;

    public void setPostalAddresses(List<AddressRecord> postalAddresses) {
        for (AddressRecord record : postalAddresses) {
            record.setType(AddressRecord.Type.POSTAL);
        }
        this.postalAddresses = postalAddresses;
    }

    @JsonProperty(value = "forretningsadresse")
    public List<AddressRecord> businessAddresses;

    public void setBusinessAddresses(List<AddressRecord> businessAddresses) {
        for (AddressRecord record : businessAddresses) {
            record.setType(AddressRecord.Type.BUSINESS);
        }
        this.businessAddresses = businessAddresses;
    }

    @JsonProperty(value = "telefonNummer")
    public List<ContactRecord> phoneRecords;

    public void setPhoneRecords(List<ContactRecord> phoneRecords) {
        for (ContactRecord record : phoneRecords) {
            record.setType(ContactRecord.Type.PHONE);
        }
        this.phoneRecords = phoneRecords;
    }

    @JsonProperty(value = "telefaxNummer")
    public List<ContactRecord> faxRecords;

    public void setFaxRecords(List<ContactRecord> faxRecords) {
        for (ContactRecord record : faxRecords) {
            record.setType(ContactRecord.Type.FAX);
        }
        this.faxRecords = faxRecords;
    }

    @JsonProperty(value = "elektroniskPost")
    public List<ContactRecord> emailRecords;

    public void setEmailRecords(List<ContactRecord> emailRecords) {
        for (ContactRecord record : emailRecords) {
            record.setType(ContactRecord.Type.EMAIL);
        }
        this.emailRecords = emailRecords;
    }


    @JsonProperty(value = "attributter")
    public List<AttributeRecord> attributeRecords;



    public List<BaseRecord> getAll() {
        ArrayList<BaseRecord> list = new ArrayList<>();
        list.addAll(this.names);
        list.addAll(this.locationAddresses);
        list.addAll(this.postalAddresses);
        list.addAll(this.businessAddresses);
        list.addAll(this.phoneRecords);
        list.addAll(this.faxRecords);
        list.addAll(this.emailRecords);
        for (AttributeRecord attributeRecord : this.attributeRecords) {
            list.addAll(attributeRecord.getValues());
        }
        return list;
    }
}
