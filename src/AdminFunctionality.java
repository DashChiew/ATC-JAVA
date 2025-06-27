import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminFunctionality {

    public static final String USERS_FILE = "users.txt";

    /**
     * Reads all users from the users file.
     * @return A list of User objects.
     */
    public static List<User> readAllUsers() {
        List<String> lines = FileHandler.readAllLines(USERS_FILE);
        List<User> users = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 3) {
                users.add(new User(parts[0].trim(), parts[1].trim(), parts[2].trim()));
            }
        }
        return users;
    }

    /**
     * Writes a list of User objects to the users file, overwriting existing content.
     * @param users The list of User objects to write.
     */
    public static void writeAllUsers(List<User> users) {
        List<String> lines = users.stream()
                .map(user -> user.getUsername() + "," + user.getPassword() + "," + user.getRole())
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
                users.set(i, newUser); // Update existing user
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
     */
    public static void initializeUsersFile() {
        if (!FileHandler.createFileIfNotExists(USERS_FILE)) {
            System.err.println("Failed to create users file! Application may not function correctly.");
            return;
        }

        List<User> currentUsers = readAllUsers();
        List<User> usersToAdd = new ArrayList<>();

        // Add default admin if not present
        if (currentUsers.stream().noneMatch(u -> u.getUsername().equals("CYChang") && u.getRole().equals("admin"))) {
            usersToAdd.add(new User("CYChang", "admin123", "admin"));
        }
        // Add default tutor if not present
        if (currentUsers.stream().noneMatch(u -> u.getUsername().equals("tutor1") && u.getRole().equals("tutor"))) {
            usersToAdd.add(new User("tutor1", "tutorpass", "tutor"));
        }
        // Add default receptionist if not present
        if (currentUsers.stream().noneMatch(u -> u.getUsername().equals("reception1") && u.getRole().equals("receptionist"))) {
            usersToAdd.add(new User("reception1", "recpass", "receptionist"));
        }

        if (!usersToAdd.isEmpty()) {
            List<User> allUsers = new ArrayList<>(currentUsers);
            allUsers.addAll(usersToAdd);
            writeAllUsers(allUsers);
        }
    }
}