import java.util.ArrayList;
import java.util.List;

public class TeacherManager {

    private List<Teacher> teachers;

    public TeacherManager() {
        teachers = FileHandler.loadTeachers();
    }

    public boolean addTeacher(Teacher teacher) {

        if (searchTeacher(teacher.getTeacherID()) != null) {
            return false; // duplicate ID
        }

        teachers.add(teacher);
        FileHandler.saveTeachers(teachers);
        return true;
    }

    public Teacher searchTeacher(String id) {

        for (Teacher t : teachers) {
            if (t.getTeacherID().equals(id)) {
                return t;
            }
        }

        return null;
    }

    public Teacher login(String id, String password) {

        Teacher t = searchTeacher(id);

        if (t != null && t.getPassword().equals(password)) {
            return t;
        }

        return null;
    }

    public boolean deleteTeacher(String id) {

        Teacher t = searchTeacher(id);
        if (t == null) return false;

        teachers.remove(t);
        FileHandler.saveTeachers(teachers);
        return true;
    }

    public boolean assignSubject(String teacherId, String subjectCode) {

        Teacher t = searchTeacher(teacherId);
        if (t == null) return false;

        if (!t.getAssignedSubjectCodes().contains(subjectCode)) {
            t.getAssignedSubjectCodes().add(subjectCode);
            FileHandler.saveTeachers(teachers);
        }

        return true;
    }

    public boolean unassignSubject(String teacherId, String subjectCode) {

        Teacher t = searchTeacher(teacherId);
        if (t == null) return false;

        t.getAssignedSubjectCodes().remove(subjectCode);
        FileHandler.saveTeachers(teachers);
        return true;
    }

    public List<Teacher> getAllTeachers() {
        return teachers;
    }
}
