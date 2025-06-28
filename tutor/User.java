package com.tutorapp.data;
import java.util.Objects;

public class User {
    private String name;
    private String username;
    private String password;
    private String icPassport;
    private String email;
    private String contactNumber;
    private String address;
    private String role;

    // Full constructor for all user details
    public User(String name, String username, String password, String icPassport, String email, String contactNumber, String address, String role) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.icPassport = icPassport;
        this.email = email;
        this.contactNumber = contactNumber;
        this.address = address;
        this.role = role;
    }

    // NEW Simplified constructor for login (username, password, role only)
    // Other fields are initialized to default/empty values
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = ""; // Default empty
        this.icPassport = ""; // Default empty
        this.email = ""; // Default empty
        this.contactNumber = ""; // Default empty
        this.address = ""; // Default empty
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getIcPassport() {
        return icPassport;
    }

    public String getEmail() {
        return email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getRole() {
        return role;
    }

    // Setters
    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcPassport(String icPassport) {
        this.icPassport = icPassport;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Overridden methods for equality, hash code, and string representation
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        // Equality is based solely on username, assuming it's unique
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        // Hash code is based on username for consistency with equals
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        // Format: Name,Username,Password,IC/Passport,Email,ContactNumber,Address,Role
        // This toString method will still output all 8 fields.
        // If your users.txt only has 3 fields, you'll need another method to
        // get just the username,password,role string, or adjust where it's used.
        return String.join(",",
                name,
                username,
                password,
                icPassport,
                email,
                contactNumber,
                address,
                role);
    }

    // Optional: A method to get a simplified string for login file
    public String toLoginString() {
        return String.join(",", username, password, role);
    }
}