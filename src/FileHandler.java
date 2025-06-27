import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    /**
     * Creates a file (and parent directories if needed)
     * @param filePath Path to the file
     * @return true if successful, false if failed
     */
    public static boolean createFileIfNotExists(String filePath) {
        File file = new File(filePath);
        if (file.exists()) return true;

        try {
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs(); // Create parent directories if missing
            }
            return file.createNewFile();
        } catch (IOException e) {
            System.err.println("Error creating file: " + e.getMessage());
            return false;
        }
    }


    /**
     * Reads all lines from a file
     * @param filePath Path to the file
     * @return List of lines (empty list if file doesn't exist)
     */
    public static List<String> readAllLines(String filePath) {
        List<String> lines = new ArrayList<>();
        if (!Files.exists(Paths.get(filePath))) return lines;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return lines;
    }


    /**
     * Writes lines to a file (overwrites existing content)
     * @param filePath Path to the file
     * @param lines List of strings to write
     * @return true if successful, false if failed
     */
    public static boolean writeAllLines(String filePath, List<String> lines) {
        if (!createFileIfNotExists(filePath)) return false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            return false;
        }
    }


    /**
     * Appends a line to a file
     * @param filePath Path to the file
     * @param line Line to append
     * @return true if successful, false if failed
     */
    public static boolean appendLine(String filePath, String line) {
        if (!createFileIfNotExists(filePath)) return false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(line);
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Error appending to file: " + e.getMessage());
            return false;
        }
    }


    /**
     * Deletes a file
     * @param filePath Path to the file
     * @return true if successful, false if failed
     */
    public static boolean deleteFile(String filePath) {
        try {
            return Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            System.err.println("Error deleting file: " + e.getMessage());
            return false;
        }
    }




}