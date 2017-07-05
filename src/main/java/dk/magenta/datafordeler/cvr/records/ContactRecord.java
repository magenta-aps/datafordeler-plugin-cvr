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
        PHONE,
        FAX,
        EMAIL,
        HOMEPAGE,
        MANDATORY_EMAIL
    }

    @JsonProperty(value = "kontaktoplysning")
    protected String contactInfo;

    @JsonProperty(value = "hemmelig")
    protected boolean secret;

    @JsonIgnore
    private Type type;

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public void populateBaseData(CompanyBaseData baseData, QueryManager queryManager, Session session) {
        switch (this.type) {
            case PHONE:
                baseData.setPhone(this.contactInfo, this.secret);
                break;
            case FAX:
                baseData.setFax(this.contactInfo, this.secret);
                break;
            case EMAIL:
                baseData.setEmail(this.contactInfo, this.secret);
                break;
            case HOMEPAGE:
                baseData.setHomepage(this.contactInfo, this.secret);
                break;
            case MANDATORY_EMAIL:
                baseData.setMandatoryEmail(this.contactInfo, this.secret);
                break;
        }
    }

    @Override
    public void populateBaseData(CompanyUnitBaseData baseData, QueryManager queryManager, Session session) {
        switch (this.type) {
            case PHONE:
                baseData.setPhone(this.contactInfo, this.secret);
                break;
            case FAX:
                baseData.setFax(this.contactInfo, this.secret);
                break;
            case EMAIL:
                baseData.setEmail(this.contactInfo, this.secret);
                break;
        }
    }

    @Override
    public void populateBaseData(ParticipantBaseData baseData, QueryManager queryManager, Session session) {
        switch (this.type) {
            case PHONE:
                baseData.setTelefonnummer(this.contactInfo, this.secret);
                break;
            case FAX:
                baseData.setTelefaxnummer(this.contactInfo, this.secret);
                break;
            case EMAIL:
                baseData.setEmailadresse(this.contactInfo, this.secret);
                break;
        }
    }
}
