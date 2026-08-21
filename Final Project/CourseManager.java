import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CourseManager {

    private Map<String, CourseOffering> courses; // code -> course

    public CourseManager() {
        courses = new LinkedHashMap<>();
        FileHandler.loadCourses(this);

        if (courses.isEmpty()) {
            seedDefaultCatalog();
        }
    }

    private void seedDefaultCatalog() {
        for (String[] row : Electives.CATALOG_SEED) {
            loadRaw(row[0], row[1], row[2], row[3], Integer.parseInt(row[4]));
        }
        persist();
    }

    public boolean addCourse(String code, String name, String day, String timeSlot, int capacity) {

        if (courses.containsKey(code)) {
            return false; // duplicate course code
        }

        courses.put(code, new CourseOffering(code, name, day, timeSlot, capacity));
        FileHandler.saveCourses(this);
        return true;
    }

    // Used only when restoring from file - registrations are re-applied separately
    public void loadRaw(String code, String name, String day, String timeSlot, int capacity) {
        courses.put(code, new CourseOffering(code, name, day, timeSlot, capacity));
    }

    public boolean removeCourse(String code) {

        if (!courses.containsKey(code)) return false;

        courses.remove(code);
        FileHandler.saveCourses(this);
        return true;
    }

    public CourseOffering getCourse(String code) {
        return courses.get(code);
    }

    public List<CourseOffering> getAllCourses() {
        return new ArrayList<>(courses.values());
    }

    public void persist() {
        FileHandler.saveCourses(this);
    }
}
