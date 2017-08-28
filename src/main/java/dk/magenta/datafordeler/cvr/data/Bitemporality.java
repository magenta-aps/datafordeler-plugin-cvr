package dk.magenta.datafordeler.cvr.data;

import dk.magenta.datafordeler.core.util.Equality;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class Bitemporality {
    public OffsetDateTime registrationFrom = null;
    public OffsetDateTime registrationTo = null;
    public OffsetDateTime effectFrom = null;
    public OffsetDateTime effectTo = null;

    public Bitemporality(OffsetDateTime registrationFrom, OffsetDateTime registrationTo, OffsetDateTime effectFrom, OffsetDateTime effectTo) {
        this.registrationFrom = registrationFrom;
        this.registrationTo = registrationTo;
        this.effectFrom = effectFrom;
        this.effectTo = effectTo;
    }

    public Bitemporality(OffsetDateTime registrationFrom, OffsetDateTime registrationTo) {
        this.registrationFrom = registrationFrom;
        this.registrationTo = registrationTo;
    }

    public Bitemporality(OffsetDateTime registrationFrom) {
        this(registrationFrom, null);
    }

    public Bitemporality(OffsetDateTime registrationFrom, OffsetDateTime registrationTo, LocalDate effectFrom, LocalDate effectTo) {
        this(registrationFrom, registrationTo, effectFrom != null ? effectFrom.atStartOfDay().atOffset(ZoneOffset.UTC) : null, effectTo != null ? effectTo.atStartOfDay().atOffset(ZoneOffset.UTC) : null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bitemporality that = (Bitemporality) o;
        if (registrationFrom != null ? !registrationFrom.equals(that.registrationFrom) : that.registrationFrom != null)
            return false;
        if (effectFrom != null ? !effectFrom.equals(that.effectFrom) : that.effectFrom != null)
            return false;
        return effectTo != null ? effectTo.equals(that.effectTo) : that.effectTo == null;
    }

    @Override
    public int hashCode() {
        int result = registrationFrom != null ? registrationFrom.hashCode() : 0;
        result = 31 * result + (effectFrom != null ? effectFrom.hashCode() : 0);
        result = 31 * result + (effectTo != null ? effectTo.hashCode() : 0);
        return result;
    }

    public boolean matches(OffsetDateTime registrationTime, CvrEffect effect) {
        return Equality.equal(this.registrationFrom, registrationTime) && effect.compareRange(this);
    }
}
