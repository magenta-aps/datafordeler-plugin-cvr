package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantBaseData;
import org.hibernate.Session;

/**
 * Created by lars on 26-06-17.
 */
public class ContactRecord extends BaseRecord {

    public enum Type {
        TELEFONNUMMER,
        TELEFAXNUMMER,
        EMAILADRESSE,
        HJEMMESIDE,
        OBLIGATORISK_EMAILADRESSE
    }

    @JsonProperty(value = "kontaktoplysning")
    protected String kontaktoplysning;

    @JsonProperty(value = "hemmelig")
    protected boolean hemmelig;

    @JsonIgnore
    private Type type;

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public void populateBaseData(CompanyBaseData baseData, QueryManager queryManager, Session session) {
        switch (this.type) {
            case TELEFONNUMMER:
                baseData.setTelefonnummer(this.kontaktoplysning, this.hemmelig);
                break;
            case TELEFAXNUMMER:
                baseData.setTelefaxnummer(this.kontaktoplysning, this.hemmelig);
                break;
            case EMAILADRESSE:
                baseData.setEmailadresse(this.kontaktoplysning, this.hemmelig);
                break;
            case HJEMMESIDE:
                baseData.setHjemmeside(this.kontaktoplysning, this.hemmelig);
                break;
            case OBLIGATORISK_EMAILADRESSE:
                baseData.setObligatoriskEmail(this.kontaktoplysning, this.hemmelig);
                break;
        }
    }

    @Override
    public void populateBaseData(CompanyUnitBaseData baseData, QueryManager queryManager, Session session) {
        switch (this.type) {
            case TELEFONNUMMER:
                baseData.setTelefonnummer(this.kontaktoplysning, this.hemmelig);
                break;
            case TELEFAXNUMMER:
                baseData.setTelefaxnummer(this.kontaktoplysning, this.hemmelig);
                break;
            case EMAILADRESSE:
                baseData.setEmailadresse(this.kontaktoplysning, this.hemmelig);
                break;
        }
    }

    @Override
    public void populateBaseData(ParticipantBaseData baseData, QueryManager queryManager, Session session) {
        switch (this.type) {
            case TELEFONNUMMER:
                baseData.setTelefonnummer(this.kontaktoplysning, this.hemmelig);
                break;
            case TELEFAXNUMMER:
                baseData.setTelefaxnummer(this.kontaktoplysning, this.hemmelig);
                break;
            case EMAILADRESSE:
                baseData.setEmailadresse(this.kontaktoplysning, this.hemmelig);
                break;
        }
    }
}
