package AdminApp.controller;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import common.util.FileHandler;

// Assuming User, FileHandler, and AdminFunctionality classes are defined in their respective .java files
// and are correctly imported into the project build path.

public class AdminPaymentReportFunction {
    private static final String STUDENTS_FILE = "students.txt";
    private static final String ENROLLMENT_FILE = "student_enrollment.txt";
    private static final String PAYMENTS_FILE = "payments.txt";

    // Note: Student registration and related functionalities (like studentCounter, readLastStudentId,
    // registerStudent, generateUsername, updateStudentEnrollment, deleteStudent, saveNewPayment,
    // getUnpaidPaymentLines, updatePaymentStatus, generateReceipt) have been removed as they are
    // not directly related to generating payment reports. This class now focuses solely on
    // retrieving data for reporting purposes.

    /**
     * Retrieves an array of all student usernames from the students file.
     * This method assumes the STUDENTS_FILE stores student data where the username
     * is the second comma-separated part of each line (e.g., "Name,Username,Password,...").
     *
     * @return A String array containing all student usernames.
     */
    public static String[] getStudentUsername() {
        // Ensure the file exists before attempting to read
        FileHandler.createFileIfNotExists(STUDENTS_FILE);
        List<String> lines = FileHandler.readAllLines(STUDENTS_FILE);
        return lines.stream()
                // Split each line by comma and get the second part (username)
                .map(l -> {
                    String[] parts = l.split(",");
                    return parts.length >= 2 ? parts[1].trim() : "";
                })
                .filter(username -> !username.isEmpty()) // Filter out any empty strings
                .toArray(String[]::new);
    }

    /**
     * Retrieves the form level and subjects for a given student username from the enrollment file.
     * This method parses the student_enrollment.txt file.
     * The expected format for student_enrollment.txt lines is:
     * Name,Username,IC/Passport,Email,Contact Number,Address,Level,Subjects,Enroll Month
     *
     * @param username The username of the student.
     * @return A String array where index 0 is the form level and index 1 is the subjects.
     * Returns {"N/A", "N/A"} if the student's enrollment details are not found or parts are missing.
     */
    public static String[] getStudentEnrollmentDetails(String username) {
        // Ensure the file exists before attempting to read
        FileHandler.createFileIfNotExists(ENROLLMENT_FILE);
        List<String> enrollments = FileHandler.readAllLines(ENROLLMENT_FILE);
        for (String line : enrollments) {
            // Use regex to split by comma, but not inside double quotes, to correctly parse fields
            String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            if (parts.length > 1) { // Ensure at least enough parts for username
                // The username is expected to be the second part (index 1) and might be quoted
                String enrollmentUsername = parts[1].trim().replaceAll("^\"|\"$", "");
                if (enrollmentUsername.equals(username)) {
                    // Level is at index 6, Subjects at index 7 (0-indexed)
                    String level = parts.length > 6 ? parts[6].trim().replaceAll("^\"|\"$", "") : "N/A";
                    String subjects = parts.length > 7 ? parts[7].trim().replaceAll("^\"|\"$", "") : "N/A";
                    return new String[]{level, subjects};
                }
            }
        }
        return new String[]{"N/A", "N/A"}; // Return N/A if student enrollment not found
    }

    /**
     * Retrieves all payment records from the payments file.
     * Each record is expected to be in the format: username,amount,status,dueDate.
     *
     * @return A List of String arrays, where each inner array represents a payment record.
     * Returns an empty list if no records are found or if the file is empty.
     */
    public static List<String[]> getAllPaymentRecords() {
        // Ensure the file exists before attempting to read
        FileHandler.createFileIfNotExists(PAYMENTS_FILE);
        List<String> lines = FileHandler.readAllLines(PAYMENTS_FILE);
        List<String[]> records = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 4) { // Ensure all expected parts are present
                records.add(new String[]{parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim()});
            } else {
                System.err.println("Malformed payment record skipped: " + line);
            }
        }
        return records;
    }
}