# Design notes and video script

Supporting notes for `A1-lhuanman`. Nothing here is compiled; it exists so that every
design decision in the code can be explained, and so the video can be recorded quickly.

---

## 1. The brief's design considerations, answered

### Part 1 — Modelling a health professional

| Question from the brief | Answer in this design |
| --- | --- |
| What is genuinely common to all professionals? | ID, name and the days worked. They live in `HealthProfessional` and nowhere else. |
| How is a "fixed collection of values" stored? | `Weekday` is an `enum`, so only real days exist, and the days are held in an `ArrayList<Weekday>` that is sorted Monday–Sunday. |
| How is controlled access provided? | Every field is `private final`; values are validated in the constructor; `getAvailableDays()` returns a copy. |
| How does an object print its details? | `HealthProfessional.toString()` builds a labelled block using the protected `detailLine()` helper; printing the object with `System.out.println` uses it automatically. |
| Is a second constructor worthwhile? | Yes: `(id, name)` defaults to Monday–Friday for the majority of staff, while `(id, name, availableDays)` sets exact days. |

### Part 2 — Specialising health professionals

| Question | Answer |
| --- | --- |
| What relationship allows reuse? | Inheritance. `extends` gives every type the shared data and behaviour with no copying. |
| What is unique to each type? | GP: room, experience. Specialist: specialty, department. Surgeon: theatre, surgical area, operations. Nurse: ward, grade. Dietitian: focus area, qualification. |
| Is a second constructor worthwhile here? | Yes, each type repeats the same pattern: full constructor plus a Monday–Friday convenience constructor. |
| How are shared and type-specific values set up correctly? | The subclass constructor calls `super(...)` first, so the shared fields are validated before the subclass adds its own. |
| How does a specific type print shared *and* own details? | Each `toString()` calls `super.toString()` and appends its own lines — three levels deep for the doctor types. |
| Why hold different types through one type? | A `HealthProfessional[]`/`ArrayList<HealthProfessional>` holds all five types; a single loop calls `provideService()` and each object responds in its own way. Adding a sixth type changes no loop. |
| Should a plain professional ever exist? | No. `HealthProfessional` and `Doctor` are `abstract`, which stops `new HealthProfessional(...)` at compile time and forces every type to implement `getProfessionalType()`, `getDuties()` and `provideService()`. |

### Part 3 — Working with health professional objects

* Nine objects of five types are created in `main` (three GPs, two specialists, one surgeon,
  two nurses, one dietitian) and stored in one `ArrayList<HealthProfessional>`.
* The details loop, the table loop and the service loop are each written once and do not
  mention any concrete type, so they do not change when a type is added.
* Each object responds differently to the same call because `provideService()` is overridden.

### Part 4 — Modelling an appointment

| Question | Answer |
| --- | --- |
| Should `Patient` be its own class? | Yes — separate entity, reused across appointments, own validation rules. |
| What does an appointment hold? | A `Patient`, a `HealthProfessional` and a `TimeSlot`. |
| How does one appointment work with any professional type? | The field is declared as `HealthProfessional`, the common supertype. |
| How is an incomplete appointment prevented? | The constructor rejects a null patient, professional or time with `IllegalArgumentException`. |
| How does it print the professional's details? | `toString()` prints a summary and then the professional's own detail block, indented. |
| How can it know how it orders? | `implements Comparable<Appointment>`; `compareTo` compares time first, then professional ID only to make equal times stable. |
| How should the time be stored? | `TimeSlot` stores hour and minute and compares by minutes since midnight. |

### Part 5 — Managing the collection

| Question | Answer |
| --- | --- |
| Which data structure? | `ArrayList<Appointment>`: the size changes as bookings come and go, the count is unknown in advance, and an ordered list is simple to filter and sort. |
| How does "add" handle any type? | It takes a `HealthProfessional` parameter and constructs the `Appointment` internally. |
| What happens when cancelling something that does not exist? | A `"Warning: no appointment found ..."` message is returned; nothing is changed. |
| What feedback is given? | Every operation returns a message: `Booked : ...` / `Cancelled: ...` on success, `Warning: ...` on failure. |
| How is the fixed daily schedule represented? | `ClinicSchedule` builds the slots from an opening time, a closing time and a slot length. Change the constructor arguments and the rest of the system follows. |
| When are two appointments for the same professional? | `HealthProfessional.equals()` compares IDs, and `Appointment.clashesWith()` combines that with `TimeSlot.equals()`. |
| How are appointments shown in time order? | `Collections.sort()` on a copy, using `Comparable`. Equal times may appear in any order. |

