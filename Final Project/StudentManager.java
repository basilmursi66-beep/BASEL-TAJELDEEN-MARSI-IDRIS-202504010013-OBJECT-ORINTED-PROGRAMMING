import java.util.ArrayList;
import java.util.List;

public class StudentManager {

    public enum RegistrationResult {
        SUCCESS,
        ALREADY_REGISTERED,
        COURSE_FULL,
        SCHEDULE_CONFLICT,
        COURSE_NOT_FOUND,
        STUDENT_NOT_FOUND
    }

    private List<Student> students;

    public StudentManager() {
        students = FileHandler.loadStudents();
    }

    public boolean addStudent(Student student) {

        if (searchStudent(student.getStudentID()) != null) {
            return false; // duplicate ID
        }

        students.add(student);
        FileHandler.saveStudents(students);
        return true;
    }

    public Student searchStudent(String id) {

        for (Student s : students) {
            if (s.getStudentID().equals(id)) {
                return s;
            }
        }

        return null;
    }

    public Student login(String id, String password) {

        Student s = searchStudent(id);

        if (s != null && s.getPassword().equals(password)) {
            return s;
        }

        return null;
    }

    public boolean deleteStudent(String id, CourseManager courseManager) {

        Student s = searchStudent(id);
        if (s == null) return false;

        // Clean up their seat in every course they were registered in
        for (String code : new ArrayList<>(s.getRegisteredCourseCodes())) {
            CourseOffering course = courseManager.getCourse(code);
            if (course != null) {
                course.getRegisteredStudentIDs().remove(id);
            }
        }
        courseManager.persist();

        students.remove(s);
        FileHandler.saveStudents(students);
        return true;
    }

    public List<Student> getAllStudents() {
        return students;
    }

    // The core "not just CRUD" logic: registration is only allowed if the course
    // has an open seat AND doesn't collide with anything the student is already taking.
    public RegistrationResult registerForCourse(String studentId, String courseCode, CourseManager courseManager) {

        Student student = searchStudent(studentId);
        if (student == null) return RegistrationResult.STUDENT_NOT_FOUND;

        CourseOffering course = courseManager.getCourse(courseCode);
        if (course == null) return RegistrationResult.COURSE_NOT_FOUND;

        if (student.isRegisteredIn(courseCode)) {
            return RegistrationResult.ALREADY_REGISTERED;
        }

        if (course.isFull()) {
            return RegistrationResult.COURSE_FULL;
        }

        for (String existingCode : student.getRegisteredCourseCodes()) {
            CourseOffering existingCourse = courseManager.getCourse(existingCode);
            if (existingCourse != null && existingCourse.collidesWith(course)) {
                return RegistrationResult.SCHEDULE_CONFLICT;
            }
        }

        student.getRegisteredCourseCodes().add(courseCode);
        course.getRegisteredStudentIDs().add(studentId);

        FileHandler.saveStudents(students);
        courseManager.persist();

        return RegistrationResult.SUCCESS;
    }

    // Enrolls a student into every subject for their elective, using the same
    // capacity/conflict rules as manual registration. Rolls back partial
    // progress if any subject in the group can't be granted.
    // Returns null on success, or an error message on failure.
    public String autoEnrollElective(String studentId, String elective, CourseManager courseManager) {

        Student student = searchStudent(studentId);
        if (student == null) return "Student not found.";

        String[] codes = Electives.getCourseCodesForElective(elective);
        List<String> newlyRegistered = new ArrayList<>();

        for (String code : codes) {

            RegistrationResult result = registerForCourse(studentId, code, courseManager);

            if (result == RegistrationResult.SUCCESS) {
                newlyRegistered.add(code);

            } else if (result == RegistrationResult.ALREADY_REGISTERED) {
                // already has this one, nothing to do

            } else {
                // roll back everything this attempt just added
                for (String rc : newlyRegistered) {
                    dropCourse(studentId, rc, courseManager);
                }

                CourseOffering course = courseManager.getCourse(code);
                String courseName = course != null ? course.getName() : code;

                if (result == RegistrationResult.COURSE_FULL) return courseName + " is full.";
                if (result == RegistrationResult.SCHEDULE_CONFLICT) return courseName + " has a schedule conflict.";
                return "Could not enroll in " + courseName + ".";
            }
        }

        return null; // success
    }

