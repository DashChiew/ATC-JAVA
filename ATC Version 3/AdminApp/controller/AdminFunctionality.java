package AdminApp.controller;

import java.io.IOException; // Although not directly thrown by methods shown, good practice if FileHandler throws it
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import common.model.User;
import common.util.FileHandler;
// Assuming User, FileHandler, and AssignmentFunctionality classes are defined in their respective .java files
// and are correctly imported into the project build path.
// The placeholders are removed from this file to resolve the "incompatible types" and "cannot resolve symbol" errors.

public class AdminFunctionality {

    public static final String USERS_FILE = "users.txt";

    /**
     * Reads all users from the users file.
     * It expects each line to represent a User object with 8 comma-separated fields.
     * @return A list of User objects.
     */
    public static List<User> readAllUsers() {
        List<String> lines = FileHandler.readAllLines(USERS_FILE);
        List<User> users = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(",");
            // Ensure that the line has exactly 8 parts to match the User constructor
            if (parts.length == 8) {
                // Construct User object with all 8 fields
                users.add(new User(
                        parts[0].trim(), // Name
                        parts[1].trim(), // Username
                        parts[2].trim(), // Password
                        parts[3].trim(), // IC/Passport
                        parts[4].trim(), // Email
                        parts[5].trim(), // Contact Number
                        parts[6].trim(), // Address
                        parts[7].trim()  // Role
                ));
            } else {
                System.err.println("Skipping malformed user line (expected 8 parts, found " + parts.length + "): " + line);
            }
        }
        return users;
    }

    /**
     * Writes a list of User objects to the users file, overwriting existing content.
     * Each User object is converted to its string representation (8 comma-separated fields).
     * @param users The list of User objects to write.
     */
    public static void writeAllUsers(List<User> users) {
        List<String> lines = users.stream()
                .map(User::toString) // Use User's toString() which now provides all 8 fields
                .collect(Collectors.toList());
        FileHandler.writeAllLines(USERS_FILE, lines);
    }

    /**
     * Saves (adds or updates) a user to the file. If a user with the same username exists, it updates.
     * Otherwise, it adds a new user.
     * @param newUser The User object to save.
     */
    public static void saveUser(User newUser) {
        List<User> users = readAllUsers();
        boolean userExists = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(newUser.getUsername())) {
                users.set(i, newUser); // Update existing user with new details
                userExists = true;
                break;
            }
        }
        if (!userExists) {
            users.add(newUser); // Add new user
        }
        writeAllUsers(users);
    }

    /**
     * Deletes a specific user from the file based on username and role.
     * If the deleted user is a tutor, also deletes their assignments.
     * @param userToDelete The User object to delete.
     * @return true if the user was deleted, false otherwise.
     */
    public static boolean deleteUser(User userToDelete) {
        List<User> users = readAllUsers();
        // Remove the user based on username and role
        boolean removed = users.removeIf(user ->
                user.getUsername().equals(userToDelete.getUsername()) &&
                        user.getRole().equals(userToDelete.getRole()));

        if (removed) {
            writeAllUsers(users);
            // If the deleted user is a tutor, remove their assignments as well
            if (userToDelete.getRole().equalsIgnoreCase("tutor")) {
                AssignmentFunctionality.deleteAssignmentsByTutor(userToDelete.getUsername());
                System.out.println("Removed assignments for deleted tutor: " + userToDelete.getUsername());
            }
        }
        return removed;
    }

    /**
     * Retrieves a list of users filtered by their role.
     * @param role The role to filter by (e.g., "tutor", "receptionist").
     * @return A list of User objects matching the specified role.
     */
    public static List<User> getUsersByRole(String role) {
        return readAllUsers().stream()
                .filter(user -> user.getRole().equalsIgnoreCase(role))
                .collect(Collectors.toList());
    }

    /**
     * Initializes the users file with default users if it's empty or doesn't contain specific defaults.
     * Default users are created with all 8 fields.
     */
    public static void initializeUsersFile() {
        if (!FileHandler.createFileIfNotExists(USERS_FILE)) {
            System.err.println("Failed to create users file! Application may not function correctly.");
            return;
        }

        List<User> currentUsers = readAllUsers();
        List<User> usersToAdd = new ArrayList<>();

        // Define a placeholder for common new fields
        String defaultIcPassport = "N/A";
        String defaultEmail = "default@example.com";
        String defaultContact = "000-0000000";
        String defaultAddress = "Default Address";

        // Add default admin if not present
        if (currentUsers.stream().noneMatch(u -> u.getUsername().equals("CYChang") && u.getRole().equals("admin"))) {
            usersToAdd.add(new User("Admin User", "CYChang", "admin123", defaultIcPassport, defaultEmail, defaultContact, defaultAddress, "admin"));
        }
        // Add default tutor if not present
        if (currentUsers.stream().noneMatch(u -> u.getUsername().equals("tutor1") && u.getRole().equals("tutor"))) {
            usersToAdd.add(new User("Tutor One", "tutor1", "tutorpass", defaultIcPassport, defaultEmail, defaultContact, defaultAddress, "tutor"));
        }
        // Add default receptionist if not present
        if (currentUsers.stream().noneMatch(u -> u.getUsername().equals("reception1") && u.getRole().equals("receptionist"))) {
            usersToAdd.add(new User("Receptionist One", "reception1", "recpass", defaultIcPassport, defaultEmail, defaultContact, defaultAddress, "receptionist"));
        }

        if (!usersToAdd.isEmpty()) {
            List<User> allUsers = new ArrayList<>(currentUsers);
            allUsers.addAll(usersToAdd);
            writeAllUsers(allUsers);
        }
    }
    // Inside your AdminApp.controller.AdminFunctionality class:

    /**
     * Updates a user's information in the specified file.
     * It reads all lines, finds the old user by username, replaces the line with the new user's data,
     * and writes the updated list back to the file.
     *
     * @param filePath The path to the file (e.g., AdminFunctionality.USERS_FILE).
     * @param oldUser The User object representing the user's old data.
     * @param newUser The User object representing the user's new data.
     * @return true if the user was found and updated, false otherwise.
     */
    public static boolean updateUserInFile(String filePath, common.model.User oldUser, common.model.User newUser) {
        List<String> lines = common.util.FileHandler.readAllLines(filePath);
        boolean updated = false;
        List<String> updatedLines = new ArrayList<>();

        for (String line : lines) {
            // Assuming the User.toString() method generates a line that starts with the username
            // Or, more reliably, split the line and check the username part.
            // For this example, let's parse the line to a User object to compare usernames.
            // This requires a constructor in User that can parse a line, or a static factory method.
            // For simplicity here, we'll assume the line contains the oldUser's username.

            // A more robust way would be to re-parse the line into a User object
            // and compare its username with oldUser.getUsername().
            // For now, let's assume oldUser's username is present in the line string for comparison.
            // It's better if User.toString() includes the username clearly and consistently.

            // Example: If line format is "Name,Username,Password,..."
            String[] parts = line.split(",");
            if (parts.length >= 2 && parts[1].trim().equals(oldUser.getUsername())) {
                // This is the line for the old user, replace it with the new user's data
                updatedLines.add(newUser.toString());
                updated = true;
            } else {
                // Keep existing lines
                updatedLines.add(line);
            }
        }

        if (updated) {
            common.util.FileHandler.writeAllLines(filePath, updatedLines);
            System.out.println("User '" + oldUser.getUsername() + "' successfully updated in " + filePath);
        } else {
            System.out.println("User '" + oldUser.getUsername() + "' not found in " + filePath + ". No update performed.");
        }
        return updated;
    }
}