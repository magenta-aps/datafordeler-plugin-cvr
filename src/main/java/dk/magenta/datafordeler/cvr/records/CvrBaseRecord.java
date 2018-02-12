package dk.magenta.datafordeler.cvr.records;

import dk.magenta.datafordeler.core.database.DataItem;
import dk.magenta.datafordeler.core.exception.ParseException;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantBaseData;
import org.hibernate.Session;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.time.OffsetDateTime;

/**
 * A superclass for data-holding records, able to populate Cvr DataItems (thus updating the DB)
 */
public abstract class CvrBaseRecord extends CvrRecord {


}
