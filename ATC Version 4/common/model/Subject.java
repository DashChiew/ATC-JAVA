package common.model;

// Represents a single subject with its details
public class Subject {
    private String name;
    private String form;
    private double price;
    private String time;
    private String tutor;

    // Constructor to initialize a Subject object
    public Subject(String name, String form, double price, String time, String tutor) {
        this.name = name;
        this.form = form;
        this.price = price;
        this.time = time;
        this.tutor = tutor;
    }

    // Getter methods for each property
    public String getName() {
        return name;
    }

    public String getForm() {
        return form;
    }

    public double getPrice() {
        return price;
    }

    public String getTime() {
        return time;
    }

    public String getTutor() {
        return tutor;
    }

    // Helper method to get the day of the week from the time string
    public String getDayOfWeek() {
        if (this.time != null && !this.time.isEmpty()) {
            // Assumes format like "Monday 4:00 PM - 5:00 PM"
            String[] parts = this.time.split(" ", 2); // Split only at the first space
            return parts[0];
        }
        return "N/A"; // Or throw an exception, depending on how strict you want to be
    }

    // NEW METHOD: Returns only the time part (e.g., "4:00 PM - 5:00 PM")
    public String getLessonTimeOnly() {
        if (this.time != null && !this.time.isEmpty()) {
            int firstSpaceIndex = this.time.indexOf(' ');
            if (firstSpaceIndex != -1) {
                return this.time.substring(firstSpaceIndex + 1); // Get substring after the first space
            }
        }
        return "N/A"; // Or appropriate default if parsing fails
    }

    // Override toString for easy printing (optional, but good for debugging)
    @Override
    public String toString() {
        return "Subject{" +
                "name='" + name + '\'' +
                ", form='" + form + '\'' +
                ", price=" + price +
                ", time='" + time + '\'' +
                ", tutor='" + tutor + '\'' +
                '}';
    }
}