import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

class Student {
    private String name;
    private String icPassport;
    private String email;
    private String contactNumber;
    private String address;
    private String level;
    private List<String> subjects;
    private String enrollmentMonth; // Stored as "Month Year" string

    public Student(String name, String icPassport, String email, String contactNumber, String address, String level, List<String> subjects, String enrollmentMonth) {
        this.name = name;
        this.icPassport = icPassport;
        this.email = email;
        this.contactNumber = contactNumber;
        this.address = address;
        this.level = level;
        this.subjects = subjects != null ? new ArrayList<>(subjects) : new ArrayList<>();
        this.enrollmentMonth = enrollmentMonth;
    }

    // Getters
    public String getName() { return name; }
    public String getIcPassport() { return icPassport; }
    public String getEmail() { return email; }
    public String getContactNumber() { return contactNumber; }
    public String getAddress() { return address; }
    public String getLevel() { return level; }
    public List<String> getSubjects() { return new ArrayList<>(subjects); } // Return a copy to prevent external modification
    public String getEnrollmentMonth() { return enrollmentMonth; }

    // Setters (if needed for updating individual fields, though saveStudent handles full object updates)
    public void setName(String name) { this.name = name; }
    public void setIcPassport(String icPassport) { this.icPassport = icPassport; }
    public void setEmail(String email) { this.email = email; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public void setAddress(String address) { this.address = address; }
    public void setLevel(String level) { this.level = level; }
    public void setSubjects(List<String> subjects) { this.subjects = new ArrayList<>(subjects); }
    public void setEnrollmentMonth(String enrollmentMonth) { this.enrollmentMonth = enrollmentMonth; }


    @Override
    public String toString() {
        return "Name: " + name + ", IC/Passport: " + icPassport + ", Level: " + level +
                ", Subjects: " + String.join(", ", subjects) + ", Enrolled: " + enrollmentMonth;
    }

    // Helper to get current month as a string
    public static String getCurrentMonthYear() {
        return YearMonth.now().format(DateTimeFormatter.ofPattern("MMMM yyyy"));
    }
}