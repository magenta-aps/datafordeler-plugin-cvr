package dk.magenta.datafordeler.cvr.data.participant;

import dk.magenta.datafordeler.core.database.DataItem;
import dk.magenta.datafordeler.core.database.LookupDefinition;
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
public class ParticipantBaseData extends DataItem<ParticipantEffect, ParticipantBaseData> {

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private TextData navne;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private ContactData telefonnummer;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private ContactData emailadresse;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private ContactData telefaxnummer;


    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private AddressData beliggenhedsadresse;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private AddressData postadresse;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private AddressData forretningsadresse;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private IntegerData enhedsnummer;

    @ManyToOne(optional = true)
    private ParticipantType enhedstype;

    @ManyToOne(optional = true)
    private ParticipantRole rolle;

    @ManyToOne(optional = true)
    private ParticipantStatus status;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<AttributeData> attributter = new HashSet<>();

    public String getNavne() {
        if(navne != null)
            return navne.getVaerdi();
        else
            return null;
    }

    public String getTelefonnummer() {
        if(telefonnummer != null)
            return telefonnummer.getVaerdi();
        else
            return null;
    }

    public String getEmailadresse() {
        if(emailadresse != null)
            return emailadresse.getVaerdi();
        else
            return null;
    }

    public String getTelefaxnummer() {
        if(telefaxnummer != null)
            return telefaxnummer.getVaerdi();
        else
            return null;
    }

    public Address getBeliggenhedsadresse() {
        if(beliggenhedsadresse != null)
            return beliggenhedsadresse.getAdresse();
        else
            return null;
    }

    public Address getPostadresse() {
        if(postadresse != null)
            return postadresse.getAdresse();
        else
            return null;
    }

    public Address getForretningsadresse() {
        if(forretningsadresse != null)
            return forretningsadresse.getAdresse();
        else
            return null;
    }

    public Long getEnhedsnummer() {
        if(enhedsnummer != null)
            return enhedsnummer.getVaerdi();
        else
            return null;
    }

    public Long getEnhedstype() {
        if(enhedstype != null)
            return enhedstype.getId();
        else
            return null;
    }

    public Long getRolle() {
        if(rolle != null)
            return rolle.getId();
        else
            return null;
    }

    public Long getStatus() {
        if(status != null)
            return status.getId();
        else
            return null;
    }

    public Set<AttributeData> getAttributter() {
        if(attributter.isEmpty())
            return null;
        return attributter;
    }

    /**
     * Return a map of attributes, including those from the superclass
     * @return
     */
    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();



