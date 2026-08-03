public class student extends user {

    private int age;
    private String studentID;
    private course course;

    public student(String name, int age, String studentID) {
        super(name);
        this.age = age;
        this.studentID = studentID;
    }

    public void setCourse(course course) {
        this.course = course;
    }

    public String getStudentID() {
        return studentID;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void displayProfile() {

        System.out.println("\n================================");
        System.out.println("       STUDENT PROFILE");
        System.out.println("================================");

        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Student ID: " + studentID);

        if (course != null) {

            System.out.println("Course: " + course.getCourseName());
            System.out.println("Elective: " + course.getElectiveCourse());

            System.out.println("\nSubjects:");

            for (String subject : course.getSubjects()) {
                System.out.println("- " + subject);
            }
        }

        System.out.println("================================");
    }

    public course getCourse() {
    return course;
}
}