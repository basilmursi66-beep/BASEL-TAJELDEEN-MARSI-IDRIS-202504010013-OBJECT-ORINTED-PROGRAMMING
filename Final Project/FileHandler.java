import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    private static final String STUDENTS_FILE = "students.csv";
    private static final String COURSES_FILE = "courses.csv";
    private static final String TEACHERS_FILE = "teachers.csv";

    // Students 
    // Format: id,password,name,age,email,elective,feeAmount,feePaid,gradeAccessStatus,courseCodes,gradeEntries
    // courseCodes = code1;code2;...   gradeEntries = code1:grade1;code2:grade2;...

    public static void saveStudents(List<Student> students) {

        try (PrintWriter writer = new PrintWriter(new FileWriter(STUDENTS_FILE))) {

            for (Student s : students) {

                String courses = String.join(";", s.getRegisteredCourseCodes());
                String elective = s.getElective() == null ? "" : s.getElective();
                String email = s.getEmail() == null ? "" : s.getEmail();

                StringBuilder gradeEntries = new StringBuilder();
                for (var entry : s.getGrades().entrySet()) {
                    gradeEntries.append(entry.getKey()).append(":").append(entry.getValue()).append(";");
                }

                writer.println(
                    s.getStudentID() + "," + s.getPassword() + "," + s.getName() + "," + s.getAge() + "," +
                    email + "," + elective + "," + s.getFeeAmount() + "," + s.isFeePaid() + "," +
                    s.getGradeAccessStatus() + "," + courses + "," + gradeEntries
                );
            }

        } catch (IOException e) {
            System.out.println("Error saving students: " + e.getMessage());
        }
    }

    public static ArrayList<Student> loadStudents() {

        ArrayList<Student> students = new ArrayList<>();
        File file = new File(STUDENTS_FILE);

        if (!file.exists()) return students;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",", 11);
                if (parts.length < 9) continue;

                String id = parts[0];
                String password = parts[1];
                String name = parts[2];
                int age = Integer.parseInt(parts[3]);
                String email = parts[4].isEmpty() ? null : parts[4];
                String elective = parts[5].isEmpty() ? null : parts[5];
                double feeAmount = Double.parseDouble(parts[6]);
                boolean feePaid = Boolean.parseBoolean(parts[7]);
                Student.GradeAccessStatus status = Student.GradeAccessStatus.valueOf(parts[8]);

                Student student = new Student(name, age, id, password, email, elective);
                student.setFeeAmount(feeAmount);
                student.setFeePaid(feePaid);
                student.setGradeAccessStatus(status);

                if (parts.length >= 10 && !parts[9].isEmpty()) {
                    for (String code : parts[9].split(";")) {
                        student.getRegisteredCourseCodes().add(code);
                    }
                }

                if (parts.length == 11 && !parts[10].isEmpty()) {
                    for (String entry : parts[10].split(";")) {
                        String[] kv = entry.split(":");
                        if (kv.length == 2) {
                            student.getGrades().put(kv[0], kv[1]);
                        }
                    }
                }

                students.add(student);
            }

        } catch (IOException e) {
            System.out.println("Error loading students: " + e.getMessage());
        }

        return students;
    }

    // Courses 
    // Format: code,name,day,timeSlot,capacity,registeredId1;registeredId2;...

    public static void saveCourses(CourseManager courseManager) {

        try (PrintWriter writer = new PrintWriter(new FileWriter(COURSES_FILE))) {

            for (CourseOffering c : courseManager.getAllCourses()) {
                String registered = String.join(";", c.getRegisteredStudentIDs());
                writer.println(
                    c.getCode() + "," + c.getName() + "," + c.getDay() + "," +
                    c.getTimeSlot() + "," + c.getCapacity() + "," + registered
                );
            }

        } catch (IOException e) {
            System.out.println("Error saving courses: " + e.getMessage());
        }
    }

    public static void loadCourses(CourseManager courseManager) {

        File file = new File(COURSES_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",", 6);
                if (parts.length < 5) continue;

                String code = parts[0];
                String name = parts[1];
                String day = parts[2];
                String timeSlot = parts[3];
                int capacity = Integer.parseInt(parts[4]);

                courseManager.loadRaw(code, name, day, timeSlot, capacity);

                if (parts.length == 6 && !parts[5].isEmpty()) {
                    CourseOffering course = courseManager.getCourse(code);
                    for (String studentId : parts[5].split(";")) {
                        course.getRegisteredStudentIDs().add(studentId);
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading courses: " + e.getMessage());
        }
    }

    // Teachers
    // Format: id,password,name,subjectCode1;subjectCode2;...

    public static void saveTeachers(List<Teacher> teachers) {

        try (PrintWriter writer = new PrintWriter(new FileWriter(TEACHERS_FILE))) {

            for (Teacher t : teachers) {
                String subjects = String.join(";", t.getAssignedSubjectCodes());
                writer.println(t.getTeacherID() + "," + t.getPassword() + "," + t.getName() + "," + subjects);
            }

        } catch (IOException e) {
            System.out.println("Error saving teachers: " + e.getMessage());
        }
    }

    public static ArrayList<Teacher> loadTeachers() {

        ArrayList<Teacher> teachers = new ArrayList<>();
        File file = new File(TEACHERS_FILE);

        if (!file.exists()) return teachers;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",", 4);
                if (parts.length < 3) continue;

                String id = parts[0];
                String password = parts[1];
                String name = parts[2];

                Teacher teacher = new Teacher(name, id, password);

                if (parts.length == 4 && !parts[3].isEmpty()) {
                    for (String code : parts[3].split(";")) {
                        teacher.getAssignedSubjectCodes().add(code);
                    }
                }

                teachers.add(teacher);
            }

        } catch (IOException e) {
            System.out.println("Error loading teachers: " + e.getMessage());
        }

        return teachers;
    }
}
