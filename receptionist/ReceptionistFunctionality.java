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
    private static final String PAYMENTS_FILE = "payments.txt";

    private static int studentCounter = readLastStudentId() + 1;

    private static int readLastStudentId() {
        // Ensure files exist before attempting to read
        FileHandler.createFileIfNotExists(STUDENTS_FILE);
        FileHandler.createFileIfNotExists(ENROLLMENT_FILE);
        FileHandler.createFileIfNotExists(PAYMENTS_FILE);

        List<String> lines = FileHandler.readAllLines(STUDENTS_FILE);
        if (lines.isEmpty()) return 0;
        String lastLine = lines.getLast();
        // Handle potential empty last line or malformed lines gracefully
        if (lastLine.isEmpty()) return 0;
        String[] parts = lastLine.split(",");
        if (parts.length == 0) return 0;
        // The first part is the username, e.g., "S001"
        // Ensure that it's the username that determines the ID, not necessarily just parts[0] if the User toString format changes
        // Based on User toString: Name,Username,Password,IC/Passport,Email,ContactNumber,Address,Role
        // So, username is parts[1] if reading full User line.
        // However, STUDENTS_FILE format seems to be simpler ("username,password,role").
        // Let's assume STUDENTS_FILE only stores username, password, role for student users.
        // If STUDENTS_FILE now stores the full 8-part User string, then parts[1] would be username.
        // Given `l.split(",")[0].trim()` in `getStudentUsername()`, it's likely parts[0] is still the username here.
        String lastUsername = parts[0].trim();
        if (lastUsername.length() < 2 || !lastUsername.startsWith("S")) return 0; // Basic format check
        try {
            return Integer.parseInt(lastUsername.substring(1)); // Convert "001" to 1
        } catch (NumberFormatException e) {
            System.err.println("Error parsing last student ID from '" + lastUsername + "': " + e.getMessage());
            return 0; // Fallback to 0 if parsing fails
        }
    }

    /**
     * Registers a new student. This method now creates a User object with all 8 required fields,
     * using placeholder values for information not directly provided (IC, Email, Contact, Address).
     * It saves the student's credentials to both students.txt and users.txt for authentication.
     * Enrollment data is saved to student_enrollment.txt.
     *
     * @param name The full name of the student.
     * @param password The password for the student's account.
     */
    public static void registerStudent(String name, String password) {
        String username = generateUsername();

        // Define placeholder values for new User fields not directly provided
        String defaultIcPassport = "N/A";
        String defaultEmail = "student@example.com";
        String defaultContact = "000-0000000";
        String defaultAddress = "N/A";

        // Create User object with all 8 arguments
        User newStudent = new User(name, username, password, defaultIcPassport, defaultEmail, defaultContact, defaultAddress, "student");

        // Save student credentials to STUDENTS_FILE (assuming this file is for student-specific basic login info)
        // Ensure this file also stores the 8-part user string now
        FileHandler.appendLine(STUDENTS_FILE, newStudent.toString());

        // IMPORTANT ADDITION: Save student credentials to users.txt for centralized login functionality
        // This ensures the student account is available for login alongside admin, tutor, receptionist.
        AdminFunctionality.saveUser(newStudent); // Using AdminFunctionality.saveUser for consistency

        // Save username, name and other stuff to enrollment file
        // The enrollmentData format matches the previous implementation, so no change here,
        // it's for student-specific details beyond basic user credentials.
        String enrollmentData = String.format(
                "%s,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                name, username, "IC/Passport", "Email", "Contact Number", "Address",
                "Level", "Subjects", "Enroll Month" // These should be updated later via updateStudentEnrollment
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
            if (parts.length > 0 && parts[0].trim().replaceAll("^\"|\"$", "").equals(username)) {
                // Update only non-blank fields, preserving quotes where necessary
                if (!name.isEmpty()) parts[1] = "\"" + name + "\"";
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
    }


    public static boolean deleteStudent(String username) {
        List<String> studentAcc = FileHandler.readAllLines(STUDENTS_FILE);
        boolean removedFromStudents = studentAcc.removeIf(line -> line.startsWith(username + ","));
        FileHandler.writeAllLines(STUDENTS_FILE, studentAcc);

        List<String> studentEnrollment = FileHandler.readAllLines(ENROLLMENT_FILE);
        // Remove based on username at the beginning of the line, considering quotes
        boolean removedFromEnrollment = studentEnrollment.removeIf(line -> {
            String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            return parts.length > 0 && parts[0].trim().replaceAll("^\"|\"$", "").equals(username);
        });
        FileHandler.writeAllLines(ENROLLMENT_FILE, studentEnrollment);

        // Also delete from the main users.txt file
        List<User> allUsers = AdminFunctionality.readAllUsers();
        boolean removedFromUsersFile = allUsers.removeIf(user -> user.getUsername().equals(username) && user.getRole().equals("student"));
        if (removedFromUsersFile) {
            AdminFunctionality.writeAllUsers(allUsers);
        }

        // Optionally, delete payment records for the deleted student
        List<String> payments = FileHandler.readAllLines(PAYMENTS_FILE);
        boolean removedFromPayments = payments.removeIf(line -> line.startsWith(username + ","));
        if (removedFromPayments) {
            FileHandler.writeAllLines(PAYMENTS_FILE, payments);
        }

        return removedFromStudents || removedFromEnrollment || removedFromPayments || removedFromUsersFile;
    }

    public static String[] getStudentUsername() {
        List<String> lines = FileHandler.readAllLines(STUDENTS_FILE);
        return lines.stream()
                // Assuming STUDENTS_FILE now stores the full 8-part User string: Name,Username,Password,...
                // So, the username is at index 1 after splitting.
                .map(l -> {
                    String[] parts = l.split(",");
                    return parts.length >= 2 ? parts[1].trim() : ""; // Get username from 2nd part
                })
                .filter(username -> !username.isEmpty()) // Filter out empty strings from malformed lines
                .toArray(String[]::new);
    }

    public static boolean saveNewPayment(String username, double amount, String status, String dueDate) {
        String paymentData = String.format("%s,%.2f,%s,%s", username, amount, status, dueDate);
        return FileHandler.appendLine(PAYMENTS_FILE, paymentData);
    }

    public static List<String[]> getAllPaymentRecords() {
        List<String> lines = FileHandler.readAllLines(PAYMENTS_FILE);
        List<String[]> records = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 4) {
                records.add(new String[]{parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim()});
            } else {
                System.err.println("Malformed payment record skipped: " + line);
            }
        }
        return records;
    }

    public static List<String> getUnpaidPaymentLines() {
        List<String> allUnpaidPayments = FileHandler.readAllLines(PAYMENTS_FILE);
        return allUnpaidPayments.stream()
                .filter(line -> {
                    String[] parts = line.split(",");
                    return parts.length >= 3 && parts[2].trim().equalsIgnoreCase("UNPAID");
                }).collect(Collectors.toList());
    }

    public static boolean updatePaymentStatus(String originalPaymentLine, String newStatus) throws IOException {
        List<String> lines = FileHandler.readAllLines(PAYMENTS_FILE);
        boolean updated = false;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).equals(originalPaymentLine)) {
                String[] parts = lines.get(i).split(",");
                if (parts.length >= 3) {
                    parts[2] = newStatus; // Update the status part
                    lines.set(i, String.join(",", parts));
                    updated = true;
                    break;
                }
            }
        }
        if (updated) {
            FileHandler.writeAllLines(PAYMENTS_FILE, lines);
        }
        return updated;
    }

    public static String generateReceipt(String[] paymentDetails) {
        String username = paymentDetails[0];
        double amount = Double.parseDouble(paymentDetails[1]);
        String status = paymentDetails[2];
        String dueDate = paymentDetails[3];

        String receiptContent = String.format(
                "===== PAYMENT RECEIPT =====\n" +
                        "Date: %s\n" +
                        "Student: %s\n" +
                        "Amount Paid: RM %.2f\n" +
                        "Status: %s\n" +
                        "Original Due Date: %s\n" +
                        "===========================\n",
                LocalDate.now().toString(), username, amount, status, dueDate);

        // Optional: Save receipt to a file
        try {
            // Ensure 'receipts' directory exists
            java.nio.file.Files.createDirectories(java.nio.file.Paths.get("receipts"));
            String receiptFileName = "receipts/" + username + "_" + System.currentTimeMillis() + ".txt";
            FileHandler.appendLine(receiptFileName, receiptContent);
            System.out.println("Receipt saved to: " + receiptFileName);
        } catch (IOException ex) {
            System.err.println("Error saving receipt: " + ex.getMessage());
        }
        return receiptContent;
    }
}