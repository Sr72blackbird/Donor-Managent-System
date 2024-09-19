import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DonationItemsForm extends JFrame {
    private JTextField itemNameField, quantityField, donorIdField, studentIdField, donationDateField;
    private JButton addButton, updateButton, deleteButton, viewButton;
    private JTable donationItemsTable;
    private DefaultTableModel tableModel;

    public DonationItemsForm() {
        setTitle("Manage Donation Items");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Input fields for donation item details
        itemNameField = new JTextField(20);
        quantityField = new JTextField(10);
        donorIdField = new JTextField(10);
        studentIdField = new JTextField(10);
        donationDateField = new JTextField(10);

        // Setup JTable
        String[] columnNames = {"Item ID", "Item Name", "Quantity", "Donor ID", "Student ID", "Donation Date"};
        tableModel = new DefaultTableModel(columnNames, 0);
        donationItemsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(donationItemsTable);

        // Buttons
        viewButton = new JButton("View Donation Items");
        addButton = new JButton("Add Donation Item");
        updateButton = new JButton("Update Donation Item");
        deleteButton = new JButton("Delete Donation Item");

        // Panel for input fields and buttons
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Item Name:"));
        inputPanel.add(itemNameField);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantityField);
        inputPanel.add(new JLabel("Donor ID:"));
        inputPanel.add(donorIdField);
        inputPanel.add(new JLabel("Student ID:"));
        inputPanel.add(studentIdField);
        inputPanel.add(new JLabel("Donation Date:"));
        inputPanel.add(donationDateField);

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
                loadDonationItems();
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String itemName = itemNameField.getText();
                int quantity = Integer.parseInt(quantityField.getText());
                int donorId = Integer.parseInt(donorIdField.getText());
                int studentId = Integer.parseInt(studentIdField.getText());
                String donationDate = donationDateField.getText();
                addDonationItem(itemName, quantity, donorId, studentId, donationDate);
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (donationItemsTable.getSelectedRow() != -1) {
                    int itemId = (int) donationItemsTable.getValueAt(donationItemsTable.getSelectedRow(), 0);
                    String itemName = itemNameField.getText();
                    int quantity = Integer.parseInt(quantityField.getText());
                    int donorId = Integer.parseInt(donorIdField.getText());
                    int studentId = Integer.parseInt(studentIdField.getText());
                    String donationDate = donationDateField.getText();
                    updateDonationItem(itemId, itemName, quantity, donorId, studentId, donationDate);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (donationItemsTable.getSelectedRow() != -1) {
                    int itemId = (int) donationItemsTable.getValueAt(donationItemsTable.getSelectedRow(), 0);
                    deleteDonationItem(itemId);
                }
            }
        });

        donationItemsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && donationItemsTable.getSelectedRow() != -1) {
                int selectedRow = donationItemsTable.getSelectedRow();
                itemNameField.setText(donationItemsTable.getValueAt(selectedRow, 1).toString());
                quantityField.setText(donationItemsTable.getValueAt(selectedRow, 2).toString());
                donorIdField.setText(donationItemsTable.getValueAt(selectedRow, 3).toString());
                studentIdField.setText(donationItemsTable.getValueAt(selectedRow, 4).toString());
                donationDateField.setText(donationItemsTable.getValueAt(selectedRow, 5).toString());
            }
        });

        setVisible(true);
    }

    // Load donation items into the table
    private void loadDonationItems() {
        List<DonationItem> items = getDonationItems();

        // Clear the table
        tableModel.setRowCount(0);

        // Add donation items to the table
        for (DonationItem item : items) {
            Object[] rowData = {
                    item.getItemId(),
                    item.getItemName(),
                    item.getQuantity(),
                    item.getDonorId(),
                    item.getStudentId(),
                    item.getDonationDate()
            };
            tableModel.addRow(rowData);
        }
    }


    // Method to Add a new Donation item
    public void addDonationItem(String itemName, int quantity, int donorId, int studentId, String donationDate) {
        String sql = "INSERT INTO donation_items(item_name, quantity, donor_id, student_id, donation_date) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, itemName);
            pstmt.setInt(2, quantity);
            pstmt.setInt(3, donorId);
            pstmt.setInt(4, studentId);
            pstmt.setString(5, donationDate);

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Donation item added successfully.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    // Method to view all Donation items
    public List<DonationItem> getDonationItems() {
        List<DonationItem> items = new ArrayList<>();
        String sql = "SELECT item_id, item_name, quantity, donor_id, student_id, donation_date FROM donation_items";

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int itemId = rs.getInt("item_id");
                String itemName = rs.getString("item_name");
                int quantity = rs.getInt("quantity");
                int donorId = rs.getInt("donor_id");
                int studentId = rs.getInt("student_id");
                String donationDate = rs.getString("donation_date");

                items.add(new DonationItem(itemId, itemName, quantity, donorId, studentId, donationDate));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return items;
    }

    // Update Donation Item
    public void updateDonationItem(int itemId, String itemName, int quantity, int donorId, int studentId, String donationDate) {
        String sql = "UPDATE donation_items SET item_name = ?, quantity = ?, donor_id = ?, student_id = ?, donation_date = ? WHERE item_id = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, itemName);
            pstmt.setInt(2, quantity);
            pstmt.setInt(3, donorId);
            pstmt.setInt(4, studentId);
            pstmt.setString(5, donationDate);
            pstmt.setInt(6, itemId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Donation item updated successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Donation item not found.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    // Delete Donation
    public void deleteDonationItem(int itemId) {
        String sql = "DELETE FROM donation_items WHERE item_id = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, itemId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Donation item deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Donation item not found.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DonationItemsForm());
    }
}
