package dk.magenta.datafordeler.cvr.data;

import dk.magenta.datafordeler.core.util.Equality;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

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

    public OffsetDateTime getRegistrationFrom() {
        return this.registrationFrom;
    }

    public OffsetDateTime getRegistrationTo() {
        return this.registrationTo;
    }

    public OffsetDateTime getEffectFrom() {
        return this.effectFrom;
    }

    public OffsetDateTime getEffectTo() {
        return this.effectTo;
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

    public boolean equalRegistration(Bitemporality o) {
        return o != null && Objects.equals(this.registrationFrom, o.registrationFrom) && Objects.equals(this.registrationTo, o.registrationTo);
    }

    public boolean equalEffect(Bitemporality o) {
        return o != null && Objects.equals(this.effectFrom, o.effectFrom) && Objects.equals(this.effectTo, o.effectTo);
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

    public boolean overlapsRegistration(OffsetDateTime rangeStart, OffsetDateTime rangeEnd) {
        return (this.registrationFrom == null || rangeEnd == null || !rangeEnd.isBefore(this.registrationFrom)) && (this.registrationTo == null || rangeStart == null || !rangeStart.isAfter(this.registrationTo));
    }

    public boolean overlapsEffect(OffsetDateTime rangeStart, OffsetDateTime rangeEnd) {
        return (this.effectFrom == null || rangeEnd == null || !rangeEnd.isBefore(this.effectFrom)) && (this.effectTo == null || rangeStart == null || !rangeStart.isAfter(this.effectTo));
    }

    public boolean overlaps(Bitemporality other) {
        return this.overlapsRegistration(other.registrationFrom, other.registrationTo) && this.overlapsEffect(other.effectFrom, other.effectTo);
    }

    public boolean containsRegistration(OffsetDateTime rangeStart, OffsetDateTime rangeEnd) {
        return (this.registrationFrom == null || (rangeStart != null && !rangeStart.isBefore(this.registrationFrom))) && (this.registrationTo == null || (rangeEnd != null && !rangeEnd.isAfter(this.registrationTo)));
    }

    public boolean containsEffect(OffsetDateTime rangeStart, OffsetDateTime rangeEnd) {
        return (this.effectFrom == null || (rangeStart != null && !rangeStart.isBefore(this.effectFrom))) && (this.effectTo == null || (rangeEnd != null && !rangeEnd.isAfter(this.effectTo)));
    }

    public boolean contains(Bitemporality other) {
        return this.containsRegistration(other.registrationFrom, other.registrationTo) && this.containsEffect(other.effectFrom, other.effectTo);
    }

    public String toString() {
        return "Bitemporality " + this.registrationFrom + "|" + this.registrationTo + "|" + this.effectFrom + "|" + this.effectTo;
    }

    /*
    public static Bitemporality

    public static Set<Bitemporality> split(Set<Bitemporality> b) {
        HashMap<OffsetDateTime, Bitemporality> registrations = new HashMap<>();
        for (Bitemporality a : b) {

        }
    }*/
}
