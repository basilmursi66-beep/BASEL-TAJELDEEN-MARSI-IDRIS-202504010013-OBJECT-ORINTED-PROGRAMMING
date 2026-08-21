import java.util.ArrayList;
import java.util.List;

public class CourseOffering {

    private String code;
    private String name;
    private String day;
    private String timeSlot;
    private int capacity;
    private List<String> registeredStudentIDs;

    public CourseOffering(String code, String name, String day, String timeSlot, int capacity) {
        this.code = code;
        this.name = name;
        this.day = day;
        this.timeSlot = timeSlot;
        this.capacity = capacity;
        this.registeredStudentIDs = new ArrayList<>();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<String> getRegisteredStudentIDs() {
        return registeredStudentIDs;
    }

    public int getSeatsRemaining() {
        return capacity - registeredStudentIDs.size();
    }

    public boolean isFull() {
        return registeredStudentIDs.size() >= capacity;
    }

    public boolean isRegistered(String studentId) {
        return registeredStudentIDs.contains(studentId);
    }

    // Same day AND overlapping time slot as another course
    public boolean collidesWith(CourseOffering other) {
        return this.day.equals(other.day) && this.timeSlot.equals(other.timeSlot);
    }
}
