import java.util.ArrayList;
import java.util.Collections;

/**
 * Assessment 1 - a health service appointment system.
 *
 * <p>This class is the entry point of the program and the scripted demonstration
 * the brief asks for. There is no user interface, so {@link #main(String[])} plays
 * the part of a real user: it creates the health professionals and the patients,
 * books appointments, and prints what the system does at every step, including what
 * happens when an operation cannot be completed.</p>
 *
 * <p>The classes work together as follows:</p>
 * <ul>
 *   <li>{@link HealthProfessional} holds what every professional shares and is
 *       abstract, so only real types can exist.</li>
 *   <li>{@link Doctor} adds prescribing behaviour for the doctor types;
 *       {@link GeneralPractitioner}, {@link Specialist} and {@link Surgeon} extend
 *       it, while {@link Nurse} and {@link Dietitian} extend
 *       {@link HealthProfessional} directly.</li>
 *   <li>{@link Patient} is its own class, so an appointment links two objects.</li>
 *   <li>{@link TimeSlot} and {@link ClinicSchedule} describe when the clinic is
 *       open and give the appointments a real time order.</li>
 *   <li>{@link Appointment} links a patient to a professional at a time, and
 *       {@link AppointmentManager} owns and manages the collection.</li>
 * </ul>
 *
 * @author Liu Huanman (25259078)
 * @version 1.0
 */
public class AssignmentOne {

    /** A heavy separator used between the parts of the demonstration. */
    private static final String SEPARATOR =
            "================================================================================";

    /** A light separator used between the steps inside a part. */
    private static final String THIN_SEPARATOR =
            "--------------------------------------------------------------------------------";

    /** The patient used as the example throughout the demonstration. */
    private static final String EXAMPLE_PATIENT = "Chloe Martin";

    /**
     * Runs the demonstration.
     *
     * @param args command line arguments; not used
     */
    public static void main(String[] args) {
        printBanner();

        // ------------------------------------------------------------------
        // Create the health professionals. Each one is created as its own type,
        // but they are also stored together as HealthProfessional objects.
        // ------------------------------------------------------------------
        ArrayList<HealthProfessional> professionals = new ArrayList<>();

        GeneralPractitioner drNguyen = new GeneralPractitioner("2001", "Dr Alice Nguyen",
                days(Weekday.MONDAY, Weekday.WEDNESDAY, Weekday.FRIDAY),
                "MED10023", "Room 3", 12);
        GeneralPractitioner drCarter = new GeneralPractitioner("2002", "Dr Ben Carter",
                days(Weekday.MONDAY, Weekday.TUESDAY, Weekday.THURSDAY),
                "MED10047", "Room 5", 6);
        GeneralPractitioner drSharma = new GeneralPractitioner("2003", "Dr Priya Sharma",
                days(Weekday.TUESDAY, Weekday.THURSDAY),
                "MED10052", "Room 1", 9);

        Specialist drLee = new Specialist("3001", "Dr Marcus Lee",
                days(Weekday.MONDAY, Weekday.TUESDAY, Weekday.FRIDAY),
                "MED20015", "Cardiology", "St Vincent's Hospital");
        Specialist drRossi = new Specialist("3002", "Dr Sofia Rossi",
                days(Weekday.WEDNESDAY, Weekday.THURSDAY, Weekday.FRIDAY),
                "MED20031", "Paediatrics", "Lismore Base Hospital");

        Surgeon drOkafor = new Surgeon("4001", "Dr Henry Okafor",
                days(Weekday.MONDAY, Weekday.THURSDAY),
                "MED30008", "Theatre 2", "Orthopaedics");

        Nurse nurseMiller = new Nurse("5001", "Nurse Grace Miller", "Ward 4B",
                "Registered Nurse");
        Nurse nurseBecker = new Nurse("5002", "Nurse Tom Becker",
                days(Weekday.MONDAY, Weekday.TUESDAY, Weekday.WEDNESDAY),
                "Ward 2A", "Enrolled Nurse");

        Dietitian msPrice = new Dietitian("6001", "Ms Hannah Price",
                days(Weekday.MONDAY, Weekday.FRIDAY),
                "Diabetes and weight management", "Accredited Practising Dietitian");

        professionals.add(drNguyen);
        professionals.add(drCarter);
        professionals.add(drSharma);
        professionals.add(drLee);
        professionals.add(drRossi);
        professionals.add(drOkafor);
        professionals.add(nurseMiller);
        professionals.add(nurseBecker);
        professionals.add(msPrice);

        // ------------------------------------------------------------------
        // Create the patients.
        // ------------------------------------------------------------------
        Patient chloe = new Patient(EXAMPLE_PATIENT, "0412 345 678");
        Patient daniel = new Patient("Daniel Kim", "0423 456 789");
        Patient emma = new Patient("Emma Wilson", "0434 567 890");
        Patient liam = new Patient("Liam O'Brien", "0445 678 901");

        // The doctor types also share the Doctor supertype, so they can be handled
        // together without knowing which kind of doctor each one is.
        Doctor[] doctors = {drNguyen, drCarter, drSharma, drLee, drRossi, drOkafor};

        demonstratePartOne(drNguyen);
        demonstratePartsTwoAndThree(professionals, doctors);
        demonstrateTypeSpecificBehaviour(drLee, drOkafor, nurseMiller, msPrice);
        demonstratePartFour(drNguyen, nurseMiller, chloe, daniel, emma);
        demonstratePartsFiveAndSix(professionals, chloe, daniel, emma, liam);

        printFooter();
    }