### Part 6 — Working with appointments

`main` demonstrates, in order: adding seven appointments **out of time order**, all the
error cases (double booking, non-working day, time outside clinic hours, time off the
30-minute grid, missing patient), the sorted list, filtering by professional ID, filtering by
mobile number typed with spaces, invalid and empty filters, cancelling by ID and time,
cancelling by phone number, and the collection after the cancellations.

---

## 2. The four pillars, and exactly where they are

### Encapsulation

* `HealthProfessional`: every field is `private final`; `copyAndValidateDays()` checks the
  day list; `getAvailableDays()` returns a copy.
* `Patient`: the mobile number is normalised and its length checked in the constructor.
* `Surgeon.operationsCompleted` is `private` and only changes through
  `performOperation()`.

### Abstraction

* `HealthProfessional` is `abstract` and declares `getProfessionalType()`, `getDuties()` and
  `provideService()` without saying how they work.
* `Doctor` is `abstract` and adds the shared prescribing behaviour.
* `ClinicSchedule` hides how the slots are generated behind `isScheduledSlot()`.

### Inheritance

* `Doctor extends HealthProfessional`; `GeneralPractitioner`, `Specialist` and `Surgeon`
  extend `Doctor`; `Nurse` and `Dietitian` extend `HealthProfessional`.
* Each subclass calls `super(...)` in its constructor and `super.toString()` in `toString()`,
  so no shared code is repeated.

### Polymorphism

* Runtime: the loops in `demonstratePartsTwoAndThree()` call `toString()`, `getDuties()` and
  `provideService()` on `HealthProfessional` references, and the correct override runs.
* Compile-time (overloading): each type has two constructors; `detailLine()` takes `Object`.
* `Comparable`: `Collections.sort()` works on appointments because `compareTo` exists.

---

## 3. Five-minute video script

| Time | What to show |
| --- | --- |
| 0:00–0:25 | Introduce yourself, the project and what the system models. Show the project tree in the IDE. |
| 0:25–1:10 | **Encapsulation.** Open `HealthProfessional`. Point at the `private final` fields, the constructor validation, and `getAvailableDays()` returning a copy. Run the "rejected ID" / "blank name" output from Part 1 of the console. |
| 1:10–2:00 | **Abstraction.** Show `abstract class HealthProfessional` and the three abstract methods. Explain why `new HealthProfessional(...)` will not compile, and why `Doctor` is abstract too. Mention `ClinicSchedule.isScheduledSlot()` hiding the slot grid. |
| 2:00–2:55 | **Inheritance.** Show the hierarchy diagram in the console, then `Doctor.prescribe()` written once and inherited by three types, and `GeneralPractitioner.toString()` calling `super.toString()`. Stress that nothing shared is copied. |
| 2:55–3:50 | **Polymorphism.** Scroll to the loop over `ArrayList<HealthProfessional>` printing details, then the `provideService()` loop where each type answers differently. Then `Collections.sort(unsortedAppointments)` in Part 4 to show `Comparable`. |
| 3:50–4:40 | Run the program and walk through Part 6: the double-booking warning, the non-working-day warning, filtering, and cancelling. |
| 4:40–5:00 | Close: what you would add next (persistence, dates, a UI) and what you learned. |

Before recording: run the program once, have the console output ready, and rehearse so you do
not exceed five minutes.

---

## 4. Still to do before submitting

1. **GitHub (done).** The project is in a private repository of 20 commits:
   <https://github.com/Liu205519/A1-lhuanman>. Still the owner's job: invite the tutor and the
   unit assessor under *Settings → Collaborators*, because a private repository they cannot
   open scores nothing for version control.
2. **WakaTime.** Install the WakaTime plugin, open the project with tracking on, then export
   the report as `A1-lhuanman-WakaTime.png` (or `.jpg` / `.pdf`).
3. **Video.** Record with the script above, upload to your SCU OneDrive, get a shareable link.
4. **GenAI declaration.** The brief requires a declaration in the submission comments. Check
   the current wording on Blackboard, then declare accurately.
5. **Zip.** Compress the project folder so that the archive is named `A1-lhuanman.zip`.

> Be ready to explain any line of this code. The brief treats a submission you cannot explain
> as an academic misconduct matter, so read every class before you submit, and change anything
> you would not be comfortable defending.
