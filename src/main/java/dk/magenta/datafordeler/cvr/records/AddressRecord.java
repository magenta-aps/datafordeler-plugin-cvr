package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.DatabaseEntry;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantBaseData;
import dk.magenta.datafordeler.cvr.data.unversioned.Address;
import dk.magenta.datafordeler.cvr.data.unversioned.Municipality;
import dk.magenta.datafordeler.cvr.data.unversioned.PostCode;
import org.hibernate.Session;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.util.Objects;
import java.util.UUID;

/**
 * Record for Company, CompanyUnit and Participant address data.
 */
@Entity
@Table(name = AddressRecord.TABLE_NAME, indexes = {
        @Index(name = AddressRecord.TABLE_NAME + "__company", columnList = AddressRecord.DB_FIELD_COMPANY + DatabaseEntry.REF),
        @Index(name = AddressRecord.TABLE_NAME + "__unit", columnList = AddressRecord.DB_FIELD_COMPANYUNIT + DatabaseEntry.REF),
        @Index(name = AddressRecord.TABLE_NAME + "__participant", columnList = AddressRecord.DB_FIELD_PARTICIPANT + DatabaseEntry.REF),
        @Index(name = AddressRecord.TABLE_NAME + "__companymetadata", columnList = AddressRecord.DB_FIELD_COMPANY_METADATA + DatabaseEntry.REF),
        @Index(name = AddressRecord.TABLE_NAME + "__unitmetadata", columnList = AddressRecord.DB_FIELD_UNIT_METADATA + DatabaseEntry.REF),
        @Index(name = AddressRecord.TABLE_NAME + "__officeunit", columnList = AddressRecord.DB_FIELD_OFFICE_UNIT + DatabaseEntry.REF),
        @Index(name = AddressRecord.TABLE_NAME + "__participantrelation", columnList = AddressRecord.DB_FIELD_PARTICIPANT_RELATION + DatabaseEntry.REF),
        @Index(name = AddressRecord.TABLE_NAME + "__type", columnList = AddressRecord.DB_FIELD_TYPE),
        @Index(name = AddressRecord.TABLE_NAME + "__municipality", columnList = AddressRecord.DB_FIELD_MUNICIPALITY + DatabaseEntry.REF),
        @Index(name = AddressRecord.TABLE_NAME + "__postcode", columnList = AddressRecord.DB_FIELD_POSTCODE_REF + DatabaseEntry.REF),
})
public class AddressRecord extends CvrBitemporalDataMetaRecord {

    public static final String TABLE_NAME = "cvr_record_address";

    public static final int TYPE_LOCATION = 0;
    public static final int TYPE_POSTAL = 1;
    public static final int TYPE_BUSINESS = 2;

    @JsonIgnore
    public Address getAddress() {
        //return this.address;
        Address address = new Address();
        address.setRoadName(this.roadName);
        address.setRoadCode(this.roadCode);
        address.setMunicipality(this.municipality.getMunicipality());
        address.setHouseNumberFrom(this.houseNumberFrom);
        address.setHouseNumberTo(this.houseNumberTo);
        address.setPost(this.post);
        address.setAddressText(this.addressText);
        address.setCoName(this.coName);
        address.setCountryCode(this.countryCode);
        address.setFloor(this.floor);
        address.setDoor(this.door);
        address.setLetterFrom(this.letterFrom);
        address.setLetterTo(this.letterTo);
        address.setLastValidated(this.lastValidated);
        address.setPostdistrikt(this.postdistrikt);
        try {
            address.setPostBox(Integer.parseInt(this.postBox, 10));
        } catch (NumberFormatException e) {}
        address.setPostnummer(this.postnummer);
        address.setSupplementalCityName(this.supplementalCityName);
        return address;
    }




    public static final String DB_FIELD_OFFICE_UNIT = "officeUnitRecord";

