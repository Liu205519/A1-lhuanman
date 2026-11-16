/**
 * Models a patient who books an appointment.
 *
 * <p>The brief asks whether a patient should be its own class. It should: a patient
 * is a real thing with its own data and its own rules, it is reused by many
 * appointments, and giving it a class means the rules about a valid name and a
 * valid mobile number are written once instead of being repeated in every place
 * that stores patient details.</p>
 *
 * <p>The mobile number is stored in a single normalised form (ten digits, no
 * spaces) so that the same number can always be compared reliably, while
 * {@link #getFormattedMobileNumber()} displays it in the readable
 * "0412 345 678" layout.</p>
 */
public class Patient {

    /** The number of digits in a mobile number, for example 0412345678. */
    private static final int MOBILE_NUMBER_LENGTH = 10;

    /** The full name of the patient. */
    private final String name;

    /** The mobile number of the patient, stored as ten digits only. */
    private final String mobileNumber;

    /**
     * Creates a patient.
     *
     * @param name         the full name of the patient
     * @param mobileNumber the mobile number, which may be typed with spaces or hyphens
     * @throws IllegalArgumentException if the name is blank or the mobile number
     *                                  does not contain ten digits
     */
    public Patient(String name, String mobileNumber) {
        this.name = requireName(name);
        // Stored as ten digits only, so "0412 345 678" and "0412345678" are the
        // same patient when appointments are filtered or cancelled.
        this.mobileNumber = requireMobileNumber(mobileNumber);
    }

    /**
     * Returns the full name of the patient.
     *
     * @return the name of the patient
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the mobile number as ten digits with no separators, so that two
     * numbers can be compared reliably.
     *
     * @return the normalised mobile number
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * Returns the mobile number in the readable "0412 345 678" layout.
     *
     * @return the formatted mobile number
     */
    public String getFormattedMobileNumber() {
        return formatMobileNumber(mobileNumber);
    }

    /**
     * Reports whether a value is a valid mobile number.
     *
     * <p>Used by the appointment manager to check a number typed by a user before
     * searching for appointments with it.</p>
     *
     * @param mobileNumber the value to check
     * @return {@code true} if the value contains exactly ten digits
     */
    public static boolean isValidMobileNumber(String mobileNumber) {
        return normaliseMobileNumber(mobileNumber).length() == MOBILE_NUMBER_LENGTH;
    }

    /**
     * Removes the separators people type in a phone number, such as spaces, hyphens
     * and brackets, and returns the digits only.
     *
     * @param mobileNumber the value to normalise
     * @return the digits of the number, or an empty string if the value is null or
     *         contains a character that cannot be part of a phone number
     */
    public static String normaliseMobileNumber(String mobileNumber) {
        if (mobileNumber == null) {
            return "";
        }
        StringBuilder digits = new StringBuilder();
        for (int i = 0; i < mobileNumber.length(); i++) {
            char character = mobileNumber.charAt(i);
            if (Character.isDigit(character)) {
                digits.append(character);
            } else if (character != ' ' && character != '-' && character != '('
                    && character != ')') {
                // Anything else, such as a letter, means the number cannot be valid.
                return "";
            }
        }
        return digits.toString();
    }

    /**
     * Formats a ten digit mobile number in the readable "0412 345 678" layout.
     *
     * @param mobileNumber a mobile number already normalised to ten digits
     * @return the formatted number, or the input unchanged if it is not ten digits
     */
    public static String formatMobileNumber(String mobileNumber) {
        if (mobileNumber == null || mobileNumber.length() != MOBILE_NUMBER_LENGTH) {
            return String.valueOf(mobileNumber);
        }
        return mobileNumber.substring(0, 4) + " "
                + mobileNumber.substring(4, 7) + " "
                + mobileNumber.substring(7);
    }

    /**
     * Compares this patient with another object. Two patients are the same person
     * when they have the same mobile number.
     *
     * @param other the object to compare with
     * @return {@code true} if both objects are patients with the same mobile number
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Patient)) {
            return false;
        }
        Patient otherPatient = (Patient) other;
        return mobileNumber.equals(otherPatient.mobileNumber);
    }

    /**
     * Returns a hash code consistent with {@link #equals(Object)}.
     *
     * @return the hash code of the mobile number
     */
    @Override
    public int hashCode() {
        return mobileNumber.hashCode();
    }

    /**
     * Returns the name and mobile number of the patient.
     *
     * @return the details of the patient, for example "Chloe Martin (0412 345 678)"
     */
    @Override
    public String toString() {
        return name + " (" + getFormattedMobileNumber() + ")";
    }

    /**
     * Validates the name of a patient.
     *
     * @param name the value to validate
     * @return the trimmed name
     * @throws IllegalArgumentException if the name is null or blank
     */
    private static String requireName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Patient name is required.");
        }
        return name.trim();
    }

    /**
     * Validates a mobile number and returns it in its normalised form.
     *
     * @param mobileNumber the value to validate
     * @return the mobile number as ten digits
     * @throws IllegalArgumentException if the value does not contain ten digits
     */
    private static String requireMobileNumber(String mobileNumber) {
        String normalised = normaliseMobileNumber(mobileNumber);
        if (normalised.length() != MOBILE_NUMBER_LENGTH) {
            throw new IllegalArgumentException(
                    "Mobile number must contain " + MOBILE_NUMBER_LENGTH
                            + " digits, but was: \"" + mobileNumber + "\"");
        }
        return normalised;
    }
}
/**
 * Models a patient who books an appointment.
 *
 * <p>The brief asks whether a patient should be its own class. It should: a patient
 * is a real thing with its own data and its own rules, it is reused by many
 * appointments, and giving it a class means the rules about a valid name and a
 * valid mobile number are written once instead of being repeated in every place
 * that stores patient details.</p>
 *
 * <p>The mobile number is stored in a single normalised form (ten digits, no
 * spaces) so that the same number can always be compared reliably, while
 * {@link #getFormattedMobileNumber()} displays it in the readable
 * "0412 345 678" layout.</p>
 */