        if (this.navne != null) {
            map.put("navne", this.navne.getVaerdi());
        }
        if (this.telefonnummer != null) {
            map.put("telefonnummer", this.telefonnummer.getVaerdi());
        }
        if (this.emailadresse != null) {
            map.put("emailadresse", this.emailadresse.getVaerdi());
        }
        if (this.telefaxnummer != null) {
            map.put("telefaxnummer", this.telefaxnummer.getVaerdi());
        }
        if (this.beliggenhedsadresse != null) {
            map.put("beliggenhedsadresse", this.beliggenhedsadresse.getAdresse());
        }
        if (this.postadresse != null) {
            map.put("postadresse", this.postadresse.getAdresse());
        }
        if (this.forretningsadresse != null) {
            map.put("forretningsadresse", this.forretningsadresse.getAdresse());
        }
        if (this.enhedsnummer != null) {
            map.put("enhedsnummer", this.enhedsnummer.getVaerdi());
        }
        if (this.enhedstype != null) {
            map.put("enhedstype", this.enhedstype.getNavn());
        }
        if (this.rolle != null) {
            map.put("rolle", this.rolle.getNavn());
        }
        if (this.status != null) {
            map.put("status", this.status.getNavn());
        }
        if (this.attributter != null && !this.attributter.isEmpty()) {
            map.put("attributter", this.attributter);
        }
        return map;
    }


    public void setNavne(String name) {
        if (this.navne == null) {
            this.navne = new TextData(TextData.Type.NAVN);
        }
        this.navne.setVaerdi(name);
    }
    public void setTelefonnummer(String phone, boolean secret) {
        if (this.telefonnummer == null) {
            this.telefonnummer = new ContactData(ContactData.Type.TELEFONNUMMER);
        }
        this.telefonnummer.setVaerdi(phone);
        this.telefonnummer.setHemmelig(secret);
    }
    public void setEmailadresse(String email, boolean secret) {
        if (this.emailadresse == null) {
            this.emailadresse = new ContactData(ContactData.Type.EMAILADRESSE);
        }
        this.emailadresse.setVaerdi(email);
        this.emailadresse.setHemmelig(secret);
    }
    public void setTelefaxnummer(String fax, boolean secret) {
        if (this.telefaxnummer == null) {
            this.telefaxnummer = new ContactData(ContactData.Type.TELEFAXNUMMER);
        }
        this.telefaxnummer.setVaerdi(fax);
        this.telefaxnummer.setHemmelig(secret);
    }
    public void setBeliggenhedsadresse(Address address) {
        if (this.beliggenhedsadresse == null) {
            this.beliggenhedsadresse = new AddressData();
        }
        this.beliggenhedsadresse.setAdresse(address);
    }
    public void setPostadresse(Address address) {
        if (this.postadresse == null) {
            this.postadresse = new AddressData();
        }
        this.postadresse.setAdresse(address);
    }
    public void setForretningsadresse(Address address) {
        if (this.forretningsadresse == null) {
            this.forretningsadresse = new AddressData();
        }
        this.forretningsadresse.setAdresse(address);
    }

    public void setEnhedsnummer(long unitNumber) {
        if (this.enhedsnummer == null) {
            this.enhedsnummer = new IntegerData();
        }
        this.enhedsnummer.setVaerdi(unitNumber);
    }

    public void setEnhedstype(ParticipantType enhedstype) {
        this.enhedstype = enhedstype;
    }

    public void setRolle(ParticipantRole rolle) {
        this.rolle = rolle;
    }

    public void setStatus(ParticipantStatus status) {
        this.status = status;
    }

    public void addAttributter(AttributeData attributeData) {
        this.attributter.add(attributeData);
    }

    @Override
    public LookupDefinition getLookupDefinition() {
        LookupDefinition lookupDefinition = new LookupDefinition();
        lookupDefinition.setMatchNulls(true);
        if (this.navne != null) {
            lookupDefinition.putAll("navne", this.navne.databaseFields());
        }
        if (this.telefonnummer != null) {
            lookupDefinition.putAll("telefonnummer", this.telefonnummer.databaseFields());
        }
        if (this.emailadresse != null) {
            lookupDefinition.putAll("emailadresse", this.emailadresse.databaseFields());
        }
        if (this.telefaxnummer != null) {
            lookupDefinition.putAll("telefaxnummer", this.telefaxnummer.databaseFields());
        }
        if (this.beliggenhedsadresse != null) {
            lookupDefinition.putAll("beliggenhedsadresse", this.beliggenhedsadresse.databaseFields());
        }
        if (this.postadresse != null) {
            lookupDefinition.putAll("postadresse", this.postadresse.databaseFields());
        }
        if (this.forretningsadresse != null) {
            lookupDefinition.putAll("forretningsadresse", this.forretningsadresse.databaseFields());
        }
        if (this.enhedsnummer != null) {
            lookupDefinition.putAll("enhedsnummer", this.enhedsnummer.databaseFields());
        }
        if (this.status != null) {
            lookupDefinition.putAll("status", this.status.databaseFields());
        }
        if (this.enhedstype != null) {
            lookupDefinition.putAll("enhedstype", this.enhedstype.databaseFields());
        }

        return lookupDefinition;
    }


    public void forceLoad(Session session) {
        Hibernate.initialize(this.attributter);
    }
}
