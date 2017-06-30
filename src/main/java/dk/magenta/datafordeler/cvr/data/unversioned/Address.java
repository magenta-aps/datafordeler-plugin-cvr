package dk.magenta.datafordeler.cvr.data.unversioned;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.QueryManager;
import org.hibernate.Session;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by lars on 14-06-17.
 */
@Entity
@Table(name = "cvr_address", indexes = {
        @Index(name = "roadCode", columnList = "roadCode"),
        @Index(name = "roadName", columnList = "roadName")
})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Address extends UnversionedEntity {


    @JsonProperty(value = "adresseId")
    @XmlElement(name = "adresseId")
    @Column
    private String addressId;

    public String getAddressId() {
        return this.addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }



    @JsonProperty(value = "vejkode")
    @XmlElement(name = "vejkode")
    @Column
    private int roadCode;

    public int getRoadCode() {
        return this.roadCode;
    }

    public void setRoadCode(int roadCode) {
        this.roadCode = roadCode;
    }



    @JsonProperty(value = "bynavn")
    @XmlElement(name = "bynavn")
    @Column
    private String cityName;

    public String getCityName() {
        return this.cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }



    @JsonProperty(value = "vejnavn")
    @XmlElement(name = "vejnavn")
    @Column
    private String roadName;

    public String getRoadName() {
        return this.roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }



    @JsonProperty(value = "husnummerFra")
    @XmlElement(name = "husnummerFra")
    @Column
    private int houseNumberFrom;

    public int getHouseNumberFrom() {
        return this.houseNumberFrom;
    }

    public void setHouseNumberFrom(int houseNumberFrom) {
        this.houseNumberFrom = houseNumberFrom;
    }



    @JsonProperty(value = "husnummerTil")
    @XmlElement(name = "husnummerTil")
    @Column
    private int houseNumberTo;

    public int getHouseNumberTo() {
        return this.houseNumberTo;
    }

    public void setHouseNumberTo(int houseNumberTo) {
        this.houseNumberTo = houseNumberTo;
    }



    @JsonProperty(value = "bogstavFra")
    @XmlElement(name = "bogstavFra")
    @Column
    private String letterFrom;

    public String getLetterFrom() {
        return this.letterFrom;
    }

    public void setLetterFrom(String letterFrom) {
        this.letterFrom = letterFrom;
    }



    @JsonProperty(value = "bogstavTil")
    @XmlElement(name = "bogstavTil")
    @Column
    private String letterTo;

    public String getLetterTo() {
        return this.letterTo;
    }

    public void setLetterTo(String letterTo) {
        this.letterTo = letterTo;
    }



    @JsonProperty(value = "etage")
    @XmlElement(name = "etage")
    @Column
    private String floor;

    public String getFloor() {
        return this.floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    @JsonProperty(value = "sidedoer")
    @XmlElement(name = "sidedoer")
    @Column
    private String door;

    public String getDoor() {
        return this.door;
    }

    public void setDoor(String door) {
        this.door = door;
    }



    @JsonProperty(value = "kommune")
    @XmlElement(name = "kommune")
    @ManyToOne
    @Cascade(value = CascadeType.SAVE_UPDATE)
    private Municipality municipality;

    public Municipality getMunicipality() {
        return this.municipality;
    }

    public void setMunicipality(Municipality municipality) {
        this.municipality = municipality;
    }



    @JsonProperty(value = "postnummer")
    @XmlElement(name = "postnummer")
    @ManyToOne
    @Cascade(value = CascadeType.SAVE_UPDATE)
    private PostCode postCodeObject;

    public PostCode getPostCodeObject() {
        return this.postCodeObject;
    }

    public void setPostCodeObject(PostCode postCode) {
        this.postCodeObject = postCode;
    }


    @JsonIgnore
    @XmlTransient
    private int postCode;

    @JsonIgnore
    @XmlTransient
    private String postDistrict;

    @JsonProperty(value = "postnummer")
    public void setPostCode(int code) {
        this.postCode = code;
    }

    @JsonIgnore
    public int getPostCode() {
        return this.postCode;
    }

    @JsonProperty("postdistrikt")
    public void setPostDistrict(String district) {
        this.postDistrict = district;
    }

    @JsonIgnore
    public String getPostDistrict() {
        return this.postDistrict;
    }

    @JsonProperty(value = "postboks")
    @XmlElement(name = "postboks")
    @Column
    private int postBox;

    public int getPostBox() {
        return this.postBox;
    }

    public void setPostBox(int postBox) {
        this.postBox = postBox;
    }



    @JsonProperty(value = "conavn")
    @XmlElement(name = "conavn")
    @Column
    private String coName;

    public String getCoName() {
        return this.coName;
    }

    public void setCoName(String coName) {
        this.coName = coName;
    }



    @JsonProperty(value = "landekode")
    @XmlElement(name = "landekode")
    @Column
    private String countryCode;

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }



    @JsonProperty(value = "fritekst")
    @XmlElement(name = "fritekst")
    @Column
    private String freeText;

    public String getFreeText() {
        return this.freeText;
    }

    public void setFreeText(String freeText) {
        this.freeText = freeText;
    }



    @JsonProperty(value = "sidstValideret")
    @XmlElement(name = "sidstValideret")
    @Column
    private String lastValidated;

    public String getLastValidated() {
        return this.lastValidated;
    }

    public void setLastValidated(String lastValidated) {
        this.lastValidated = lastValidated;
    }

}