    /**
     * Demonstrates Part 1: the shared health professional class.
     *
     * @param sample a professional used to show the shared details
     */
    private static void demonstratePartOne(HealthProfessional sample) {
        printPartHeading("PART 1 - MODELLING A HEALTH PROFESSIONAL");

        System.out.println("HealthProfessional is abstract, so the compiler rejects");
        System.out.println("    new HealthProfessional(\"2001\", \"Dr Nobody\")");
        System.out.println("and a professional can only ever be created as a real type.");
        System.out.println();
        System.out.println("The class offers two ways of constructing an object:");
        System.out.println("    (id, name)                   -> works the default Monday to Friday week");
        System.out.println("    (id, name, availableDays)    -> works exactly the days supplied");
        System.out.println();
        System.out.println("Every field is private and set once by the constructor, and the");
        System.out.println("constructor validates each value, so an object can never exist in an");
        System.out.println("invalid state. The getters return copies of the mutable data.");
        System.out.println();
        System.out.println("Details of one professional, built by HealthProfessional.toString()");
        System.out.println("and the toString() of each type it inherits from:");
        System.out.println(indent(sample.toString(), "  "));

        printSubHeading("The constructor refuses to build an invalid object");
        demonstrateValidation();
    }

    /**
     * Demonstrates the validation rules of the classes by attempting to create
     * objects that break them and catching the exceptions.
     */
    private static void demonstrateValidation() {
        try {
            new Nurse("AB12", "Nurse Bad Id", "Ward 1A", "Registered Nurse");
            System.out.println("  ERROR: an ID with letters was accepted.");
        } catch (IllegalArgumentException error) {
            printRejection("rejected ID \"AB12\"", error.getMessage());
        }

        try {
            new Nurse("5003", "   ", "Ward 1A", "Registered Nurse");
            System.out.println("  ERROR: a blank name was accepted.");
        } catch (IllegalArgumentException error) {
            printRejection("rejected a blank name", error.getMessage());
        }

        try {
            new Nurse("5004", "Nurse No Days", new ArrayList<Weekday>(), "Ward 1A",
                    "Registered Nurse");
            System.out.println("  ERROR: an empty day list was accepted.");
        } catch (IllegalArgumentException error) {
            printRejection("rejected an empty week", error.getMessage());
        }

        try {
            new Nurse("5005", "Nurse Double Day",
                    days(Weekday.MONDAY, Weekday.MONDAY), "Ward 1A", "Registered Nurse");
            System.out.println("  ERROR: a repeated day was accepted.");
        } catch (IllegalArgumentException error) {
            printRejection("rejected a repeated day", error.getMessage());
        }

        try {
            new Patient("No Number", "0412-ABC");
            System.out.println("  ERROR: a mobile number with letters was accepted.");
        } catch (IllegalArgumentException error) {
            printRejection("rejected a bad mobile", error.getMessage());
        }

        try {
            new TimeSlot(9, 75);
            System.out.println("  ERROR: an impossible minute was accepted.");
        } catch (IllegalArgumentException error) {
            printRejection("rejected minute 75", error.getMessage());
        }
    }

