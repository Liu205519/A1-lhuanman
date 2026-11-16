import java.util.ArrayList;

/**
 * Models the fixed daily schedule that the clinic runs to.
 *
 * <p>Rather than scattering 08:00, 16:00 and 30 across the code, the whole
 * schedule is described in one place. The opening time, the closing time and the
 * length of an appointment are all supplied through the constructor, so if the
 * clinic ever changes its hours the rest of the system keeps working without any
 * changes: the appointments are still checked against this object.</p>
 */
public class ClinicSchedule {

    /** The default opening time of the clinic, 08:00. */
    public static final int DEFAULT_OPENING_HOUR = 8;

    /** The default closing time of the clinic, 16:00. */
    public static final int DEFAULT_CLOSING_HOUR = 16;

    /** The default length of one appointment, in minutes. */
    public static final int DEFAULT_SLOT_LENGTH_MINUTES = 30;

    /** The time the clinic opens. */
    private final TimeSlot openingTime;

    /** The time the clinic closes; no appointment may finish after this time. */
    private final TimeSlot closingTime;

    /** The length of one appointment, in minutes. */
    private final int slotLengthMinutes;

    /** Every bookable time slot of the day, from the opening time onwards. */
    private final ArrayList<TimeSlot> timeSlots;

    /**
     * Creates the standard clinic schedule of 08:00 to 16:00 in 30 minute slots.
     */
    public ClinicSchedule() {
        this(new TimeSlot(DEFAULT_OPENING_HOUR, 0),
                new TimeSlot(DEFAULT_CLOSING_HOUR, 0),
                DEFAULT_SLOT_LENGTH_MINUTES);
    }

    /**
     * Creates a clinic schedule with specific hours and appointment length.
     *
     * @param openingTime      the time the clinic opens
     * @param closingTime      the time the clinic closes
     * @param slotLengthMinutes the length of one appointment, in minutes
     * @throws IllegalArgumentException if a time is missing, the closing time is not
     *                                  after the opening time, or the slot length is
     *                                  not positive
     */
    public ClinicSchedule(TimeSlot openingTime, TimeSlot closingTime, int slotLengthMinutes) {
        if (openingTime == null) {
            throw new IllegalArgumentException("An opening time is required.");
        }
        if (closingTime == null) {
            throw new IllegalArgumentException("A closing time is required.");
        }
        if (slotLengthMinutes <= 0) {
            throw new IllegalArgumentException("The slot length must be greater than zero.");
        }
        if (closingTime.compareTo(openingTime) <= 0) {
            throw new IllegalArgumentException("The clinic must close after it opens.");
        }
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.slotLengthMinutes = slotLengthMinutes;
        this.timeSlots = buildTimeSlots();
    }

    /**
     * Returns the time the clinic opens.
     *
     * @return the opening time
     */
    public TimeSlot getOpeningTime() {
        return openingTime;
    }

    /**
     * Returns the time the clinic closes.
     *
     * @return the closing time
     */
    public TimeSlot getClosingTime() {
        return closingTime;
    }

    /**
     * Returns the length of one appointment.
     *
     * @return the slot length in minutes
     */
    public int getSlotLengthMinutes() {
        return slotLengthMinutes;
    }

    /**
     * Returns a copy of every bookable slot of the day.
     *
     * <p>A copy is returned so that external code cannot change the schedule the
     * manager is using.</p>
     *
     * @return a new list of the bookable time slots, in time order
     */
    public ArrayList<TimeSlot> getTimeSlots() {
        return new ArrayList<>(timeSlots);
    }

    /**
     * Returns how many appointments fit into one clinic day.
     *
     * @return the number of bookable slots
     */
    public int getSlotCount() {
        return timeSlots.size();
    }

    /**
     * Reports whether a time is one of the bookable slots of the clinic day.
     *
     * <p>This is what makes the 30 minute grid real: 09:15 is inside the opening
     * hours but is not an appointment slot, so it is rejected.</p>
     *
     * @param timeSlot the time to check
     * @return {@code true} if the time is a bookable slot
     */
    public boolean isScheduledSlot(TimeSlot timeSlot) {
        return timeSlot != null && timeSlots.contains(timeSlot);
    }

    /**
     * Returns a one line description of the schedule.
     *
     * @return the schedule, for example
     *         "08:00 - 16:00 in 30-minute slots (16 slots per day)"
     */
    @Override
    public String toString() {
        return openingTime + " - " + closingTime + " in " + slotLengthMinutes
                + "-minute slots (" + timeSlots.size() + " slots per day)";
    }

    /**
     * Builds every bookable slot from the opening time up to the closing time.
     *
     * @return the slots of the day, in time order
     */
    private ArrayList<TimeSlot> buildTimeSlots() {
        ArrayList<TimeSlot> slots = new ArrayList<>();
        // The slots are generated from the opening time, the closing time and the
        // slot length, so changing the clinic hours needs no other change anywhere.
        int closingMinutes = closingTime.getMinutesSinceMidnight();
        for (int start = openingTime.getMinutesSinceMidnight();
                start + slotLengthMinutes <= closingMinutes;
                start = start + slotLengthMinutes) {
            slots.add(new TimeSlot(start / 60, start % 60));
        }
        return slots;
    }
}
