package dk.magenta.datafordeler.cvr;

import dk.magenta.datafordeler.core.database.DataItem;
import dk.magenta.datafordeler.core.database.LookupDefinition;
import dk.magenta.datafordeler.core.fapi.Query;

import java.util.StringJoiner;

public class CvrRecordLookupDefinition extends LookupDefinition {

    public CvrRecordLookupDefinition(Class<? extends DataItem> dataClass) {
        super(dataClass);
    }

    public CvrRecordLookupDefinition(Query query, Class<? extends DataItem> dataClass) {
        super(query, dataClass);
    }

    @Override
    public boolean usingRVDModel() {
        return false;
    }

    @Override
    public String getHqlWhereString(String dataItemKey, String entityKey, String prefix) {
        StringJoiner extraWhere = new StringJoiner(" AND ");
        for (FieldDefinition fieldDefinition : this.fieldDefinitions) {
            extraWhere.add("(" + this.getHqlWherePart(dataItemKey, entityKey, fieldDefinition, true) + ")");
        }
        if (extraWhere.length() > 0) {
            return prefix + " " + extraWhere.toString();
        }
        return "";
    }

}