    // Drops the student's current elective group (if any) and enrolls them in the new one.
    public String changeElective(String studentId, String newElective, CourseManager courseManager) {

        Student student = searchStudent(studentId);
        if (student == null) return "Student not found.";

        String oldElective = student.getElective();

        if (oldElective != null) {
            for (String code : Electives.getCourseCodesForElective(oldElective)) {
                dropCourse(studentId, code, courseManager);
            }
        }

        String result = autoEnrollElective(studentId, newElective, courseManager);

        if (result == null) {
            student.setElective(newElective);
            FileHandler.saveStudents(students);
        }

        return result;
    }

    // Lets an admin move a course to a new day/time, but only if it doesn't
    // create a clash for any student currently enrolled in it.
    public String updateCourseSchedule(String courseCode, String newDay, String newTimeSlot, CourseManager courseManager) {

        CourseOffering course = courseManager.getCourse(courseCode);
        if (course == null) return "Course not found.";

        for (String studentId : course.getRegisteredStudentIDs()) {

            Student student = searchStudent(studentId);
            if (student == null) continue;

            for (String otherCode : student.getRegisteredCourseCodes()) {

                if (otherCode.equals(courseCode)) continue;

                CourseOffering other = courseManager.getCourse(otherCode);

                if (other != null && other.getDay().equals(newDay) && other.getTimeSlot().equals(newTimeSlot)) {
                    return student.getName() + " (" + student.getStudentID() + ") already has " +
                           other.getName() + " at that day/time.";
                }
            }
        }

        course.setDay(newDay);
        course.setTimeSlot(newTimeSlot);
        courseManager.persist();
        return null; // success
    }

    public boolean dropCourse(String studentId, String courseCode, CourseManager courseManager) {

        Student student = searchStudent(studentId);
        if (student == null || !student.isRegisteredIn(courseCode)) return false;

        student.getRegisteredCourseCodes().remove(courseCode);

        CourseOffering course = courseManager.getCourse(courseCode);
        if (course != null) {
            course.getRegisteredStudentIDs().remove(studentId);
        }

        FileHandler.saveStudents(students);
        courseManager.persist();
        return true;
    }

    // ---- Fees ----

    public boolean updateFees(String studentId, double feeAmount, boolean feePaid) {

        Student student = searchStudent(studentId);
        if (student == null) return false;

        if (feePaid) {
            student.markAsPaid(); // A paid student gets direct grade access.
        } else {
            student.setFeeAmount(feeAmount);
            student.setFeePaid(false);
            // A previous approval is no longer needed if the student becomes unpaid.
            if (student.getGradeAccessStatus() == Student.GradeAccessStatus.APPROVED) {
                student.setGradeAccessStatus(Student.GradeAccessStatus.NOT_REQUESTED);
            }
        }

        FileHandler.saveStudents(students);
        return true;
    }

    // ---- Grades ----

    public boolean setGrade(String studentId, String courseCode, String grade) {

        Student student = searchStudent(studentId);
        if (student == null || !student.isRegisteredIn(courseCode)) return false;

        student.getGrades().put(courseCode, grade);
        FileHandler.saveStudents(students);
        return true;
    }

    // ---- Grade access requests ----
    // A student must request access; admin approves or rejects, informed by fee status.

    public boolean requestGradeAccess(String studentId) {

        Student student = searchStudent(studentId);
        if (student == null) return false;

        // Only allow a fresh request if there isn't already one pending or approved
        if (student.getGradeAccessStatus() == Student.GradeAccessStatus.PENDING ||
            student.getGradeAccessStatus() == Student.GradeAccessStatus.APPROVED) {
            return false;
        }

        student.setGradeAccessStatus(Student.GradeAccessStatus.PENDING);
        FileHandler.saveStudents(students);
        return true;
    }

    public List<Student> getPendingGradeRequests() {

        List<Student> pending = new ArrayList<>();
        for (Student s : students) {
            if (s.getGradeAccessStatus() == Student.GradeAccessStatus.PENDING) {
                pending.add(s);
            }
        }
        return pending;
    }

    public boolean approveGradeRequest(String studentId) {

        Student student = searchStudent(studentId);
        if (student == null) return false;

        student.setGradeAccessStatus(Student.GradeAccessStatus.APPROVED);
        FileHandler.saveStudents(students);
        return true;
    }

    public boolean rejectGradeRequest(String studentId) {

        Student student = searchStudent(studentId);
        if (student == null) return false;

        student.setGradeAccessStatus(Student.GradeAccessStatus.REJECTED);
        FileHandler.saveStudents(students);
        return true;
    }
}
