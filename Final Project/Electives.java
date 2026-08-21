import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Electives {

    public static final String COURSE_NAME = "Bachelor of Computer Science";
    public static final String[] ELECTIVE_NAMES = {"Cybersecurity", "Data Science", "Artificial Intelligence"};

    // Pre-built catalog: code, name, day, time, capacity
    // This is what "subjects are already registered with time and day" means -
    // the schedule exists before any student is ever added.
    public static final String[][] CATALOG_SEED = {
        {"STD1", "Programming Fundamentals", "Monday", "8:00 AM - 9:30 AM", "30"},
        {"STD2", "Database Systems", "Tuesday", "8:00 AM - 9:30 AM", "30"},
        {"STD3", "Software Engineering", "Wednesday", "8:00 AM - 9:30 AM", "30"},
        {"CYB1", "Network Security", "Thursday", "8:00 AM - 9:30 AM", "30"},
        {"CYB2", "Ethical Hacking", "Friday", "8:00 AM - 9:30 AM", "30"},
        {"CYB3", "Cyber Defense", "Monday", "9:30 AM - 11:00 AM", "30"},
        {"DS1", "Statistics", "Tuesday", "9:30 AM - 11:00 AM", "30"},
        {"DS2", "Data Analysis", "Wednesday", "9:30 AM - 11:00 AM", "30"},
        {"ML1", "Machine Learning", "Thursday", "9:30 AM - 11:00 AM", "30"},
        {"AI1", "AI Fundamentals", "Friday", "9:30 AM - 11:00 AM", "30"},
        {"AI2", "Deep Learning", "Monday", "11:00 AM - 12:30 PM", "30"}
    };

    private static final String[] STANDARD_CODES = {"STD1", "STD2", "STD3"};
    private static final String[] CYBERSECURITY_CODES = {"CYB1", "CYB2", "CYB3"};
    private static final String[] DATA_SCIENCE_CODES = {"DS1", "DS2", "ML1"};
    private static final String[] AI_CODES = {"AI1", "ML1", "AI2"};

    // Every student takes the 3 standard subjects plus their elective's 3 subjects
    public static String[] getCourseCodesForElective(String elective) {

        List<String> codes = new ArrayList<>(Arrays.asList(STANDARD_CODES));

        if (elective.equals("Cybersecurity")) {
            codes.addAll(Arrays.asList(CYBERSECURITY_CODES));
        } else if (elective.equals("Data Science")) {
            codes.addAll(Arrays.asList(DATA_SCIENCE_CODES));
        } else if (elective.equals("Artificial Intelligence")) {
            codes.addAll(Arrays.asList(AI_CODES));
        }

        return codes.toArray(new String[0]);
    }
}
