import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AdminDashboard extends JFrame {

    private StudentManager studentManager;
    private CourseManager courseManager;
    private TeacherManager teacherManager;
    private Admin admin;

    private DefaultTableModel courseTableModel;
    private DefaultTableModel studentTableModel;
    private DefaultTableModel requestTableModel;
    private DefaultTableModel teacherTableModel;

    public AdminDashboard(StudentManager studentManager, CourseManager courseManager, TeacherManager teacherManager, Admin admin) {

        this.studentManager = studentManager;
        this.courseManager = courseManager;
        this.teacherManager = teacherManager;
        this.admin = admin;

        setTitle("CampusHub - Administrator Dashboard");
        setSize(1050, 580);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Course Catalog", buildCoursesTab());
        tabs.addTab("Students", buildStudentsTab());
        tabs.addTab("Teachers", buildTeachersTab());
        tabs.addTab("Grade Requests", buildGradeRequestsTab());
        add(tabs, BorderLayout.CENTER);

        JButton backBtn = new JButton("Log Out");
        backBtn.addActionListener(e -> {
            dispose();
            new LoginFrame(studentManager, courseManager, teacherManager, admin);
        });
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(backBtn);
        add(bottom, BorderLayout.SOUTH);

        setVisible(true);
    }

    //Courses Tab 

    private JPanel buildCoursesTab() {

        JPanel panel = new JPanel(new BorderLayout());

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        JButton addBtn = new JButton("Add Subject");
        JButton removeBtn = new JButton("Remove Subject");
        JButton scheduleBtn = new JButton("Edit Schedule");
        JButton refreshBtn = new JButton("Refresh");
        toolbar.add(addBtn);
        toolbar.add(removeBtn);
        toolbar.add(scheduleBtn);
        toolbar.add(refreshBtn);
        panel.add(toolbar, BorderLayout.NORTH);

        String[] columns = {"Code", "Subject Name", "Day", "Time", "Seats"};
        courseTableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        JTable table = new JTable(courseTableModel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        refreshCourseTable();

        addBtn.addActionListener(e -> addCourseDialog());
        removeBtn.addActionListener(e -> removeCourseDialog());
        scheduleBtn.addActionListener(e -> editScheduleDialog());
        refreshBtn.addActionListener(e -> refreshCourseTable());

        return panel;
    }

    private void editScheduleDialog() {

        String code = JOptionPane.showInputDialog(this, "Enter Subject Code to reschedule:");
        if (code == null || code.trim().isEmpty()) return;

        CourseOffering course = courseManager.getCourse(code.trim());
        if (course == null) {
            JOptionPane.showMessageDialog(this, "Subject not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JComboBox<String> dayBox = new JComboBox<>(Scheduling.DAYS);
        JComboBox<String> timeBox = new JComboBox<>(Scheduling.TIME_SLOTS);
        dayBox.setSelectedItem(course.getDay());
        timeBox.setSelectedItem(course.getTimeSlot());

        Object[] message = {
            "Subject: " + course.getName() + " (currently " + course.getDay() + ", " + course.getTimeSlot() + ")",
            "New Day:", dayBox,
            "New Time:", timeBox
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Edit Schedule", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) return;

        String newDay = (String) dayBox.getSelectedItem();
        String newTime = (String) timeBox.getSelectedItem();

        String error = studentManager.updateCourseSchedule(code.trim(), newDay, newTime, courseManager);

        if (error == null) {
            refreshCourseTable();
            JOptionPane.showMessageDialog(this, "Schedule updated.");
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Schedule conflict - can't move this subject:\n" + error,
                "Conflict Detected",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void refreshCourseTable() {

        courseTableModel.setRowCount(0);

        for (CourseOffering c : courseManager.getAllCourses()) {
            courseTableModel.addRow(new Object[]{
                c.getCode(),
                c.getName(),
                c.getDay(),
                c.getTimeSlot(),
                (c.getCapacity() - c.getSeatsRemaining()) + " / " + c.getCapacity() + (c.isFull() ? "  [FULL]" : "")
            });
        }
    }

    private void addCourseDialog() {

        JTextField codeField = TextFieldFilters.alphanumericField(10);
        JTextField nameField = new JTextField();
        JComboBox<String> dayBox = new JComboBox<>(Scheduling.DAYS);
        JComboBox<String> timeBox = new JComboBox<>(Scheduling.TIME_SLOTS);
        JTextField capacityField = TextFieldFilters.digitsOnlyField(4);

        Object[] message = {
            "Subject Code (letters/numbers, e.g. CS101):", codeField,
            "Subject Name:", nameField,
            "Day:", dayBox,
            "Time:", timeBox,
            "Capacity:", capacityField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Subject", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) return;

        String code = codeField.getText().trim();
        String name = nameField.getText().trim();
        String day = (String) dayBox.getSelectedItem();
        String time = (String) timeBox.getSelectedItem();
        String capacityText = capacityField.getText().trim();

        String error = Validator.validateCourseCode(code);
        if (error == null) error = Validator.validateCourseName(name);
        if (error == null) error = Validator.validateCapacity(capacityText);

        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int capacity = Integer.parseInt(capacityText);

        if (courseManager.addCourse(code, name, day, time, capacity)) {
            refreshCourseTable();
            JOptionPane.showMessageDialog(this, "Subject added.");
        } else {
            JOptionPane.showMessageDialog(this, "A subject with that code already exists.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeCourseDialog() {

        String code = JOptionPane.showInputDialog(this, "Enter Subject Code to remove:");
        if (code == null || code.trim().isEmpty()) return;

        if (courseManager.removeCourse(code.trim())) {
            refreshCourseTable();
            JOptionPane.showMessageDialog(this, "Subject removed. Any students registered in it were unenrolled.");
        } else {
            JOptionPane.showMessageDialog(this, "Subject not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Students Tab 

    private JPanel buildStudentsTab() {

        JPanel panel = new JPanel(new BorderLayout());

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        JButton addBtn = new JButton("Add Student");
        JButton deleteBtn = new JButton("Delete Student");
        JButton electiveBtn = new JButton("Update Elective");
        JButton feesBtn = new JButton("Update Fees");
        JButton viewBtn = new JButton("View Profile");
        JButton refreshBtn = new JButton("Refresh");
        toolbar.add(addBtn);
        toolbar.add(deleteBtn);
        toolbar.add(electiveBtn);
        toolbar.add(feesBtn);
        toolbar.add(viewBtn);
        toolbar.add(refreshBtn);
        panel.add(toolbar, BorderLayout.NORTH);

        String[] columns = {"ID", "Name", "Email", "Age", "Elective", "Fees", "Grade Access"};
        studentTableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        JTable table = new JTable(studentTableModel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        refreshStudentTable();

        addBtn.addActionListener(e -> addStudentDialog());
        deleteBtn.addActionListener(e -> deleteStudentDialog());
        electiveBtn.addActionListener(e -> updateElectiveDialog());
        feesBtn.addActionListener(e -> updateFeesDialog());
        viewBtn.addActionListener(e -> viewRegistrationsDialog());
        refreshBtn.addActionListener(e -> refreshStudentTable());

        return panel;
    }

    private void refreshStudentTable() {

        studentTableModel.setRowCount(0);

        for (Student s : studentManager.getAllStudents()) {
            studentTableModel.addRow(new Object[]{
                s.getStudentID(),
                s.getName(),
                s.getEmail() == null ? "-" : s.getEmail(),
                s.getAge(),
                s.getElective() == null ? "-" : s.getElective(),
                formatFeeStatus(s),
                s.getGradeAccessStatus()
            });
        }
    }

    private String formatFeeStatus(Student s) {
        if (s.isFeePaid()) {
            return "Paid";
        }
        if (s.getFeeAmount() <= 0) {
            return "-";
        }
        return String.format("RM %.2f", s.getFeeAmount());
    }

    private void updateElectiveDialog() {

        String id = JOptionPane.showInputDialog(this, "Enter Student ID:");
        if (id == null || id.trim().isEmpty()) return;

        Student student = studentManager.searchStudent(id.trim());
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Student not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String currentElective = student.getElective() == null ? "(none)" : student.getElective();

        String newElective = (String) JOptionPane.showInputDialog(
            this,
            "Current elective: " + currentElective + "\nChoose new elective:",
            "Update Elective",
            JOptionPane.QUESTION_MESSAGE,
            null,
            Electives.ELECTIVE_NAMES,
            student.getElective()
        );

        if (newElective == null) return;

        String error = studentManager.changeElective(id.trim(), newElective, courseManager);

        refreshStudentTable();
        refreshCourseTable();

        if (error == null) {
            JOptionPane.showMessageDialog(this, "Elective updated to " + newElective + ".");
        } else {
            JOptionPane.showMessageDialog(this, "Could not switch elective:\n" + error, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateFeesDialog() {

        String id = JOptionPane.showInputDialog(this, "Enter Student ID:");
        if (id == null || id.trim().isEmpty()) return;

        Student student = studentManager.searchStudent(id.trim());
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Student not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField amountField = TextFieldFilters.decimalField(10);
        amountField.setText(student.getFeeAmount() > 0 ? String.valueOf(student.getFeeAmount()) : "");

        JCheckBox paidBox = new JCheckBox("Fees Paid", student.isFeePaid());

        Object[] message = {
            student.getName() + " (" + student.getStudentID() + ")",
            "Fee Amount (RM):", amountField,
            paidBox
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Update Fees", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) return;

        String amountText = amountField.getText().trim();
        String error = Validator.validateFeeAmount(amountText);

        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double amount = amountText.isEmpty() ? 0 : Double.parseDouble(amountText);
        studentManager.updateFees(id.trim(), amount, paidBox.isSelected());

        refreshStudentTable();
        JOptionPane.showMessageDialog(this, "Fees updated.");
    }

    private void addStudentDialog() {

        JTextField nameField = TextFieldFilters.nameField();
        JTextField ageField = TextFieldFilters.digitsOnlyField(3);
        JTextField idField = TextFieldFilters.alphanumericField(12);
        JTextField passwordField = TextFieldFilters.noSpacesField(30);
        JTextField emailField = TextFieldFilters.emailField(50);
        JTextField feeField = TextFieldFilters.decimalField(10);
        JComboBox<String> courseBox = new JComboBox<>(new String[]{Electives.COURSE_NAME});
        JComboBox<String> electiveBox = new JComboBox<>(Electives.ELECTIVE_NAMES);

        Object[] message = {
            "Name (letters only):", nameField,
            "Age:", ageField,
            "Student ID (letters/numbers, 3-12 chars):", idField,
            "Password (min 4 chars, no spaces):", passwordField,
            "Email:", emailField,
            "Course:", courseBox,
            "Elective:", electiveBox,
            "Fee Amount (RM, optional):", feeField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Student", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) return;

        String name = nameField.getText().trim();
        String ageText = ageField.getText().trim();
        String id = idField.getText().trim();
        String password = passwordField.getText();
        String email = emailField.getText().trim();
        String feeText = feeField.getText().trim();
        String elective = (String) electiveBox.getSelectedItem();

        String error = Validator.validateName(name);
        if (error == null) error = Validator.validateAge(ageText);
        if (error == null) error = Validator.validateStudentId(id);
        if (error == null) error = Validator.validatePassword(password);
        if (error == null) error = Validator.validateEmail(email);
        if (error == null) error = Validator.validateFeeAmount(feeText);

        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (studentManager.searchStudent(id) != null) {
            JOptionPane.showMessageDialog(this, "Student ID already exists. Choose a different ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int age = Integer.parseInt(ageText);
        double feeAmount = feeText.isEmpty() ? 0 : Double.parseDouble(feeText);

        Student student = new Student(name, age, id, password, email, null);
        student.setFeeAmount(feeAmount);
        studentManager.addStudent(student);

        String enrollError = studentManager.autoEnrollElective(id, elective, courseManager);

        refreshStudentTable();
        refreshCourseTable();

        if (enrollError == null) {
            student.setElective(elective);
            FileHandler.saveStudents(studentManager.getAllStudents());
            JOptionPane.showMessageDialog(this, "Student added and enrolled in " + elective + " subjects.");
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Student was added, but subject enrollment failed:\n" + enrollError +
                "\nYou can retry the elective assignment from Update Elective once space is available.",
                "Partial Success",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void deleteStudentDialog() {

        String id = JOptionPane.showInputDialog(this, "Enter Student ID to delete:");
        if (id == null || id.trim().isEmpty()) return;

        if (studentManager.deleteStudent(id.trim(), courseManager)) {
            refreshStudentTable();
            refreshCourseTable();
            JOptionPane.showMessageDialog(this, "Student deleted and unenrolled from all subjects.");
        } else {
            JOptionPane.showMessageDialog(this, "Student not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewRegistrationsDialog() {

        String id = JOptionPane.showInputDialog(this, "Enter Student ID:");
        if (id == null || id.trim().isEmpty()) return;

        Student student = studentManager.searchStudent(id.trim());
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Student not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        new StudentPortalFrame(studentManager, courseManager, teacherManager, admin, student);
    }

    // Grade Requests Tab 

    private JPanel buildGradeRequestsTab() {

        JPanel panel = new JPanel(new BorderLayout());

        JLabel note = new JLabel("  Paid students can view grades directly. Requests are only used for students who have not paid.");
        note.setFont(new Font("Arial", Font.ITALIC, 12));
        note.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        panel.add(note, BorderLayout.NORTH);

        String[] columns = {"ID", "Name", "Elective", "Fees", "Status"};
        requestTableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        JTable table = new JTable(requestTableModel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        JButton approveBtn = new JButton("Approve Selected");
        JButton rejectBtn = new JButton("Reject Selected");
        JButton refreshBtn = new JButton("Refresh");
        toolbar.add(approveBtn);
        toolbar.add(rejectBtn);
        toolbar.add(refreshBtn);
        panel.add(toolbar, BorderLayout.SOUTH);

        refreshRequestTable();

        approveBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a request from the table first.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String id = (String) requestTableModel.getValueAt(row, 0);
            studentManager.approveGradeRequest(id);
            refreshRequestTable();
            refreshStudentTable();
            JOptionPane.showMessageDialog(this, "Grade access approved for " + id + ".");
        });

        rejectBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a request from the table first.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String id = (String) requestTableModel.getValueAt(row, 0);
            studentManager.rejectGradeRequest(id);
            refreshRequestTable();
            refreshStudentTable();
            JOptionPane.showMessageDialog(this, "Grade access rejected for " + id + ".");
        });

        refreshBtn.addActionListener(e -> refreshRequestTable());

        return panel;
    }

    private void refreshRequestTable() {

        requestTableModel.setRowCount(0);

        for (Student s : studentManager.getPendingGradeRequests()) {
            requestTableModel.addRow(new Object[]{
                s.getStudentID(),
                s.getName(),
                s.getElective() == null ? "-" : s.getElective(),
                formatFeeStatus(s),
                s.getGradeAccessStatus()
            });
        }
    }

    // Teachers Tab 
    private JPanel buildTeachersTab() {

        JPanel panel = new JPanel(new BorderLayout());

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        JButton addBtn = new JButton("Add Teacher");
        JButton deleteBtn = new JButton("Delete Teacher");
        JButton assignBtn = new JButton("Assign Subject");
        JButton unassignBtn = new JButton("Unassign Subject");
        JButton refreshBtn = new JButton("Refresh");
        toolbar.add(addBtn);
        toolbar.add(deleteBtn);
        toolbar.add(assignBtn);
        toolbar.add(unassignBtn);
        toolbar.add(refreshBtn);
        panel.add(toolbar, BorderLayout.NORTH);

        String[] columns = {"Teacher ID", "Name", "Assigned Subjects"};
        teacherTableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        JTable table = new JTable(teacherTableModel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        refreshTeacherTable();

        addBtn.addActionListener(e -> addTeacherDialog());
        deleteBtn.addActionListener(e -> deleteTeacherDialog());
        assignBtn.addActionListener(e -> assignSubjectDialog());
        unassignBtn.addActionListener(e -> unassignSubjectDialog());
        refreshBtn.addActionListener(e -> refreshTeacherTable());

        return panel;
    }

    private void refreshTeacherTable() {

        teacherTableModel.setRowCount(0);

        for (Teacher t : teacherManager.getAllTeachers()) {

            StringBuilder subjects = new StringBuilder();
            for (String code : t.getAssignedSubjectCodes()) {
                CourseOffering c = courseManager.getCourse(code);
                subjects.append(c != null ? c.getName() : code).append("; ");
            }

            teacherTableModel.addRow(new Object[]{
                t.getTeacherID(),
                t.getName(),
                subjects.length() == 0 ? "(none)" : subjects.toString()
            });
        }
    }

    private void addTeacherDialog() {

        JTextField nameField = TextFieldFilters.nameField();
        JTextField idField = TextFieldFilters.alphanumericField(12);
        JTextField passwordField = TextFieldFilters.noSpacesField(30);

        Object[] message = {
            "Name (letters only):", nameField,
            "Teacher ID (letters/numbers, 3-12 chars):", idField,
            "Password (min 4 chars, no spaces):", passwordField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Teacher", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) return;

        String name = nameField.getText().trim();
        String id = idField.getText().trim();
        String password = passwordField.getText();

        String error = Validator.validateName(name);
        if (error == null) error = Validator.validateStudentId(id); // same ID rules apply
        if (error == null) error = Validator.validatePassword(password);

        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Teacher teacher = new Teacher(name, id, password);

        if (teacherManager.addTeacher(teacher)) {
            refreshTeacherTable();
            JOptionPane.showMessageDialog(this, "Teacher added.");
        } else {
            JOptionPane.showMessageDialog(this, "Teacher ID already exists. Choose a different ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteTeacherDialog() {

        String id = JOptionPane.showInputDialog(this, "Enter Teacher ID to delete:");
        if (id == null || id.trim().isEmpty()) return;

        if (teacherManager.deleteTeacher(id.trim())) {
            refreshTeacherTable();
            JOptionPane.showMessageDialog(this, "Teacher deleted.");
        } else {
            JOptionPane.showMessageDialog(this, "Teacher not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void assignSubjectDialog() {

        String id = JOptionPane.showInputDialog(this, "Enter Teacher ID:");
        if (id == null || id.trim().isEmpty()) return;

        Teacher teacher = teacherManager.searchTeacher(id.trim());
        if (teacher == null) {
            JOptionPane.showMessageDialog(this, "Teacher not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        java.util.List<CourseOffering> allCourses = courseManager.getAllCourses();
        if (allCourses.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No subjects exist yet in the catalog.", "Nothing to Assign", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] subjectLabels = new String[allCourses.size()];
        for (int i = 0; i < allCourses.size(); i++) {
            subjectLabels[i] = allCourses.get(i).getCode() + " - " + allCourses.get(i).getName();
        }

        JList<String> subjectList = new JList<>(subjectLabels);
        subjectList.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        subjectList.setVisibleRowCount(8);

        // Pre-select subjects already assigned to this teacher
        java.util.List<Integer> preselected = new java.util.ArrayList<>();
        for (int i = 0; i < allCourses.size(); i++) {
            if (teacher.teaches(allCourses.get(i).getCode())) {
                preselected.add(i);
            }
        }
        int[] preselectedArray = preselected.stream().mapToInt(Integer::intValue).toArray();
        subjectList.setSelectedIndices(preselectedArray);

        JScrollPane scrollPane = new JScrollPane(subjectList);

        Object[] message = {
            "Hold Ctrl (or Cmd) / Shift to select multiple subjects for " + teacher.getName() + ":",
            scrollPane
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Assign Subjects", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) return;

        java.util.List<String> selected = subjectList.getSelectedValuesList();

        if (selected.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No subjects were selected.", "Nothing Assigned", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (String label : selected) {
            String code = label.split(" - ")[0];
            teacherManager.assignSubject(id.trim(), code);
        }

        refreshTeacherTable();
        JOptionPane.showMessageDialog(this, selected.size() + " subject(s) assigned.");
    }

    private void unassignSubjectDialog() {

        String id = JOptionPane.showInputDialog(this, "Enter Teacher ID:");
        if (id == null || id.trim().isEmpty()) return;

        Teacher teacher = teacherManager.searchTeacher(id.trim());
        if (teacher == null) {
            JOptionPane.showMessageDialog(this, "Teacher not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (teacher.getAssignedSubjectCodes().isEmpty()) {
            JOptionPane.showMessageDialog(this, "This teacher has no assigned subjects.", "Nothing to Unassign", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] codes = teacher.getAssignedSubjectCodes().toArray(new String[0]);
        String[] subjectLabels = new String[codes.length];
        for (int i = 0; i < codes.length; i++) {
            CourseOffering c = courseManager.getCourse(codes[i]);
            subjectLabels[i] = c != null ? c.getCode() + " - " + c.getName() : codes[i];
        }

        String chosen = (String) JOptionPane.showInputDialog(
            this, "Remove which subject from " + teacher.getName() + "?", "Unassign Subject",
            JOptionPane.QUESTION_MESSAGE, null, subjectLabels, subjectLabels[0]
        );
        if (chosen == null) return;

        String code = chosen.split(" - ")[0];
        teacherManager.unassignSubject(id.trim(), code);

        refreshTeacherTable();
        JOptionPane.showMessageDialog(this, "Subject unassigned.");
    }
}
