import java.util.ArrayList;

/**
 * Models a general practitioner: the doctor a patient usually sees first.
 *
 * <p>A general practitioner adds a consulting room and a number of years of
 * experience to what a {@link Doctor} already provides, and sees patients for
 * general consultations.</p>
 */
public class GeneralPractitioner extends Doctor {

    /** The consulting room the general practitioner works from. */
    private final String consultRoom;

    /** The number of years the general practitioner has been practising. */
    private final int yearsOfExperience;

    /**
     * Creates a general practitioner who works the default Monday to Friday week
     * and has just started practising.
     *
     * <p>This is the convenience way of constructing the object: the caller
     * supplies only what makes this general practitioner different from any other,
     * and the working days default to a standard week.</p>
     *
     * @param professionalId            the identification number, digits only
     * @param name                      the full name of the doctor
     * @param medicalRegistrationNumber the registration number with the medical board
     * @param consultRoom               the consulting room
     * @throws IllegalArgumentException if any value is missing or invalid
     */
    public GeneralPractitioner(String professionalId, String name,
                               String medicalRegistrationNumber, String consultRoom) {
        super(professionalId, name, medicalRegistrationNumber);
        this.consultRoom = requireText(consultRoom, "Consulting room");
        this.yearsOfExperience = 0;
    }

    /**
     * Creates a general practitioner with a specific set of working days and a
     * number of years of experience.
     *
     * @param professionalId            the identification number, digits only
     * @param name                      the full name of the doctor
     * @param availableDays             the days the doctor works
     * @param medicalRegistrationNumber the registration number with the medical board
     * @param consultRoom               the consulting room
     * @param yearsOfExperience         the number of years practising; zero or more
     * @throws IllegalArgumentException if any value is missing or invalid
     */
    public GeneralPractitioner(String professionalId, String name, ArrayList<Weekday> availableDays,
                               String medicalRegistrationNumber, String consultRoom,
                               int yearsOfExperience) {
        super(professionalId, name, availableDays, medicalRegistrationNumber);
        this.consultRoom = requireText(consultRoom, "Consulting room");
        if (yearsOfExperience < 0) {
            throw new IllegalArgumentException("Years of experience cannot be negative.");
        }
        this.yearsOfExperience = yearsOfExperience;
    }

    /**
     * Returns the consulting room of the general practitioner.
     *
     * @return the consulting room
     */
    public String getConsultRoom() {
        return consultRoom;
    }

    /**
     * Returns the years of experience of the general practitioner.
     *
     * @return the number of years practising
     */
    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    /**
     * Returns the type name shown wherever the professional is described.
     *
     * @return "General Practitioner"
     */
    @Override
    public String getProfessionalType() {
        return "General Practitioner";
    }

    /**
     * Returns a description of the work a general practitioner does.
     *
     * @return the duties of a general practitioner
     */
    @Override
    public String getDuties() {
        return "Diagnoses and treats common health problems and refers patients on.";
    }

    /**
     * Sees a patient for a general consultation.
     *
     * @param patientName the name of the patient being seen
     * @return a description of the consultation
     */
    @Override
    public String provideService(String patientName) {
        String checkedPatient = requireText(patientName, "Patient name");
        return getName() + " conducts a general consultation with " + checkedPatient
                + " in " + consultRoom + ".";
    }

    /**
     * Returns the details of the general practitioner, adding the consulting room
     * and experience to the details of a doctor.
     *
     * @return the details of the general practitioner
     */
    @Override
    public String toString() {
        return super.toString() + NEW_LINE
                + detailLine("Consult room", consultRoom) + NEW_LINE
                + detailLine("Experience", yearsOfExperience + " years");
    }
}
