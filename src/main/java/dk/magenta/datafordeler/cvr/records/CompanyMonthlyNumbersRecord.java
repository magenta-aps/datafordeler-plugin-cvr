package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.company.CompanyEntityManager;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import org.hibernate.Session;

/**
 * Created by lars on 26-06-17.
 */
public class CompanyMonthlyNumbersRecord extends CompanyNumbersRecord {

    @JsonProperty(value = "aar")
    private int year;

    @JsonProperty(value = "maaned")
    private int month;

    @Override
    public void populateBaseData(CompanyBaseData baseData, QueryManager queryManager, Session session) {
        baseData.addMonthlyEmployeeNumbers(
                this.year,
                this.month,
                this.getEmployeeLow(),
                this.getEmployeeHigh(),
                this.getFulltimeEquivalentLow(),
                this.getFulltimeEquivalentHigh(),
                this.getIncludingOwnersLow(),
                this.getIncludingOwnersHigh()
        );
    }

    @Override
    public void populateBaseData(CompanyUnitBaseData baseData, QueryManager queryManager, Session session) {
        baseData.addMonthlyEmployeeNumbers(
                this.year,
                this.month,
                this.getEmployeeLow(),
                this.getEmployeeHigh(),
                this.getFulltimeEquivalentLow(),
                this.getFulltimeEquivalentHigh(),
                this.getIncludingOwnersLow(),
                this.getIncludingOwnersHigh()
        );
    }
}
