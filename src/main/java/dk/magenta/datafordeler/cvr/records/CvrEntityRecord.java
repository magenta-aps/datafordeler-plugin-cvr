package dk.magenta.datafordeler.cvr.records;

import java.util.List;

public abstract class CvrEntityRecord extends CvrBaseRecord {
    public abstract List<CvrBaseRecord> getAll();
}
