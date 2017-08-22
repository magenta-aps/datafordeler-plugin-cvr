package dk.magenta.datafordeler.cvr.records;

import java.util.List;

/**
 * Created by lars on 26-06-17.
 */
public abstract class EntityRecord extends BaseRecord {
    public abstract List<BaseRecord> getAll();
}
