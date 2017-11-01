package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantBaseData;
import org.hibernate.Session;

/**
 * Created by lars on 26-06-17.
 */
public class ContactRecord extends CvrBaseRecord {

    public enum Type {
        TELEFONNUMMER,
        TELEFAXNUMMER,
        EMAILADRESSE,
        HJEMMESIDE,
        OBLIGATORISK_EMAILADRESSE
    }

    @JsonProperty(value = "kontaktoplysning")
    protected String contectInformation;

    @JsonProperty(value = "hemmelig")
    protected boolean secret;

    @JsonIgnore
    private Type type;

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public void populateBaseData(CompanyBaseData baseData, Session session) {
        switch (this.type) {
            case TELEFONNUMMER:
                baseData.setPhoneNumber(this.contectInformation, this.secret);
                break;
            case TELEFAXNUMMER:
                baseData.setFaxNumber(this.contectInformation, this.secret);
                break;
            case EMAILADRESSE:
                baseData.setEmailAddress(this.contectInformation, this.secret);
                break;
            case HJEMMESIDE:
                baseData.setHomepage(this.contectInformation, this.secret);
                break;
            case OBLIGATORISK_EMAILADRESSE:
                baseData.setMandatoryEmail(this.contectInformation, this.secret);
                break;
        }
    }

    @Override
    public void populateBaseData(CompanyUnitBaseData baseData, Session session) {
        switch (this.type) {
            case TELEFONNUMMER:
                baseData.setPhoneNumber(this.contectInformation, this.secret);
                break;
            case TELEFAXNUMMER:
                baseData.setFaxNumber(this.contectInformation, this.secret);
                break;
            case EMAILADRESSE:
                baseData.setEmailAddress(this.contectInformation, this.secret);
                break;
        }
    }

    @Override
    public void populateBaseData(ParticipantBaseData baseData, Session session) {
        switch (this.type) {
            case TELEFONNUMMER:
                baseData.setPhoneNumber(this.contectInformation, this.secret);
                break;
            case TELEFAXNUMMER:
                baseData.setFaxNumber(this.contectInformation, this.secret);
                break;
            case EMAILADRESSE:
                baseData.setEmailAddress(this.contectInformation, this.secret);
                break;
        }
    }
}
