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
    public long enhedsnummer;

    @JsonProperty(value = "enhedstype")
    public String enhedstype;

    @JsonProperty(value = "navne")
    public List<NameRecord> navne;

    @JsonProperty(value = "beliggenhedsadresse")
    public List<AddressRecord> beliggenhedsadresse;

    public void setBeliggenhedsadresse(List<AddressRecord> beliggenhedsadresse) {
        for (AddressRecord record : beliggenhedsadresse) {
            record.setType(AddressRecord.Type.LOCATION);
        }
        this.beliggenhedsadresse = beliggenhedsadresse;
    }

    @JsonProperty(value = "postadresse")
    public List<AddressRecord> postadresse;

    public void setPostadresse(List<AddressRecord> postadresse) {
        for (AddressRecord record : postadresse) {
            record.setType(AddressRecord.Type.POSTAL);
        }
        this.postadresse = postadresse;
    }

    @JsonProperty(value = "forretningsadresse")
    public List<AddressRecord> forretningsadresse;

    public void setForretningsadresse(List<AddressRecord> forretningsadresse) {
        for (AddressRecord record : forretningsadresse) {
            record.setType(AddressRecord.Type.BUSINESS);
        }
        this.forretningsadresse = forretningsadresse;
    }

    @JsonProperty(value = "telefonNummer")
    public List<ContactRecord> telefonnummer;

    public void setTelefonnummer(List<ContactRecord> telefonnummer) {
        for (ContactRecord record : telefonnummer) {
            record.setType(ContactRecord.Type.TELEFONNUMMER);
        }
        this.telefonnummer = telefonnummer;
    }

    @JsonProperty(value = "telefaxNummer")
    public List<ContactRecord> telefaxnummer;

    public void setTelefaxnummer(List<ContactRecord> telefaxnummer) {
        for (ContactRecord record : telefaxnummer) {
            record.setType(ContactRecord.Type.TELEFAXNUMMER);
        }
        this.telefaxnummer = telefaxnummer;
    }

    @JsonProperty(value = "elektroniskPost")
    public List<ContactRecord> emailadresse;

    public void setEmailadresse(List<ContactRecord> emailadresse) {
        for (ContactRecord record : emailadresse) {
            record.setType(ContactRecord.Type.EMAILADRESSE);
        }
        this.emailadresse = emailadresse;
    }


    @JsonProperty(value = "attributter")
    public List<AttributeRecord> attributter;



    public List<BaseRecord> getAll() {
        ArrayList<BaseRecord> list = new ArrayList<>();
        list.add(this);
        if (this.navne != null) {
            list.addAll(this.navne);
        }
        if (this.beliggenhedsadresse != null) {
            list.addAll(this.beliggenhedsadresse);
        }
        if (this.postadresse != null) {
            list.addAll(this.postadresse);
        }
        if (this.forretningsadresse != null) {
            list.addAll(this.forretningsadresse);
        }
        if (this.telefonnummer != null) {
            list.addAll(this.telefonnummer);
        }
        if (this.telefaxnummer != null) {
            list.addAll(this.telefaxnummer);
        }
        if (this.emailadresse != null) {
            list.addAll(this.emailadresse);
        }
        if (this.attributter != null) {
            for (AttributeRecord attributeRecord : this.attributter) {
                list.addAll(attributeRecord.getValues());
            }
        }
        return list;
    }

    public UUID generateUUID() {
        return ParticipantEntity.generateUUID(this.enhedstype, this.enhedsnummer);
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
        baseData.setEnhedsnummer(this.enhedsnummer);
        baseData.setEnhedstype(ParticipantType.getType(this.enhedstype, queryManager, session));
    }
}
