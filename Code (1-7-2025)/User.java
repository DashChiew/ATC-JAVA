import java.util.Objects;

class User {
    private String name;
    private String username;
    private String password;
    private String icPassport;
    private String email;
    private String contactNumber;
    private String address;
    private String role;

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

    public void setUsername(String username) { // Added this setter, as it was missing but used in Setting class
        this.username = username;
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
}