package dk.magenta.datafordeler.cvr.data.embeddable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.OffsetDateTime;
import java.util.Date;

/**
 * Created by jubk on 04-03-2015.
 */
@Embeddable
public class LifeCycleEmbed {
    @Column(nullable = true)
    private OffsetDateTime startDate;

    public OffsetDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(OffsetDateTime startDate) {
        this.startDate = startDate;
    }

    //----------------------------------------------------

    @Column(nullable = true)
    private OffsetDateTime endDate;

    public OffsetDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(OffsetDateTime endDate) {
        this.endDate = endDate;
    }

/*
    public boolean equals(Object otherObject) {
        if (otherObject == null || otherObject.getClass() != LifeCycle.class) {
            return false;
        }
        return this.equals((LifeCycle) otherObject);
    }
    public boolean equals(LifeCycle otherLifecycle) {
        return Util.compare(this.startDate, otherLifecycle.getStartDate()) &&
                Util.compare(this.endDate, otherLifecycle.getEndDate());
    }*/
}
