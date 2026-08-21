import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentPortalFrame extends JFrame {

    private StudentManager studentManager;
    private CourseManager courseManager;
    private TeacherManager teacherManager;
    private Admin admin;
    private Student student;

    private JPanel gradesPanel;

    public StudentPortalFrame(StudentManager studentManager, CourseManager courseManager, TeacherManager teacherManager, Admin admin, Student student) {

        this.studentManager = studentManager;
        this.courseManager = courseManager;
        this.teacherManager = teacherManager;
        this.admin = admin;
        this.student = student;

        setTitle("Student Portal - " + student.getName());
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 8));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        infoPanel.add(boldLabel("Name:"));
        infoPanel.add(new JLabel(student.getName()));

        infoPanel.add(boldLabel("Student ID:"));
        infoPanel.add(new JLabel(student.getStudentID()));

        infoPanel.add(boldLabel("Age:"));
        infoPanel.add(new JLabel(String.valueOf(student.getAge())));

        infoPanel.add(boldLabel("Email:"));
        infoPanel.add(new JLabel(student.getEmail() == null ? "-" : student.getEmail()));

        infoPanel.add(boldLabel("Main Course:"));
        infoPanel.add(new JLabel(Electives.COURSE_NAME));

        infoPanel.add(boldLabel("Elective Course:"));
        infoPanel.add(new JLabel(student.getElective() == null ? "Not assigned yet" : student.getElective()));

        infoPanel.add(boldLabel("Fees:"));
        infoPanel.add(new JLabel(formatFeeStatus()));

        add(infoPanel, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("My Schedule", buildScheduleTab());
        tabs.addTab("My Grades", buildGradesTab());
        add(tabs, BorderLayout.CENTER);

        JButton logoutBtn = new JButton("Log Out");
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame(studentManager, courseManager, teacherManager, admin);
        });
        JPanel bottom = new JPanel();
        bottom.add(logoutBtn);
        add(bottom, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel buildScheduleTab() {

        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"Subject", "Day", "Time"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        for (String code : student.getRegisteredCourseCodes()) {
            CourseOffering course = courseManager.getCourse(code);
            if (course != null) {
                model.addRow(new Object[]{ course.getName(), course.getDay(), course.getTimeSlot() });
            }
        }

        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        return panel;
    }

    // Rebuilt each time since the status can change (request / approve / reject)
    private JPanel buildGradesTab() {

        gradesPanel = new JPanel(new BorderLayout());
        refreshGradesPanel();
        return gradesPanel;
    }

    private void refreshGradesPanel() {

        gradesPanel.removeAll();

        Student.GradeAccessStatus status = student.getGradeAccessStatus();

        // Students with no fee entered or paid fees can view their grades immediately.
        // A grade request is only required when an actual fee is outstanding.
        if (student.isFeePaid() || student.getFeeAmount() <= 0 || status == Student.GradeAccessStatus.APPROVED) {

            String[] columns = {"Subject", "Grade"};
            DefaultTableModel model = new DefaultTableModel(columns, 0) {
                public boolean isCellEditable(int row, int col) {
                    return false;
                }
            };

            for (String code : student.getRegisteredCourseCodes()) {
                CourseOffering course = courseManager.getCourse(code);
                String grade = student.getGrades().get(code);
                model.addRow(new Object[]{
                    course != null ? course.getName() : code,
                    grade == null ? "Not graded yet" : grade
                });
            }

            JTable table = new JTable(model);
            gradesPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        } else {

            JPanel statusPanel = new JPanel();
            statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
            statusPanel.setBorder(BorderFactory.createEmptyBorder(40, 30, 30, 30));

            String message;
            String buttonText = null;

            switch (status) {
                case NOT_REQUESTED:
                    message = "You haven't requested access to your grades yet.";
                    buttonText = "Request Grade Access";
                    break;
                case PENDING:
                    message = "Your request is pending admin review.";
                    break;
                case REJECTED:
                    message = "Grade access is currently unavailable. Once the outstanding fee is paid, your grades will appear automatically.";
                    buttonText = "Request Again";
                    break;
                default:
                    message = "";
            }

            JLabel statusLabel = new JLabel("<html><div style='width:400px;'>" + message + "</div></html>");
            statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            statusPanel.add(statusLabel);

            if (buttonText != null) {
                statusPanel.add(Box.createVerticalStrut(15));
                JButton requestBtn = new JButton(buttonText);
                requestBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
                requestBtn.addActionListener(e -> {
                    studentManager.requestGradeAccess(student.getStudentID());
                    refreshGradesPanel();
                });
                statusPanel.add(requestBtn);
            }

            gradesPanel.add(statusPanel, BorderLayout.NORTH);
        }

        gradesPanel.revalidate();
        gradesPanel.repaint();
    }

    private String formatFeeStatus() {
        if (student.isFeePaid()) {
            return "Paid";
        }
        if (student.getFeeAmount() <= 0) {
            return "-";
        }
        return String.format("RM %.2f", student.getFeeAmount());
    }

    private JLabel boldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        return label;
    }
}
