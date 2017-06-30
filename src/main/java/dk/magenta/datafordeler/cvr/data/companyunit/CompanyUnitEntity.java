package dk.magenta.datafordeler.cvr.data.companyunit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dk.magenta.datafordeler.core.database.Entity;
import dk.magenta.datafordeler.core.database.Identification;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.UUID;

/**
 * Created by lars on 16-05-17.
 */
@javax.persistence.Entity
@Table(name="cvr_companyunit_entity")
public class CompanyUnitEntity extends Entity<CompanyUnitEntity, CompanyUnitRegistration> {

    @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="type")
    public static final String schema = "CompanyUnit";

    public CompanyUnitEntity() {
    }

    public CompanyUnitEntity(int pNumber) {
        this.pNumber = pNumber;
    }

    public CompanyUnitEntity(Identification identification, int pNumber) {
        super(identification);
        this.pNumber = pNumber;
    }

    public CompanyUnitEntity(UUID uuid, String domain, int pNumber) {
        super(uuid, domain);
        this.pNumber = pNumber;
    }

    @Column
    @JsonProperty
    @XmlElement
    private int pNumber;

    @JsonProperty(value = "pNumber")
    @XmlElement(name = "pNumber")
    public int getPNumber() {
        return pNumber;
    }

    public void setPNumber(int pNumber) {
        this.pNumber = pNumber;
    }
}
