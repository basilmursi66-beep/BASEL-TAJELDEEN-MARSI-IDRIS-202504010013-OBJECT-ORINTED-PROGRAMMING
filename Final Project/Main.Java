import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        CourseManager courseManager = new CourseManager();
        StudentManager studentManager = new StudentManager();
        TeacherManager teacherManager = new TeacherManager();
        Admin admin = new Admin("System Admin", "admin", "admin123");

        printRoster(admin, teacherManager, studentManager);

        javax.swing.SwingUtilities.invokeLater(() -> new LoginFrame(studentManager, courseManager, teacherManager, admin));
    }

    // Builds a list of Person references holding Admin, Teacher, and Student
    // objects underneath. Calling describeRole() on each runs a different
    // override depending on the real object - this is runtime polymorphism:
    // the reference type is Person, but the behavior is decided per-object.
    private static void printRoster(Admin admin, TeacherManager teacherManager, StudentManager studentManager) {

        List<Person> people = new ArrayList<>();
        people.add(admin);
        people.addAll(teacherManager.getAllTeachers());
        people.addAll(studentManager.getAllStudents());

        System.out.println("=== CampusHub Roster ===");
        for (Person p : people) {
            System.out.println(p.describeRole());
        }
        System.out.println("========================");
    }
}
