package dk.magenta.datafordeler.cvr.data.embeddable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.Identification;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantEntity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jubk on 04-03-2015.
 */
@Embeddable
public class CompanySharedData {

    @OneToMany
    @JsonProperty(value = "deltagere")
    @XmlElement(name = "deltagere")
    private Collection<ParticipantEntity> participants;


    @Column
    @JsonProperty(value = "advertProtection")
    @XmlElement(name = "advertProtection")
    private boolean advertProtection;

    @JsonIgnore
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("participants", this.participants);
        map.put("advertProtection", this.advertProtection);
        return map;
    }

    /**
     * Return a map of references (omit this method if there are no references in the class)
     * @return
     */
    @JsonIgnore
    public HashMap<String, Identification> getReferences() {
        HashMap<String, Identification> references = new HashMap<>();
        return references;
    }


    /**
     * Update this object from a map of references (omit this method if there are no references in the class)
     * @return
     */
    public void updateReferences(HashMap<String, Identification> references) {
    }














    //--------------------------------------------------------------------------

    @Embedded
    private LifeCycle lifeCycle;

    public LifeCycle getLifeCycle() {
        return lifeCycle;
    }

    public void setLifeCycle(LifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    //-----------------------------------------------------------
/*
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="validFrom", column = @Column(name = "locationAddressValidFrom")),
            @AttributeOverride(name="roadName", column = @Column(name = "locationAddressRoadName")),
            @AttributeOverride(name="roadCode", column = @Column(name = "locationAddressRoadCode")),
            @AttributeOverride(name="housenumberFrom", column = @Column(name = "locationAddressHouseNumberFrom")),
            @AttributeOverride(name="housenumberTo", column = @Column(name = "locationAddressHouseNumberTo")),
            @AttributeOverride(name="letterFrom", column = @Column(name = "locationAddressLetterFrom")),
            @AttributeOverride(name="letterTo", column = @Column(name = "locationAddressLetterTo")),
            @AttributeOverride(name="floor", column = @Column(name = "locationAddressFloor")),
            @AttributeOverride(name="sideOrDoor", column = @Column(name = "locationAddressSideOrDoor")),
            @AttributeOverride(name="postalCode", column = @Column(name = "locationAddressPostalCode")),
            @AttributeOverride(name="postalDistrict", column = @Column(name = "locationAddressPostalDistrict")),
            @AttributeOverride(name="cityName", column = @Column(name = "locationAddressCityName")),
            @AttributeOverride(name="municipalityCode", column = @Column(name = "locationAddressMunicipalityCode")),
            @AttributeOverride(name="municipalityText", column = @Column(name = "locationAddressMunicipalityText")),
            @AttributeOverride(name="postBox", column = @Column(name = "locationAddressPostbox")),
            @AttributeOverride(name="coName", column = @Column(name = "locationAddressCoName")),
            @AttributeOverride(name="freetextAddress", column = @Column(name = "locationAddressFreetext")),
            @AttributeOverride(name="descriptor", column = @Column(name = "locationAddressDescriptor")),
    })
    @AssociationOverrides({
            @AssociationOverride(name="enhedsAdresse", joinColumns = @JoinColumn(name = "locationAddressEnhedsAdresse")),

    })
    private CvrAddress locationAddress;

    public CvrAddress getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(CvrAddress locationAddress) {
        this.locationAddress = locationAddress;
    }

    //-----------------------------------------------------------

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="validFrom", column = @Column(name = "postalAddressValidFrom")),
            @AttributeOverride(name="roadName", column = @Column(name = "postalAddressRoadName")),
            @AttributeOverride(name="roadCode", column = @Column(name = "postalAddressRoadCode")),
            @AttributeOverride(name="housenumberFrom", column = @Column(name = "postalAddressHouseNumberFrom")),
            @AttributeOverride(name="housenumberTo", column = @Column(name = "postalAddressHouseNumberTo")),
            @AttributeOverride(name="letterFrom", column = @Column(name = "postalAddressLetterFrom")),
            @AttributeOverride(name="letterTo", column = @Column(name = "postalAddressLetterTo")),
            @AttributeOverride(name="floor", column = @Column(name = "postalAddressFloor")),
            @AttributeOverride(name="sideOrDoor", column = @Column(name = "postalAddressSideOrDoor")),
            @AttributeOverride(name="postalCode", column = @Column(name = "postalAddressPostalCode")),
            @AttributeOverride(name="postalDistrict", column = @Column(name = "postalAddressPostalDistrict")),
            @AttributeOverride(name="cityName", column = @Column(name = "postalAddressCityName")),
            @AttributeOverride(name="municipalityCode", column = @Column(name = "postalAddressMunicipalityCode")),
            @AttributeOverride(name="municipalityText", column = @Column(name = "postalAddressMunicipalityText")),
            @AttributeOverride(name="postBox", column = @Column(name = "postalAddressPostbox")),
            @AttributeOverride(name="coName", column = @Column(name = "postalAddressCoName")),
            @AttributeOverride(name="freetextAddress", column = @Column(name = "postalAddressFreetext")),
            @AttributeOverride(name="descriptor", column = @Column(name = "postalAddressDescriptor")),
    })
    @AssociationOverrides({
            @AssociationOverride(name="enhedsAdresse", joinColumns = @JoinColumn(name = "postalAddressEnhedsAdresse")),

    })
    private CvrAddress postalAddress;

    public CvrAddress getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(CvrAddress postalAddress) {
        this.postalAddress = postalAddress;
    }
*/

    //----------------------------------------------------

    private YearlyEmployeeNumbers yearlyEmployeeNumbers;

    public YearlyEmployeeNumbers getYearlyEmployeeNumbers() {
        return yearlyEmployeeNumbers;
    }

    public void setYearlyEmployeeNumbers(YearlyEmployeeNumbers yearlyEmployeeNumbers) {
        this.yearlyEmployeeNumbers = yearlyEmployeeNumbers;
    }

    //----------------------------------------------------

    private QuarterlyEmployeeNumbers quarterlyEmployeeNumbers;

    public QuarterlyEmployeeNumbers getQuarterlyEmployeeNumbers() {
        return quarterlyEmployeeNumbers;
    }

    public void setQuarterlyEmployeeNumbers(QuarterlyEmployeeNumbers quarterlyEmployeeNumbers) {
        this.quarterlyEmployeeNumbers = quarterlyEmployeeNumbers;
    }

    //----------------------------------------------------

    public CompanySharedData() {
        //this.lifeCycle = new LifeCycle();
        //this.locationAddress = new CvrAddress();
        //this.postalAddress = new CvrAddress();
        this.yearlyEmployeeNumbers = new YearlyEmployeeNumbers();
        this.quarterlyEmployeeNumbers = new QuarterlyEmployeeNumbers();
    }

}
