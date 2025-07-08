package common.model;

import java.util.Objects;

public class TutorAssignment {
    private String tutorUsername;
    private String level;
    private String subject;

    public TutorAssignment(String tutorUsername, String level, String subject) {
        this.tutorUsername = tutorUsername;
        this.level = level;
        this.subject = subject;
    }

    // Getters
    public String getTutorUsername() { return tutorUsername; }
    public String getLevel() { return level; }
    public String getSubject() { return subject; }

    /**
     * Returns a human-readable string representation of the TutorAssignment.
     * This is suitable for display purposes.
     * @return A formatted string with tutor, level, and subject.
     */
    @Override
    public String toString() {
        return "Tutor: " + tutorUsername + ", Level: " + level + ", Subject: " + subject;
    }

    /**
     * Returns a comma-separated string representation of the TutorAssignment
     * suitable for saving to a file.
     * Format: tutorUsername,level,subject
     * @return A comma-separated string for file storage.
     */
    public String toFileString() {
        return String.join(",", tutorUsername, level, subject);
    }

    /**
     * Parses a comma-separated string from a file and creates a TutorAssignment object.
     * @param fileString The string read from the file (e.g., "tutor1,Primary,Math").
     * @return A new TutorAssignment object, or null if the string is malformed.
     */
    public static TutorAssignment parseFromFileString(String fileString) {
        String[] parts = fileString.split(",");
        if (parts.length == 3) {
            return new TutorAssignment(parts[0].trim(), parts[1].trim(), parts[2].trim());
        } else {
            System.err.println("Skipping malformed assignment line (expected 3 parts, found " + parts.length + "): " + fileString);
            return null; // Or throw an IllegalArgumentException
        }
    }

    // Equality check based on all fields
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TutorAssignment that = (TutorAssignment) o;
        return tutorUsername.equals(that.tutorUsername) &&
                level.equals(that.level) &&
                subject.equals(that.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tutorUsername, level, subject);
    }
}