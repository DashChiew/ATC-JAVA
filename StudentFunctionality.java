import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class StudentFunctionality {
    private static final String STUDENTS_FILE = "students.txt";

    /**
     * Initializes the students file.
     */
    public static void initializeStudentsFile() {
        if (!FileHandler.createFileIfNotExists(STUDENTS_FILE)) {
            System.err.println("Failed to create students file! Application may not function correctly.");
        }
        // No default students are added.
    }

    /**
     * Reads all students from the students file.
     * @return A list of Student objects.
     */
    public static List<Student> readAllStudents() {
        List<String> lines = FileHandler.readAllLines(STUDENTS_FILE);
        List<Student> students = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 7) { // Minimum parts for basic info
                String name = parts[0].trim();
                String icPassport = parts[1].trim();
                String email = parts[2].trim();
                String contact = parts[3].trim();
                String address = parts[4].trim();
                String level = parts[5].trim();
                String enrollmentMonth = parts[6].trim();

                List<String> subjects = new ArrayList<>();
                if (parts.length > 7) { // Subjects are optional
                    // Subjects are stored as a comma-separated string within the 8th part
                    String subjectsString = parts[7].trim();
                    if (!subjectsString.isEmpty()) {
                        for (String subject : subjectsString.split(";")) { // Use semicolon to separate subjects
                            subjects.add(subject.trim());
                        }
                    }
                }
                students.add(new Student(name, icPassport, email, contact, address, level, subjects, enrollmentMonth));
            }
        }
        return students;
    }

    /**
     * Writes a list of Student objects to the students file, overwriting existing content.
     * @param students The list of Student objects to write.
     */
    public static void writeAllStudents(List<Student> students) {
        List<String> lines = students.stream()
                .map(student -> {
                    String subjectsString = String.join(";", student.getSubjects()); // Join subjects with semicolon
                    return student.getName() + "," +
                            student.getIcPassport() + "," +
                            student.getEmail() + "," +
                            student.getContactNumber() + "," +
                            student.getAddress() + "," +
                            student.getLevel() + "," +
                            student.getEnrollmentMonth() + "," +
                            subjectsString;
                })
                .collect(Collectors.toList());
        FileHandler.writeAllLines(STUDENTS_FILE, lines);
    }

    /**
     * Registers or updates a student's information. If a student with the same IC/Passport exists, it updates.
     * Otherwise, it adds a new student. This method can also be used for initial enrollment or updating enrollment details.
     * @param student The Student object to save (register or update).
     */
    public static void saveStudent(Student student) {
        List<Student> students = readAllStudents();
        boolean studentExists = false;
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getIcPassport().equals(student.getIcPassport())) {
                students.set(i, student); // Update existing student
                studentExists = true;
                break;
            }
        }
        if (!studentExists) {
            students.add(student); // Add new student
        }
        writeAllStudents(students);
    }

    /**
     * Retrieves a student by their IC/Passport number.
     * @param icPassport The IC/Passport number of the student.
     * @return The Student object if found, null otherwise.
     */
    public static Student getStudentByIcPassport(String icPassport) {
        return readAllStudents().stream()
                .filter(s -> s.getIcPassport().equals(icPassport))
                .findFirst()
                .orElse(null);
    }

    /**
     * Deletes a student by their IC/Passport number.
     * @param icPassport The IC/Passport number of the student to delete.
     * @return true if the student was deleted, false otherwise.
     */
    public static boolean deleteStudent(String icPassport) {
        List<Student> students = readAllStudents();
        boolean removed = students.removeIf(s -> s.getIcPassport().equals(icPassport));
        if (removed) {
            writeAllStudents(students);
        }
        return removed;
    }

    /**
     * Registers a new student with basic information.
     * This is a convenience method that calls saveStudent.
     * @param name Student's full name.
     * @param icPassport Student's IC/Passport number (unique identifier).
     * @param email Student's email.
     * @param contactNumber Student's contact number.
     * @param address Student's address.
     * @return The registered Student object.
     */
    public static Student registerStudent(String name, String icPassport, String email, String contactNumber, String address) {
        // Initial registration, no level or subjects assigned yet.
        Student newStudent = new Student(name, icPassport, email, contactNumber, address, "", new ArrayList<>(), "");
        saveStudent(newStudent);
        return newStudent;
    }

    /**
     * Enrolls an existing student in subjects for a specific level and records the enrollment month.
     * This method updates an existing student's record.
     * @param icPassport The IC/Passport of the student to enroll.
     * @param level The level the student is enrolling in.
     * @param subjects A list of subjects the student is enrolling in (up to 3).
     * @return true if enrollment was successful, false if student not found or subjects exceed limit.
     */
    public static boolean enrollStudentInSubjects(String icPassport, String level, List<String> subjects) {
        if (subjects == null || subjects.size() > 3) {
            System.err.println("Enrollment failed: Subjects list is null or exceeds 3.");
            return false;
        }

        Student student = getStudentByIcPassport(icPassport);
        if (student != null) {
            student.setLevel(level);
            student.setSubjects(subjects);
            student.setEnrollmentMonth(Student.getCurrentMonthYear());
            saveStudent(student); // Update the student record
            return true;
        }
        return false; // Student not found
    }
}