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
        File file = new File(filePath);

        if (!file.exists()) {
            return lines;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
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

        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new IOException("Failed to create parent directories for file: " + filePath);
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        }
    }

    /**
     * Parses a CSV line, handling quoted fields with commas and escaped double quotes.
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
                if (inQuote && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    // This is an escaped double quote ("") inside a quoted field
                    currentPart.append('"');
                    i++; // Skip the next quote character
                } else {
                    inQuote = !inQuote; // Toggle quote state
                }
            } else if (c == ',' && !inQuote) {
                // End of a field, not inside a quote
                parts.add(currentPart.toString()); // No trim here, trim after unquoting if necessary
                currentPart = new StringBuilder();
            } else {
                currentPart.append(c);
            }
        }
        parts.add(currentPart.toString()); // Add the last part

        // Post-process to remove surrounding quotes and handle internal escaped quotes
        List<String> processedParts = new ArrayList<>();
        for (String part : parts) {
            if (part.startsWith("\"") && part.endsWith("\"") && part.length() >= 2) {
                // Remove surrounding quotes and replace "" with "
                processedParts.add(part.substring(1, part.length() - 1).replace("\"\"", "\""));
            } else {
                processedParts.add(part);
            }
        }
        return processedParts.toArray(new String[0]);
    }
}
