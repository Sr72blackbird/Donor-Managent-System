import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManageStudentsForm extends JFrame {
    private JTextField admnoField, nameField, courseField;
    private JButton addButton, updateButton, deleteButton, viewButton;
    private JTable studentsTable;
    private DefaultTableModel tableModel;

    public ManageStudentsForm() {
        setTitle("Manage Students");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Input fields for student information
        admnoField = new JTextField(10);
        nameField = new JTextField(20);
        courseField = new JTextField(20);

        // Setup JTable
        String[] columnNames = {"ID", "Adm No", "Name", "Course"};
        tableModel = new DefaultTableModel(columnNames, 0);
        studentsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentsTable);

        // Buttons
        viewButton = new JButton("View Students");
        addButton = new JButton("Add Student");
        updateButton = new JButton("Update Student");
        deleteButton = new JButton("Delete Student");

        // Panel for input fields and buttons
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Adm No:"));
        inputPanel.add(admnoField);
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Course:"));
        inputPanel.add(courseField);

        // Add buttons to the panel
        inputPanel.add(viewButton);
        inputPanel.add(addButton);
        inputPanel.add(updateButton);
        inputPanel.add(deleteButton);

        // Main panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel);

        // Action Listeners for buttons
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadStudents();
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String admno = admnoField.getText();
                String name = nameField.getText();
                String course = courseField.getText();
                addStudent(admno, name, course);
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (studentsTable.getSelectedRow() != -1) {
                    int id = (int) studentsTable.getValueAt(studentsTable.getSelectedRow(), 0);
                    int admno = Integer.parseInt(admnoField.getText());
                    String name = nameField.getText();
                    String course = courseField.getText();
                    updateStudent(id, admno, name, course);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (studentsTable.getSelectedRow() != -1) {
                    int id = (int) studentsTable.getValueAt(studentsTable.getSelectedRow(), 0);
                    deleteStudent(id);
                }
            }
        });

        // Table selection listener to populate fields
        studentsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && studentsTable.getSelectedRow() != -1) {
                int selectedRow = studentsTable.getSelectedRow();
                admnoField.setText(studentsTable.getValueAt(selectedRow, 1).toString());
                nameField.setText(studentsTable.getValueAt(selectedRow, 2).toString());
                courseField.setText(studentsTable.getValueAt(selectedRow, 3).toString());
            }
        });

        setVisible(true);
    }

    // Load students into the table
    private void loadStudents() {
        List<Student> students = getStudents();

        // Clear the table
        tableModel.setRowCount(0);

        // Add students to the table
        for (Student student : students) {
            Object[] rowData = {
                    student.getId(),
                    student.getAdmno(),
                    student.getName(),
                    student.getCourse()
            };
            tableModel.addRow(rowData);
        }
    }

    // Method to add new student
    public void addStudent(String admno, String name, String course) {
        String sql = "INSERT INTO students(admno, name, course) VALUES(?, ?, ?)";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, admno);
            pstmt.setString(2, name);
            pstmt.setString(3, course);

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Student added successfully.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    //Method to view students
    public List<Student> getStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT id, admno, name, course FROM students";

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String admno = rs.getString("admno");
                String name = rs.getString("name");
                String course = rs.getString("course");
                students.add(new Student(id, admno, name, course));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return students;
    }

    //Method for updating student information
    public void updateStudent(int id, int admno, String name, String course) {
        String sql = "UPDATE students SET admno = ?, name = ?, course = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, admno);
            pstmt.setString(2, name);
            pstmt.setString(3, course);
            pstmt.setInt(4, id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Student updated successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Student not found.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    // Method for deleting information
    public void deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Student deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Student not found.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManageStudentsForm());
    }


}
