package TutorApp.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    /**
     * Helper method to create a temporary placeholder image file.
     * This is used if the local image resource is not found.
     */
    public static File createPlaceholderImage(int width, int height, Color bgColor, Color textColor, String text) throws IOException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setColor(bgColor);
        g2d.fillRect(0, 0, width, height);

        g2d.setColor(textColor);
        g2d.setFont(new Font("Inter", Font.BOLD, Math.min(width, height) / 4));
        FontMetrics fm = g2d.getFontMetrics();
        int x = (width - fm.stringWidth(text)) / 2;
        int y = (fm.getAscent() + (height - (fm.getAscent() + fm.getDescent())) / 2);
        g2d.drawString(text, x, y);

        g2d.dispose();

        File tempFile = File.createTempFile("placeholder", ".png");
        ImageIO.write(image, "png", tempFile);
        tempFile.deleteOnExit(); // Ensure temporary file is deleted on exit
        return tempFile;
    }

    /**
     * Reads all lines from a given file and returns them as a List of Strings.
     * If the file does not exist, an empty list is returned instead of throwing an exception.
     *
     * @param filePath The path to the file.
     * @return A List of Strings, each representing a line from the file.
     * @throws IOException If an I/O error occurs (other than FileNotFound).
     */
    public static List<String> readAllLines(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        File file = new File(filePath); // Create a File object

        // Check if the file exists before attempting to read
        if (!file.exists()) {
            return lines; // Return empty list if file does not exist
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) { // Use the File object
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    /**
     * Writes a list of strings to a file, overwriting existing content.
     * This method will create the file and its parent directories if they do not exist.
     *
     * @param filePath The path to the file.
     * @param lines    The list of strings to write.
     * @throws IOException If an I/O error occurs.
     */
    public static void writeAllLines(String filePath, List<String> lines) throws IOException {
        File file = new File(filePath);

        // Ensure parent directories exist
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) { // mkdirs() returns true if directories were created, false if they already exist or failed
                throw new IOException("Failed to create parent directories for file: " + filePath);
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) { // Use the File object
            for (String line : lines) {
                bw.write(line);
                bw.newLine(); // Use newLine() for platform-independent line separator
            }
        }
    }

    /**
     * Parses a CSV line, handling quoted fields with commas.
     *
     * @param line The CSV line to parse.
     * @return An array of strings representing the parsed fields.
     */
    public static String[] parseCsvLine(String line) {
        List<String> parts = new ArrayList<>();
        StringBuilder currentPart = new StringBuilder();
        boolean inQuote = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuote = !inQuote;
            } else if (c == ',' && !inQuote) {
                parts.add(currentPart.toString().trim());
                currentPart = new StringBuilder();
            } else {
                currentPart.append(c);
            }
        }
        parts.add(currentPart.toString().trim()); // Add the last part
        return parts.toArray(new String[0]);
    }
}