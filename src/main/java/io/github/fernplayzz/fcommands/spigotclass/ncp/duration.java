package io.github.fernplayzz.fcommands.spigotclass.ncp;


import java.io.Serializable;
import java.util.Date;

public class duration implements Comparable<duration>, Serializable {
    private static final long serialVersionUID = -8834522925774502711L;
    public static final duration ZERO = new duration(0, Unit.MILLISECOND);

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
            switch (this) {
                case DAY:
                    return "d";
                case HOUR:
                    return "h";
                case MILLISECOND:
                    return "ms";
                case MINUTE:
                    return "m";
                case SECOND:
                    return "s";
                case TICK:
                    return "t";
                default:
                    return "";
            }
        }
    }

    private final long milliseconds;

    public duration(long value, Unit unit) {
        this.milliseconds = value * unit.getMilliseconds();
    }

    public duration(Date startTime, Date endTime) {
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
     * Combines the values of this duration and the given duration.
     *
     * @param duration The duration to combine with this.
     * @return The sum of both durations.
     */
    public duration add(duration duration) {
        return milliseconds(this.milliseconds + duration.milliseconds);
    }

    @Override
    public int compareTo(duration duration) {
        return Long.compare(toMilliseconds(), duration.toMilliseconds());
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof duration && toMilliseconds() == ((duration) o).toMilliseconds();
    }

    @Override
    public int hashCode() {
        return new Long(toMilliseconds()).hashCode();
    }

    @Override
    public String toString() {
        return toMilliseconds() + " ms";
    }

    public static duration milliseconds(long milliseconds) {
        return new duration(milliseconds, Unit.MILLISECOND);
    }

    public static duration ticks(long ticks) {
        return new duration(ticks, Unit.TICK);
    }

    public static duration seconds(long seconds) {
        return new duration(seconds, Unit.SECOND);
    }

    public static duration minutes(long minutes) {
        return new duration(minutes, Unit.MINUTE);
    }

    public static duration hours(long hours) {
        return new duration(hours, Unit.HOUR);
    }

    public static duration days(long days) {
        return new duration(days, Unit.DAY);
    }

    public static duration infinite() {
        return new duration(Integer.MAX_VALUE, Unit.TICK) {
            private static final long serialVersionUID = 6114246530237568526L;

            @Override
            public boolean isInfinite() {
                return true;
            }
        };
    }

}