    /**
     * Prints the label of a rejected object and the reason it was rejected, in two
     * aligned columns.
     *
     * @param label   what was rejected
     * @param message the message from the exception
     */
    private static void printRejection(String label, String message) {
        System.out.println(String.format("  %-24s -> %s", label, message));
    }

    /**
     * Demonstrates Parts 2 and 3: the specialised types, and working with objects of
     * different types through one common type.
     *
     * @param professionals the professionals to display and process
     * @param doctors       the same doctors, held through the shared Doctor type
     */
    private static void demonstratePartsTwoAndThree(ArrayList<HealthProfessional> professionals,
                                                    Doctor[] doctors) {
        printPartHeading("PARTS 2 AND 3 - SPECIALISING AND WORKING WITH HEALTH PROFESSIONALS");

        System.out.println("Hierarchy:");
        System.out.println("    HealthProfessional (abstract)");
        System.out.println("        +-- Doctor (abstract) -- adds prescribing and registration");
        System.out.println("        |     +-- GeneralPractitioner");
        System.out.println("        |     +-- Specialist");
        System.out.println("        |     +-- Surgeon");
        System.out.println("        +-- Nurse");
        System.out.println("        +-- Dietitian");
        System.out.println();
        System.out.println("Each type inherits the shared data and adds only what makes it");
        System.out.println("different. Each type overrides getProfessionalType(), getDuties()");
        System.out.println("and provideService(), so it responds in its own way.");

        printSubHeading("All professionals, stored together as HealthProfessional objects");
        System.out.printf("  %-8s %-26s %-28s %s%n", "ID", "NAME", "TYPE", "AVAILABLE DAYS");
        System.out.println("  " + THIN_SEPARATOR);
        for (HealthProfessional professional : professionals) {
            System.out.printf("  %-8s %-26s %-28s %s%n",
                    professional.getProfessionalId(),
                    professional.getName(),
                    professional.getProfessionalType(),
                    professional.getAvailableDaysText());
        }

        printSubHeading("Full details of each professional");
        for (HealthProfessional professional : professionals) {
            // One reference type, many real types: toString() is chosen at run time.
            System.out.println(indent(professional.toString(), "  "));
            System.out.println();
        }

        printSubHeading("Each professional provides its own service (polymorphism)");
        for (HealthProfessional professional : professionals) {
            System.out.println("  [" + professional.getProfessionalType() + "]");
            System.out.println("  " + professional.provideService(EXAMPLE_PATIENT));
        }

        printSubHeading("Doctor-only behaviour, through the Doctor type");
        System.out.println("  Only doctor types can prescribe, so they are processed as Doctor");
        System.out.println("  objects. prescribe() is written once in Doctor and inherited by every");
        System.out.println("  doctor type, so one line per doctor is produced:");
        System.out.println();
        for (Doctor doctor : doctors) {
            System.out.println("  " + doctor.prescribe(EXAMPLE_PATIENT, "Amoxicillin"));
        }
    }

