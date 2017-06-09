package dk.magenta.datafordeler.cvr.data.industry;

import dk.magenta.datafordeler.core.database.Registration;

import javax.persistence.Table;

/**
 * Created by lars on 16-05-17.
 */
@javax.persistence.Entity
@Table(name="cvr_industry_registration")
public class IndustryRegistration extends Registration<IndustryEntity, IndustryRegistration, IndustryEffect> {
}
