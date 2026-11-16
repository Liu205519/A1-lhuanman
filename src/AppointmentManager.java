import java.util.ArrayList;
import java.util.Collections;

/**
 * Manages every appointment for the current day.
 *
 * <p>This is the single place that owns the collection of appointments, so it is
 * also the only place that can enforce the clinic's booking rules: the time must be
 * one of the clinic's slots, the professional must work that day, and a
 * professional can never be booked twice into the same slot.</p>
 *
 * <p>{@link java.util.ArrayList} is used to hold the appointments because the
 * collection grows and shrinks as appointments are booked and cancelled, the number
 * of appointments in a day is not known in advance, and an ordered list keeps the
 * code simple when appointments are filtered and sorted. Every operation returns a
 * message: what happened, or a warning explaining why it could not happen, so the
 * caller always has feedback to show the user.</p>
 */
public class AppointmentManager {

    /** The line separator of the current platform, used when building listings. */
    private static final String NEW_LINE = System.lineSeparator();

    /** A rule drawn under a listing heading: a dash for every character, a plus for every pipe. */
    private static final String DASHED_LINE = ruleFor(Appointment.summaryHeading());

    /** The appointments of the day, in the order they were booked. */
    private final ArrayList<Appointment> appointments;

    /** The schedule the clinic runs to. */
    private final ClinicSchedule clinicSchedule;

    /** The day the appointments are being made for. */
    private final Weekday bookingDay;

    /**
     * Creates a manager for one clinic day.
     *
     * @param clinicSchedule the schedule the clinic runs to
     * @param bookingDay     the day the appointments are for
     * @throws IllegalArgumentException if either value is missing
     */
    public AppointmentManager(ClinicSchedule clinicSchedule, Weekday bookingDay) {
        if (clinicSchedule == null) {
            throw new IllegalArgumentException("A clinic schedule is required.");
        }
        if (bookingDay == null) {
            throw new IllegalArgumentException("The day of the appointments is required.");
        }
        this.clinicSchedule = clinicSchedule;
        this.bookingDay = bookingDay;
        this.appointments = new ArrayList<>();
    }

    /**
     * Returns the schedule the clinic runs to.
     *
     * @return the clinic schedule
     */
    public ClinicSchedule getClinicSchedule() {
        return clinicSchedule;
    }

    /**
     * Returns the day the appointments are for.
     *
     * @return the booking day
     */
    public Weekday getBookingDay() {
        return bookingDay;
    }

    /**
     * Returns how many appointments are currently booked.
     *
     * @return the number of appointments
     */
    public int getAppointmentCount() {
        return appointments.size();
    }

    /**
     * Books an appointment.
     *
     * <p>The booking is rejected, with a warning, when any required detail is
     * missing, when the time is not one of the clinic's slots, when the professional
     * does not work on the booking day, or when the professional is already booked
     * at that time.</p>
     *
     * @param patient            the patient who is booking
     * @param healthProfessional the health professional the patient wants to see
     * @param timeSlot           the requested time
     * @return a message describing the booking, or a warning explaining the refusal
     */
    public String addAppointment(Patient patient, HealthProfessional healthProfessional,
                                 TimeSlot timeSlot) {
        if (patient == null) {
            return "Warning: an appointment needs a patient.";
        }
        if (healthProfessional == null) {
            return "Warning: an appointment needs a health professional.";
        }
        if (timeSlot == null) {
            return "Warning: an appointment needs a time slot.";
        }
        if (!clinicSchedule.isScheduledSlot(timeSlot)) {
            // Checked before the clash test: an off-grid time is a bad request,
            // not a booking conflict, and the message should say which it is.
            return "Warning: " + timeSlot + " is not a clinic time slot. "
                    + "The clinic runs " + clinicSchedule + ".";
        }
        if (!healthProfessional.worksOn(bookingDay)) {
            return "Warning: " + healthProfessional.getName() + " does not work on "
                    + bookingDay + ". Available days: "
                    + healthProfessional.getAvailableDaysText() + ".";
        }
        for (Appointment existing : appointments) {
            if (existing.clashesWith(healthProfessional, timeSlot)) {
                return "Warning: " + healthProfessional.getName() + " (ID "
                        + healthProfessional.getProfessionalId() + ") is already booked at "
                        + timeSlot + " with " + existing.getPatient().getName() + ".";
            }
        }
        appointments.add(new Appointment(patient, healthProfessional, timeSlot));
        return "Booked : " + timeSlot + " with " + healthProfessional.getName()
                + " (" + healthProfessional.getProfessionalType() + ", ID "
                + healthProfessional.getProfessionalId() + ") for " + patient.getName()
                + ", mobile " + patient.getFormattedMobileNumber() + ".";
    }

