package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.core.exception.ParseException;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantBaseData;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantEntity;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantType;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by lars on 26-06-17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParticipantRecord extends BaseRecord {

    @JsonProperty(value = "enhedsNummer")
    public long unitNumber;

    @JsonProperty(value = "enhedstype")
    public String unitType;

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
            record.setType(ContactRecord.Type.TELEFONNUMMER);
        }
        this.phoneRecords = phoneRecords;
    }

    @JsonProperty(value = "telefaxNummer")
    public List<ContactRecord> faxRecords;

    public void setFaxRecords(List<ContactRecord> faxRecords) {
        for (ContactRecord record : faxRecords) {
            record.setType(ContactRecord.Type.TELEFAXNUMMER);
        }
        this.faxRecords = faxRecords;
    }

    @JsonProperty(value = "elektroniskPost")
    public List<ContactRecord> emailRecords;

    public void setEmailRecords(List<ContactRecord> emailRecords) {
        for (ContactRecord record : emailRecords) {
            record.setType(ContactRecord.Type.EMAILADRESSE);
        }
        this.emailRecords = emailRecords;
    }


    @JsonProperty(value = "attributter")
    public List<AttributeRecord> attributeRecords;



    public List<BaseRecord> getAll() {
        ArrayList<BaseRecord> list = new ArrayList<>();
        list.add(this);
        if (this.names != null) {
            list.addAll(this.names);
        }
        if (this.locationAddresses != null) {
            list.addAll(this.locationAddresses);
        }
        if (this.postalAddresses != null) {
            list.addAll(this.postalAddresses);
        }
        if (this.businessAddresses != null) {
            list.addAll(this.businessAddresses);
        }
        if (this.phoneRecords != null) {
            list.addAll(this.phoneRecords);
        }
        if (this.faxRecords != null) {
            list.addAll(this.faxRecords);
        }
        if (this.emailRecords != null) {
            list.addAll(this.emailRecords);
        }
        if (this.attributeRecords != null) {
            for (AttributeRecord attributeRecord : this.attributeRecords) {
                list.addAll(attributeRecord.getValues());
            }
        }
        return list;
    }

    public UUID generateUUID() {
        return ParticipantEntity.generateUUID(this.unitType, this.unitNumber);
    }

    @Override
    public void populateBaseData(CompanyBaseData baseData, QueryManager queryManager, Session session) throws ParseException {
        // Noop
    }

    @Override
    public void populateBaseData(CompanyUnitBaseData baseData, QueryManager queryManager, Session session) throws ParseException {
        // Noop
    }

    @Override
    public void populateBaseData(ParticipantBaseData baseData, QueryManager queryManager, Session session) {
        baseData.setEnhedsnummer(this.unitNumber);
        baseData.setEnhedstype(ParticipantType.getType(this.unitType, queryManager, session));
    }
}
