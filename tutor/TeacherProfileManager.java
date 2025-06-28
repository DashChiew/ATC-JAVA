// File: src/main/java/com/tutorapp/data/TeacherProfileManager.java
package com.tutorapp.data;

import com.tutorapp.utils.FileUtil; // Using FileUtil for file operations

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TeacherProfileManager {

    private static final String TEACHER_PROFILE_FILE = "src/teacherProfile.txt";

    /**
     * Helper method to read tutor profile data from teacherProfile.txt.
     * @param username The username to search for.
     * @return An array of strings representing the tutor's data, or null if not found.
     * Format: ID,Username,ContactNumber,Email,Address,Subject
     */
    public static String[] getTutorProfileData(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader(TEACHER_PROFILE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1); // -1 to keep trailing empty strings
                if (parts.length >= 2 && parts[1].trim().equals(username)) { // Match by username (parts[1])
                    return parts;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading teacher profile data: " + e.getMessage());
        }
        return null;
    }

    /**
     * Updates a tutor's profile data in the teacherProfile.txt file.
     * It finds the line corresponding to the original username and replaces it with updated data.
     *
     * @param originalUsername The username of the tutor whose profile is to be updated.
     * @param newId The new ID (usually kept same).
     * @param newUsername The new username.
     * @param newContact The new contact number.
     * @param newEmail The new email.
     * @param newAddress The new address.
     * @param newSubject The new subject (usually kept same).
     * @return true if the profile was successfully updated, false otherwise.
     */
    public static boolean updateTutorProfile(String originalUsername, String newId, String newUsername,
                                             String newContact, String newEmail, String newAddress, String newSubject) {
        List<String> allLines = new ArrayList<>();
        boolean updated = false;

        try {
            allLines = FileUtil.readAllLines(TEACHER_PROFILE_FILE);
            List<String> finalLinesToWrite = new ArrayList<>();

            for (String line : allLines) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 6 && parts[1].trim().equals(originalUsername) && !updated) {
                    // This is the line to update
                    String updatedLine = newId + "," +
                            newUsername + "," +
                            newContact + "," +
                            newEmail + "," +
                            newAddress + "," +
                            newSubject;
                    finalLinesToWrite.add(updatedLine);
                    updated = true;
                } else {
                    finalLinesToWrite.add(line);
                }
            }

            if (updated) {
                FileUtil.writeAllLines(TEACHER_PROFILE_FILE, finalLinesToWrite);
                return true;
            }
        } catch (IOException e) {
            System.err.println("Error updating teacher profile data: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}