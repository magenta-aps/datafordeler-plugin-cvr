package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.Identification;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.cvr.CvrPlugin;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitEntity;
import org.hibernate.Session;

import java.util.UUID;

/**
 * Created by lars on 26-06-17.
 */
public class CompanyUnitLinkRecord extends CvrBaseRecord {

    @JsonProperty(value = "pNummer")
    private int pNumber;

    @Override
    public void populateBaseData(CompanyBaseData baseData, Session session) {
        baseData.addCompanyUnit(this.pNumber, this.getUnitIdentification(session));
    }

    private Identification getUnitIdentification(Session session) {
        UUID unitUUID = CompanyUnitEntity.generateUUID(this.pNumber);
        Identification unitIdentification = QueryManager.getIdentification(session, unitUUID);
        if (unitIdentification == null) {
            unitIdentification = new Identification(unitUUID, CvrPlugin.getDomain());
            session.save(unitIdentification);
        }
        return unitIdentification;
    }

}
