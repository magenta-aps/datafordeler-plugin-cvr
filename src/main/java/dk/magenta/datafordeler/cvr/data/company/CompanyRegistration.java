package dk.magenta.datafordeler.cvr.data.company;

import dk.magenta.datafordeler.core.database.Registration;

import javax.persistence.Table;

/**
 * Created by lars on 16-05-17.
 */
@javax.persistence.Entity
@Table(name="cvr_company_registration")
public class CompanyRegistration extends Registration<CompanyEntity, CompanyRegistration, CompanyEffect> {
    // A Registration has a registrationFrom field and a registrationTo field (with setters)
    // Both are of type java.time.OffsetDateTime, and describe the time range within which the contained data is considered authoritative
    // RegistrationTo may be null, indicating that the data is currently authoritative
    // The Registration also contains a list of Effects (here CompanyEffect, as seen in the erasure).
}
