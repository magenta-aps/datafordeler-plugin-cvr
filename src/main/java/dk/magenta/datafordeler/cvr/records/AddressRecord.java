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
    public void populateBaseData(CompanyBaseData baseData, QueryManager queryManager, Session session) {
        this.normalizeAddress(queryManager, session);
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
    public void populateBaseData(CompanyUnitBaseData baseData, QueryManager queryManager, Session session) {
        this.normalizeAddress(queryManager, session);
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
    public void populateBaseData(ParticipantBaseData baseData, QueryManager queryManager, Session session) {
        this.normalizeAddress(queryManager, session);
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

    private void normalizeAddress(QueryManager queryManager, Session session) {
        if (this.address != null) {
            Municipality oldMunicipality = this.address.getMunicipality();
            if (oldMunicipality != null) {
                this.address.setMunicipality(Municipality.getMunicipality(oldMunicipality, queryManager, session));
            }
            int postcode = this.address.getPostCode();
            if (postcode != 0) {
                this.address.setPostCodeObject(PostCode.getPostcode(postcode, this.address.getPostDistrict(), queryManager, session));
            }
        }
    }
}
