import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AssignmentFunctionality {
    private static final String ASSIGNMENTS_FILE = "assignments.txt";

    /**
     * Reads all tutor assignments from the assignments file.
     * @return A list of TutorAssignment objects.
     */
    public static List<TutorAssignment> readAllAssignments() {
        List<String> lines = FileHandler.readAllLines(ASSIGNMENTS_FILE);
        List<TutorAssignment> assignments = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 3) {
                assignments.add(new TutorAssignment(parts[0].trim(), parts[1].trim(), parts[2].trim()));
            }
        }
        return assignments;
    }

    /**
     * Writes a list of TutorAssignment objects to the assignments file, overwriting existing content.
     * @param assignments The list of TutorAssignment objects to write.
     */
    public static void writeAllAssignments(List<TutorAssignment> assignments) {
        List<String> lines = assignments.stream()
                .map(assignment -> assignment.getTutorUsername() + "," + assignment.getLevel() + "," + assignment.getSubject())
                .collect(Collectors.toList());
        FileHandler.writeAllLines(ASSIGNMENTS_FILE, lines);
    }

    /**
     * Saves a tutor assignment to the file. If an identical assignment (tutor-level-subject) already exists, it prevents duplicates.
     * @param newAssignment The TutorAssignment object to save.
     */
    public static void saveAssignment(TutorAssignment newAssignment) {
        List<TutorAssignment> assignments = readAllAssignments();
        // Prevent duplicate assignments
        if (!assignments.contains(newAssignment)) {
            assignments.add(newAssignment);
            writeAllAssignments(assignments);
        } else {
            System.out.println("Assignment already exists: " + newAssignment);
        }
    }

    /**
     * Deletes all assignments associated with a specific tutor.
     * This method is called when a tutor is deleted.
     * @param tutorUsername The username of the tutor whose assignments are to be deleted.
     */
    public static void deleteAssignmentsByTutor(String tutorUsername) {
        List<TutorAssignment> assignments = readAllAssignments();
        boolean changed = assignments.removeIf(assignment -> assignment.getTutorUsername().equalsIgnoreCase(tutorUsername));
        if (changed) {
            writeAllAssignments(assignments);
        }
    }

    /**
     * Retrieves assignments for a specific tutor.
     * @param tutorUsername The username of the tutor.
     * @return A list of TutorAssignment objects for the given tutor.
     */
    public static List<TutorAssignment> getAssignmentsByTutor(String tutorUsername) {
        return readAllAssignments().stream()
                .filter(assignment -> assignment.getTutorUsername().equalsIgnoreCase(tutorUsername))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves assignments for a specific level and subject.
     * @param level The level.
     * @param subject The subject.
     * @return A list of TutorAssignment objects matching the specified level and subject.
     */
    public static List<TutorAssignment> getAssignmentsByLevelSubject(String level, String subject) {
        return readAllAssignments().stream()
                .filter(assignment -> assignment.getLevel().equalsIgnoreCase(level) &&
                        assignment.getSubject().equalsIgnoreCase(subject))
                .collect(Collectors.toList());
    }

    /**
     * Initializes the assignments file.
     */
    public static void initializeAssignmentsFile() {
        if (!FileHandler.createFileIfNotExists(ASSIGNMENTS_FILE)) {
            System.err.println("Failed to create assignments file! Application may not function correctly.");
        }
        // No default assignments are added, as they are dynamic.
    }
}