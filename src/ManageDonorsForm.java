import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ManageDonorsForm extends JFrame {
    private JTextField nameField, emailField, phoneField;
    private JButton addButton, updateButton, deleteButton, viewButton;
    private JTable donorsTable;
    private DefaultTableModel tableModel;

    public ManageDonorsForm() {
        setTitle("Manage Donors");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Text fields for donor information
        nameField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(20);

        // Setup the JTable
        String[] columnNames = {"ID", "Name", "Email", "Phone"};
        tableModel = new DefaultTableModel(columnNames, 0);
        donorsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(donorsTable);

        // Buttons
        addButton = new JButton("Add Donor");
        viewButton = new JButton("View Donors");
        updateButton = new JButton("Update Donor");
        deleteButton = new JButton("Delete Donor");

        // Panel for donor information
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(emailField);
        inputPanel.add(new JLabel("Phone:"));
        inputPanel.add(phoneField);

        // Panel for buttons and table
        JPanel panel = new JPanel();
        panel.add(inputPanel);
        panel.add(addButton);
        panel.add(viewButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(scrollPane);

        add(panel);

        // Add Donor Button Listener{
        addButton.addActionListener(e -> addDonor());

        // View Donors Button Listener
        viewButton.addActionListener(e -> loadDonors());

        // Update and Delete Button Listeners
        updateButton.addActionListener(e -> {
            if (donorsTable.getSelectedRow() != -1) {
                int donorId = (int) donorsTable.getValueAt(donorsTable.getSelectedRow(), 0);
                updateDonor(donorId);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a donor to update.");
            }
        });

        deleteButton.addActionListener(e -> {
            if (donorsTable.getSelectedRow() != -1) {
                int donorId = (int) donorsTable.getValueAt(donorsTable.getSelectedRow(), 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this donor?");
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteDonor(donorId);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a donor to delete.");
            }
        });

        // Table selection listener to populate fields
        donorsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && donorsTable.getSelectedRow() != -1) {
                int selectedRow = donorsTable.getSelectedRow();
                nameField.setText((String) donorsTable.getValueAt(selectedRow, 1));
                emailField.setText((String) donorsTable.getValueAt(selectedRow, 2));
                phoneField.setText((String) donorsTable.getValueAt(selectedRow, 3));
            }
        });

        setVisible(true);
    }

// Method to add donor into sql database
    private void addDonor() {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();

        String sql = "INSERT INTO donors(name, email, phone) VALUES(?, ?, ?)";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, phone);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Donor added successfully");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    // Method to load all donors into the JTable
    private void loadDonors() {
        List<Donor> donors = DatabaseManager.getDonors();

        // Clear existing rows
        tableModel.setRowCount(0);

        // Add each donor to the table
        for (Donor donor : donors) {
            Object[] rowData = {
                    donor.getId(),
                    donor.getName(),
                    donor.getEmail(),
                    donor.getPhone()
            };
            tableModel.addRow(rowData);
        }
    }

    // Method to update a donor's information
    private void updateDonor(int donorId) {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();

        String sql = "UPDATE donors SET name = ?, email = ?, phone = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, phone);
            pstmt.setInt(4, donorId);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Donor updated successfully.");
                loadDonors(); // Refresh table
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update donor.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // Method to delete a donor by ID
    private void deleteDonor(int donorId) {
        String sql = "DELETE FROM donors WHERE id = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, donorId);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Donor deleted successfully.");
                loadDonors(); // Refresh table
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete donor.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManageDonorsForm());
    }
}