public class Patient {

    /** The number of digits in a mobile number, for example 0412345678. */
    private static final int MOBILE_NUMBER_LENGTH = 10;

    /** The full name of the patient. */
    private final String name;

    /** The mobile number of the patient, stored as ten digits only. */
    private final String mobileNumber;

    /**
     * Creates a patient.
     *
     * @param name         the full name of the patient
     * @param mobileNumber the mobile number, which may be typed with spaces or hyphens
     * @throws IllegalArgumentException if the name is blank or the mobile number
     *                                  does not contain ten digits
     */
    public Patient(String name, String mobileNumber) {
        this.name = requireName(name);
        this.mobileNumber = requireMobileNumber(mobileNumber);
    }

    /**
     * Returns the full name of the patient.
     *
     * @return the name of the patient
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the mobile number as ten digits with no separators, so that two
     * numbers can be compared reliably.
     *
     * @return the normalised mobile number
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * Returns the mobile number in the readable "0412 345 678" layout.
     *
     * @return the formatted mobile number
     */
    public String getFormattedMobileNumber() {
        return formatMobileNumber(mobileNumber);
    }

    /**
     * Reports whether a value is a valid mobile number.
     *
     * <p>Used by the appointment manager to check a number typed by a user before
     * searching for appointments with it.</p>
     *
     * @param mobileNumber the value to check
     * @return {@code true} if the value contains exactly ten digits
     */
    public static boolean isValidMobileNumber(String mobileNumber) {
        return normaliseMobileNumber(mobileNumber).length() == MOBILE_NUMBER_LENGTH;
    }

    /**
     * Removes the separators people type in a phone number, such as spaces, hyphens
     * and brackets, and returns the digits only.
     *
     * @param mobileNumber the value to normalise
     * @return the digits of the number, or an empty string if the value is null or
     *         contains a character that cannot be part of a phone number
     */
    public static String normaliseMobileNumber(String mobileNumber) {
        if (mobileNumber == null) {
            return "";
        }
        StringBuilder digits = new StringBuilder();
        for (int i = 0; i < mobileNumber.length(); i++) {
            char character = mobileNumber.charAt(i);
            if (Character.isDigit(character)) {
                digits.append(character);
            } else if (character != ' ' && character != '-' && character != '('
                    && character != ')') {
                // Anything else, such as a letter, means the number cannot be valid.
                return "";
            }
        }
        return digits.toString();
    }

    /**
     * Formats a ten digit mobile number in the readable "0412 345 678" layout.
     *
     * @param mobileNumber a mobile number already normalised to ten digits
     * @return the formatted number, or the input unchanged if it is not ten digits
     */
    public static String formatMobileNumber(String mobileNumber) {
        if (mobileNumber == null || mobileNumber.length() != MOBILE_NUMBER_LENGTH) {
            return String.valueOf(mobileNumber);
        }
        return mobileNumber.substring(0, 4) + " "
                + mobileNumber.substring(4, 7) + " "
                + mobileNumber.substring(7);
    }

    /**
     * Compares this patient with another object. Two patients are the same person
     * when they have the same mobile number.
     *
     * @param other the object to compare with
     * @return {@code true} if both objects are patients with the same mobile number
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Patient)) {
            return false;
        }
        Patient otherPatient = (Patient) other;
        return mobileNumber.equals(otherPatient.mobileNumber);
    }

    /**
     * Returns a hash code consistent with {@link #equals(Object)}.
     *
     * @return the hash code of the mobile number
     */
    @Override
    public int hashCode() {
        return mobileNumber.hashCode();
    }

    /**
     * Returns the name and mobile number of the patient.
     *
     * @return the details of the patient, for example "Chloe Martin (0412 345 678)"
     */
    @Override
    public String toString() {
        return name + " (" + getFormattedMobileNumber() + ")";
    }

    /**
     * Validates the name of a patient.
     *
     * @param name the value to validate
     * @return the trimmed name
     * @throws IllegalArgumentException if the name is null or blank
     */
    private static String requireName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Patient name is required.");
        }
        return name.trim();
    }

    /**
     * Validates a mobile number and returns it in its normalised form.
     *
     * @param mobileNumber the value to validate
     * @return the mobile number as ten digits
     * @throws IllegalArgumentException if the value does not contain ten digits
     */
    private static String requireMobileNumber(String mobileNumber) {
        String normalised = normaliseMobileNumber(mobileNumber);
        if (normalised.length() != MOBILE_NUMBER_LENGTH) {
            throw new IllegalArgumentException(
                    "Mobile number must contain " + MOBILE_NUMBER_LENGTH
                            + " digits, but was: \"" + mobileNumber + "\"");
        }
        return normalised;
    }
}