    /**
     * Demonstrates the behaviour that belongs to one type only, such as a nurse
     * taking a blood sample.
     *
     * <p>These methods are deliberately not declared on
     * {@link HealthProfessional}: a dietitian cannot order a specialist test, so the
     * method only exists on the type that can actually do the job. Reaching it
     * through the specific type is what keeps the common type honest.</p>
     *
     * @param specialist a specialist doctor
     * @param surgeon    a surgeon
     * @param nurse      a nurse
     * @param dietitian  a dietitian
     */
    private static void demonstrateTypeSpecificBehaviour(Specialist specialist, Surgeon surgeon,
                                                         Nurse nurse, Dietitian dietitian) {
        printSubHeading("Behaviour that belongs to one type only");
        System.out.println("  Each of these methods exists on one class only, not on");
        System.out.println("  HealthProfessional, so the type that owns it must be used.");
        System.out.println();
        System.out.println("  " + specialist.orderTest("Daniel Kim", "blood culture"));
        System.out.println("  " + surgeon.performOperation("Daniel Kim", "knee arthroscopy"));
        System.out.println("  " + nurse.takeBloodSample("Daniel Kim"));
        System.out.println("  " + dietitian.createMealPlan("Daniel Kim"));
        System.out.println();
        System.out.println("  The surgeon's private operation count is now "
                + surgeon.getOperationsCompleted() + ", changed only through");
        System.out.println("  performOperation(), never by touching a field.");

        System.out.println();
        System.out.println("  Adding a sixth type would mean adding one subclass, and none of the");
        System.out.println("  loops in this demonstration would have to change.");
    }

    /**
     * Demonstrates Part 4: a single appointment, and the ordering of appointments.
     *
     * @param generalPractitioner a general practitioner to book
     * @param nurse               a nurse, to show an appointment works with any type
     * @param chloe               the first patient
     * @param daniel              the second patient
     * @param emma                the third patient
     */
    private static void demonstratePartFour(HealthProfessional generalPractitioner,
                                            HealthProfessional nurse, Patient chloe,
                                            Patient daniel, Patient emma) {
        printPartHeading("PART 4 - MODELLING AN APPOINTMENT");

        System.out.println("An appointment holds a Patient, a HealthProfessional and a TimeSlot.");
        System.out.println("All three are required, so the constructor refuses to build an");
        System.out.println("incomplete appointment. The professional is held as a");
        System.out.println("HealthProfessional, so any of the five types can be booked.");
        System.out.println();

        Appointment appointment = new Appointment(chloe, generalPractitioner,
                TimeSlot.parse("09:30"));
        System.out.println(indent(appointment.toString(), "  "));

        printSubHeading("The same appointment class works with a nurse");
        Appointment nurseAppointment = new Appointment(emma, nurse, TimeSlot.parse("08:30"));
        System.out.println("  " + Appointment.summaryHeading());
        System.out.println("  " + nurseAppointment.toSummaryLine());

        printSubHeading("Appointments know how to order themselves");
        System.out.println("  Appointment implements Comparable, comparing TimeSlot first, so");
        System.out.println("  Collections.sort() puts a whole day in time order.");
        System.out.println();

        ArrayList<Appointment> unsortedAppointments = new ArrayList<>();
        unsortedAppointments.add(new Appointment(chloe, generalPractitioner,
                TimeSlot.parse("10:30")));
        unsortedAppointments.add(new Appointment(daniel, generalPractitioner,
                TimeSlot.parse("08:00")));
        unsortedAppointments.add(new Appointment(emma, generalPractitioner,
                TimeSlot.parse("09:30")));

        System.out.println("  Before sorting (in the order they were created):");
        for (Appointment each : unsortedAppointments) {
            System.out.println("    " + each.getTimeSlot() + "  " + each.getPatient().getName());
        }

        // Sorting relies only on compareTo(), so it works for any appointment.
        Collections.sort(unsortedAppointments);

        System.out.println("  After Collections.sort():");
        for (Appointment each : unsortedAppointments) {
            System.out.println("    " + each.getTimeSlot() + "  " + each.getPatient().getName());
        }
    }

