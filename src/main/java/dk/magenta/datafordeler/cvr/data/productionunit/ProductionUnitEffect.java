package dk.magenta.datafordeler.cvr.data.productionunit;

import dk.magenta.datafordeler.core.database.Effect;

import javax.persistence.Table;

/**
 * Created by lars on 16-05-17.
 */
@javax.persistence.Entity
@Table(name="cvr_productionunit_effect")
public class ProductionUnitEffect extends Effect<ProductionUnitRegistration, ProductionUnitEffect, ProductionUnitData> {
    // Each effect has an effectFrom and an effectTo timestamp, both java.time.OffsetDateTime, with setters
    // Also, a collection of CompanyData instances
}
