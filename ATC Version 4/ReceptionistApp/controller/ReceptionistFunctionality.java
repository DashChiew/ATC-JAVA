package ReceptionistApp.controller;

import AdminApp.controller.AdminFunctionality;
import common.model.User;
import common.util.FileHandler;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Assuming User, FileHandler, and AdminFunctionality classes are defined in their respective .java files
// and are correctly imported into the project build path.

public class ReceptionistFunctionality {
    private static final String STUDENTS_FILE = "students.txt";
    private static final String ENROLLMENT_FILE = "student_enrollment.txt";
    // CHANGED TO "payment.txt" to align with the functionality desired from previous steps
    // Standardize to use PAYMENT_FILE for all payment operations
    private static final String PAYMENT_FILE = "payment.txt";
    // Added USERS_FILE for login functionality, consistent with User object needs
    private static final String USERS_FILE = "users.txt";

    private static int studentCounter = generateStudentCounter();

    private static int generateStudentCounter() {
        int maxId = readLastStudentId();
        return maxId + 1;
    }

    private static int readLastStudentId() {
        // Ensure files exist before attempting to read
        FileHandler.createFileIfNotExists(STUDENTS_FILE);
        FileHandler.createFileIfNotExists(ENROLLMENT_FILE);
        // Use PAYMENT_FILE consistently
        FileHandler.createFileIfNotExists(PAYMENT_FILE);
        // Ensure users.txt exists for findUserByUsername
        FileHandler.createFileIfNotExists(USERS_FILE);

        List<String> lines = FileHandler.readAllLines(STUDENTS_FILE);
        int maxId = 0;
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 2 && parts[1].trim().startsWith("S")) {
                try {
                    String username = parts[1].trim();
                    int currentId = Integer.parseInt(username.substring(1));
                    maxId = Math.max(maxId, currentId);
                } catch (NumberFormatException e) {
                    System.out.println("Skip invalid username: " + parts[1]);
                }
            }
        }
        return maxId;
    }

    public static String getNextStudentId() {
        return "S" + String.format("%03d", studentCounter++);
    }

    /**
     * Registers a new student. This method uses placeholder values for
     * information not directly provided (IC, Email, Contact, Address, etc.).
     * It saves the student's credentials to students.txt and student_enrollment.txt.
     * Note: This method's signature and behavior remain as per your "old code"
     * and does NOT automatically add to users.txt for general login or create
     * detailed payment records with all fields.
     *
     * @param name The full name of the student.
     * @param password The password for the student's account.
     */
    public static void registerStudent(String name, String password) {
        String username = generateUsername();

        // placeholders
        String defaultIcPassport = "N/A";
        String defaultEmail = "student@example.com";
        String defaultContact = "000-0000000";
        String defaultAddress = "N/A";

        String studentData = String.format(
                "%s,%s,%s,%s,%s,%s,%s,%s",
                name, username, password, defaultIcPassport, defaultEmail, defaultContact,
                defaultAddress, "student" // Role is 'student'
        );
        FileHandler.appendLine(STUDENTS_FILE, studentData);

        String enrollmentData = String.format(
                "%s,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                name, username, defaultIcPassport, defaultEmail, defaultContact, defaultAddress, "Level",
                "Subjects", "Enroll Month"
        );
        FileHandler.appendLine(ENROLLMENT_FILE, enrollmentData);

        System.out.println("Student " + name + " registered. Username: " + username);
    }

    private static String generateUsername() {
        String username = String.format("S%03d", studentCounter);
        studentCounter++;
        return username;
    }

    public static int getStudentCounter() {
        return studentCounter;
    }

    public static void updateStudentEnrollment(
            String username, String name, String ic, String email, String contact,
            String address, String level, String subjects, String month
    ) throws IOException {
        List<String> enrollments = FileHandler.readAllLines(ENROLLMENT_FILE);

        boolean found = false;
        for (int i = 0; i < enrollments.size(); i++) {
            String line = enrollments.get(i);
            // Use regex to split by comma, but not inside double quotes
            String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            if (parts.length > 1 && parts[1].trim().replaceAll("^\"|\"$", "").equals(username)) {
                // Update and preserve quotes where necessary
                // Note: parts[0] is name, parts[1] is username (SXXX)
                if (!name.isEmpty()) parts[0] = name; // Update name at index 0, not parts[1]
                if (!ic.isEmpty()) parts[2] = "\"" + ic + "\"";
                if (!email.isEmpty()) parts[3] = "\"" + email + "\"";
                if (!contact.isEmpty()) parts[4] = "\"" + contact + "\"";
                if (!address.isEmpty()) parts[5] = "\"" + address + "\"";
                if (!level.isEmpty()) parts[6] = "\"" + level + "\"";
                if (!subjects.isEmpty()) parts[7] = "\"" + subjects + "\"";
                if (!month.isEmpty()) parts[8] = "\"" + month + "\"";

                enrollments.set(i, String.join(",", parts));
                found = true;
                break; // Found and updated, exit loop
            }
        }
        if (!found) {
            throw new IOException("Student " + username + " not found in enrollment file.");
        }
        FileHandler.writeAllLines(ENROLLMENT_FILE, enrollments);

        // Also update students.txt for consistency with updated enrollment data
        List<String> studentRecords = FileHandler.readAllLines(STUDENTS_FILE);
        boolean foundStudent = false;

        for (int i = 0; i < studentRecords.size(); i++) {
            String[] parts = studentRecords.get(i).split(",");
            // Format: Name,Username,Password,IC,Email,Contact,Address,Role
            if (parts.length >= 2 && parts[1].trim().equals(username)) { // Match by username (index 1)
                StringBuilder updated = new StringBuilder();
                updated.append(!name.isEmpty() ? name : parts[0].trim()).append(","); // Name
                updated.append(username).append(","); // Username
                updated.append(parts[2].trim()).append(","); // Password (no change from this method)
                updated.append(!ic.isEmpty() ? ic : (parts.length > 3 ? parts[3].trim() : "N/A")).append(","); // IC
                updated.append(!email.isEmpty() ? email : (parts.length > 4 ? parts[4].trim() : "student@example.com")).append(","); // Email
                updated.append(!contact.isEmpty() ? contact : (parts.length > 5 ? parts[5].trim() : "000-0000000")).append(","); // Contact
                updated.append(!address.isEmpty() ? address : (parts.length > 6 ? parts[6].trim() : "N/A")).append(","); // Address
                updated.append("student"); // Role (unchanged)

                studentRecords.set(i, updated.toString());
                foundStudent = true;
                break;
            }
        }

        if (!foundStudent) {
            throw new IOException("Student " + username + " not found in students.txt");
        }
        FileHandler.writeAllLines(STUDENTS_FILE, studentRecords);
    }


    public static boolean deleteStudent(String username) {
        List<String> studentAcc = FileHandler.readAllLines(STUDENTS_FILE);
        boolean removedFromStudents = studentAcc.removeIf(line -> {
            String[] parts = line.split(",");
            return parts.length >= 2 && parts[1].trim().equals(username);
        });
        FileHandler.writeAllLines(STUDENTS_FILE, studentAcc);

        List<String> studentEnrollment = FileHandler.readAllLines(ENROLLMENT_FILE);
        // Remove based on username at the beginning of the line, considering quotes
        boolean removedFromEnrollment = studentEnrollment.removeIf(line -> {
            String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            return parts.length > 1 && parts[1].trim().replaceAll("^\"|\"$", "").equals(username);
        });
        FileHandler.writeAllLines(ENROLLMENT_FILE, studentEnrollment);

        // Optionally, delete payment records for the deleted student
        List<String> payments = FileHandler.readAllLines(PAYMENT_FILE); // Use PAYMENT_FILE
        boolean removedFromPayments = payments.removeIf(line -> {
            String[] parts = line.split(",");
            return parts.length > 0 && parts[0].trim().equals(username);
        });
        if (removedFromPayments) {
            FileHandler.writeAllLines(PAYMENT_FILE, payments); // Use PAYMENT_FILE
        }

        // IMPORTANT: If 'users.txt' is used for all logins, you might also need to remove from there.
        // As per the "old code" no explicit users.txt management in registerStudent/deleteStudent.
        // Assuming findUserByUsername is the only place using users.txt directly for lookup.

        return removedFromStudents || removedFromEnrollment || removedFromPayments;
    }

    public static String[] getStudentUsername() {
        List<String> lines = FileHandler.readAllLines(STUDENTS_FILE);
        return lines.stream()
                // Assuming STUDENTS_FILE stores format: Name,Username,Password,...
                // So, the username is at index 1 after splitting.
                .map(l -> {
                    String[] parts = l.split(",");
                    return parts.length >= 2 ? parts[1].trim() : ""; // Get username from 2nd part
                })
                .filter(username -> !username.isEmpty()) // Filter out empty strings from malformed lines
                .toArray(String[]::new);
    }

    /**
     * Saves a new payment record to the PAYMENT_FILE ("payment.txt").
     * Now saves in the detailed 5-part format.
     */
    public static boolean saveNewPayment(String username, double amount, String status, String dueDate) {
        // We need a payment method and payment date (the date the payment is recorded)
        // For "Record New Payment" from RPaymentManagementGUI, we can use "Manual Entry"
        // and the current date as the payment date.
        String paymentMethod = "Manual Entry";
        String paymentDate = LocalDate.now().toString(); // Use current date for payment date

        // Detailed payment.txt format: username,paymentMethod,paymentDate,amount,status
        String paymentData = String.format("%s,%s,%s,%.2f,%s", username, paymentMethod, paymentDate, amount, status);
        return FileHandler.appendLine(PAYMENT_FILE, paymentData);
    }

    // START OF METHODS PORTED/UPDATED FROM PREVIOUS "NEW CODE" OUTPUT

    /**
     * Finds a User by username by reading from the common "users.txt" file.
     * Assumes "users.txt" contains user data in the format:
     * Name,Username,Password,IC/Passport,Email,ContactNumber,Address,Role
     *
     * @param username The username to search for.
     * @return A User object if found, otherwise null.
     */
    public static User findUserByUsername(String username) {
        List<String> lines = FileHandler.readAllLines(USERS_FILE); // Reads from users.txt
        for (String line : lines) {
            String[] parts = line.split(",");
            // Expected format for users.txt: Name,Username,Password,IC/Passport,Email,ContactNumber,Address,Role
            // So, username is at parts[1], and we need 8 parts for the User constructor
            if (parts.length >= 8 && parts[1].trim().equals(username)) { // Check length for 8 parts and match username at index 1
                return new User(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(),
                        parts[4].trim(), parts[5].trim(), parts[6].trim(), parts[7].trim());
            }
        }
        return null;
    }

    /**
     * Retrieves a list of lines representing unpaid or pending payments from "payment.txt".
     * Expects "payment.txt" to have a detailed format:
     * username,paymentMethod,paymentDate,amount,status
     *
     * @return A list of strings, each representing an unpaid/pending payment record.
     */
    public static List<String> getUnpaidPaymentLines() {
        List<String> unpaidLines = new ArrayList<>();
        List<String> lines = FileHandler.readAllLines(PAYMENT_FILE); // Reads from "payment.txt"
        for (String line : lines) {
            String[] parts = line.split(",");
            // Detailed payment.txt format: username,paymentMethod,paymentDate,amount,status
            if (parts.length >= 5) {
                String status = parts[4].trim(); // Status is at index 4
                if ("UNPAID".equalsIgnoreCase(status) || "PENDING".equalsIgnoreCase(status)) {
                    unpaidLines.add(line); // Add the full detailed line
                }
            }
        }
        return unpaidLines;
    }

    /**
     * Updates the status of a specific payment record in "payment.txt".
     * Expects "payment.txt" to have a detailed format.
     *
     * @param originalLineFullDetailed The full original line of the payment record to update.
     * @param newStatus The new status to set (e.g., "PAID").
     * @return true if the payment status was updated, false otherwise.
     * @throws IOException If an I/O error occurs during file operations.
     */
    public static boolean updatePaymentStatus(String originalLineFullDetailed, String newStatus) throws IOException {
        List<String> lines = FileHandler.readAllLines(PAYMENT_FILE); // Reads from "payment.txt"
        boolean updated = false;

        // originalLineFullDetailed is expected to be a full line from payment.txt (detailed)
        // e.g., "S001,Online Banking,04 July 2025,785.00,Pending"
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).equals(originalLineFullDetailed)) {
                String[] parts = lines.get(i).split(",");
                if (parts.length >= 5) { // Ensure there's a status field at index 4
                    parts[4] = newStatus; // Update the status field (index 4 for detailed payment.txt)
                    lines.set(i, String.join(",", parts));
                    updated = true;
                    break;
                }
            }
        }
        if (updated) {
            FileHandler.writeAllLines(PAYMENT_FILE, lines);
        }
        return updated;
    }

    /**
     * Retrieves all payment records from "payment.txt".
     * Expects "payment.txt" to have a detailed format.
     *
     * @return A list of String arrays, where each array represents a payment record.
     */
    public static List<String[]> getAllPaymentRecords() {
        List<String[]> records = new ArrayList<>();
        List<String> lines = FileHandler.readAllLines(PAYMENT_FILE); // Reads from "payment.txt"
        for (String line : lines) {
            // Detailed payment.txt format: username,paymentMethod,paymentDate,amount,status
            String[] parts = line.split(",");
            if (parts.length >= 5) { // Ensure it's a valid detailed payment line
                records.add(parts);
            }
        }
        return records;
    }

    /**
     * Generates a payment receipt string based on detailed payment information.
     * Expects paymentDetails array to be from a detailed payment record:
     * username,paymentMethod,paymentDate,amount,status
     *
     * @param paymentDetails An array containing detailed payment information.
     * @return A formatted string representing the payment receipt.
     */
    public static String generateReceipt(String[] paymentDetails) {
        // paymentDetails now comes from getAllPaymentRecords, which reads from payment.txt (detailed)
        // So, paymentDetails format is: username,paymentMethod,paymentDate,amount,status
        String username = paymentDetails[0];
        String paymentMethod = paymentDetails[1];
        String paymentDate = paymentDetails[2];
        double amount = Double.parseDouble(paymentDetails[3]);
        String status = paymentDetails[4];

        String receiptContent = String.format(
                "===== PAYMENT RECEIPT =====\n" +
                        "Date: %s\n" +
                        "Student: %s\n" +
                        "Payment Method: %s\n" +
                        "Payment Date: %s\n" +
                        "Amount Paid: RM %.2f\n" +
                        "Status: %s\n" +
                        "===========================\n",
                LocalDate.now().toString(), username, paymentMethod, paymentDate, amount, status);

        try {
            java.nio.file.Files.createDirectories(java.nio.file.Paths.get("receipts"));
            String receiptFileName = "receipts/" + username + "_" + System.currentTimeMillis() + ".txt";
            FileHandler.appendLine(receiptFileName, receiptContent);
            System.out.println("Receipt saved to: " + receiptFileName);
        } catch (IOException ex) {
            System.err.println("Error saving receipt: " + ex.getMessage());
        }
        return receiptContent;
    }
    // END OF METHODS PORTED/UPDATED FROM PREVIOUS "NEW CODE" OUTPUT
}