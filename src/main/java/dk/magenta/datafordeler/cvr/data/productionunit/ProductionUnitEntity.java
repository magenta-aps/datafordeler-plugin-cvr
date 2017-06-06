package dk.magenta.datafordeler.cvr.data.productionunit;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dk.magenta.datafordeler.core.database.Entity;

import javax.persistence.Table;

/**
 * Created by lars on 16-05-17.
 */
@javax.persistence.Entity
@Table(name="cvr_productionunit_entity")
public class ProductionUnitEntity extends Entity<ProductionUnitEntity, ProductionUnitRegistration> {

    @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="type")
    public static final String schema = "ProductionUnit";

}
