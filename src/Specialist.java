import java.util.ArrayList;

/**
 * Models a specialist doctor, such as a cardiologist or a paediatrician.
 *
 * <p>A specialist adds the area of medicine they specialise in, and an order for
 * tests, to what a {@link Doctor} already provides.</p>
 */
public class Specialist extends Doctor {

    /** The area of medicine the specialist works in, for example "Cardiology". */
    private final String specialty;

    /** The hospital department the specialist is based in. */
    private final String department;

    /**
     * Creates a specialist who works the default Monday to Friday week.
     *
     * @param professionalId            the identification number, digits only
     * @param name                      the full name of the doctor
     * @param medicalRegistrationNumber the registration number with the medical board
     * @param specialty                 the area of medicine, for example "Cardiology"
     * @param department                the hospital department
     * @throws IllegalArgumentException if any value is missing or invalid
     */
    public Specialist(String professionalId, String name, String medicalRegistrationNumber,
                      String specialty, String department) {
        super(professionalId, name, medicalRegistrationNumber);
        this.specialty = requireText(specialty, "Specialty");
        this.department = requireText(department, "Department");
    }

    /**
     * Creates a specialist with a specific set of working days.
     *
     * @param professionalId            the identification number, digits only
     * @param name                      the full name of the doctor
     * @param availableDays             the days the doctor works
     * @param medicalRegistrationNumber the registration number with the medical board
     * @param specialty                 the area of medicine, for example "Cardiology"
     * @param department                the hospital department
     * @throws IllegalArgumentException if any value is missing or invalid
     */
    public Specialist(String professionalId, String name, ArrayList<Weekday> availableDays,
                      String medicalRegistrationNumber, String specialty, String department) {
        super(professionalId, name, availableDays, medicalRegistrationNumber);
        this.specialty = requireText(specialty, "Specialty");
        this.department = requireText(department, "Department");
    }

    /**
     * Returns the area of medicine the specialist works in.
     *
     * @return the specialty, for example "Cardiology"
     */
    public String getSpecialty() {
        return specialty;
    }

    /**
     * Returns the hospital department of the specialist.
     *
     * @return the department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Returns the type name shown wherever the professional is described. The
     * specialty is included so the name is specific to this doctor.
     *
     * @return "Specialist (Cardiology)", for example
     */
    @Override
    public String getProfessionalType() {
        return "Specialist (" + specialty + ")";
    }

    /**
     * Returns a description of the work a specialist does.
     *
     * @return the duties of a specialist
     */
    @Override
    public String getDuties() {
        return "Diagnoses and treats complex conditions within " + specialty + ".";
    }

    /**
     * Orders a test for a patient.
     *
     * @param patientName the name of the patient
     * @param testName    the test being ordered
     * @return a description of the test order
     */
    public String orderTest(String patientName, String testName) {
        String checkedPatient = requireText(patientName, "Patient name");
        String checkedTest = requireText(testName, "Test name");
        return getName() + " orders a " + checkedTest + " for " + checkedPatient
                + " through " + department + ".";
    }

    /**
     * Reviews a patient in a specialist consultation.
     *
     * @param patientName the name of the patient being seen
     * @return a description of the consultation
     */
    @Override
    public String provideService(String patientName) {
        String checkedPatient = requireText(patientName, "Patient name");
        return getName() + " reviews " + checkedPatient + " in the " + specialty
                + " clinic in " + department + ".";
    }

    /**
     * Returns the details of the specialist, adding the specialty and department to
     * the details of a doctor.
     *
     * @return the details of the specialist
     */
    @Override
    public String toString() {
        return super.toString() + NEW_LINE
                + detailLine("Specialty", specialty) + NEW_LINE
                + detailLine("Department", department);
    }
}
