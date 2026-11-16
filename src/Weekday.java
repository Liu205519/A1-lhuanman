/**
 * The days of the week on which a health professional can work.
 *
 * <p>The brief describes the days a professional works as a "fixed collection of
 * values", so an {@code enum} is used instead of free text. This guarantees that
 * only real days can ever be stored, and lets the rest of the system work with
 * constants such as {@code Weekday.MONDAY} rather than with strings that could
 * contain a typo.</p>
 */
public enum Weekday {

    /** Monday. */
    MONDAY("Monday"),
    /** Tuesday. */
    TUESDAY("Tuesday"),
    /** Wednesday. */
    WEDNESDAY("Wednesday"),
    /** Thursday. */
    THURSDAY("Thursday"),
    /** Friday. */
    FRIDAY("Friday"),
    /** Saturday. */
    SATURDAY("Saturday"),
    /** Sunday. */
    SUNDAY("Sunday");

    /** The name of the day as it should be shown to a user. */
    private final String displayName;

    /**
     * Creates a weekday constant.
     *
     * @param displayName the name of the day in readable form
     */
    Weekday(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the readable name of the day.
     *
     * @return the name of the day, for example "Monday"
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the readable name of the day so that printing a {@code Weekday}
     * produces something a user can read.
     *
     * @return the name of the day, for example "Monday"
     */
    @Override
    public String toString() {
        return displayName;
    }
}
