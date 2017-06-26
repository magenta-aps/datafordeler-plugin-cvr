package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.unversioned.Address;
import org.hibernate.Session;

/**
 * Created by lars on 26-06-17.
 */
public class CompanyAddressRecord extends CompanyBaseRecord {

    public enum Type {
        LOCATION,
        POSTAL
    }

    @JsonUnwrapped
    private Address address;

    @JsonIgnore
    private Type type;

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public void populateCompanyBaseData(CompanyBaseData baseData, QueryManager queryManager, Session session) {
        switch (this.type) {
            case LOCATION:
                baseData.setLocationAddress(this.address);
                break;
            case POSTAL:
                baseData.setPostalAddress(this.address);
                break;
        }
    }
}
