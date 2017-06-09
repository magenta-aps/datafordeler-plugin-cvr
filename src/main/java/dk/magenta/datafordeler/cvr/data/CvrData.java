package dk.magenta.datafordeler.cvr.data;

import dk.magenta.datafordeler.core.database.DataItem;
import dk.magenta.datafordeler.core.database.Effect;

import javax.persistence.MappedSuperclass;

/**
 * Created by lars on 19-05-17.
 */
@MappedSuperclass
public abstract class CvrData<V extends Effect, D extends DataItem> extends DataItem<V, D> {
}
