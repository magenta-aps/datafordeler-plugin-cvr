package dk.magenta.datafordeler.cvr.data.companyunit;

import dk.magenta.datafordeler.core.database.Effect;

import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAccessor;

/**
 * Created by lars on 16-05-17.
 */
@javax.persistence.Entity
@Table(name="cvr_companyunit_effect")
public class CompanyUnitEffect extends Effect<CompanyUnitRegistration, CompanyUnitEffect, CompanyUnitBaseData> {
    public CompanyUnitEffect() {
        super();
    }

    public CompanyUnitEffect(CompanyUnitRegistration registration, OffsetDateTime effectFrom, OffsetDateTime effectTo) {
        super(registration, effectFrom, effectTo);
    }

    public CompanyUnitEffect(CompanyUnitRegistration registration, TemporalAccessor effectFrom, TemporalAccessor effectTo) {
        super(registration, effectFrom, effectTo);
    }

    public CompanyUnitEffect(CompanyUnitRegistration registration, String effectFrom, String effectTo) {
        super(registration, effectFrom, effectTo);
    }
}
