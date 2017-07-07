package dk.magenta.datafordeler.cvr.data.unversioned;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
        @Index(name = "companyRoadCode", columnList = "vejkode"),
        @Index(name = "companyRoadName", columnList = "vejnavn")
})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Address extends UnversionedEntity {


    @JsonProperty(value = "adresseId")
    @XmlElement(name = "adresseId")
    @Column
    private String adresseId;

    public String getAdresseId() {
        return this.adresseId;
    }

    public void setAdresseId(String adresseId) {
        this.adresseId = adresseId;
    }



    @JsonProperty(value = "vejkode")
    @XmlElement(name = "vejkode")
    @Column
    private String vejkode;

    public String getVejkode() {
        return this.vejkode;
    }

    public void setVejkode(String vejkode) {
        this.vejkode = vejkode;
    }



    @JsonProperty(value = "supplerendeBynavn")
    @XmlElement(name = "supplerendeBynavn")
    @Column
    private String supplerendeBynavn;

    public String getSupplerendeBynavn() {
        return this.supplerendeBynavn;
    }

    public void setSupplerendeBynavn(String supplerendeBynavn) {
        this.supplerendeBynavn = supplerendeBynavn;
    }



    @JsonProperty(value = "vejnavn")
    @XmlElement(name = "vejnavn")
    @Column
    private String vejnavn;

    public String getVejnavn() {
        return this.vejnavn;
    }

    public void setVejnavn(String roadName) {
        this.vejnavn = roadName;
    }



    @JsonProperty(value = "husnummerFra")
    @XmlElement(name = "husnummerFra")
    @Column
    private String husnummerFra;

    public String getHusnummerFra() {
        return this.husnummerFra;
    }

    public void setHusnummerFra(String husnummerFra) {
        this.husnummerFra = husnummerFra;
    }



    @JsonProperty(value = "husnummerTil")
    @XmlElement(name = "husnummerTil")
    @Column
    private String husnummerTil;

    public String getHusnummerTil() {
        return this.husnummerTil;
    }

    public void setHusnummerTil(String husnummerTil) {
        this.husnummerTil = husnummerTil;
    }



    @JsonProperty(value = "bogstavFra")
    @XmlElement(name = "bogstavFra")
    @Column
    private String bogstavFra;

    public String getBogstavFra() {
        return this.bogstavFra;
    }

    public void setBogstavFra(String bogstavFra) {
        this.bogstavFra = bogstavFra;
    }



    @JsonProperty(value = "bogstavTil")
    @XmlElement(name = "bogstavTil")
    @Column
    private String bogstavTil;

    public String getBogstavTil() {
        return this.bogstavTil;
    }

    public void setBogstavTil(String bogstavTil) {
        this.bogstavTil = bogstavTil;
    }



    @JsonProperty(value = "etagebetegnelse")
    @XmlElement(name = "etagebetegnelse")
    @Column(name="etagebetegnelse")
    private String etagebetegnelse;

    public String getEtagebetegnelse() {
        return this.etagebetegnelse;
    }

    public void setEtagebetegnelse(String etagebetegnelse) {
        this.etagebetegnelse = etagebetegnelse;
    }

    @JsonProperty(value = "dørbetegnelse")
    @XmlElement(name = "dørbetegnelse")
    @Column
    private String dørbetegnelse;

    public String getDørbetegnelse() {
        return this.dørbetegnelse;
    }

    public void setDørbetegnelse(String dørbetegnelse) {
        this.dørbetegnelse = dørbetegnelse;
    }



    @JsonProperty(value = "kommune")
    @XmlElement(name = "kommune")
    @ManyToOne
    @Cascade(value = CascadeType.SAVE_UPDATE)
    private Municipality kommune;

    public Municipality getKommune() {
        return this.kommune;
    }

    public void setKommune(Municipality kommune) {
        this.kommune = kommune;
    }



    @JsonProperty(value = "post")
    @XmlElement(name = "post")
    @ManyToOne
    @Cascade(value = CascadeType.SAVE_UPDATE)
    private PostCode post;

    public PostCode getPost() {
        return this.post;
    }

    public void setPost(PostCode postCode) {
        this.post = postCode;
    }


    @JsonIgnore
    @XmlTransient
    private int postnummer;

    @JsonIgnore
    @XmlTransient
    private String postdistrikt;

    @JsonProperty(value = "postnummer")
    public void setPostnummer(int code) {
        this.postnummer = code;
    }

    @JsonIgnore
    public int getPostnummer() {
        return this.postnummer;
    }

    @JsonProperty("postdistrikt")
    public void setPostdistrikt(String district) {
        this.postdistrikt = district;
    }

    @JsonIgnore
    public String getPostdistrikt() {
        return this.postdistrikt;
    }

    @JsonProperty(value = "postboks")
    @XmlElement(name = "postboks")
    @Column
    private int postboks;

    public int getPostboks() {
        return this.postboks;
    }

    public void setPostboks(int postboks) {
        this.postboks = postboks;
    }



    @JsonProperty(value = "conavn")
    @XmlElement(name = "conavn")
    @Column
    private String conavn;

    public String getConavn() {
        return this.conavn;
    }

    public void setConavn(String conavn) {
        this.conavn = conavn;
    }



    @JsonProperty(value = "landekode")
    @XmlElement(name = "landekode")
    @Column
    private String landekode;

    public String getLandekode() {
        return this.landekode;
    }

    public void setLandekode(String landekode) {
        this.landekode = landekode;
    }



    @JsonProperty(value = "adresseFritekst")
    @XmlElement(name = "adresseFritekst")
    @Column(name="adresseFritekst")
    private String adresseFritekst;

    public String getAdresseFritekst() {
        return this.adresseFritekst;
    }

    public void setAdresseFritekst(String adresseFritekst) {
        this.adresseFritekst = adresseFritekst;
    }



    @JsonProperty(value = "sidstValideret")
    @XmlElement(name = "sidstValideret")
    @Column
    private String sidstValideret;

    public String getSidstValideret() {
        return this.sidstValideret;
    }

    public void setSidstValideret(String sidstValideret) {
        this.sidstValideret = sidstValideret;
    }

}
