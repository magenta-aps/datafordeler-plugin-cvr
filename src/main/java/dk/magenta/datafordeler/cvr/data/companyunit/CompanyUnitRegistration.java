package dk.magenta.datafordeler.cvr.data.companyunit;

import dk.magenta.datafordeler.core.database.Registration;

import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAccessor;

/**
 * Created by lars on 16-05-17.
 */
@javax.persistence.Entity
@Table(name="cvr_companyunit_registration")
public class CompanyUnitRegistration extends Registration<CompanyUnitEntity, CompanyUnitRegistration, CompanyUnitEffect> {
    public CompanyUnitRegistration() {
        super();
    }

    public CompanyUnitRegistration(OffsetDateTime registrationFrom, OffsetDateTime registrationTo, int sequenceNumber) {
        super(registrationFrom, registrationTo, sequenceNumber);
    }

    public CompanyUnitRegistration(TemporalAccessor registrationFrom, TemporalAccessor registrationTo, int sequenceNumber) {
        super(registrationFrom, registrationTo, sequenceNumber);
    }

    public CompanyUnitRegistration(String registrationFrom, String registrationTo, int sequenceNumber) {
        super(registrationFrom, registrationTo, sequenceNumber);
    }

    @Override
    protected CompanyUnitEffect createEmptyEffect(OffsetDateTime effectFrom, OffsetDateTime effectTo) {
        return new CompanyUnitEffect(this, effectFrom, effectTo);
    }
}
