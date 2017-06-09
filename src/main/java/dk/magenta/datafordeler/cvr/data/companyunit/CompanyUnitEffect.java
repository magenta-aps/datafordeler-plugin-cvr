package dk.magenta.datafordeler.cvr.data.companyunit;

import dk.magenta.datafordeler.core.database.Effect;

import javax.persistence.Table;

/**
 * Created by lars on 16-05-17.
 */
@javax.persistence.Entity
@Table(name="cvr_companyunit_effect")
public class CompanyUnitEffect extends Effect<CompanyUnitRegistration, CompanyUnitEffect, CompanyUnitData> {
    // Each effect has an effectFrom and an effectTo timestamp, both java.time.OffsetDateTime, with setters
    // Also, a collection of CompanyData instances
}
