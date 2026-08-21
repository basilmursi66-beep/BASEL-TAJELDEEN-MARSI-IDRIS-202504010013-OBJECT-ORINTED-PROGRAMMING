import java.util.ArrayList;
import java.util.List;

public class Teacher extends Person {

    private String teacherID;
    private String password;
    private List<String> assignedSubjectCodes;

    public Teacher(String name, String teacherID, String password) {
        super(name);
        this.teacherID = teacherID;
        this.password = password;
        this.assignedSubjectCodes = new ArrayList<>();
    }

    public String getTeacherID() {
        return teacherID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getAssignedSubjectCodes() {
        return assignedSubjectCodes;
    }

    public boolean teaches(String subjectCode) {
        return assignedSubjectCodes.contains(subjectCode);
    }

    @Override
    public String describeRole() {
        return name + " (Teacher, " + assignedSubjectCodes.size() + " subject(s) assigned)";
    }
}
