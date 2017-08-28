package dk.magenta.datafordeler.cvr.data.participant;

import dk.magenta.datafordeler.core.database.DataItem;
import dk.magenta.datafordeler.core.database.LookupDefinition;
import dk.magenta.datafordeler.cvr.data.CvrData;
import dk.magenta.datafordeler.cvr.data.shared.*;
import dk.magenta.datafordeler.cvr.data.unversioned.Address;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by lars on 16-05-17.
 */
@javax.persistence.Entity
@Table(name="cvr_participant_data")
public class ParticipantBaseData extends CvrData<ParticipantEffect, ParticipantBaseData> {

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private TextData nameData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private ContactData phoneData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private ContactData emailData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private ContactData faxData;


    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private AddressData locationAddressData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private AddressData postalAddressData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private AddressData businessAddressData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private IntegerData unitNumberData;

    @ManyToOne(optional = true)
    private ParticipantType type;

    @ManyToOne(optional = true)
    private ParticipantRole role;

    @ManyToOne(optional = true)
    private ParticipantStatus status;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<AttributeData> attributeData = new HashSet<>();



    /**
     * Return a map of attributes, including those from the superclass
     * @return
     */
    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();



        if (this.nameData != null) {
            map.put("navn", this.nameData.getData());
        }
        if (this.phoneData != null) {
            map.put("telefon", this.phoneData.getData());
        }
        if (this.emailData != null) {
            map.put("email", this.emailData.getData());
        }
        if (this.faxData != null) {
            map.put("fax", this.faxData.getData());
        }
        if (this.locationAddressData != null) {
            map.put("beliggenhedsadresse", this.locationAddressData.getAddress());
        }
        if (this.postalAddressData != null) {
            map.put("postadresse", this.postalAddressData.getAddress());
        }
        if (this.businessAddressData != null) {
            map.put("forretningsadresse", this.businessAddressData.getAddress());
        }
        if (this.unitNumberData != null) {
            map.put("enhedsNummer", this.unitNumberData.getData());
        }
        if (this.type != null) {
            map.put("enhedsType", this.type.getName());
        }
        if (this.role != null) {
            map.put("rolle", this.role.getName());
        }
        if (this.status != null) {
            map.put("status", this.status.getName());
        }
        if (this.attributeData != null && !this.attributeData.isEmpty()) {
            map.put("attributter", this.attributeData);
        }
        return map;
    }


    public void setName(String name) {
        if (this.nameData == null) {
            this.nameData = new TextData(TextData.Type.NAME);
        }
        this.nameData.setData(name);
    }
    public void setPhone(String phone, boolean secret) {
        if (this.phoneData == null) {
            this.phoneData = new ContactData(ContactData.Type.PHONE);
        }
        this.phoneData.setData(phone);
        this.phoneData.setSecret(secret);
    }
    public void setEmail(String email, boolean secret) {
        if (this.emailData == null) {
            this.emailData = new ContactData(ContactData.Type.EMAIL);
        }
        this.emailData.setData(email);
        this.emailData.setSecret(secret);
    }
    public void setFax(String fax, boolean secret) {
        if (this.faxData == null) {
            this.faxData = new ContactData(ContactData.Type.FAX);
        }
        this.faxData.setData(fax);
        this.faxData.setSecret(secret);
    }
    public void setLocationAddress(Address address) {
        if (this.locationAddressData == null) {
            this.locationAddressData = new AddressData();
        }
        this.locationAddressData.setAddress(address);
    }
    public void setPostalAddress(Address address) {
        if (this.postalAddressData == null) {
            this.postalAddressData = new AddressData();
        }
        this.postalAddressData.setAddress(address);
    }
    public void setBusinessAddress(Address address) {
        if (this.businessAddressData == null) {
            this.businessAddressData = new AddressData();
        }
        this.businessAddressData.setAddress(address);
    }

    public void setUnitNumber(long unitNumber) {
        if (this.unitNumberData == null) {
            this.unitNumberData = new IntegerData();
        }
        this.unitNumberData.setData(unitNumber);
    }

    public void setType(ParticipantType type) {
        this.type = type;
    }

    public void setRole(ParticipantRole role) {
        this.role = role;
    }

    public void setStatus(ParticipantStatus status) {
        this.status = status;
    }

    public void addAttribute(AttributeData attributeData) {
        this.attributeData.add(attributeData);
    }

    @Override
    public LookupDefinition getLookupDefinition() {
        LookupDefinition lookupDefinition = new LookupDefinition();
        lookupDefinition.setMatchNulls(true);
        if (this.nameData != null) {
            lookupDefinition.putAll("nameData", this.nameData.databaseFields());
        }
        if (this.phoneData != null) {
            lookupDefinition.putAll("phoneData", this.phoneData.databaseFields());
        }
        if (this.emailData != null) {
            lookupDefinition.putAll("emailData", this.emailData.databaseFields());
        }
        if (this.faxData != null) {
            lookupDefinition.putAll("faxData", this.faxData.databaseFields());
        }
        if (this.locationAddressData != null) {
            lookupDefinition.putAll("locationAddressData", this.locationAddressData.databaseFields());
        }
        if (this.postalAddressData != null) {
            lookupDefinition.putAll("postalAddressData", this.postalAddressData.databaseFields());
        }
        if (this.businessAddressData != null) {
            lookupDefinition.putAll("businessAddressData", this.businessAddressData.databaseFields());
        }
        if (this.unitNumberData != null) {
            lookupDefinition.putAll("unitNumberData", this.unitNumberData.databaseFields());
        }
        if (this.status != null) {
            lookupDefinition.putAll("status", this.status.databaseFields());
        }
        if (this.type != null) {
            lookupDefinition.putAll("type", this.type.databaseFields());
        }

        return lookupDefinition;
    }


    public void forceLoad(Session session) {
        Hibernate.initialize(this.attributeData);
    }
}
