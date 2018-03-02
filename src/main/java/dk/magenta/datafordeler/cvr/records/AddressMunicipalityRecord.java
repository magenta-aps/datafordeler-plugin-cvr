package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.DatabaseEntry;
import dk.magenta.datafordeler.cvr.data.unversioned.Municipality;
import org.hibernate.Session;

import javax.persistence.*;

/**
 * Record for Company form.
 */
@Entity
@Table(name = "cvr_record_address_municipality", indexes = {
        @Index(name = "cvr_record_address_municipality_municipality", columnList = AddressMunicipalityRecord.DB_FIELD_MUNICIPALITY + DatabaseEntry.REF)
})
public class AddressMunicipalityRecord extends CvrBitemporalRecord {

    public static final String IO_FIELD_MUNICIPALITY_CODE = "kommuneKode";

    @Transient
    @JsonProperty(value = IO_FIELD_MUNICIPALITY_CODE)
    private int municipalityCode;

    public int getMunicipalityCode() {
        if (this.municipality != null) {
            return this.municipality.getCode();
        }
        return this.municipalityCode;
    }

    public void setMunicipalityCode(int municipalityCode) {
        System.out.println("setMunicipalityCode "+municipalityCode+ " on "+System.identityHashCode(this));
        this.municipalityCode = municipalityCode;
    }

    public static final String IO_FIELD_MUNICIPALITY_NAME = "kommuneNavn";

    @Transient
    @JsonProperty(value = IO_FIELD_MUNICIPALITY_NAME)
    private String municipalityName;

    public String getMunicipalityName() {
        if (this.municipality != null) {
            return this.municipality.getName();
        }
        return this.municipalityName;
    }

    public void setMunicipalityName(String municipalityName) {
        System.out.println("setMunicipalityName "+municipalityName);
        this.municipalityName = municipalityName;
    }

    public static final String DB_FIELD_MUNICIPALITY = "municipality";

    @ManyToOne(targetEntity = Municipality.class)
    @JoinColumn(name = DB_FIELD_MUNICIPALITY + DatabaseEntry.REF)
    @JsonIgnore
    private Municipality municipality;

    public Municipality getMunicipality() {
        return this.municipality;
    }

    public void wire(Session session) {
        if (this.municipalityCode != 0 && (this.municipality == null || this.municipality.getCode() != this.municipalityCode)) {
            this.municipality = Municipality.getMunicipality(this.municipalityCode, this.municipalityName, session);
        }
    }

}
