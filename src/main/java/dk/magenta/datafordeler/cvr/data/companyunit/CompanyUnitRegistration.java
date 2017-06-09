package dk.magenta.datafordeler.cvr.data.companyunit;

import dk.magenta.datafordeler.core.database.Registration;

import javax.persistence.Table;

/**
 * Created by lars on 16-05-17.
 */
@javax.persistence.Entity
@Table(name="cvr_companyunit_registration")
public class CompanyUnitRegistration extends Registration<CompanyUnitEntity, CompanyUnitRegistration, CompanyUnitEffect> {
}
