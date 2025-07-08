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
    private static final String PAYMENTS_FILE = "payments.txt";

    private static int studentCounter = generateStudentCounter();

    private static int generateStudentCounter() {
        int maxId = readLastStudentId();
        return maxId + 1;
    }

    private static int readLastStudentId() {
        // Ensure files exist before attempting to read
        FileHandler.createFileIfNotExists(STUDENTS_FILE);
        FileHandler.createFileIfNotExists(ENROLLMENT_FILE);
        FileHandler.createFileIfNotExists(PAYMENTS_FILE);

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

        // placeholders
        String defaultIcPassport = "N/A";
        String defaultEmail = "student@example.com";
        String defaultContact = "000-0000000";
        String defaultAddress = "N/A";

        String studentData = String.format(
                "%s,%s,%s,%s,%s,%s,%s,%s",
                name, username, password, "N/A", "student@example.com", "000-0000000",
                "N/A", "Enroll Month"
        );
        FileHandler.appendLine(STUDENTS_FILE, studentData);

        String enrollmentData = String.format(
                "%s,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                name, username, "N/A", "student@example.com", "000-0000000", "N/A", "Level",
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

        List<String> studentRecords = FileHandler.readAllLines(STUDENTS_FILE);
        boolean foundStudent = false;

        for (int i = 0; i < studentRecords.size(); i++) {
            String[] parts = studentRecords.get(i).split(",");
            if (parts.length >= 2 && parts[1].trim().replaceAll("^\"|\"$", "").equals(username)) {
                // Format: Name,Username,Password,IC,Email,Contact,Address,Role
                StringBuilder updated = new StringBuilder();
                updated.append(!name.isEmpty() ? name : parts[0].trim()).append(",");
                updated.append(username).append(",");
                updated.append(parts[2].trim()).append(","); // Pass no change
                updated.append(!ic.isEmpty() ? ic : (parts.length > 3 ? parts[3].trim() : "N/A")).append(",");
                updated.append(!email.isEmpty() ? email : (parts.length > 4 ? parts[4].trim() : "student@example.com")).append(",");
                updated.append(!contact.isEmpty() ? contact : (parts.length > 5 ? parts[5].trim() : "000-0000000")).append(",");
                updated.append(!address.isEmpty() ? address : (parts.length > 6 ? parts[6].trim() : "N/A")).append(",");
                updated.append("student");

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
        List<String> payments = FileHandler.readAllLines(PAYMENTS_FILE);
        boolean removedFromPayments = payments.removeIf(line -> {
            String[] parts = line.split(",");
            return parts.length > 0 && parts[0].trim().equals(username);
        });
        if (removedFromPayments) {
            FileHandler.writeAllLines(PAYMENTS_FILE, payments);
        }


        return removedFromStudents || removedFromEnrollment || removedFromPayments;
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