    /**
     * Returns every appointment of the day, sorted from earliest to latest.
     *
     * @return a listing of all appointments
     */
    public String displayAllAppointments() {
        return buildListing("All appointments for " + bookingDay, getAppointmentsInTimeOrder());
    }

    /**
     * Returns the appointments for one health professional, sorted from earliest to
     * latest.
     *
     * @param professionalId the ID of the health professional, digits only
     * @return a listing of the matching appointments, or a warning if there are none
     *         or the ID is not valid
     */
    public String displayAppointmentsForProfessional(String professionalId) {
        if (!HealthProfessional.isValidProfessionalId(professionalId)) {
            return "Warning: a professional ID must contain numbers only, but was: \""
                    + professionalId + "\".";
        }
        String id = professionalId.trim();
        ArrayList<Appointment> matches = new ArrayList<>();
        String professionalName = "";
        String professionalType = "";
        for (Appointment appointment : appointments) {
            if (appointment.getHealthProfessional().getProfessionalId().equals(id)) {
                matches.add(appointment);
                professionalName = appointment.getHealthProfessional().getName();
                professionalType = appointment.getHealthProfessional().getProfessionalType();
            }
        }
        if (matches.isEmpty()) {
            return "Warning: no appointments found for health professional ID " + id + ".";
        }
        Collections.sort(matches);
        return buildListing("Appointments for " + professionalName + " (" + professionalType
                + ", ID " + id + ")", matches);
    }

