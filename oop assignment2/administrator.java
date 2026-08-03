import java.util.Scanner;

public class administrator extends user {

    private Scanner input = new Scanner(System.in);

    public administrator(String name) {
        super(name);
    }

    public void adminMenu(studentmanager manager) {

        while (true) {

            System.out.println("\n=====================================");
            System.out.println("        ADMINISTRATOR MENU");
            System.out.println("=====================================");

            System.out.println("1. Add Student");
            System.out.println("2. Search Student");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. View All Students");
            System.out.println("6. Back to Home");

            System.out.print("\nEnter choice: ");
            int choice = input.nextInt();

            switch (choice) {

                case 1:
                    addStudent(manager);
                    break;

                case 2:
                    searchStudent(manager);
                    break;

                case 3:
                    updateStudent(manager);
                    break;

                case 4:
                    deleteStudent(manager);
                    break;

                case 5:
                    manager.displayAllStudents();
                    break;

                case 6:
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // Add Student
    public void addStudent(studentmanager manager) {

        input.nextLine();

        System.out.print("Enter Student Name: ");
        String name = input.nextLine();

        System.out.print("Enter Age: ");
        int age = input.nextInt();

        input.nextLine();

        System.out.print("Enter Student ID: ");
        String id = input.nextLine();

        System.out.println("\nChoose Course");
        System.out.println("1. Bachelor of Computer Science");

        System.out.print("Choice: ");
        input.nextInt();

        input.nextLine();

        String courseName = "Bachelor of Computer Science";

        System.out.println("\nChoose Elective Course");
        System.out.println("1. Cybersecurity");
        System.out.println("2. Data Science");
        System.out.println("3. Artificial Intelligence");

        System.out.print("Choice: ");
        int electiveChoice = input.nextInt();

        String electiveCourse;

        switch (electiveChoice) {

            case 1:
                electiveCourse = "Cybersecurity";
                break;

            case 2:
                electiveCourse = "Data Science";
                break;

            case 3:
                electiveCourse = "Artificial Intelligence";
                break;

            default:
                System.out.println("Invalid choice. Cybersecurity selected.");
                electiveCourse = "Cybersecurity";
        }

        course course = new course(courseName, electiveCourse);

        student student = new student(name, age, id);
        student.setCourse(course);

        manager.addStudent(student);

        System.out.println("\nStudent added successfully!");
    }

    // Search Student
    public void searchStudent(studentmanager manager) {

        input.nextLine();

        System.out.print("Enter Student ID: ");
        String id = input.nextLine();

        student student = manager.searchStudent(id);

        if (student != null) {

            student.displayProfile();

        } else {

            System.out.println("Student not found.");

        }
    }

    // Update Student
    public void updateStudent(studentmanager manager) {

    input.nextLine();

    System.out.print("Enter Student ID: ");
    String id = input.nextLine();

    student student = manager.searchStudent(id);

    if (student == null) {
        System.out.println("Student not found.");
        return;
    }

    System.out.println("\nCurrent Elective: " + student.getCourse().getElectiveCourse());

    System.out.println("\nChoose New Elective");
    System.out.println("1. Cybersecurity");
    System.out.println("2. Data Science");
    System.out.println("3. Artificial Intelligence");

    System.out.print("Choice: ");
    int electiveChoice = input.nextInt();

    String newElective;

    switch (electiveChoice) {
        case 1: newElective = "Cybersecurity"; break;
        case 2: newElective = "Data Science"; break;
        case 3: newElective = "Artificial Intelligence"; break;
        default:
            System.out.println("Invalid choice. Update cancelled.");
            return;
    }

    if (manager.updateStudent(id, newElective)) {
        System.out.println("Elective updated successfully!");
    } else {
        System.out.println("Update failed.");
    }
}

    // Delete Student
    public void deleteStudent(studentmanager manager) {

        input.nextLine();

        System.out.print("Enter Student ID: ");
        String id = input.nextLine();

        if (manager.deleteStudent(id)) {

            System.out.println("Student deleted successfully!");

        } else {

            System.out.println("Student not found.");

        }
    }
}