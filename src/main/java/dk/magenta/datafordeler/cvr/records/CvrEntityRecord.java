package dk.magenta.datafordeler.cvr.records;

import javax.persistence.MappedSuperclass;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class CvrEntityRecord extends CvrBitemporalRecord {
    public abstract List<CvrRecord> getAll();

    public List<CvrRecord> getSince(OffsetDateTime time) {
        ArrayList<CvrRecord> newer = new ArrayList<>();
        for (CvrRecord record : this.getAll()) {
            if (record instanceof CvrBitemporalRecord) {
                OffsetDateTime recordTime = ((CvrBitemporalRecord) record).getLastUpdated();
                if (recordTime == null || time == null || !recordTime.isBefore(time)) {
                    newer.add(record);
                }
            }
        }
        return newer;
    }
}
