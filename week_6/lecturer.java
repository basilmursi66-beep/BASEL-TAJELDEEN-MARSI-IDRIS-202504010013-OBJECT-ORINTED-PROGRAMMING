package week_6;

public class lecturer {
    
    private String subject;
    private String department;

        public lecturer(String id, String name, String subject, String department) {
        
        this.subject = subject;
        this.department = department;
    }

    

    public void displaySubject() {
        System.out.println("Subject     : " + subject);
    }

    public void displayDepartment() {
        System.out.println("Department    : " + department);
    }
}

