package dk.magenta.datafordeler.cvr.data.company;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dk.magenta.datafordeler.core.database.Entity;
import dk.magenta.datafordeler.core.database.Identification;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import java.util.UUID;

/**
 * Created by lars on 16-05-17.
 */
@javax.persistence.Entity
@Table(name="cvr_company_entity")
public class CompanyEntity extends Entity<CompanyEntity, CompanyRegistration> {

    @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="type")
    public static final String schema = "virksomhed";

    public CompanyEntity() {
    }

    public CompanyEntity(int cvrNumber) {
        this.cvrNumber = cvrNumber;
    }

    public CompanyEntity(Identification identification, int cvrNumber) {
        super(identification);
        this.cvrNumber = cvrNumber;
    }

    public CompanyEntity(UUID uuid, String domain, int cvrNumber) {
        super(uuid, domain);
        this.cvrNumber = cvrNumber;
    }

    @Column
    private int cvrNumber;

    @JsonProperty
    @XmlElement
    public int getCvrNumber() {
        return cvrNumber;
    }

    public void setCvrNumber(int cvrNumber) {
        this.cvrNumber = cvrNumber;
    }
}
