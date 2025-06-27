import javax.swing.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        String lastUsername = parts[0].trim(); // Extract "S001" from "S001,password,student"
        if (lastUsername.length() < 2 || !lastUsername.startsWith("S")) return 0; // Basic format check
        try {
            return Integer.parseInt(lastUsername.substring(1)); // Convert "001" to 1
        } catch (NumberFormatException e) {
            System.err.println("Error parsing last student ID: " + e.getMessage());
            return 0; // Fallback to 0 if parsing fails
        }
    }

//    public static void registerStudent(String name, String password) {
//        String username = generateUsername();
//        User newStudent = new User(username, password, "student");
//        // Save username, password, role to students file
//        FileHandler.appendLine(STUDENTS_FILE, newStudent.toString());
//        // Save username, name and other stuff to enrollment file
//        String enrollmentData = String.format(
//                "%s,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
//                username, name, "IC/Passport", "Email", "Contact Number", "Address",
//                "Level", "Subjects", "Enroll Month"
//        );
//        FileHandler.appendLine(ENROLLMENT_FILE, enrollmentData);
//        // Do not use JOptionPane here, let GUI classes handle it.
//        // JOptionPane.showMessageDialog(null, "Student " + name + " registered. Username: " + username);
//    }

    public static void registerStudent(String name, String password) {
        String username = generateUsername();
        User newStudent = new User(username, password, "student");

        // Save username, password, role to students.txt (if it's meant to hold all users or just students)
        // Based on the login logic, it seems 'users.txt' is the primary source for authentication.
        FileHandler.appendLine(STUDENTS_FILE, newStudent.toString());

        // *** IMPORTANT ADDITION: Save student credentials to users.txt for login functionality ***
        FileHandler.appendLine(AdminFunctionality.USERS_FILE, newStudent.toString());

        // Save username, name and other stuff to enrollment file
        String enrollmentData = String.format(
                "%s,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                username, name, "IC/Passport", "Email", "Contact Number", "Address",
                "Level", "Subjects", "Enroll Month"
        );
        FileHandler.appendLine(ENROLLMENT_FILE, enrollmentData);

        // In a real application, you'd log this or send it back to the GUI
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

        // Optionally, delete payment records for the deleted student
        List<String> payments = FileHandler.readAllLines(PAYMENTS_FILE);
        boolean removedFromPayments = payments.removeIf(line -> line.startsWith(username + ","));
        if (removedFromPayments) {
            FileHandler.writeAllLines(PAYMENTS_FILE, payments);
        }

        return removedFromStudents || removedFromEnrollment || removedFromPayments;
    }

    public static String[] getStudentUsername() {
        List<String> lines = FileHandler.readAllLines(STUDENTS_FILE);
        return lines.stream()
                .map(l -> l.split(",")[0].trim()) // Ensure no leading/trailing spaces
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