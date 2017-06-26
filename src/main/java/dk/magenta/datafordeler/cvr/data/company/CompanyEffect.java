package dk.magenta.datafordeler.cvr.data.company;

import dk.magenta.datafordeler.core.database.Effect;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAccessor;

/**
 * Created by lars on 16-05-17.
 */
@Entity
@Table(name="cvr_company_effect")
public class CompanyEffect extends Effect<CompanyRegistration, CompanyEffect, CompanyBaseData> {
    public CompanyEffect() {
    }

    public CompanyEffect(CompanyRegistration registration, OffsetDateTime effectFrom, OffsetDateTime effectTo) {
        super(registration, effectFrom, effectTo);
    }

    public CompanyEffect(CompanyRegistration registration, LocalDate effectFrom, LocalDate effectTo) {
        super(registration, effectFrom, effectTo);
    }
/*
    public CompanyEffect(CompanyRegistration registration, TemporalAccessor effectFrom, TemporalAccessor effectTo) {
        super(registration, effectFrom, effectTo);
    }
*/

    public CompanyEffect(CompanyRegistration registration, String effectFrom, String effectTo) {
        super(registration, effectFrom, effectTo);
    }
}
