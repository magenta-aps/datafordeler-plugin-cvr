package dk.magenta.datafordeler.cvr.data.company;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
@JsonPropertyOrder({"cvrNumber", "uuid", "domain"})
public class CompanyEntity extends Entity<CompanyEntity, CompanyRegistration> {

    @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="type")
    public static final String schema = "virksomhed";

    public CompanyEntity() {
    }

    public CompanyEntity(Identification identification) {
        super(identification);
    }

    public CompanyEntity(UUID uuid, String domain) {
        super(uuid, domain);
    }

    @Override
    public CompanyRegistration createEmptyRegistration() {
        return new CompanyRegistration();
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

    public static UUID generateUUID(int cvrNumber) {
        String uuidInput = "company:"+cvrNumber;
        return UUID.nameUUIDFromBytes(uuidInput.getBytes());
    }
}
