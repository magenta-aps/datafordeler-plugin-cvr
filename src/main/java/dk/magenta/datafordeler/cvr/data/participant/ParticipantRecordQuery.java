package dk.magenta.datafordeler.cvr.data.participant;

import dk.magenta.datafordeler.core.database.LookupDefinition;
import dk.magenta.datafordeler.core.fapi.ParameterMap;
import dk.magenta.datafordeler.core.fapi.QueryField;
import dk.magenta.datafordeler.cvr.CvrRecordLookupDefinition;
import dk.magenta.datafordeler.cvr.data.CvrQuery;
import dk.magenta.datafordeler.cvr.data.shared.AddressData;
import dk.magenta.datafordeler.cvr.data.shared.IntegerData;
import dk.magenta.datafordeler.cvr.data.shared.TextData;
import dk.magenta.datafordeler.cvr.data.unversioned.Address;
import dk.magenta.datafordeler.cvr.data.unversioned.Municipality;
import dk.magenta.datafordeler.cvr.records.AddressMunicipalityRecord;
import dk.magenta.datafordeler.cvr.records.AddressRecord;
import dk.magenta.datafordeler.cvr.records.NameRecord;
import dk.magenta.datafordeler.cvr.records.ParticipantRecord;

import java.util.*;

/**
 * Container for a query for Participants, defining fields and database lookup
 */
public class ParticipantRecordQuery extends ParticipantQuery {

    @Override
    public LookupDefinition getLookupDefinition() {
        LookupDefinition lookupDefinition = new CvrRecordLookupDefinition(this, null);

        if (this.getEnhedsNummer() != null) {
            lookupDefinition.put(LookupDefinition.entityref + LookupDefinition.separator + ParticipantRecord.DB_FIELD_UNIT_NUMBER, this.getEnhedsNummer(), Long.class);
        }

        if (this.getNavne() != null) {
            lookupDefinition.put(LookupDefinition.entityref + LookupDefinition.separator + ParticipantRecord.DB_FIELD_NAMES + LookupDefinition.separator + NameRecord.DB_FIELD_NAME, this.getNavne(), String.class);
        }

        if (!this.getKommunekoder().isEmpty()) {
            StringJoiner sj = new StringJoiner(LookupDefinition.separator);
            sj.add(LookupDefinition.entityref);
            sj.add(ParticipantRecord.DB_FIELD_LOCATION_ADDRESS);
            sj.add(AddressRecord.DB_FIELD_MUNICIPALITY);
            sj.add(AddressMunicipalityRecord.DB_FIELD_MUNICIPALITY);
            sj.add(Municipality.DB_FIELD_CODE);
            lookupDefinition.put(sj.toString(), this.getKommunekoder(), Integer.class);
        }
        if (!this.getKommunekodeRestriction().isEmpty()) {
            StringJoiner sj = new StringJoiner(LookupDefinition.separator);
            sj.add(LookupDefinition.entityref);
            sj.add(ParticipantRecord.DB_FIELD_LOCATION_ADDRESS);
            sj.add(AddressRecord.DB_FIELD_MUNICIPALITY);
            sj.add(AddressMunicipalityRecord.DB_FIELD_MUNICIPALITY);
            sj.add(Municipality.DB_FIELD_CODE);
            lookupDefinition.put(sj.toString(), this.getKommunekodeRestriction(), Integer.class);
        }
        return lookupDefinition;
    }

}
