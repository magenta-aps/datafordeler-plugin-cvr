package dk.magenta.datafordeler.cvr.data.companyunit;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dk.magenta.datafordeler.core.database.Identification;
import dk.magenta.datafordeler.cvr.data.CvrEntity;

import javax.persistence.Column;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import java.util.UUID;

/**
 * Created by lars on 16-05-17.
 */
@javax.persistence.Entity
@Table(name="cvr_companyunit_entity", indexes = {
        @Index(name = "cvr_companyunit_identification", columnList = "identification_id"),
        @Index(name = "cvr_companyunit_pNumber", columnList = "pNumber")
})
public class CompanyUnitEntity extends CvrEntity<CompanyUnitEntity, CompanyUnitRegistration> {

    @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="type")
    public static final String schema = "produktionsenhed";

    public CompanyUnitEntity() {
    }

    public CompanyUnitEntity(Identification identification) {
        super(identification);
    }

    public CompanyUnitEntity(UUID uuid, String domain) {
        super(uuid, domain);
    }

    @Override
    protected CompanyUnitRegistration createEmptyRegistration() {
        return new CompanyUnitRegistration();
    }

    @Column
    private int pNumber;

    @JsonProperty(value = "pNumber")
    @XmlElement(name = "pNumber")
    public int getPNumber() {
        return pNumber;
    }

    public void setPNumber(int pNumber) {
        this.pNumber = pNumber;
    }

    public static UUID generateUUID(int pNumber) {
        String uuidInput = "companyunit:"+pNumber;
        return UUID.nameUUIDFromBytes(uuidInput.getBytes());
    }
}
