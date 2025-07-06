package TutorApp.models;

import java.util.ArrayList;
import java.util.List;
import TutorApp.utils.FileUtil;

public class TutorData {
    private String username;
    private String password;
    private List<String> subjects; // This seems unused in your current code, but keeping it for completeness

    public TutorData(String username, String password) {
        this.username = username;
        this.password = password;
        this.subjects = new ArrayList<>();
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public List<String> getSubjects() { return subjects; }

    public void addSubject(String subject) {
        subjects.add(subject);
    }
}