    /**
     * Demonstrates Parts 5 and 6: managing and working with the collection of
     * appointments, including the cases where an operation cannot be completed.
     *
     * @param professionals the professionals of the clinic
     * @param chloe         the first patient
     * @param daniel        the second patient
     * @param emma          the third patient
     * @param liam          the fourth patient
     */
    private static void demonstratePartsFiveAndSix(ArrayList<HealthProfessional> professionals,
                                                   Patient chloe, Patient daniel,
                                                   Patient emma, Patient liam) {
        printPartHeading("PARTS 5 AND 6 - MANAGING AND WORKING WITH APPOINTMENTS");

        HealthProfessional drNguyen = professionalWithId(professionals, "2001");
        HealthProfessional drCarter = professionalWithId(professionals, "2002");
        HealthProfessional drSharma = professionalWithId(professionals, "2003");
        HealthProfessional drLee = professionalWithId(professionals, "3001");
        HealthProfessional drOkafor = professionalWithId(professionals, "4001");
        HealthProfessional nurseMiller = professionalWithId(professionals, "5001");
        HealthProfessional msPrice = professionalWithId(professionals, "6001");

        // The clinic day is fixed for the demonstration: all appointments are for
        // one day, which is what "bookings are only for the current day" means here.
        ClinicSchedule schedule = new ClinicSchedule();
        AppointmentManager manager = new AppointmentManager(schedule, Weekday.MONDAY);

        System.out.println("Clinic schedule : " + manager.getClinicSchedule());
        System.out.println("Booking day     : " + manager.getBookingDay());
        System.out.println("Booked so far   : " + manager.getAppointmentCount() + " appointment(s)");

        printSubHeading("Adding appointments (deliberately out of time order)");
        System.out.println("  " + manager.addAppointment(daniel, drOkafor, TimeSlot.parse("13:30")));
        System.out.println("  " + manager.addAppointment(emma, nurseMiller, TimeSlot.parse("08:30")));
        System.out.println("  " + manager.addAppointment(liam, drCarter, TimeSlot.parse("10:00")));
        System.out.println("  " + manager.addAppointment(chloe, drNguyen, TimeSlot.parse("08:00")));
        System.out.println("  " + manager.addAppointment(chloe, drNguyen, TimeSlot.parse("14:00")));
        System.out.println("  " + manager.addAppointment(daniel, drLee, TimeSlot.parse("09:00")));
        System.out.println("  " + manager.addAppointment(emma, msPrice, TimeSlot.parse("11:00")));

        printSubHeading("Bookings that must be refused");
        System.out.println("  A professional is already booked in that slot:");
        System.out.println("  " + manager.addAppointment(liam, drLee, TimeSlot.parse("09:00")));

        System.out.println("  The professional does not work on the booking day:");
        System.out.println("  " + manager.addAppointment(emma, drSharma, TimeSlot.parse("10:30")));

        System.out.println("  The time is outside the clinic hours:");
        System.out.println("  " + manager.addAppointment(liam, drNguyen, TimeSlot.parse("17:00")));

        System.out.println("  The time is not on the 30 minute slot grid:");
        System.out.println("  " + manager.addAppointment(liam, drNguyen, TimeSlot.parse("08:15")));

        System.out.println("  A required detail is missing:");
        System.out.println("  " + manager.addAppointment(null, drNguyen, TimeSlot.parse("12:00")));

        printSubHeading("All appointments, sorted from earliest to latest");
        System.out.println(manager.displayAllAppointments());

        printSubHeading("Filtering by health professional ID");
        System.out.println(manager.displayAppointmentsForProfessional("2001"));
        System.out.println();
        System.out.println(manager.displayAppointmentsForProfessional("5001"));

        printSubHeading("Filtering by patient mobile number");
        System.out.println("  The number can be typed with spaces; it is normalised first.");
        System.out.println();
        System.out.println(manager.displayAppointmentsForPatient("0412 345 678"));

        printSubHeading("Filters that find nothing, or are not valid");
        System.out.println("  " + manager.displayAppointmentsForProfessional("9999"));
        System.out.println("  " + manager.displayAppointmentsForProfessional("20A1"));
        System.out.println("  " + manager.displayAppointmentsForPatient("0499 999 999"));
        System.out.println("  " + manager.displayAppointmentsForPatient("0412-345"));

        printSubHeading("Cancelling an appointment by professional ID and time");
        System.out.println("  " + manager.cancelAppointment("2001", TimeSlot.parse("08:00")));
        System.out.println("  Cancelling the same appointment again:");
        System.out.println("  " + manager.cancelAppointment("2001", TimeSlot.parse("08:00")));

        printSubHeading("Cancellations that cannot be done");
        System.out.println("  " + manager.cancelAppointment("3001", TimeSlot.parse("15:00")));
        System.out.println("  " + manager.cancelAppointment("20A1", TimeSlot.parse("09:00")));

        printSubHeading("Cancelling every appointment for one patient");
        System.out.println("  " + manager.cancelAppointmentsForPatient("0412 345 678"));
        System.out.println("  Cancelling for the same patient again:");
        System.out.println("  " + manager.cancelAppointmentsForPatient("0412 345 678"));

        printSubHeading("The collection after the cancellations");
        System.out.println(manager.displayAllAppointments());
        System.out.println();
        System.out.println("  Appointment count: " + manager.getAppointmentCount());
    }

