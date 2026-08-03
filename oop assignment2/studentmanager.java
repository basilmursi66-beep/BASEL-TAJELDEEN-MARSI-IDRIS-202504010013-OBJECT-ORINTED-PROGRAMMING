import java.util.ArrayList;

public class studentmanager {

    private ArrayList<student> students;

    public studentmanager() {
        students = new ArrayList<>();
    }

    public void addStudent(student student) {
        students.add(student);
        System.out.println("Total Students: " + students.size());
    }

    public student searchStudent(String id) {

        for (student student : students) {

            if (student.getStudentID().equals(id)) {
                return student;
            }
        }

        return null;
    }

    public boolean deleteStudent(String id) {

        student student = searchStudent(id);

        if (student != null) {
            students.remove(student);
            return true;
        }

        return false;
    }

    public boolean updateStudent(String id, String newElective) {

    student student = searchStudent(id);

    if (student != null && student.getCourse() != null) {
        student.getCourse().setElectiveCourse(newElective);
        return true;
    }

    return false;
}

    public void displayAllStudents() {

        if (students.isEmpty()) {

            System.out.println("No students available.");
            return;
        }

        for (student student : students) {
            student.displayProfile();
        }
    }
}