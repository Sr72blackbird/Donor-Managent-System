import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IssueDonationsForm extends JFrame {
    private JComboBox<DonationItem> donationItemComboBox;
    private JComboBox<Student> studentComboBox;
    private JTextField issueQuantityField;
    private JButton issueButton;
    private JButton viewButton;

    private JTable issuedItemsTable;
    private DefaultTableModel issuedItemsTableModel;

    public IssueDonationsForm() {
        setTitle("Issue Donation Items to Students");
        setSize(1300, 900);
        setMinimumSize(new Dimension(800, 600));  // minimum size
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Combo boxes for selecting donation items and students
        donationItemComboBox = new JComboBox<>();
        studentComboBox = new JComboBox<>();

        // Load donation items and students into the combo boxes
        loadDonationItems();
        loadStudents();

        issueQuantityField = new JTextField(10);
        issueButton = new JButton("Issue Donation");
        viewButton = new JButton("View Issued Items");


        // Setup JTable for issued items
        String[] issuedItemsColumnNames = {"Issued ID", "Item Name", "Student Name", "Quantity Issued", "Issued Date"};
        issuedItemsTableModel = new DefaultTableModel(issuedItemsColumnNames, 0);
        issuedItemsTable = new JTable(issuedItemsTableModel);
        JScrollPane issuedItemsScrollPane = new JScrollPane(issuedItemsTable);

        // Panel for input fields and buttons
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Select Donation Item:"));
        inputPanel.add(donationItemComboBox);
        inputPanel.add(new JLabel("Select Student:"));
        inputPanel.add(studentComboBox);
        inputPanel.add(new JLabel("Quantity to Issue:"));
        inputPanel.add(issueQuantityField);
        inputPanel.add(issueButton);
        inputPanel.add(viewButton);


        // Main panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(issuedItemsScrollPane, BorderLayout.CENTER);

        add(panel);

        // Add functionality for the issue button
        issueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DonationItem selectedDonationItem = (DonationItem) donationItemComboBox.getSelectedItem();
                Student selectedStudent = (Student) studentComboBox.getSelectedItem();
                int quantityToIssue = Integer.parseInt(issueQuantityField.getText());

                // Validate selected donation item and student
                if (selectedDonationItem != null && selectedStudent != null) {
                    if (quantityToIssue <= selectedDonationItem.getQuantity()) {
                        issueDonationItem(selectedDonationItem, selectedStudent, quantityToIssue);
                    } else {
                        JOptionPane.showMessageDialog(null, "Insufficient quantity in stock.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a donation item and student.");
                }
            }
        });

        // Add functionality for the view button
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadIssuedItems();  // Refresh the table with the latest issued items
            }
        });

        // Load issued items when the form is initialized
        loadIssuedItems();

        setVisible(true);
    }

    // Method to load donation items into the ComboBox from the database
    private void loadDonationItems() {
        List<DonationItem> items = getDonationItems();
        for (DonationItem item : items) {
            donationItemComboBox.addItem(item);
        }
    }

    // Method to load students into the ComboBox from the database
    private void loadStudents() {
        List<Student> students = getStudents();
        for (Student student : students) {
            studentComboBox.addItem(student);
        }
    }

    // Method to load issued items into the JTable from the database
    private void loadIssuedItems() {
        List<IssuedItem> issuedItems = getIssuedItems();

        // Clear the table
        issuedItemsTableModel.setRowCount(0);

        // Add issued items to the table
        for (IssuedItem issuedItem : issuedItems) {
            Object[] rowData = {
                    issuedItem.getIssuedId(),
                    issuedItem.getItemName(),
                    issuedItem.getStudentName(),
                    issuedItem.getQuantityIssued(),
                    issuedItem.getIssuedDate()
            };
            issuedItemsTableModel.addRow(rowData);
        }
    }

    // Method to issue a donation item to a student
    private void issueDonationItem(DonationItem item, Student student, int quantityIssued) {
        String sqlUpdateDonationItem = "UPDATE donation_items SET quantity = quantity - ? WHERE item_id = ?";
        String sqlInsertIssuedItem = "INSERT INTO issued_items(item_id, student_id, issued_date, quantity_issued) VALUES(?, ?, CURRENT_DATE, ?)";

        try (Connection conn = DatabaseManager.connect()) {
            conn.setAutoCommit(false);  // Start transaction

            // Update donation_items table to reduce the quantity
            try (PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdateDonationItem)) {
                pstmtUpdate.setInt(1, quantityIssued);
                pstmtUpdate.setInt(2, item.getItemId());
                pstmtUpdate.executeUpdate();
            }

            // Insert into issued_items table
            try (PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsertIssuedItem)) {
                pstmtInsert.setInt(1, item.getItemId());
                pstmtInsert.setInt(2, student.getId());
                pstmtInsert.setInt(3, quantityIssued);
                pstmtInsert.executeUpdate();
            }

            conn.commit();  // Commit the transaction
            JOptionPane.showMessageDialog(null, "Item issued successfully.");

            // Refresh the issued items table after issuing
            loadIssuedItems();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    // Method to retrieve issued items from the database
    private List<IssuedItem> getIssuedItems() {
        List<IssuedItem> issuedItems = new ArrayList<>();
        String sql = "SELECT issued_id, donation_items.item_name, students.name AS student_name, quantity_issued, issued_date " +
                "FROM issued_items " +
                "JOIN donation_items ON issued_items.item_id = donation_items.item_id " +
                "JOIN students ON issued_items.student_id = students.id";

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int issuedId = rs.getInt("issued_id");
                String itemName = rs.getString("item_name");
                String studentName = rs.getString("student_name");
                int quantityIssued = rs.getInt("quantity_issued");
                String issuedDate = rs.getString("issued_date");

                issuedItems.add(new IssuedItem(issuedId, itemName, studentName, quantityIssued, issuedDate));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return issuedItems;
    }

    // Method to Retrieve Donation Items and Students from the Database
    // Method to get the list of donation items from the database
    public List<DonationItem> getDonationItems() {
        List<DonationItem> items = new ArrayList<>();
        String sql = "SELECT item_id, item_name, quantity,donor_id,donation_date FROM donation_items WHERE quantity > 0";

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int itemId = rs.getInt("item_id");
                String itemName = rs.getString("item_name");
                int quantity = rs.getInt("quantity");
                int donorId = rs.getInt("donor_id");
                String donationDate = rs.getString("donation_date");
                items.add(new DonationItem(itemId, itemName, quantity,donorId, donationDate ));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return items;
    }

    // Method to get the list of students from the database
    private List<Student> getStudents() {
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
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new IssueDonationsForm());
    }
}
