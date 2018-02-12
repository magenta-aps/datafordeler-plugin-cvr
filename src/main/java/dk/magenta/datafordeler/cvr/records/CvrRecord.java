package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.DataItem;
import dk.magenta.datafordeler.core.database.DatabaseEntry;
import dk.magenta.datafordeler.core.exception.ParseException;
import dk.magenta.datafordeler.core.util.ListHashMap;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantBaseData;
import org.hibernate.Session;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collection;

/**
 * A CVR record is the object representation of a node in our input data,
 * holding at least some bitemporality, and (in subclasses) some data that take effect within this bitemporality
 */
public abstract class CvrRecord extends DatabaseEntry {

    public CvrRecord() {
    }

    public void populateBaseData(DataItem baseData, Session session, OffsetDateTime timestamp) throws ParseException {
        if (baseData instanceof CompanyBaseData) {
            CompanyBaseData companyBaseData = (CompanyBaseData) baseData;
            this.populateBaseData(companyBaseData, session);
        }
        if (baseData instanceof CompanyUnitBaseData) {
            CompanyUnitBaseData companyUnitBaseData = (CompanyUnitBaseData) baseData;
            this.populateBaseData(companyUnitBaseData, session);
        }
        if (baseData instanceof ParticipantBaseData) {
            ParticipantBaseData participantBaseData = (ParticipantBaseData) baseData;
            this.populateBaseData(participantBaseData, session);
        }
    }

    public void populateBaseData(CompanyBaseData baseData, Session session) throws ParseException {}

    public void populateBaseData(CompanyUnitBaseData baseData, Session session) throws ParseException {}

    public void populateBaseData(ParticipantBaseData baseData, Session session) throws ParseException {}

    public void save(Session session) {
        session.save(this);
    }
}
