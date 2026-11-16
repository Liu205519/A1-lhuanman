/**
 * Models a single appointment: one patient, seen by one health professional, at
 * one time on the current day.
 *
 * <p>The appointment refers to the health professional through the
 * {@link HealthProfessional} type. That is what lets one appointment class work
 * with every type created in Parts 1 and 2 without knowing which type it holds,
 * because every type is a health professional.</p>
 *
 * <p>An appointment cannot be created in an incomplete state: every value is
 * required, and the constructor refuses to build the object if one is missing. The
 * class implements {@link Comparable} so that a whole collection of appointments
 * can be sorted from the earliest to the latest.</p>
 */
public class Appointment implements Comparable<Appointment> {

    /** The line separator of the current platform, used when building detail blocks. */
    private static final String NEW_LINE = System.lineSeparator();

    /** The column widths used by {@link #summaryHeading()} and {@link #toSummaryLine()}. */
    private static final String SUMMARY_FORMAT = "%-5s | %-7s | %-24s | %-27s | %-16s | %-12s";

    /** The patient who booked the appointment. */
    private final Patient patient;

    /** The health professional the patient is seeing. */
    private final HealthProfessional healthProfessional;

    /** The time of the appointment. */
    private final TimeSlot timeSlot;

    /**
     * Creates an appointment.
     *
     * @param patient            the patient who booked the appointment
     * @param healthProfessional the health professional the patient is seeing
     * @param timeSlot           the time of the appointment
     * @throws IllegalArgumentException if any of the three values is missing
     */
    public Appointment(Patient patient, HealthProfessional healthProfessional, TimeSlot timeSlot) {
        if (patient == null) {
            throw new IllegalArgumentException("An appointment requires a patient.");
        }
        if (healthProfessional == null) {
            throw new IllegalArgumentException("An appointment requires a health professional.");
        }
        if (timeSlot == null) {
            throw new IllegalArgumentException("An appointment requires a time slot.");
        }
        this.patient = patient;
        this.healthProfessional = healthProfessional;
        this.timeSlot = timeSlot;
    }

    /**
     * Returns the patient who booked the appointment.
     *
     * @return the patient
     */
    public Patient getPatient() {
        return patient;
    }

    /**
     * Returns the health professional the patient is seeing.
     *
     * @return the health professional
     */
    public HealthProfessional getHealthProfessional() {
        return healthProfessional;
    }

    /**
     * Returns the time of the appointment.
     *
     * @return the time slot
     */
    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    /**
     * Reports whether this appointment would collide with a booking for the same
     * professional at the same time.
     *
     * <p>The check uses {@link HealthProfessional#equals(Object)} rather than
     * comparing the two objects with {@code ==}, so it also catches a booking for
     * a different object that describes the same professional.</p>
     *
     * @param otherProfessional the professional being booked
     * @param otherTimeSlot     the time being booked
     * @return {@code true} if the professional is already booked at that time
     */
    public boolean clashesWith(HealthProfessional otherProfessional, TimeSlot otherTimeSlot) {
        return healthProfessional.equals(otherProfessional) && timeSlot.equals(otherTimeSlot);
    }

    /**
     * Compares this appointment with another so that a collection can be sorted
     * from the earliest appointment to the latest.
     *
     * <p>Two appointments at the same time are ordered by professional ID, which is
     * only there to make the output stable from run to run. The brief allows two
     * appointments at the same time for different professionals to appear in any
     * order.</p>
     *
     * @param other the appointment to compare with
     * @return a negative number if this appointment is earlier, zero if they are the
     *         same, or a positive number if this appointment is later
     */
    @Override
    public int compareTo(Appointment other) {
        int timeOrder = timeSlot.compareTo(other.timeSlot);
        if (timeOrder != 0) {
            return timeOrder;
        }
        return healthProfessional.getProfessionalId()
                .compareTo(other.healthProfessional.getProfessionalId());
    }

    /**
     * Returns the column headings that match {@link #toSummaryLine()}.
     *
     * @return the heading line of an appointment listing
     */
    public static String summaryHeading() {
        return String.format(SUMMARY_FORMAT, "Time", "ID", "Health professional", "Type",
                "Patient", "Mobile");
    }

    /**
     * Returns one line describing the appointment, used in listings where many
     * appointments are shown together.
     *
     * @return one line summary of the appointment
     */
    public String toSummaryLine() {
        return String.format(SUMMARY_FORMAT, timeSlot, healthProfessional.getProfessionalId(),
                healthProfessional.getName(), healthProfessional.getProfessionalType(),
                patient.getName(), patient.getFormattedMobileNumber());
    }

    /**
     * Returns the full details of the appointment, including the details of the
     * health professional.
     *
     * @return the details of the appointment
     */
    @Override
    public String toString() {
        StringBuilder details = new StringBuilder();
        details.append("Appointment at ").append(timeSlot).append(NEW_LINE);
        details.append("  ").append(healthProfessional.getProfessionalType())
                .append(" - ").append(healthProfessional.getName())
                .append(" (ID ").append(healthProfessional.getProfessionalId()).append(")")
                .append(NEW_LINE);
        details.append("  Patient          : ").append(patient.getName())
                .append(", mobile ").append(patient.getFormattedMobileNumber()).append(NEW_LINE);
        details.append("  Health professional details:").append(NEW_LINE);
        details.append(indent(healthProfessional.toString(), "    "));
        return details.toString();
    }

    /**
     * Puts a prefix in front of every line of a block of text, so that the details
     * of the health professional line up underneath the appointment.
     *
     * @param text   the text to indent
     * @param prefix the text to put in front of each line
     * @return the indented text
     */
    private static String indent(String text, String prefix) {
        StringBuilder indented = new StringBuilder();
        String[] lines = text.split(NEW_LINE);
        for (int i = 0; i < lines.length; i++) {
            indented.append(prefix).append(lines[i]);
            if (i < lines.length - 1) {
                indented.append(NEW_LINE);
            }
        }
        return indented.toString();
    }
}
