import java.util.List;
import java.util.ArrayList;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SessionManager {
    private static final String SESSION_FILE = "sessions.txt";

    public static String createSession(String username, String role) throws IOException {
        String id = generateSessionId();
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String line = id + "," + username + "," + role + "," + time;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SESSION_FILE, true))) {
            writer.write(line);
            writer.newLine();
        }
        return id;
    }

    public static List<String[]> listActiveSessions() throws IOException {
        List<String[]> sessions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(SESSION_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    sessions.add(parts);
                }
            }
        }
        return sessions;
    }

    public static boolean removeSession(String sessionId) throws IOException {
        File inputFile = new File(SESSION_FILE);
        File tempFile = new File("sessions_temp.txt");

        boolean found = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(sessionId + ",")) {
                    found = true;
                    continue;
                }
                writer.write(line);
                writer.newLine();
            }
        }

        if (found) {
            inputFile.delete();
            tempFile.renameTo(inputFile);
        } else {
            tempFile.delete();
        }

        return found;
    }

    private static String generateSessionId() {
        return "S" + System.currentTimeMillis();
    }
}
