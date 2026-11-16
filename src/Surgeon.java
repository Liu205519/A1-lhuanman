import java.util.ArrayList;

/**
 * Models a surgeon.
 *
 * <p>A surgeon adds an operating theatre, a surgical area and a count of the
 * operations completed. The count changes while the program runs, which shows that
 * encapsulated data can still change, but only through a method that controls the
 * change instead of through a public field.</p>
 */
public class Surgeon extends Doctor {

    /** The operating theatre the surgeon works in. */
    private final String theatre;

    /** The surgical area the surgeon works in, for example "Orthopaedics". */
    private final String surgicalArea;

    /** How many operations this surgeon has completed in this session. */
    private int operationsCompleted;

    /**
     * Creates a surgeon who works the default Monday to Friday week.
     *
     * @param professionalId            the identification number, digits only
     * @param name                      the full name of the doctor
     * @param medicalRegistrationNumber the registration number with the medical board
     * @param theatre                   the operating theatre
     * @param surgicalArea              the surgical area, for example "Orthopaedics"
     * @throws IllegalArgumentException if any value is missing or invalid
     */
    public Surgeon(String professionalId, String name, String medicalRegistrationNumber,
                   String theatre, String surgicalArea) {
        super(professionalId, name, medicalRegistrationNumber);
        this.theatre = requireText(theatre, "Theatre");
        this.surgicalArea = requireText(surgicalArea, "Surgical area");
        this.operationsCompleted = 0;
    }

    /**
     * Creates a surgeon with a specific set of working days.
     *
     * @param professionalId            the identification number, digits only
     * @param name                      the full name of the doctor
     * @param availableDays             the days the doctor works
     * @param medicalRegistrationNumber the registration number with the medical board
     * @param theatre                   the operating theatre
     * @param surgicalArea              the surgical area, for example "Orthopaedics"
     * @throws IllegalArgumentException if any value is missing or invalid
     */
    public Surgeon(String professionalId, String name, ArrayList<Weekday> availableDays,
                   String medicalRegistrationNumber, String theatre, String surgicalArea) {
        super(professionalId, name, availableDays, medicalRegistrationNumber);
        this.theatre = requireText(theatre, "Theatre");
        this.surgicalArea = requireText(surgicalArea, "Surgical area");
        this.operationsCompleted = 0;
    }

    /**
     * Returns the operating theatre of the surgeon.
     *
     * @return the theatre
     */
    public String getTheatre() {
        return theatre;
    }

    /**
     * Returns the surgical area of the surgeon.
     *
     * @return the surgical area
     */
    public String getSurgicalArea() {
        return surgicalArea;
    }

    /**
     * Returns how many operations this surgeon has completed.
     *
     * @return the number of completed operations
     */
    public int getOperationsCompleted() {
        return operationsCompleted;
    }

    /**
     * Performs an operation on a patient and records that it happened.
     *
     * @param patientName the name of the patient
     * @param procedure   the procedure being performed
     * @return a description of the operation
     */
    public String performOperation(String patientName, String procedure) {
        String checkedPatient = requireText(patientName, "Patient name");
        String checkedProcedure = requireText(procedure, "Procedure");
        operationsCompleted++;
        return getName() + " performs a " + checkedProcedure + " on " + checkedPatient
                + " in " + theatre + ".";
    }

    /**
     * Returns the type name shown wherever the professional is described.
     *
     * @return "Surgeon"
     */
    @Override
    public String getProfessionalType() {
        return "Surgeon";
    }

    /**
     * Returns a description of the work a surgeon does.
     *
     * @return the duties of a surgeon
     */
    @Override
    public String getDuties() {
        return "Performs operations within " + surgicalArea + ".";
    }

    /**
     * Prepares a patient for surgery.
     *
     * @param patientName the name of the patient being seen
     * @return a description of the preparation
     */
    @Override
    public String provideService(String patientName) {
        String checkedPatient = requireText(patientName, "Patient name");
        return getName() + " assesses " + checkedPatient + " for surgery in "
                + theatre + " (" + surgicalArea + ").";
    }

    /**
     * Returns the details of the surgeon, adding the theatre, the surgical area and
     * the operations completed to the details of a doctor.
     *
     * @return the details of the surgeon
     */
    @Override
    public String toString() {
        return super.toString() + NEW_LINE
                + detailLine("Theatre", theatre) + NEW_LINE
                + detailLine("Surgical area", surgicalArea) + NEW_LINE
                + detailLine("Operations", operationsCompleted);
    }
}
