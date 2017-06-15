package dk.magenta.datafordeler.cvr.data.unversioned;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by lars on 14-06-17.
 */
@Entity
@Table(name = "cvr_address")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Address extends UnversionedEntity {

    @JsonProperty
    @XmlElement
    @Column
    private int roadCode;

    public int getRoadCode() {
        return this.roadCode;
    }

    public void setRoadCode(int roadCode) {
        this.roadCode = roadCode;
    }



    @JsonProperty
    @XmlElement
    @Column
    private String roadName;

    public String getRoadName() {
        return this.roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }



    @JsonProperty
    @XmlElement
    @Column
    private int houseNumberFrom;

    public int getHouseNumberFrom() {
        return this.houseNumberFrom;
    }

    public void setHouseNumberFrom(int houseNumberFrom) {
        this.houseNumberFrom = houseNumberFrom;
    }



    @JsonProperty
    @XmlElement
    @Column
    private int houseNumberTo;

    public int getHouseNumberTo() {
        return this.houseNumberTo;
    }

    public void setHouseNumberTo(int houseNumberTo) {
        this.houseNumberTo = houseNumberTo;
    }



    @JsonProperty
    @XmlElement
    @Column
    private String letterFrom;

    public String getLetterFrom() {
        return this.letterFrom;
    }

    public void setLetterFrom(String letterFrom) {
        this.letterFrom = letterFrom;
    }



    @JsonProperty
    @XmlElement
    @Column
    private String letterTo;

    public String getLetterTo() {
        return this.letterTo;
    }

    public void setLetterTo(String letterTo) {
        this.letterTo = letterTo;
    }



    @JsonProperty
    @XmlElement
    @Column
    private int floor;

    public int getFloor() {
        return this.floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }



    @JsonProperty
    @XmlElement
    @Column
    private String door;

    public String getDoor() {
        return this.door;
    }

    public void setDoor(String door) {
        this.door = door;
    }



    @JsonProperty
    @XmlElement
    @ManyToOne
    private Municipality municipality;

    public Municipality getMunicipality() {
        return this.municipality;
    }

    public void setMunicipality(Municipality municipality) {
        this.municipality = municipality;
    }



    @JsonProperty
    @XmlElement
    @ManyToOne
    private PostCode postCode;

    public PostCode getPostCode() {
        return this.postCode;
    }

    public void setPostCode(PostCode postCode) {
        this.postCode = postCode;
    }



    @JsonProperty
    @XmlElement
    @Column
    private String coName;

    public String getCoName() {
        return this.coName;
    }

    public void setCoName(String coName) {
        this.coName = coName;
    }
}