    /**
     * Prints the opening banner of the demonstration.
     */
    private static void printBanner() {
        System.out.println(SEPARATOR);
        System.out.println(" PROG2004 Object Oriented Programming - Assessment 1");
        System.out.println(" Health Service Appointment System");
        System.out.println(" Liu Huanman  |  Student ID 25259078");
        System.out.println();
        System.out.println(" A scripted demonstration of a set of collaborating classes. There is");
        System.out.println(" no user interface: the main method plays the part of the user, creates");
        System.out.println(" the objects and prints the results.");
        System.out.println(SEPARATOR);
    }

    /**
     * Prints the closing note of the demonstration.
     */
    private static void printFooter() {
        System.out.println();
        System.out.println(SEPARATOR);
        System.out.println(" END OF DEMONSTRATION");
        System.out.println(" Parts 1 to 6 demonstrated: class design, inheritance, polymorphism,");
        System.out.println(" encapsulation, abstraction, collection management and error handling.");
        System.out.println(SEPARATOR);
    }

    /**
     * Prints the heading of one part of the demonstration.
     *
     * @param title the title of the part
     */
    private static void printPartHeading(String title) {
        System.out.println();
        System.out.println(SEPARATOR);
        System.out.println(" " + title);
        System.out.println(SEPARATOR);
    }

    /**
     * Prints the heading of one step inside a part.
     *
     * @param title the title of the step
     */
    private static void printSubHeading(String title) {
        System.out.println();
        System.out.println("  " + title);
        System.out.println("  " + THIN_SEPARATOR);
    }

    /**
     * Puts a prefix in front of every line of a block of text, so that printed
     * objects line up under the section they belong to.
     *
     * @param text   the text to indent
     * @param prefix the text to put in front of each line
     * @return the indented text
     */
    private static String indent(String text, String prefix) {
        StringBuilder indented = new StringBuilder();
        String[] lines = text.split(System.lineSeparator());
        for (int i = 0; i < lines.length; i++) {
            indented.append(prefix).append(lines[i]);
            if (i < lines.length - 1) {
                indented.append(System.lineSeparator());
            }
        }
        return indented.toString();
    }

    /**
     * Builds a list of working days, so that a call reads like the real week.
     *
     * @param weekdays the days the professional works
     * @return a new list holding the days
     */
    private static ArrayList<Weekday> days(Weekday... weekdays) {
        ArrayList<Weekday> availableDays = new ArrayList<>();
        for (Weekday weekday : weekdays) {
            availableDays.add(weekday);
        }
        return availableDays;
    }

    /**
     * Finds a professional in the clinic by ID.
     *
     * @param professionals the professionals of the clinic
     * @param professionalId the ID to look for
     * @return the matching professional, or {@code null} if there is none
     */
    private static HealthProfessional professionalWithId(
            ArrayList<HealthProfessional> professionals, String professionalId) {
        for (HealthProfessional professional : professionals) {
            if (professional.getProfessionalId().equals(professionalId)) {
                return professional;
            }
        }
        return null;
    }
}
