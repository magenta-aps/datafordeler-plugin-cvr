package dk.magenta.datafordeler.cvr.data.companyunit;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dk.magenta.datafordeler.core.database.Entity;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by lars on 16-05-17.
 */
@javax.persistence.Entity
@Table(name="cvr_companyunit_entity")
public class CompanyUnitEntity extends Entity<CompanyUnitEntity, CompanyUnitRegistration> {

    @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="type")
    public static final String schema = "CompanyUnit";

    @Column
    @JsonProperty
    @XmlElement
    private int pNumber;

    public int getPNumber() {
        return pNumber;
    }

    public void setPNumber(int pNumber) {
        this.pNumber = pNumber;
    }
}
