class TutorAssignment {
    private String tutorUsername;
    private String level;
    private String subject;

    public TutorAssignment(String tutorUsername, String level, String subject) {
        this.tutorUsername = tutorUsername;
        this.level = level;
        this.subject = subject;
    }

    // Getters
    public String getTutorUsername() { return tutorUsername; }
    public String getLevel() { return level; }
    public String getSubject() { return subject; }

    @Override
    public String toString() {
        return "Tutor: " + tutorUsername + ", Level: " + level + ", Subject: " + subject;
    }

    // Equality check based on all fields
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TutorAssignment that = (TutorAssignment) o;
        return tutorUsername.equals(that.tutorUsername) &&
                level.equals(that.level) &&
                subject.equals(that.subject);
    }

    @Override
    public int hashCode() {
        int result = tutorUsername.hashCode();
        result = 31 * result + level.hashCode();
        result = 31 * result + subject.hashCode();
        return result;
    }
}