    /**
     * Returns the appointments for one patient, sorted from earliest to latest.
     *
     * <p>The number may be typed with spaces or hyphens, because it is normalised
     * before it is compared.</p>
     *
     * @param mobileNumber the mobile number of the patient
     * @return a listing of the matching appointments, or a warning if there are none
     *         or the number is not valid
     */
    public String displayAppointmentsForPatient(String mobileNumber) {
        if (!Patient.isValidMobileNumber(mobileNumber)) {
            return "Warning: a mobile number must contain 10 digits, but was: \""
                    + mobileNumber + "\".";
        }
        String phone = Patient.normaliseMobileNumber(mobileNumber);
        ArrayList<Appointment> matches = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getPatient().getMobileNumber().equals(phone)) {
                matches.add(appointment);
            }
        }
        if (matches.isEmpty()) {
            return "Warning: no appointments found for mobile number "
                    + Patient.formatMobileNumber(phone) + ".";
        }
        Collections.sort(matches);
        return buildListing("Appointments for " + matches.get(0).getPatient().getName()
                + " (mobile " + Patient.formatMobileNumber(phone) + ")", matches);
    }

    /**
     * Cancels the appointment for one professional at one time.
     *
     * @param professionalId the ID of the health professional, digits only
     * @param timeSlot       the time of the appointment
     * @return a message describing the cancellation, or a warning if no matching
     *         appointment exists or an argument is not valid
     */
    public String cancelAppointment(String professionalId, TimeSlot timeSlot) {
        if (!HealthProfessional.isValidProfessionalId(professionalId)) {
            return "Warning: a professional ID must contain numbers only, but was: \""
                    + professionalId + "\".";
        }
        if (timeSlot == null) {
            return "Warning: a time slot is required to cancel an appointment.";
        }
        String id = professionalId.trim();
        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            if (appointment.getHealthProfessional().getProfessionalId().equals(id)
                    && appointment.getTimeSlot().equals(timeSlot)) {
                appointments.remove(i);
                return "Cancelled: " + timeSlot + " with "
                        + appointment.getHealthProfessional().getName() + " (ID " + id
                        + ") for " + appointment.getPatient().getName() + ".";
            }
        }
        return "Warning: no appointment found at " + timeSlot
                + " for health professional ID " + id + ".";
    }

    /**
     * Cancels every appointment booked under one mobile number.
     *
     * @param mobileNumber the mobile number of the patient
     * @return a message describing the cancellations, or a warning if no matching
     *         appointment exists or the number is not valid
     */
    public String cancelAppointmentsForPatient(String mobileNumber) {
        if (!Patient.isValidMobileNumber(mobileNumber)) {
            return "Warning: a mobile number must contain 10 digits, but was: \""
                    + mobileNumber + "\".";
        }
        String phone = Patient.normaliseMobileNumber(mobileNumber);
        ArrayList<Appointment> cancelled = new ArrayList<>();
        // Walk backwards so that removing an appointment does not skip the next one.
        for (int i = appointments.size() - 1; i >= 0; i--) {
            if (appointments.get(i).getPatient().getMobileNumber().equals(phone)) {
                cancelled.add(appointments.remove(i));
            }
        }
        if (cancelled.isEmpty()) {
            return "Warning: no appointments found for mobile number "
                    + Patient.formatMobileNumber(phone) + ".";
        }
        Collections.sort(cancelled);
        StringBuilder times = new StringBuilder();
        for (int i = 0; i < cancelled.size(); i++) {
            if (i > 0) {
                times.append(", ");
            }
            times.append(cancelled.get(i).getTimeSlot());
        }
        return "Cancelled: " + cancelled.size() + " appointment(s) for "
                + cancelled.get(0).getPatient().getName() + " (mobile "
                + Patient.formatMobileNumber(phone) + ") at " + times + ".";
    }

    /**
     * Returns a copy of every appointment, sorted from earliest to latest.
     *
     * <p>Sorting the copy rather than the manager's own list means the collection
     * can be shown in time order without losing the booking order, and callers
     * cannot change the manager's list by mistake.</p>
     *
     * @return a new, sorted list of appointments
     */
    public ArrayList<Appointment> getAppointmentsInTimeOrder() {
        // Sort a copy: the manager keeps its own booking order, and callers cannot
        // reorder or modify the real collection by accident.
        ArrayList<Appointment> sorted = new ArrayList<>(appointments);
        Collections.sort(sorted);
        return sorted;
    }

    /**
     * Builds the rule that sits under a listing heading. Every character becomes a
     * dash, and every pipe becomes a plus, so the rule always lines up with the
     * columns no matter how the heading is formatted.
     *
     * @param heading the heading line of a listing
     * @return the matching rule
     */
    private static String ruleFor(String heading) {
        StringBuilder rule = new StringBuilder();
        for (int i = 0; i < heading.length(); i++) {
            rule.append(heading.charAt(i) == '|' ? '+' : '-');
        }
        return rule.toString();
    }

    /**
     * Builds a readable listing with a heading, aligned columns and a count.
     *
     * @param title     the heading of the listing
     * @param toDisplay the appointments to show
     * @return the listing
     */
    private String buildListing(String title, ArrayList<Appointment> toDisplay) {
        StringBuilder listing = new StringBuilder();
        listing.append(title).append(NEW_LINE);
        if (toDisplay.isEmpty()) {
            listing.append("(no appointments)");
            return indent(listing.toString());
        }
        listing.append(Appointment.summaryHeading()).append(NEW_LINE);
        listing.append(DASHED_LINE).append(NEW_LINE);
        for (Appointment appointment : toDisplay) {
            listing.append(appointment.toSummaryLine()).append(NEW_LINE);
        }
        listing.append(DASHED_LINE).append(NEW_LINE);
        listing.append(toDisplay.size()).append(" appointment(s) shown.");
        return indent(listing.toString());
    }

    /**
     * Puts two spaces in front of every line, so that a listing lines up with the
     * rest of the printed output.
     *
     * @param text the text to indent
     * @return the indented text
     */
    private static String indent(String text) {
        return "  " + text.replace(NEW_LINE, NEW_LINE + "  ");
    }
}