    @ManyToOne(targetEntity = OfficeRelationUnitRecord.class, fetch = FetchType.LAZY)
    @JoinColumn(name = DB_FIELD_OFFICE_UNIT + DatabaseEntry.REF)
    @JsonIgnore
    private OfficeRelationUnitRecord officeUnitRecord;

    public void setOfficeUnitRecord(OfficeRelationUnitRecord officeUnitRecord) {
        this.officeUnitRecord = officeUnitRecord;
    }



    public static final String DB_FIELD_PARTICIPANT_RELATION = "relationParticipantRecord";

    @ManyToOne(targetEntity = RelationParticipantRecord.class, fetch = FetchType.LAZY)
    @JoinColumn(name = DB_FIELD_PARTICIPANT_RELATION + DatabaseEntry.REF)
    @JsonIgnore
    private RelationParticipantRecord relationParticipantRecord;

    public void setRelationParticipantRecord(RelationParticipantRecord relationParticipantRecord) {
        this.relationParticipantRecord = relationParticipantRecord;
    }



    public static final String DB_FIELD_TYPE = "type";

    @Column(name = DB_FIELD_TYPE)
    @JsonIgnore
    private int type;

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }


    //----------------------------------------------------

    public static final String DB_FIELD_ID = "addressId";
    public static final String IO_FIELD_ID = "adresseId";

    @JsonProperty(value = IO_FIELD_ID)
    @XmlElement(name = IO_FIELD_ID)
    @Column(name = DB_FIELD_ID)
    private UUID addressId;

    public UUID getAddressId() {
        return this.addressId;
    }

    public void setAddressId(UUID addressId) {
        this.addressId = addressId;
    }

    //----------------------------------------------------

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

    //----------------------------------------------------

    public static final String DB_FIELD_CITY = "cityName";
    public static final String IO_FIELD_CITY = "bynavn";

    @JsonProperty(value = IO_FIELD_CITY)
    @XmlElement(name = IO_FIELD_CITY)
    @Column(name = DB_FIELD_CITY)
    private String cityName;

    public String getCityName() {
        return this.cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    //----------------------------------------------------

    public static final String DB_FIELD_SUPPLEMENTAL_CITY = "supplementalCityName";
    public static final String IO_FIELD_SUPPLEMENTAL_CITY = "supplerendeBynavn";

    @JsonProperty(value = IO_FIELD_SUPPLEMENTAL_CITY)
    @XmlElement(name = IO_FIELD_SUPPLEMENTAL_CITY)
    @Column(name = DB_FIELD_SUPPLEMENTAL_CITY)
    private String supplementalCityName;

    public String getSupplementalCityName() {
        return this.supplementalCityName;
    }

    public void setSupplementalCityName(String supplementalCityName) {
        this.supplementalCityName = supplementalCityName;
    }

    //----------------------------------------------------

    public static final String DB_FIELD_ROADNAME = "roadName";
    public static final String IO_FIELD_ROADNAME = "vejnavn";

    @JsonProperty(value = IO_FIELD_ROADNAME)
    @XmlElement(name = IO_FIELD_ROADNAME)
    @Column(name = DB_FIELD_ROADNAME)
    private String roadName;

    public String getRoadName() {
        return this.roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    //----------------------------------------------------

    public static final String DB_FIELD_HOUSE_FROM = "houseNumberFrom";
    public static final String IO_FIELD_HOUSE_FROM = "husnummerFra";

    @JsonProperty(value = IO_FIELD_HOUSE_FROM)
    @XmlElement(name = IO_FIELD_HOUSE_FROM)
    @Column(name = DB_FIELD_HOUSE_FROM)
    private String houseNumberFrom;

    public String getHouseNumberFrom() {
        return this.houseNumberFrom;
    }

    public void setHouseNumberFrom(String houseNumberFrom) {
        this.houseNumberFrom = houseNumberFrom;
    }

    //----------------------------------------------------

    public static final String DB_FIELD_HOUSE_TO = "houseNumberTo";
    public static final String IO_FIELD_HOUSE_TO = "husnummerTil";

    @JsonProperty(value = IO_FIELD_HOUSE_TO)
    @XmlElement(name = IO_FIELD_HOUSE_TO)
    @Column(name = DB_FIELD_HOUSE_TO)
    private String houseNumberTo;

    public String getHouseNumberTo() {
        return this.houseNumberTo;
    }

    public void setHouseNumberTo(String houseNumberTo) {
        this.houseNumberTo = houseNumberTo;
    }

    //----------------------------------------------------

    public static final String DB_FIELD_LETTER_FROM = "letterFrom";
    public static final String IO_FIELD_LETTER_FROM = "bogstavFra";

    @JsonProperty(value = IO_FIELD_LETTER_FROM)
    @XmlElement(name = IO_FIELD_LETTER_FROM)
    @Column(name = DB_FIELD_LETTER_FROM)
    private String letterFrom;

    public String getLetterFrom() {
        return this.letterFrom;
    }

    public void setLetterFrom(String letterFrom) {
        this.letterFrom = letterFrom;
    }

    //----------------------------------------------------

    public static final String DB_FIELD_LETTER_TO = "letterTo";
    public static final String IO_FIELD_LETTER_TO = "bogstavTil";

    @JsonProperty(value = IO_FIELD_LETTER_TO)
    @XmlElement(name = IO_FIELD_LETTER_TO)
    @Column(name = DB_FIELD_LETTER_TO)
    private String letterTo;

    public String getLetterTo() {
        return this.letterTo;
    }

    public void setLetterTo(String letterTo) {
        this.letterTo = letterTo;
    }

    //----------------------------------------------------

    public static final String DB_FIELD_FLOOR = "floor";
    public static final String IO_FIELD_FLOOR = "etage";

    @JsonProperty(value = IO_FIELD_FLOOR)
    @XmlElement(name = IO_FIELD_FLOOR)
    @Column(name = DB_FIELD_FLOOR)
    private String floor;

    public String getFloor() {
        return this.floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    //----------------------------------------------------

    public static final String DB_FIELD_DOOR = "door";
    public static final String IO_FIELD_DOOR = "sidedoer";

    @JsonProperty(value = IO_FIELD_DOOR)
    @XmlElement(name = IO_FIELD_DOOR)
    @Column(name = DB_FIELD_DOOR)
    private String door;

    public String getDoor() {
        return this.door;
    }

    public void setDoor(String door) {
        this.door = door;
    }

    //----------------------------------------------------

    public static final String DB_FIELD_MUNICIPALITY = "municipality";
    public static final String IO_FIELD_MUNICIPALITY = "kommune";

    @XmlElement(name = IO_FIELD_MUNICIPALITY)
    @OneToOne(cascade = CascadeType.ALL)
    private AddressMunicipalityRecord municipality;

    @JsonProperty(value = IO_FIELD_MUNICIPALITY)
    public AddressMunicipalityRecord getMunicipality() {
        return this.municipality;
    }

    @JsonProperty(value = IO_FIELD_MUNICIPALITY)
    public void setMunicipality(AddressMunicipalityRecord municipality) {
        this.municipality = municipality;
    }

    //----------------------------------------------------

    public static final String DB_FIELD_POSTCODE_REF = "post";
    public static final String IO_FIELD_POSTCODE_REF = "post";

    @XmlElement(name = IO_FIELD_POSTCODE_REF)
    @ManyToOne(targetEntity = PostCode.class)
    @JoinColumn(name = DB_FIELD_POSTCODE_REF + DatabaseEntry.REF)
    @JsonIgnore
    private PostCode post;

    public PostCode getPost() {
        return this.post;
    }

    public void setPost(PostCode postCode) {
        this.post = postCode;
    }

    //----------------------------------------------------

    public static final String DB_FIELD_POSTCODE = "postnummer";
    public static final String IO_FIELD_POSTCODE = "postnummer";

    @Transient
    @JsonProperty(value = IO_FIELD_POSTCODE)
    private int postnummer;

    public int getPostnummer() {
        if (this.post != null) {
            return this.post.getPostCode();
        }
        return this.postnummer;
    }

    @XmlElement(name = IO_FIELD_POSTCODE)
    public void setPostnummer(int code) {
        this.postnummer = code;
    }

    //----------------------------------------------------

    public static final String DB_FIELD_POSTDISTRICT = "postdistrikt";
    public static final String IO_FIELD_POSTDISTRICT = "postdistrikt";

    @Transient
    @JsonProperty(value = IO_FIELD_POSTDISTRICT)
    private String postdistrikt;

    public String getPostdistrikt() {
        if (this.post != null) {
            return this.post.getPostDistrict();
        }
        return this.postdistrikt;
    }

    @XmlElement(name = IO_FIELD_POSTDISTRICT)
    public void setPostdistrikt(String district) {
        this.postdistrikt = district;
    }

    //----------------------------------------------------

    public static final String DB_FIELD_POSTBOX = "postBox";
    public static final String IO_FIELD_POSTBOX = "postboks";

    @JsonProperty(value = IO_FIELD_POSTBOX)
    @XmlElement(name = IO_FIELD_POSTBOX)
    @Column(name = DB_FIELD_POSTBOX)
    private String postBox;

    public String getPostBox() {
        return this.postBox;
    }

    public void setPostBox(String postBox) {
        this.postBox = postBox;
    }

    //----------------------------------------------------

    public static final String DB_FIELD_CONAME = "coName";
    public static final String IO_FIELD_CONAME = "conavn";

    @JsonProperty(value = IO_FIELD_CONAME)
    @XmlElement(name = IO_FIELD_CONAME)
    @Column(name = DB_FIELD_CONAME)
    private String coName;

    public String getCoName() {
        return this.coName;
    }

    public void setCoName(String coName) {
        this.coName = coName;
    }

    //----------------------------------------------------

    public static final String DB_FIELD_COUNTRYCODE = "countryCode";
    public static final String IO_FIELD_COUNTRYCODE = "landekode";

    @JsonProperty(value = IO_FIELD_COUNTRYCODE)
    @XmlElement(name = IO_FIELD_COUNTRYCODE)
    @Column(name = DB_FIELD_COUNTRYCODE)
    private String countryCode;

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    //----------------------------------------------------

    public static final String DB_FIELD_TEXT = "addressText";
    public static final String IO_FIELD_TEXT = "adresseFritekst";

    @JsonProperty(value = IO_FIELD_TEXT)
    @XmlElement(name = IO_FIELD_TEXT)
    @Column(name = DB_FIELD_TEXT)
    private String addressText;

    public String getAddressText() {
        return this.addressText;
    }

    public void setAddressText(String addressText) {
        this.addressText = addressText;
    }

    //----------------------------------------------------

    public static final String DB_FIELD_VALIDATED = "lastValidated";
    public static final String IO_FIELD_VALIDATED = "sidstValideret";

    @JsonProperty(value = IO_FIELD_VALIDATED)
    @XmlElement(name = IO_FIELD_VALIDATED)
    @Column(name = DB_FIELD_VALIDATED)
    private String lastValidated;

    public String getLastValidated() {
        return this.lastValidated;
    }

    public void setLastValidated(String lastValidated) {
        this.lastValidated = lastValidated;
    }


    //----------------------------------------------------

    public static final String DB_FIELD_FREETEXT = "addressFreeText";
    public static final String IO_FIELD_FREETEXT = "fritekst";

    @JsonProperty(value = IO_FIELD_FREETEXT)
    @XmlElement(name = IO_FIELD_FREETEXT)
    @Column(name = DB_FIELD_FREETEXT, length = 8000)
    private String freeText;

    public String getFreeText() {
        return this.freeText;
    }

    public void setFreeText(String freeText) {
        if (freeText != null && freeText.length() > 8000) {
            freeText = freeText.substring(0, 8000);
        }
        this.freeText = freeText;
    }





    @Override
    public void populateBaseData(CompanyBaseData baseData, Session session) {
        Address address = this.normalizeAddress(session);
        //session.saveOrUpdate(this.address);
        switch (this.type) {
            case TYPE_LOCATION:
                baseData.setLocationAddress(address);
                break;
            case TYPE_POSTAL:
                baseData.setPostalAddress(address);
                break;
        }
    }

    @Override
    public void populateBaseData(CompanyUnitBaseData baseData, Session session) {
        Address address = this.normalizeAddress(session);
        //session.saveOrUpdate(this.address);
        switch (this.type) {
            case TYPE_LOCATION:
                baseData.setLocationAddress(address);
                break;
            case TYPE_POSTAL:
                baseData.setPostalAddress(address);
                break;
        }
    }

    @Override
    public void populateBaseData(ParticipantBaseData baseData, Session session) {
        Address address = this.normalizeAddress(session);
        //session.saveOrUpdate(this.address);
        switch (this.type) {
            case TYPE_LOCATION:
                baseData.setLocationAddress(address);
                break;
            case TYPE_POSTAL:
                baseData.setPostalAddress(address);
                break;
            case TYPE_BUSINESS:
                baseData.setBusinessAddress(address);
                break;
        }
    }

    private Address normalizeAddress(Session session) {
        Address address = getAddress();
        if (address != null) {
            Municipality oldMunicipality = address.getMunicipality();
            if (oldMunicipality != null) {
                address.setMunicipality(Municipality.getMunicipality(oldMunicipality, session));
            }
            int postcode = address.getPostnummer();
            if (postcode != 0) {
                address.setPost(PostCode.getPostcode(postcode, address.getPostdistrikt(), session));
            }
        }
        return address;
    }

    public void wire(Session session) {
        if (this.municipality != null) {
            this.municipality.wire(session);
        }
        if (this.postnummer != 0 && (this.post == null || this.post.getPostCode() != this.postnummer)) {
            this.post = PostCode.getPostcode(this.postnummer, this.postdistrikt, session);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AddressRecord that = (AddressRecord) o;
        return type == that.type &&
                roadCode == that.roadCode &&
                postnummer == that.postnummer &&
                Objects.equals(addressId, that.addressId) &&
                Objects.equals(cityName, that.cityName) &&
                Objects.equals(supplementalCityName, that.supplementalCityName) &&
                Objects.equals(roadName, that.roadName) &&
                Objects.equals(houseNumberFrom, that.houseNumberFrom) &&
                Objects.equals(houseNumberTo, that.houseNumberTo) &&
                Objects.equals(letterFrom, that.letterFrom) &&
                Objects.equals(letterTo, that.letterTo) &&
                Objects.equals(floor, that.floor) &&
                Objects.equals(door, that.door) &&
                Objects.equals(municipality, that.municipality) &&
                Objects.equals(post, that.post) &&
                Objects.equals(postdistrikt, that.postdistrikt) &&
                Objects.equals(postBox, that.postBox) &&
                Objects.equals(coName, that.coName) &&
                Objects.equals(countryCode, that.countryCode) &&
                Objects.equals(addressText, that.addressText) &&
                Objects.equals(lastValidated, that.lastValidated) &&
                Objects.equals(freeText, that.freeText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type, addressId, roadCode, cityName, supplementalCityName, roadName, houseNumberFrom, houseNumberTo, letterFrom, letterTo, floor, door, municipality, post, postnummer, postdistrikt, postBox, coName, countryCode, addressText, lastValidated, freeText);
    }
}
