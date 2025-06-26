// Student.java
public class Student {
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
    private String role; // Ensure this field exists

    // Constructor for student_enrollment.txt data
    public Student(String studentId, String name, String icPassport, String email,
                   String contactNumber, String address, String formLevel,
                   String subjects, String enrollmentMonth) {
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

    // Constructor to also include data from students.txt (password and role)
    public Student(String studentId, String name, String icPassport, String email,
                   String contactNumber, String address, String formLevel,
                   String subjects, String enrollmentMonth, String password, String role) {
        this(studentId, name, icPassport, email, contactNumber, address, formLevel, subjects, enrollmentMonth);
        this.password = password;
        this.role = role;
    }

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
    public String getRole() { return role; } // Make sure this getter exists

    // Setters for editable fields
    public void setName(String name) { this.name = name; }
    public void setIcPassport(String icPassport) { this.icPassport = icPassport; }
    public void setEmail(String email) { this.email = email; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public void setAddress(String address) { this.address = address; }
    public void setPassword(String password) { this.password = password; } // For demonstration, in real app handle securely
    public void setRole(String role) { this.role = role; } // <--- This is the method you need to add/verify

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", icPassport='" + icPassport + '\'' +
                ", email='" + email + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", address='" + address + '\'' +
                ", formLevel='" + formLevel + '\'' +
                ", subjects='" + subjects + '\'' +
                ", enrollmentMonth='" + enrollmentMonth + '\'' +
                ", password='" + (password != null ? "[HIDDEN]" : "N/A") + '\'' +
                ", role='" + (role != null ? role : "N/A") + '\'' +
                '}';
    }
}