package com.github.fernthedev.fcommands.spigot.ncp;


import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Duration implements Comparable<Duration>, Serializable {
    private static final long serialVersionUID = -8834522925774502711L;
    public static final Duration ZERO = new Duration(0, Unit.MILLISECOND);

    public enum Unit {

        MILLISECOND(1),
        TICK(50),
        SECOND(1000),
        MINUTE(60000),
        HOUR(3600000),
        DAY(86400000);

        private final long milliseconds;

        Unit(long milliseconds) {
            this.milliseconds = milliseconds;
        }

        public long getMilliseconds() {
            return this.milliseconds;
        }

        /**
         * @return A shortened version of the time unit that is recongizeable.
         * 		   For example, seconds returns "s" milliseconds returns "ms" etc.
         */
        public String getShortUnit() {
            return switch (this) {
                case DAY -> "d";
                case HOUR -> "h";
                case MILLISECOND -> "ms";
                case MINUTE -> "m";
                case SECOND -> "s";
                case TICK -> "t";
                default -> "";
            };
        }
    }

    private final long milliseconds;

    public Duration(long value, Unit unit) {
        this.milliseconds = value * unit.getMilliseconds();
    }

    public Duration(Date startTime, Date endTime) {
        this.milliseconds = endTime.getTime() - startTime.getTime();
    }

    public Date getOffsetDate(Date startDate) {
        return startDate == null ? null : new Date(startDate.getTime() + toMilliseconds());
    }

    public long toMilliseconds() {
        return this.milliseconds;
    }

    public int toTicks() {
        return (int) getValue(Unit.TICK);
    }

    public long toSeconds() {
        return getValue(Unit.SECOND);
    }

    public long toMinutes() {
        return getValue(Unit.MINUTE);
    }

    public long toHours() {
        return getValue(Unit.HOUR);
    }

    public long toDays() {
        return getValue(Unit.DAY);
    }

    public long getValue(Unit unit) {
        return toMilliseconds() / unit.getMilliseconds();
    }

    /**
     * @return True for {@link #infinite()}
     */
    public boolean isInfinite() {
        return false;
    }

    /**
     * Combines the values of this Duration and the given Duration.
     *
     * @param duration The Duration to combine with this.
     * @return The sum of both durations.
     */
    public Duration add(Duration duration) {
        return milliseconds(this.milliseconds + duration.milliseconds);
    }

    @Override
    public int compareTo(Duration duration) {
        return Long.compare(toMilliseconds(), duration.toMilliseconds());
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Duration && toMilliseconds() == ((Duration) o).toMilliseconds();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(toMilliseconds());
    }

    @Override
    public String toString() {
        return toMilliseconds() + " ms";
    }

    public static Duration milliseconds(long milliseconds) {
        return new Duration(milliseconds, Unit.MILLISECOND);
    }

    public static Duration ticks(long ticks) {
        return new Duration(ticks, Unit.TICK);
    }

    public static Duration seconds(long seconds) {
        return new Duration(seconds, Unit.SECOND);
    }

    public static Duration minutes(long minutes) {
        return new Duration(minutes, Unit.MINUTE);
    }

    public static Duration hours(long hours) {
        return new Duration(hours, Unit.HOUR);
    }

    public static Duration days(long days) {
        return new Duration(days, Unit.DAY);
    }

    public static Duration infinite() {
        return new Duration(Integer.MAX_VALUE, Unit.TICK) {
            private static final long serialVersionUID = 6114246530237568526L;

            @Override
            public boolean isInfinite() {
                return true;
            }
        };
    }

}
