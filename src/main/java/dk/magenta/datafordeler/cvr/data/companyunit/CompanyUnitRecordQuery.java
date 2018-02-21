package dk.magenta.datafordeler.cvr.data.companyunit;

import dk.magenta.datafordeler.core.database.LookupDefinition;
import dk.magenta.datafordeler.core.fapi.ParameterMap;
import dk.magenta.datafordeler.core.fapi.QueryField;
import dk.magenta.datafordeler.cvr.CvrRecordLookupDefinition;
import dk.magenta.datafordeler.cvr.data.CvrQuery;
import dk.magenta.datafordeler.cvr.data.shared.AddressData;
import dk.magenta.datafordeler.cvr.data.shared.IndustryData;
import dk.magenta.datafordeler.cvr.data.shared.IntegerData;
import dk.magenta.datafordeler.cvr.data.unversioned.Address;
import dk.magenta.datafordeler.cvr.data.unversioned.Industry;
import dk.magenta.datafordeler.cvr.data.unversioned.Municipality;
import dk.magenta.datafordeler.cvr.records.*;

import java.util.*;

/**
 * Container for a query for Company units, defining fields and database lookup
 */
public class CompanyUnitRecordQuery extends CompanyUnitQuery {

    @Override
    public LookupDefinition getLookupDefinition() {
        LookupDefinition lookupDefinition = new CvrRecordLookupDefinition(this, null);

        if (this.getpNumber() != null) {
            lookupDefinition.put(LookupDefinition.entityref + LookupDefinition.separator + CompanyUnitRecord.DB_FIELD_P_NUMBER, this.getpNumber(), Integer.class);
        }
        if (this.getAssociatedCompanyCvrNumber() != null) {
            lookupDefinition.put(LookupDefinition.entityref + LookupDefinition.separator + CompanyUnitRecord.DB_FIELD_COMPANY_LINK + LookupDefinition.separator + CompanyLinkRecord.DB_FIELD_CVRNUMBER, this.getAssociatedCompanyCvrNumber(), Integer.class);
        }
        if (this.getPrimaryIndustry() != null) {
            lookupDefinition.put(LookupDefinition.entityref + LookupDefinition.separator + CompanyUnitRecord.DB_FIELD_PRIMARY_INDUSTRY + LookupDefinition.separator + CompanyIndustryRecord.DB_FIELD_CODE, this.getPrimaryIndustry(), String.class);
        }
        if (!this.getKommunekoder().isEmpty()) {
            StringJoiner sj = new StringJoiner(LookupDefinition.separator);
            sj.add(LookupDefinition.entityref);
            sj.add(CompanyUnitRecord.DB_FIELD_LOCATION_ADDRESS);
            sj.add(AddressRecord.DB_FIELD_MUNICIPALITY);
            sj.add(AddressMunicipalityRecord.DB_FIELD_MUNICIPALITY);
            sj.add(Municipality.DB_FIELD_CODE);
            lookupDefinition.put(sj.toString(), this.getKommunekoder(), Integer.class);
        }
        if (!this.getKommunekodeRestriction().isEmpty()) {
            StringJoiner sj = new StringJoiner(LookupDefinition.separator);
            sj.add(LookupDefinition.entityref);
            sj.add(CompanyUnitRecord.DB_FIELD_LOCATION_ADDRESS);
            sj.add(AddressRecord.DB_FIELD_MUNICIPALITY);
            sj.add(AddressMunicipalityRecord.DB_FIELD_MUNICIPALITY);
            sj.add(Municipality.DB_FIELD_CODE);
            lookupDefinition.put(sj.toString(), this.getKommunekodeRestriction(), Integer.class);
        }
        return lookupDefinition;
    }

}
