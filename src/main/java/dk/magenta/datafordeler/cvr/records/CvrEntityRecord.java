package dk.magenta.datafordeler.cvr.records;

import java.util.List;

/**
 * Created by lars on 26-06-17.
 */
public abstract class CvrEntityRecord extends CvrBaseRecord {
    public abstract List<CvrBaseRecord> getAll();
}
