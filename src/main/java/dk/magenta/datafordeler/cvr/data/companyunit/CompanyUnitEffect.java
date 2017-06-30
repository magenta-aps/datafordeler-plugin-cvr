package dk.magenta.datafordeler.cvr.data.companyunit;

import dk.magenta.datafordeler.core.database.Effect;
import dk.magenta.datafordeler.cvr.data.company.CompanyRegistration;

import javax.persistence.Table;
import java.time.LocalDate;
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

    public CompanyUnitEffect(CompanyUnitRegistration registration, LocalDate effectFrom, LocalDate effectTo) {
        super(registration, effectFrom, effectTo);
    }

    public CompanyUnitEffect(CompanyUnitRegistration registration, String effectFrom, String effectTo) {
        super(registration, effectFrom, effectTo);
    }
}
