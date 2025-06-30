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
        List<String> lines = FileHandler.readAllLines(STUDENTS_FILE);
        if (lines.isEmpty()) return 0;
        String lastLine = lines.getLast();
        String lastUsername = lastLine.split(",")[0].trim(); // Extract "S001" from "S001,password,student"
        return Integer.parseInt(lastUsername.substring(1)); // Convert "001" to 1
    }

    public static void registerStudent(String name, String password) {
        String username = generateUsername();
        User newStudent = new User(username, password, "student");
        // Save username, password, role to students file
        FileHandler.appendLine(STUDENTS_FILE, newStudent.toString());
        // Save username, name and other stuff to enrollment file
        String enrollmentData = String.format(
                "%s,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                username, name, "IC/Passport", "Email", "Contact Number", "Address",
                "Level", "Subjects", "Enroll Month"
        );
        FileHandler.appendLine(ENROLLMENT_FILE, enrollmentData);
        JOptionPane.showMessageDialog(null, "Student " + name + " registered. Username: " + username);
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
        List<String> enrollments = FileHandler.readAllLines("student_enrollment.txt");

        for (int i = 0; i < enrollments.size(); i++) {
            String[] parts = enrollments.get(i).split(",");
            if (parts[0].equals(username)) {
                // Update only non-blank fields
                if (!name.isEmpty()) parts[1] = "\"" + name + "\"";
                if (!ic.isEmpty()) parts[2] = "\"" + ic + "\"";
                if (!email.isEmpty()) parts[3] = "\"" + email + "\"";
                if (!contact.isEmpty()) parts[4] = "\"" + contact + "\"";
                if (!address.isEmpty()) parts[5] = "\"" + address + "\"";
                if (!level.isEmpty()) parts[6] = "\"" + level + "\"";
                if (!subjects.isEmpty()) parts[7] = "\"" + subjects + "\"";
                if (!month.isEmpty()) parts[8] = "\"" + month + "\"";

                enrollments.set(i, String.join(",", parts));
                FileHandler.writeAllLines("student_enrollment.txt", enrollments);
                return;
            }
        }
        throw new IOException("Student not found in enrollment file.");
    }

    public static boolean deleteStudent(String username) {
        List<String> studentAcc = FileHandler.readAllLines(STUDENTS_FILE);
        boolean removed = studentAcc.removeIf(line -> line.startsWith(username + ","));
        FileHandler.writeAllLines(STUDENTS_FILE, studentAcc);

        List<String> studentEnrollment = FileHandler.readAllLines(ENROLLMENT_FILE);
        studentEnrollment.removeIf(line -> line.startsWith("\"" + username + "\""));
        FileHandler.writeAllLines(ENROLLMENT_FILE, studentEnrollment);

        return removed;
    }

    public static String[] getStudentUsername() {
        List<String> lines = FileHandler.readAllLines(STUDENTS_FILE);
        return lines.stream().map(l -> l.split(",")[0]).toArray(String[]::new);
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