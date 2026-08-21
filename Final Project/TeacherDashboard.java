import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TeacherDashboard extends JFrame {

    private StudentManager studentManager;
    private CourseManager courseManager;
    private TeacherManager teacherManager;
    private Admin admin;
    private Teacher teacher;

    private JComboBox<String> subjectBox;
    private DefaultTableModel studentTableModel;

    public TeacherDashboard(StudentManager studentManager, CourseManager courseManager,
                             TeacherManager teacherManager, Admin admin, Teacher teacher) {

        this.studentManager = studentManager;
        this.courseManager = courseManager;
        this.teacherManager = teacherManager;
        this.admin = admin;
        this.teacher = teacher;

        setTitle("Teacher Dashboard - " + teacher.getName());
        setSize(750, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        top.add(new JLabel("My Subject:"));

        List<String> assigned = teacher.getAssignedSubjectCodes();
        String[] subjectLabels;

        if (assigned.isEmpty()) {
            subjectLabels = new String[]{"(no subjects assigned yet)"};
        } else {
            subjectLabels = new String[assigned.size()];
            for (int i = 0; i < assigned.size(); i++) {
                CourseOffering c = courseManager.getCourse(assigned.get(i));
                subjectLabels[i] = c != null ? c.getCode() + " - " + c.getName() : assigned.get(i);
            }
        }

        subjectBox = new JComboBox<>(subjectLabels);
        top.add(subjectBox);

        JButton refreshBtn = new JButton("Refresh Class List");
        top.add(refreshBtn);

        add(top, BorderLayout.NORTH);

        String[] columns = {"Student ID", "Name", "Current Grade"};
        studentTableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        JTable table = new JTable(studentTableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refreshClassList();

        subjectBox.addActionListener(e -> refreshClassList());
        refreshBtn.addActionListener(e -> refreshClassList());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        JButton gradeBtn = new JButton("Enter / Update Grade for Selected Student");
        JButton logoutBtn = new JButton("Log Out");
        bottom.add(gradeBtn);
        bottom.add(logoutBtn);
        add(bottom, BorderLayout.SOUTH);

        gradeBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a student from the table first.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String studentId = (String) studentTableModel.getValueAt(row, 0);
            gradeSelectedStudent(studentId);
        });

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame(studentManager, courseManager, teacherManager, admin);
        });

        setVisible(true);
    }

    private String getSelectedSubjectCode() {

        List<String> assigned = teacher.getAssignedSubjectCodes();
        if (assigned.isEmpty()) return null;

        int index = subjectBox.getSelectedIndex();
        if (index < 0 || index >= assigned.size()) return null;

        return assigned.get(index);
    }

    private void refreshClassList() {

        studentTableModel.setRowCount(0);

        String subjectCode = getSelectedSubjectCode();
        if (subjectCode == null) return;

        for (Student s : studentManager.getAllStudents()) {
            if (s.isRegisteredIn(subjectCode)) {
                String grade = s.getGrades().get(subjectCode);
                studentTableModel.addRow(new Object[]{
                    s.getStudentID(),
                    s.getName(),
                    grade == null ? "Not graded yet" : grade
                });
            }
        }
    }

    private void gradeSelectedStudent(String studentId) {

        String subjectCode = getSelectedSubjectCode();
        if (subjectCode == null) return;

        Student student = studentManager.searchStudent(studentId);
        if (student == null) return;

        String grade = (String) JOptionPane.showInputDialog(
            this,
            "Grade for " + student.getName() + " (" + studentId + "):",
            "Enter Grade",
            JOptionPane.QUESTION_MESSAGE,
            null,
            Grades.LETTER_GRADES,
            Grades.LETTER_GRADES[0]
        );

        if (grade == null) return;

        studentManager.setGrade(studentId, subjectCode, grade);
        refreshClassList();
        JOptionPane.showMessageDialog(this, "Grade recorded.");
    }
}
