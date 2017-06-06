package dk.magenta.datafordeler.cvr.data.industry;

import dk.magenta.datafordeler.core.database.Effect;

import javax.persistence.Table;

/**
 * Created by lars on 16-05-17.
 */
@javax.persistence.Entity
@Table(name="cvr_industry_effect")
public class IndustryEffect extends Effect<IndustryRegistration, IndustryEffect, IndustryData> {
    // Each effect has an effectFrom and an effectTo timestamp, both java.time.OffsetDateTime, with setters
    // Also, a collection of CompanyData instances
}
