import java.util.ArrayList;
import java.util.Collections;

/**
 * Models a health professional who works for the health service.
 *
 * <p>This class holds everything that is common to <em>every</em> health
 * professional, no matter what their specialty is: an identification number made
 * of digits only, a name, and the fixed set of days they work each week. It is the
 * foundation that the more specific types in {@code Part 2} build on.</p>
 *
 * <p>The class is {@code abstract} because a "plain" health professional with no
 * specialty does not exist in this system. Making it abstract means the compiler
 * itself stops anyone from writing {@code new HealthProfessional(...)}, and forces
 * every real type to supply its own type name, duties and service behaviour.</p>
 *
 * <p>All fields are {@code private} and {@code final}: external code can read the
 * data through getters, but the only way to set it is the constructor, which
 * validates every value. An object therefore can never be created in an invalid
 * state, and can never be changed into one afterwards.</p>
 */
public abstract class HealthProfessional {

    /** The line separator of the current platform, used when building detail blocks. */
    protected static final String NEW_LINE = System.lineSeparator();

    /** Width used to line up the labels in {@link #detailLine(String, Object)}. */
    private static final int LABEL_WIDTH = 15;

    /** The identification number of the professional; digits only. */
    private final String professionalId;

    /** The full name of the professional. */
    private final String name;

    /** The fixed set of days the professional works each week. */
    private final ArrayList<Weekday> availableDays;

    /**
     * Creates a health professional who works the default Monday to Friday week.
     *
     * <p>This is the second way of constructing a professional: most staff work a
     * standard week, so the caller does not have to supply the day list every time
     * and gets a sensible default instead.</p>
     *
     * @param professionalId the identification number, digits only
     * @param name           the full name of the professional
     * @throws IllegalArgumentException if the ID is not digits only or the name is blank
     */
    protected HealthProfessional(String professionalId, String name) {
        this(professionalId, name, defaultAvailableDays());
    }

    /**
     * Creates a health professional with a specific set of working days.
     *
     * @param professionalId the identification number, digits only
     * @param name           the full name of the professional
     * @param availableDays  the days the professional works; at least one day is required
     * @throws IllegalArgumentException if the ID is not digits only, the name is blank,
     *                                  or the day list is missing, empty, duplicated or contains null
     */
    protected HealthProfessional(String professionalId, String name, ArrayList<Weekday> availableDays) {
        // Every value is validated here, once. No subclass can bypass the rules,
        // and there is no setter that could break them later.
        this.professionalId = requireProfessionalId(professionalId);
        this.name = requireText(name, "Name");
        this.availableDays = copyAndValidateDays(availableDays);
    }

    /**
     * Returns the identification number of the professional.
     *
     * @return the ID, digits only
     */
    public String getProfessionalId() {
        return professionalId;
    }

    /**
     * Returns the full name of the professional.
     *
     * @return the name of the professional
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a copy of the days the professional works.
     *
     * <p>A copy is returned so that external code cannot add or remove days from
     * the professional's own list, which would put the object into an invalid
     * state.</p>
     *
     * @return a new list containing the working days, in Monday to Sunday order
     */
    public ArrayList<Weekday> getAvailableDays() {
        // A copy, not the field itself: returning the real list would let outside
        // code add a day that the constructor would have rejected.
        return new ArrayList<>(availableDays);
    }

