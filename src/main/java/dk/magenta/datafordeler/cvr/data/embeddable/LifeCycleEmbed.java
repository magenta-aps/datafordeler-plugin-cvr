package dk.magenta.datafordeler.cvr.data.embeddable;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import java.time.OffsetDateTime;
import java.util.Date;

/**
 * Created by jubk on 04-03-2015.
 */
@Embeddable
@JsonInclude(JsonInclude.Include.NON_NULL)
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

}
