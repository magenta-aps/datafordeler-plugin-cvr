package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantBaseData;
import dk.magenta.datafordeler.cvr.data.unversioned.Address;
import dk.magenta.datafordeler.cvr.data.unversioned.Municipality;
import dk.magenta.datafordeler.cvr.data.unversioned.PostCode;
import org.hibernate.Session;

import javax.persistence.*;

/**
 * Record for Company, CompanyUnit and Participant address data.
 */
@Entity
@Table(name = "cvr_record_address")
public class AddressRecord extends CvrBitemporalDataRecord {

    public enum Type {
        LOCATION,
        POSTAL,
        BUSINESS
    }

    @Transient
    @JsonUnwrapped
    private Address address;

    public Address getAddress() {
        return this.address;
    }

    @JsonIgnore
    private Type type;

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }


/*
    public static final String DB_FIELD_ROADCODE = "roadCode";
    public static final String IO_FIELD_ROADCODE = "vejkode";

    @JsonProperty(value = IO_FIELD_ROADCODE)
    @XmlElement(name = IO_FIELD_ROADCODE)
    @Column(name = DB_FIELD_ROADCODE)
    private int roadCode;

    public int getRoadCode() {
        return this.roadCode;
    }

    public void setRoadCode(int roadCode) {
        this.roadCode = roadCode;
    }

    public void setRoadCode(String roadCode) {
        this.setRoadCode(Integer.parseInt(roadCode));
    }

*/



    @Override
    public void populateBaseData(CompanyBaseData baseData, Session session) {
        this.normalizeAddress(session);
        //session.saveOrUpdate(this.address);
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
        //session.saveOrUpdate(this.address);
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
        //session.saveOrUpdate(this.address);
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
