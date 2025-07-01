package StudentApp.model;

public class SStudent {
    private String studentId;
    private String name;
    private String icPassport;
    private String email;
    private String contactNumber;
    private String address;
    private String formLevel;
    private String subjects;
    private String enrollmentMonth;
    private String password;
    private String role;

    // Constructor based on student_enrollment.txt
    public SStudent(String studentId, String name, String icPassport, String email, String contactNumber, String address, String formLevel, String subjects, String enrollmentMonth) {
        this.studentId = studentId;
        this.name = name;
        this.icPassport = icPassport;
        this.email = email;
        this.contactNumber = contactNumber;
        this.address = address;
        this.formLevel = formLevel;
        this.subjects = subjects;
        this.enrollmentMonth = enrollmentMonth;
        this.password = ""; // Initialize
        this.role = "";     // Initialize
    }

    public SStudent() {} // Default constructor

    // Getters
    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public String getIcPassport() { return icPassport; }
    public String getEmail() { return email; }
    public String getContactNumber() { return contactNumber; }
    public String getAddress() { return address; }
    public String getFormLevel() { return formLevel; }
    public String getSubjects() { return subjects; }
    public String getEnrollmentMonth() { return enrollmentMonth; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    // Setters (Ensure these exist for all fields, even if not directly used in 'Setting' class for modification)
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public void setName(String name) { this.name = name; }
    public void setIcPassport(String icPassport) { this.icPassport = icPassport; }
    public void setEmail(String email) { this.email = email; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public void setAddress(String address) { this.address = address; }
    public void setFormLevel(String formLevel) { this.formLevel = formLevel; } // <--- ADD/CONFIRM THIS ONE
    public void setSubjects(String subjects) { this.subjects = subjects; } // <--- CONFIRM THIS ONE
    public void setEnrollmentMonth(String enrollmentMonth) { this.enrollmentMonth = enrollmentMonth; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }
}