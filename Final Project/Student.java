import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student extends Person implements Payable {

    // Unpaid students may request grade access; paid students can view grades directly.
    public enum GradeAccessStatus {
        NOT_REQUESTED,
        PENDING,
        APPROVED,
        REJECTED
    }

    private String studentID;
    private String password;
    private String email;
    private int age;
    private String elective; // null if the student has no admin-assigned elective package
    private List<String> registeredCourseCodes;
    private Map<String, String> grades; // courseCode -> letter grade

    private double feeAmount;
    private boolean feePaid;
    private GradeAccessStatus gradeAccessStatus;

    public Student(String name, int age, String studentID, String password, String email, String elective) {
        super(name);
        this.age = age;
        this.studentID = studentID;
        this.password = password;
        this.email = email;
        this.elective = elective;
        this.registeredCourseCodes = new ArrayList<>();
        this.grades = new HashMap<>();
        this.feeAmount = 0;
        this.feePaid = false;
        this.gradeAccessStatus = GradeAccessStatus.NOT_REQUESTED;
    }

    public String getStudentID() {
        return studentID;
    }

    public String getElective() {
        return elective;
    }

    public void setElective(String elective) {
        this.elective = elective;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getRegisteredCourseCodes() {
        return registeredCourseCodes;
    }

    public boolean isRegisteredIn(String courseCode) {
        return registeredCourseCodes.contains(courseCode);
    }

    public Map<String, String> getGrades() {
        return grades;
    }

    public double getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(double feeAmount) {
        this.feeAmount = feeAmount;
    }

    public boolean isFeePaid() {
        return feePaid;
    }

    public void setFeePaid(boolean feePaid) {
        this.feePaid = feePaid;
    }

    // Payable interface method - the rule "paying clears the amount owed"
    // lives here on the object itself, not scattered across whichever class
    // happens to call it.
    @Override
    public void markAsPaid() {
        this.feePaid = true;
        this.feeAmount = 0;
    }

    public GradeAccessStatus getGradeAccessStatus() {
        return gradeAccessStatus;
    }

    public void setGradeAccessStatus(GradeAccessStatus gradeAccessStatus) {
        this.gradeAccessStatus = gradeAccessStatus;
    }

    @Override
    public String describeRole() {
        return name + " (Student, " + (elective == null ? "no elective yet" : elective) + ")";
    }
}
