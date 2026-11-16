# A1-lhuanman — Health Service Appointment System

**PROG2004 Object Oriented Programming — Assessment 1**
Liu Huanman (刘欢漫) — Student ID 25259078

A Java project that models the backbone of a health service appointment system: a set of
well-designed collaborating classes, exercised by a scripted demonstration in `main`.
There is no user interface, no menus, no login and no data storage, which matches the brief.

---

## How to run

The project uses the default package (no `package` declaration), exactly as the brief
describes: a class called `AssignmentOne` containing `main` in the `src` directory.

### From a terminal

```bash
cd A1-lhuanman
javac -d out src/*.java
java -cp out AssignmentOne
```

On Windows:

```bat
javac -d out src\*.java
java -cp out AssignmentOne
```

The program only uses the standard Java library and needs no arguments or files.

`console-output.txt` in the project root is the output of one complete run, kept as
evidence of the demonstration and as a script aid for the video.

### From an IDE

* **IntelliJ IDEA** — `File > Open` the `A1-lhuanman` folder, mark `src` as the sources
  root if it is not already, then run `AssignmentOne`.
* **Eclipse** — `File > New > Java Project`, uncheck "use default location" and choose
  this folder, then run `AssignmentOne`.
* **VS Code** — open the folder with the Java extension pack and run `AssignmentOne`.

---

## Classes

| Class | Role | Brief |
| --- | --- | --- |
| `AssignmentOne` | Entry point and the scripted demonstration of the whole system | Parts 3, 6 |
| `HealthProfessional` | **Abstract** base class: ID (digits only), name, available days, shared behaviour | Part 1 |
| `Weekday` | Enum of the days of the week, so only real days can be stored | Part 1 |
| `Doctor` | **Abstract** layer under the doctor types: medical registration and `prescribe()` | Part 2 |
| `GeneralPractitioner` | Doctor type: consulting room, years of experience | Part 2 |
| `Specialist` | Doctor type: specialty (Cardiology, Paediatrics) and hospital department | Part 2 |
| `Surgeon` | Doctor type: theatre, surgical area, operations completed | Part 2 |
| `Nurse` | Non-doctor type: ward and grade, `takeBloodSample()` | Part 2 |
| `Dietitian` | Non-doctor type: focus area and qualification, `createMealPlan()` | Part 2 |
| `Patient` | Patient name and mobile number, with normalisation and validation | Part 4 |
| `TimeSlot` | Immutable time of day, `Comparable`, so times sort correctly | Part 4 |
| `ClinicSchedule` | The fixed daily schedule (08:00–16:00, 30-minute slots) in one place | Part 5 |
| `Appointment` | Links a patient, a professional and a time; `Comparable`; detects clashes | Part 4 |
| `AppointmentManager` | Owns the `ArrayList` of appointments: add, filter, sort, cancel | Part 5 |

The hierarchy:

```
HealthProfessional (abstract)
    +-- Doctor (abstract)
    |     +-- GeneralPractitioner
    |     +-- Specialist
    |     +-- Surgeon
    +-- Nurse
    +-- Dietitian
```

---

## Design decisions in short

* **`HealthProfessional` is abstract.** A "plain" professional with no specialty does not
  exist in the system, so the compiler should not allow one to be created. Every real type
  must supply `getProfessionalType()`, `getDuties()` and `provideService()`.
* **Shared behaviour sits as high as it can.** Prescribing is a doctor-only behaviour, so
  it lives in `Doctor`, not in `HealthProfessional` and not copied into the three doctor
  types. Nurses and dietitians therefore never inherit behaviour that is wrong for them.
* **Encapsulation with validation.** All fields are `private final` and are set once by the
  constructor, which rejects an invalid ID, name, mobile number or day list. Getters that
  return a collection return a **copy**, so external code cannot change the object's state.
* **Two ways of constructing.** Each type has a full constructor and a convenience
  constructor that supplies the Monday–Friday default week, so callers only give what makes
  that object different.
* **`Patient` is its own class.** A patient is a real entity, is reused by several
  appointments, and its rules (10-digit mobile number, normalised for comparison) are then
  written once.
* **`TimeSlot` instead of a `String`.** Times are stored as numbers of minutes since
  midnight, so `09:30` really is before `10:00`, and `Comparable` lets
  `Collections.sort()` order a whole day.
* **`ClinicSchedule` owns the hours.** 08:00, 16:00 and 30 minutes appear in exactly one
  place, so changing the clinic hours requires no change anywhere else.
* **`equals()` on the professional's ID.** Two objects that describe the same professional
  are the same professional, which is how the manager detects a double booking.
* **Every manager operation returns a message.** Success messages say what happened;
  failures return a `"Warning: ..."` string explaining why the action could not be
  performed. Nothing fails silently.
* **Availability is enforced, not just stored.** `AppointmentManager` is told which day it
  is booking for, and refuses a professional who does not work that day, so the stored days
  are actually used.

### Assumptions

* All appointments are for one clinic day, so the manager is given the day it books for
  (`Weekday.MONDAY` in the demonstration) instead of reading the system clock. A real
  appointment date is outside the scope of the brief, which limits bookings to the current
  day.
* Cancelling "by patient phone number" cancels **every** appointment booked under that
  number and reports how many were cancelled, because one patient may hold more than one.

---

## Submission checklist

| Item | Status |
| --- | --- |
| Java project, all sources | `A1-lhuanman/` |
| Zipped project | `A1-lhuanman.zip` |
| GitHub repository with regular commits | <https://github.com/Liu205519/A1-lhuanman> (private) |
| WakaTime report | **to do** |
| 5-minute video on the four pillars | **to do — script in `DESIGN-NOTES.md`** |
| GenAI declaration in the comments | **to do** |
