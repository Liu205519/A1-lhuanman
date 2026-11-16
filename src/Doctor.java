import java.util.ArrayList;

/**
 * Models a health professional who is a doctor.
 *
 * <p>Doctors share behaviour that nurses and dietitians do not have: they are
 * registered with a medical board and they can prescribe medication. Putting that
 * shared behaviour here, instead of copying it into every doctor type, means
 * {@code GeneralPractitioner}, {@code Specialist} and {@code Surgeon} inherit it
 * once. This middle layer also shows that inheritance can be more than one level
 * deep without repeating any of the data already held by
 * {@link HealthProfessional}.</p>
 */
public abstract class Doctor extends HealthProfessional {

    /** The medical registration number of the doctor. */
    private final String medicalRegistrationNumber;

    /**
     * Creates a doctor who works the default Monday to Friday week.
     *
     * @param professionalId            the identification number, digits only
     * @param name                      the full name of the doctor
     * @param medicalRegistrationNumber the registration number with the medical board
     * @throws IllegalArgumentException if any value is missing or invalid
     */
    protected Doctor(String professionalId, String name, String medicalRegistrationNumber) {
        super(professionalId, name);
        this.medicalRegistrationNumber = requireText(medicalRegistrationNumber,
                "Medical registration number");
    }

    /**
     * Creates a doctor with a specific set of working days.
     *
     * @param professionalId            the identification number, digits only
     * @param name                      the full name of the doctor
     * @param availableDays             the days the doctor works
     * @param medicalRegistrationNumber the registration number with the medical board
     * @throws IllegalArgumentException if any value is missing or invalid
     */
    protected Doctor(String professionalId, String name, ArrayList<Weekday> availableDays,
                     String medicalRegistrationNumber) {
        super(professionalId, name, availableDays);
        this.medicalRegistrationNumber = requireText(medicalRegistrationNumber,
                "Medical registration number");
    }

    /**
     * Returns the medical registration number of the doctor.
     *
     * @return the registration number
     */
    public String getMedicalRegistrationNumber() {
        return medicalRegistrationNumber;
    }

    /**
     * Prescribes a medication to a patient.
     *
     * <p>This behaviour belongs to doctors only, so it is declared here rather than
     * in {@link HealthProfessional}. Every doctor type can prescribe without
     * writing the method again, which is the point of putting shared behaviour as
     * high in the hierarchy as it can go.</p>
     *
     * @param patientName the name of the patient
     * @param medication  the medication being prescribed
     * @return a description of the prescription
     */
    public String prescribe(String patientName, String medication) {
        String checkedPatient = requireText(patientName, "Patient name");
        String checkedMedication = requireText(medication, "Medication");
        return getName() + " (registration " + medicalRegistrationNumber + ") prescribes "
                + checkedMedication + " to " + checkedPatient + ".";
    }

    /**
     * Returns the details of the doctor, adding the registration number to the
     * details inherited from {@link HealthProfessional}.
     *
     * @return the details of the doctor
     */
    @Override
    public String toString() {
        return super.toString() + NEW_LINE
                + detailLine("Registration", medicalRegistrationNumber);
    }
}
