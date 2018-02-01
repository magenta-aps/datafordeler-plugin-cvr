package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.Identification;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.cvr.CvrPlugin;
import dk.magenta.datafordeler.cvr.data.Bitemporality;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitEntity;
import org.hibernate.Session;

import java.util.UUID;

/**
 * Record for Company Unit references.
 */
public class CompanyUnitLinkRecord extends CvrBaseRecord {

    @JsonProperty(value = "pNummer")
    private int pNumber;

    @Override
    public void populateBaseData(CompanyBaseData baseData, Session session, Bitemporality bitemporality) {
        baseData.addCompanyUnit(this.pNumber, this.getUnitIdentification(session));
    }

    private Identification getUnitIdentification(Session session) {
        UUID unitUUID = CompanyUnitEntity.generateUUID(this.pNumber);
        Identification unitIdentification = QueryManager.getOrCreateIdentification(session, unitUUID, CvrPlugin.getDomain());
        return unitIdentification;
    }

}
