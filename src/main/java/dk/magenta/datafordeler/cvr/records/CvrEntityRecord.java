package dk.magenta.datafordeler.cvr.records;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class CvrEntityRecord extends CvrBaseRecord {
    public abstract List<CvrBaseRecord> getAll();

    public List<CvrBaseRecord> getSince(OffsetDateTime time) {
        ArrayList<CvrBaseRecord> newer = new ArrayList<>();
        for (CvrBaseRecord record : this.getAll()) {
            OffsetDateTime recordTime = record.getLastUpdated();
            if (recordTime == null || time == null || !recordTime.isBefore(time)) {
                newer.add(record);
            }
        }
        return newer;
    }
}
