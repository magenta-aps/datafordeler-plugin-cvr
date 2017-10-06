package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantBaseData;
import dk.magenta.datafordeler.cvr.data.unversioned.Address;
import dk.magenta.datafordeler.cvr.data.unversioned.Municipality;
import dk.magenta.datafordeler.cvr.data.unversioned.PostCode;
import org.hibernate.Session;

/**
 * Created by lars on 26-06-17.
 */
public class AddressRecord extends CvrBaseRecord {

    public enum Type {
        LOCATION,
        POSTAL,
        BUSINESS
    }

    @JsonUnwrapped
    private Address address;

    @JsonIgnore
    private Type type;

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public void populateBaseData(CompanyBaseData baseData, Session session) {
        this.normalizeAddress(session);
        session.saveOrUpdate(this.address);
        switch (this.type) {
            case LOCATION:
                baseData.setLocationAddress(this.address);
                break;
            case POSTAL:
                baseData.setPostalAddress(this.address);
                break;
        }
    }

    @Override
    public void populateBaseData(CompanyUnitBaseData baseData, Session session) {
        this.normalizeAddress(session);
        session.saveOrUpdate(this.address);
        switch (this.type) {
            case LOCATION:
                baseData.setLocationAddress(this.address);
                break;
            case POSTAL:
                baseData.setPostalAddress(this.address);
                break;
        }
    }

    @Override
    public void populateBaseData(ParticipantBaseData baseData, Session session) {
        this.normalizeAddress(session);
        session.saveOrUpdate(this.address);
        switch (this.type) {
            case LOCATION:
                baseData.setLocationAddress(this.address);
                break;
            case POSTAL:
                baseData.setPostalAddress(this.address);
                break;
            case BUSINESS:
                baseData.setBusinessAddress(this.address);
                break;
        }
    }

    private void normalizeAddress(Session session) {
        if (this.address != null) {
            Municipality oldMunicipality = this.address.getMunicipality();
            if (oldMunicipality != null) {
                this.address.setMunicipality(Municipality.getMunicipality(oldMunicipality, session));
            }
            int postcode = this.address.getPostnummer();
            if (postcode != 0) {
                this.address.setPost(PostCode.getPostcode(postcode, this.address.getPostdistrikt(), session));
            }
        }
    }
}
