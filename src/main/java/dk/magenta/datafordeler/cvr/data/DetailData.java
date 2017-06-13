package dk.magenta.datafordeler.cvr.data;

import dk.magenta.datafordeler.core.database.DatabaseEntry;

import javax.persistence.*;
import java.util.Map;

/**
 * Created by lars on 12-06-17.
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class DetailData extends DatabaseEntry {
    public abstract Map<String, Object> asMap();
}
