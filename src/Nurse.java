import java.util.ArrayList;

/**
 * Models a nurse.
 *
 * <p>Nurses extend {@link HealthProfessional} directly rather than
 * {@link Doctor}, because a nurse does not prescribe medication and is not
 * registered with the medical board. A nurse gets its own behaviour, taking blood
 * samples, and its own details, the ward and the nursing grade.</p>
 */
public class Nurse extends HealthProfessional {

    /** The ward the nurse works on. */
    private final String ward;

    /** The nursing grade of the nurse, for example "Registered Nurse". */
    private final String nurseGrade;

    /**
     * Creates a nurse who works the default Monday to Friday week.
     *
     * @param professionalId the identification number, digits only
     * @param name           the full name of the nurse
     * @param ward           the ward the nurse works on
     * @param nurseGrade     the nursing grade of the nurse
     * @throws IllegalArgumentException if any value is missing or invalid
     */
    public Nurse(String professionalId, String name, String ward, String nurseGrade) {
        super(professionalId, name);
        this.ward = requireText(ward, "Ward");
        this.nurseGrade = requireText(nurseGrade, "Nurse grade");
    }

    /**
     * Creates a nurse with a specific set of working days.
     *
     * @param professionalId the identification number, digits only
     * @param name           the full name of the nurse
     * @param availableDays  the days the nurse works
     * @param ward           the ward the nurse works on
     * @param nurseGrade     the nursing grade of the nurse
     * @throws IllegalArgumentException if any value is missing or invalid
     */
    public Nurse(String professionalId, String name, ArrayList<Weekday> availableDays,
                 String ward, String nurseGrade) {
        super(professionalId, name, availableDays);
        this.ward = requireText(ward, "Ward");
        this.nurseGrade = requireText(nurseGrade, "Nurse grade");
    }

    /**
     * Returns the ward of the nurse.
     *
     * @return the ward
     */
    public String getWard() {
        return ward;
    }

    /**
     * Returns the nursing grade of the nurse.
     *
     * @return the nursing grade
     */
    public String getNurseGrade() {
        return nurseGrade;
    }

    /**
     * Takes a blood sample from a patient.
     *
     * @param patientName the name of the patient
     * @return a description of the procedure
     */
    public String takeBloodSample(String patientName) {
        String checkedPatient = requireText(patientName, "Patient name");
        return getName() + " takes a blood sample from " + checkedPatient + " on " + ward + ".";
    }

    /**
     * Returns the type name shown wherever the professional is described.
     *
     * @return "Nurse"
     */
    @Override
    public String getProfessionalType() {
        return "Nurse";
    }

    /**
     * Returns a description of the work a nurse does.
     *
     * @return the duties of a nurse
     */
    @Override
    public String getDuties() {
        return "Provides day-to-day patient care and carries out clinical tasks.";
    }

    /**
     * Cares for a patient during an appointment.
     *
     * @param patientName the name of the patient being seen
     * @return a description of the care given
     */
    @Override
    public String provideService(String patientName) {
        String checkedPatient = requireText(patientName, "Patient name");
        return getName() + " checks the observations of " + checkedPatient + " on " + ward + ".";
    }

    /**
     * Returns the details of the nurse, adding the ward and grade to the details
     * inherited from {@link HealthProfessional}.
     *
     * @return the details of the nurse
     */
    @Override
    public String toString() {
        return super.toString() + NEW_LINE
                + detailLine("Ward", ward) + NEW_LINE
                + detailLine("Grade", nurseGrade);
    }
}
