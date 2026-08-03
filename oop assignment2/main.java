import java.util.Scanner;

public class main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        studentmanager manager = new studentmanager();
        administrator admin = new administrator("Administrator");

        while (true) {

            System.out.println("=====================================");
            System.out.println("     STUDENT MANAGEMENT SYSTEM");
            System.out.println("=====================================");

            System.out.println("\nWho are you?");
            System.out.println("1. Administrator");
            System.out.println("2. Student");
            System.out.println("3. Exit");

            System.out.print("\nEnter choice: ");
            int choice = input.nextInt();

            switch (choice) {

                case 1:
                    admin.adminMenu(manager);
                    break;

                case 2:

                    System.out.print("\nEnter Student ID: ");
                    String id = input.next();

                    student student = manager.searchStudent(id);

                    if(student != null){

                        student.displayProfile();

                    }
                    else{

                        System.out.println("Student not found.");

                    }

                    break;

                case 3:

                    System.out.println("Thank you!");

                    return;

                default:

                    System.out.println("Invalid choice.");
            }

        }

    }

}