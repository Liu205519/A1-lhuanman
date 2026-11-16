/**
 * Models a time on a clinic day, such as 09:30.
 *
 * <p>The brief warns that the time must be stored so that appointments can be
 * ordered correctly. Storing a time as text such as "9:30" would sort wrongly,
 * because "10:00" comes before "9:30" as text. This class stores the hour and the
 * minute as numbers instead, which gives a real time order, and it implements
 * {@link Comparable} so that a collection of appointments can be sorted from the
 * earliest to the latest.</p>
 *
 * <p>The class is immutable: the hour and minute are set once by the constructor
 * and can never change, so a time can safely be shared between objects.</p>
 */
public class TimeSlot implements Comparable<TimeSlot> {

    /** The number of minutes in one hour. */
    private static final int MINUTES_PER_HOUR = 60;

    /** The hour of the day, from 0 to 23. */
    private final int hour;

    /** The minute within the hour, from 0 to 59. */
    private final int minute;

    /**
     * Creates a time slot.
     *
     * @param hour   the hour of the day, from 0 to 23
     * @param minute the minute within the hour, from 0 to 59
     * @throws IllegalArgumentException if the hour or the minute is out of range
     */
    public TimeSlot(int hour, int minute) {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("Hour must be between 0 and 23, but was: " + hour);
        }
        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException("Minute must be between 0 and 59, but was: " + minute);
        }
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * Creates a time slot from text in the 24 hour "HH:MM" format.
     *
     * @param text the time to read, for example "09:30"
     * @return the matching time slot
     * @throws IllegalArgumentException if the text is missing or not in HH:MM format
     */
    public static TimeSlot parse(String text) {
        if (text == null) {
            throw new IllegalArgumentException("A time is required.");
        }
        String[] parts = text.trim().split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Time must be in HH:MM format, but was: \"" + text + "\"");
        }
        try {
            int hour = Integer.parseInt(parts[0].trim());
            int minute = Integer.parseInt(parts[1].trim());
            return new TimeSlot(hour, minute);
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("Time must be in HH:MM format, but was: \"" + text + "\"");
        }
    }

    /**
     * Returns the hour of the day.
     *
     * @return the hour, from 0 to 23
     */
    public int getHour() {
        return hour;
    }

    /**
     * Returns the minute within the hour.
     *
     * @return the minute, from 0 to 59
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Returns the time as a number of minutes since midnight.
     *
     * <p>Comparing this number is the same as comparing the times, which keeps the
     * ordering logic simple and correct.</p>
     *
     * @return minutes since midnight, for example 570 for 09:30
     */
    public int getMinutesSinceMidnight() {
        // One number instead of two fields: comparing minutes is the same as
        // comparing times, which keeps compareTo short and correct.
        return hour * MINUTES_PER_HOUR + minute;
    }

    /**
     * Compares this time with another, so that a collection of objects holding
     * time slots can be sorted from earliest to latest.
     *
     * @param other the time to compare with
     * @return a negative number if this time is earlier, zero if they are the same,
     *         or a positive number if this time is later
     */
    @Override
    public int compareTo(TimeSlot other) {
        return Integer.compare(getMinutesSinceMidnight(), other.getMinutesSinceMidnight());
    }

    /**
     * Compares this time with another object.
     *
     * @param other the object to compare with
     * @return {@code true} if both objects describe the same time
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TimeSlot)) {
            return false;
        }
        TimeSlot otherSlot = (TimeSlot) other;
        return hour == otherSlot.hour && minute == otherSlot.minute;
    }

    /**
     * Returns a hash code consistent with {@link #equals(Object)}.
     *
     * @return the hash code of the time
     */
    @Override
    public int hashCode() {
        return getMinutesSinceMidnight();
    }

    /**
     * Returns the time in the 24 hour "HH:MM" format.
     *
     * @return the formatted time, for example "09:30"
     */
    @Override
    public String toString() {
        return String.format("%02d:%02d", hour, minute);
    }
}
