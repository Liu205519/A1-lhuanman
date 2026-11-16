import java.util.ArrayList;

/**
 * Models a dietitian.
 *
 * <p>Dietitians extend {@link HealthProfessional} directly. Their own behaviour is
 * creating a meal plan for a patient, and their own details are the area of
 * nutrition they focus on and their qualification.</p>
 */
public class Dietitian extends HealthProfessional {

    /** The nutrition area the dietitian focuses on. */
    private final String focusArea;

    /** The qualification of the dietitian. */
    private final String qualification;

    /**
     * Creates a dietitian who works the default Monday to Friday week.
     *
     * @param professionalId the identification number, digits only
     * @param name           the full name of the dietitian
     * @param focusArea      the nutrition area the dietitian focuses on
     * @param qualification  the qualification of the dietitian
     * @throws IllegalArgumentException if any value is missing or invalid
     */
    public Dietitian(String professionalId, String name, String focusArea, String qualification) {
        super(professionalId, name);
        this.focusArea = requireText(focusArea, "Focus area");
        this.qualification = requireText(qualification, "Qualification");
    }

    /**
     * Creates a dietitian with a specific set of working days.
     *
     * @param professionalId the identification number, digits only
     * @param name           the full name of the dietitian
     * @param availableDays  the days the dietitian works
     * @param focusArea      the nutrition area the dietitian focuses on
     * @param qualification  the qualification of the dietitian
     * @throws IllegalArgumentException if any value is missing or invalid
     */
    public Dietitian(String professionalId, String name, ArrayList<Weekday> availableDays,
                     String focusArea, String qualification) {
        super(professionalId, name, availableDays);
        this.focusArea = requireText(focusArea, "Focus area");
        this.qualification = requireText(qualification, "Qualification");
    }

    /**
     * Returns the nutrition area the dietitian focuses on.
     *
     * @return the focus area
     */
    public String getFocusArea() {
        return focusArea;
    }

    /**
     * Returns the qualification of the dietitian.
     *
     * @return the qualification
     */
    public String getQualification() {
        return qualification;
    }

    /**
     * Creates a meal plan for a patient.
     *
     * @param patientName the name of the patient
     * @return a description of the meal plan
     */
    public String createMealPlan(String patientName) {
        String checkedPatient = requireText(patientName, "Patient name");
        return getName() + " creates a meal plan for " + checkedPatient + " focused on "
                + focusArea + ".";
    }

    /**
     * Returns the type name shown wherever the professional is described.
     *
     * @return "Dietitian"
     */
    @Override
    public String getProfessionalType() {
        return "Dietitian";
    }

    /**
     * Returns a description of the work a dietitian does.
     *
     * @return the duties of a dietitian
     */
    @Override
    public String getDuties() {
        return "Plans and reviews the nutrition and diet of patients.";
    }

    /**
     * Reviews the diet of a patient during an appointment.
     *
     * @param patientName the name of the patient being seen
     * @return a description of the review
     */
    @Override
    public String provideService(String patientName) {
        String checkedPatient = requireText(patientName, "Patient name");
        return getName() + " reviews the diet of " + checkedPatient + " for "
                + focusArea + ".";
    }

    /**
     * Returns the details of the dietitian, adding the focus area and qualification
     * to the details inherited from {@link HealthProfessional}.
     *
     * @return the details of the dietitian
     */
    @Override
    public String toString() {
        return super.toString() + NEW_LINE
                + detailLine("Focus area", focusArea) + NEW_LINE
                + detailLine("Qualification", qualification);
    }
}
