public class Student {
    private String studentId;
    private String name;
    private String icPassport;
    private String email;
    private String contactNumber;
    private String address;
    private String formLevel;
    private String subjects; // This is the field we care about for this task
    private String enrollmentMonth;
    private String password; // Added for Setting class functionality
    private String role;     // Added for Setting class functionality

    // Constructor based on student_enrollment.txt
    public Student(String studentId, String name, String icPassport, String email, String contactNumber, String address, String formLevel, String subjects, String enrollmentMonth) {
        this.studentId = studentId;
        this.name = name;
        this.icPassport = icPassport;
        this.email = email;
        this.contactNumber = contactNumber;
        this.address = address;
        this.formLevel = formLevel;
        this.subjects = subjects;
        this.enrollmentMonth = enrollmentMonth;
    }

    // Default constructor (if needed, though the above is primary)
    public Student() {}

    // Getters
    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public String getIcPassport() { return icPassport; }
    public String getEmail() { return email; }
    public String getContactNumber() { return contactNumber; }
    public String getAddress() { return address; }
    public String getFormLevel() { return formLevel; }
    public String getSubjects() { return subjects; } // This is key!
    public String getEnrollmentMonth() { return enrollmentMonth; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    // Setters (especially for fields editable in Setting)
    public void setIcPassport(String icPassport) { this.icPassport = icPassport; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public void setAddress(String address) { this.address = address; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }
    public void setSubjects(String subjects) { this.subjects = subjects; } // Also useful if subjects were updated elsewhere
}