    /**
     * Returns the working days as one readable line, for example
     * "Monday, Wednesday, Friday".
     *
     * @return the working days joined by commas
     */
    public String getAvailableDaysText() {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < availableDays.size(); i++) {
            if (i > 0) {
                text.append(", ");
            }
            text.append(availableDays.get(i).getDisplayName());
        }
        return text.toString();
    }

    /**
     * Reports whether the professional works on a given day.
     *
     * <p>The appointment manager uses this to reject a booking that falls on a day
     * the professional does not work.</p>
     *
     * @param day the day to check
     * @return {@code true} if the professional works on that day
     */
    public boolean worksOn(Weekday day) {
        return availableDays.contains(day);
    }

    /**
     * Returns the name of the kind of health professional this object is, for
     * example "General Practitioner".
     *
     * <p>Every concrete type must provide its own answer, which is what allows the
     * different types to respond in their own way when they are processed through
     * a {@code HealthProfessional} reference.</p>
     *
     * @return the type name of the professional
     */
    public abstract String getProfessionalType();

    /**
     * Returns a short description of what this kind of professional does.
     *
     * @return the duties of the professional
     */
    public abstract String getDuties();

    /**
     * Performs the core service this kind of professional provides to a patient.
     *
     * <p>This abstract method is the polymorphic hook of the hierarchy: the
     * demonstration code can loop over a list of {@code HealthProfessional} objects
     * and call this one method, and each object will carry out its own task.</p>
     *
     * @param patientName the name of the patient being seen
     * @return a description of the service that was performed
     */
    public abstract String provideService(String patientName);

    /**
     * Builds one line of a detail block, aligning every label in the same column.
     *
     * @param label the label shown on the left, for example "ID"
     * @param value the value shown on the right
     * @return one formatted line, without a line separator
     */
    protected String detailLine(String label, Object value) {
        return String.format("| %-" + LABEL_WIDTH + "s | %s |", label, value);
    }

    /**
     * Returns every detail of the professional in a readable block of text.
     *
     * <p>Specific types override this method, call {@code super.toString()} and
     * then append their own extra details, so the shared information is never
     * repeated in the subclasses.</p>
     *
     * @return the details of the professional
     */
    @Override
    public String toString() {
        return detailLine("ID", professionalId) + NEW_LINE
                + detailLine("Name", name) + NEW_LINE
                + detailLine("Type", getProfessionalType()) + NEW_LINE
                + detailLine("Available days", getAvailableDaysText()) + NEW_LINE
                + detailLine("Duties", getDuties());
    }

    /**
     * Compares this professional with another object.
     *
     * <p>Two professionals are considered the same person when they share the same
     * identification number, because the ID is what the health service uses to
     * identify a professional. The appointment manager relies on this rule to
     * decide whether a new booking would double-book the same professional, so it
     * does not matter whether the manager is holding the exact same object or a
     * different object that describes the same person.</p>
     *
     * @param other the object to compare with
     * @return {@code true} if both objects are health professionals with the same ID
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HealthProfessional)) {
            return false;
        }
        HealthProfessional otherProfessional = (HealthProfessional) other;
        return professionalId.equals(otherProfessional.professionalId);
    }

    /**
     * Returns a hash code consistent with {@link #equals(Object)}.
     *
     * @return the hash code of the identification number
     */
    @Override
    public int hashCode() {
        return professionalId.hashCode();
    }

    /**
     * Reports whether a value is a valid professional ID, that is, digits only.
     *
     * <p>The appointment manager uses this to validate an ID it is given by a user
     * before searching with it.</p>
     *
     * @param professionalId the value to check
     * @return {@code true} if the value is not null, not blank and contains digits only
     */
    public static boolean isValidProfessionalId(String professionalId) {
        if (professionalId == null) {
            return false;
        }
        String trimmed = professionalId.trim();
        if (trimmed.isEmpty()) {
            return false;
        }
        for (int i = 0; i < trimmed.length(); i++) {
            if (!Character.isDigit(trimmed.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validates a professional ID and returns it without surrounding spaces.
     *
     * @param professionalId the value to validate
     * @return the trimmed ID
     * @throws IllegalArgumentException if the value is not digits only
     */
    private static String requireProfessionalId(String professionalId) {
        if (!isValidProfessionalId(professionalId)) {
            throw new IllegalArgumentException(
                    "ID must be numbers only, but was: \"" + professionalId + "\"");
        }
        return professionalId.trim();
    }

    /**
     * Validates that a text value has been supplied and returns it without
     * surrounding spaces.
     *
     * @param value     the value to validate
     * @param fieldName the name of the field, used in the error message
     * @return the trimmed value
     * @throws IllegalArgumentException if the value is null or blank
     */
    protected static String requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }
        return value.trim();
    }

    /**
     * Builds the default working week of Monday to Friday.
     *
     * @return a new list holding Monday to Friday
     */
    private static ArrayList<Weekday> defaultAvailableDays() {
        ArrayList<Weekday> days = new ArrayList<>();
        days.add(Weekday.MONDAY);
        days.add(Weekday.TUESDAY);
        days.add(Weekday.WEDNESDAY);
        days.add(Weekday.THURSDAY);
        days.add(Weekday.FRIDAY);
        return days;
    }

    /**
     * Copies the supplied days, rejecting anything that would leave the object in
     * an invalid state, and sorts them from Monday to Sunday.
     *
     * @param days the days supplied by the caller
     * @return a validated copy of the days
     * @throws IllegalArgumentException if the list is null or empty, contains null,
     *                                  or lists the same day twice
     */
    private static ArrayList<Weekday> copyAndValidateDays(ArrayList<Weekday> days) {
        if (days == null || days.isEmpty()) {
            throw new IllegalArgumentException("At least one available day is required.");
        }
        ArrayList<Weekday> copy = new ArrayList<>();
        for (Weekday day : days) {
            if (day == null) {
                throw new IllegalArgumentException("Available days cannot contain null.");
            }
            if (copy.contains(day)) {
                throw new IllegalArgumentException(day + " is listed more than once.");
            }
            copy.add(day);
        }
        // Weekday is an enum, so its natural order is Monday to Sunday.
        Collections.sort(copy);
        return copy;
    }
}
