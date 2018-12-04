package dk.magenta.datafordeler.cvr.output;

import dk.magenta.datafordeler.cvr.records.CvrBitemporalRecord;
import dk.magenta.datafordeler.cvr.records.CvrEntityRecord;

import java.util.Arrays;
import java.util.List;

public abstract class CvrRecordOutputWrapper<E extends CvrEntityRecord> extends RecordOutputWrapper<E> {
    
    private final List<String> removeFieldNames = Arrays.asList(new String[]{
            CvrBitemporalRecord.IO_FIELD_PERIOD,
            CvrBitemporalRecord.IO_FIELD_LAST_UPDATED,
            CvrBitemporalRecord.IO_FIELD_LAST_LOADED,
            CvrBitemporalRecord.IO_FIELD_DAFO_UPDATED
    });

    public List<String> getRemoveFieldNames() {
        return this.removeFieldNames;
    }
}
