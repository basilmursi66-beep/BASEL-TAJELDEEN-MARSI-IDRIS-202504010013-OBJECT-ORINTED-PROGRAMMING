import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private StudentManager studentManager;
    private CourseManager courseManager;
    private TeacherManager teacherManager;
    private Admin admin;

    public LoginFrame(StudentManager studentManager, CourseManager courseManager, TeacherManager teacherManager, Admin admin) {

        this.studentManager = studentManager;
        this.courseManager = courseManager;
        this.teacherManager = teacherManager;
        this.admin = admin;

        setTitle("CampusHub");
        setSize(440, 340);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("CampusHub", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(25, 10, 5, 10));
        add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());

        JLabel subtitle = new JLabel("Student Portal System", SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitle.setForeground(Color.GRAY);
        subtitle.setBorder(BorderFactory.createEmptyBorder(0, 10, 15, 10));
        centerPanel.add(subtitle, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 80, 20, 80));

        JButton adminBtn = new JButton("Administrator Login");
        JButton teacherBtn = new JButton("Teacher Login");
        JButton studentBtn = new JButton("Student Login");

        adminBtn.addActionListener(e -> adminLogin());
        teacherBtn.addActionListener(e -> teacherLogin());
        studentBtn.addActionListener(e -> studentLogin());

        buttonPanel.add(adminBtn);
        buttonPanel.add(teacherBtn);
        buttonPanel.add(studentBtn);

        centerPanel.add(buttonPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void adminLogin() {

        JTextField userField = TextFieldFilters.alphanumericField(30);
        JPasswordField passField = new JPasswordField();

        Object[] message = {
            "Username:", userField,
            "Password:", passField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Administrator Login", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {

            String username = userField.getText().trim();
            String password = new String(passField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter both a username and password.", "Missing Information", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (admin.authenticate(username, password)) {
                dispose();
                new AdminDashboard(studentManager, courseManager, teacherManager, admin);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid admin credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void teacherLogin() {

        JTextField idField = TextFieldFilters.alphanumericField(12);
        JPasswordField passField = new JPasswordField();

        Object[] message = {
            "Teacher ID:", idField,
            "Password:", passField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Teacher Login", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {

            String id = idField.getText().trim();
            String password = new String(passField.getPassword());

            if (id.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter both your Teacher ID and password.", "Missing Information", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Teacher teacher = teacherManager.login(id, password);

            if (teacher != null) {
                dispose();
                new TeacherDashboard(studentManager, courseManager, teacherManager, admin, teacher);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid teacher ID or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void studentLogin() {

        JTextField idField = TextFieldFilters.alphanumericField(12);
        JPasswordField passField = new JPasswordField();

        Object[] message = {
            "Student ID:", idField,
            "Password:", passField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Student Login", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {

            String id = idField.getText().trim();
            String password = new String(passField.getPassword());

            if (id.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter both your Student ID and password.", "Missing Information", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Student student = studentManager.login(id, password);

            if (student != null) {
                dispose();
                new StudentPortalFrame(studentManager, courseManager, teacherManager, admin, student);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid student ID or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
