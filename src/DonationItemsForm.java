import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DonationItemsForm extends JFrame {
    private JTextField itemNameField, quantityField, donationDateField;
    private JComboBox<Donor> donorComboBox;  // Combo box for selecting donors
    private JButton addButton, updateButton, deleteButton, viewButton;
    private JTable donationItemsTable;
    private DefaultTableModel tableModel;

    public DonationItemsForm() {
        setTitle("Manage Donation Items");
        setSize(1300, 900);
        setMinimumSize(new Dimension(800, 600));  // Optionally, set a minimum size
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Input fields for donation item details
        itemNameField = new JTextField(20);
        quantityField = new JTextField(10);
        donationDateField = new JTextField(10);
        donorComboBox = new JComboBox<>();  // ComboBox to display donors

        // Setup JTable
        String[] columnNames = {"Item ID", "Item Name", "Quantity", "Donor ID", "Donation Date"};
        tableModel = new DefaultTableModel(columnNames, 0);
        donationItemsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(donationItemsTable);

        // Buttons
        viewButton = new JButton("View Donation Items");
        addButton = new JButton("Add Donation Item");
        updateButton = new JButton("Update Donation Item");
        deleteButton = new JButton("Delete Donation Item");

        // Load donors into the ComboBox
        loadDonors();

        // Panel for input fields and buttons
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Item Name:"));
        inputPanel.add(itemNameField);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantityField);
        inputPanel.add(new JLabel("Donor:"));
        inputPanel.add(donorComboBox);  // Adding the donorComboBox to the form
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
                Donor selectedDonor = (Donor) donorComboBox.getSelectedItem(); // Get the selected donor from the ComboBox
                int donorId = selectedDonor.getId();  // Get the donor's ID
                String donationDate = donationDateField.getText();
                addDonationItem(itemName, quantity, donorId, donationDate);
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (donationItemsTable.getSelectedRow() != -1) {
                    String itemName = itemNameField.getText();
                    int quantity = Integer.parseInt(quantityField.getText());
                    Donor selectedDonor = (Donor) donorComboBox.getSelectedItem();
                    int donorId = selectedDonor.getId();
                    String donationDate = donationDateField.getText();
                    addDonationItem(itemName, quantity, donorId, donationDate);
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
                donationDateField.setText(donationItemsTable.getValueAt(selectedRow, 4).toString());

                // Set the donorComboBox to the correct donor based on the donorId
                int donorId = (int) donationItemsTable.getValueAt(selectedRow, 3);
                selectDonorInComboBox(donorId);
            }
        });

        setVisible(true);
    }

    // Method to load donors into the ComboBox from the database
    private void loadDonors() {
        List<Donor> donors = getDonors();  // Retrieve the list of donors from the database
        for (Donor donor : donors) {
            donorComboBox.addItem(donor);  // Add each donor to the ComboBox
        }
    }

    // Method to select the correct donor in the ComboBox based on donor ID
    private void selectDonorInComboBox(int donorId) {
        for (int i = 0; i < donorComboBox.getItemCount(); i++) {
            Donor donor = donorComboBox.getItemAt(i);
            if (donor.getId() == donorId) {
                donorComboBox.setSelectedIndex(i);
                break;
            }
        }
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
                    item.getDonationDate()
            };
            tableModel.addRow(rowData);
        }
    }

    // Add a new donation item to the database
    private void addDonationItem(String itemName, int quantity, int donorId, String donationDate) {
        String sql = "INSERT INTO donation_items(item_name, quantity, donor_id, donation_date) VALUES(?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, itemName);
            pstmt.setInt(2, quantity);
            pstmt.setInt(3, donorId);
            pstmt.setString(4, donationDate);

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Donation item added successfully.");
            loadDonationItems();  // Refresh the table after adding
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    // Delete Donation from database Method
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

    // Method to load all Donation items
    public List<DonationItem> getDonationItems() {
        List<DonationItem> items = new ArrayList<>();
        String sql = "SELECT item_id, item_name, quantity, donor_id, donation_date FROM donation_items";

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int itemId = rs.getInt("item_id");
                String itemName = rs.getString("item_name");
                int quantity = rs.getInt("quantity");
                int donorId = rs.getInt("donor_id");
                String donationDate = rs.getString("donation_date");

                items.add(new DonationItem(itemId, itemName, quantity, donorId, donationDate));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return items;
    }
    //

    public List<Donor> getDonors() {
        List<Donor> donors = new ArrayList<>();
        String sql = "SELECT id, name, email, phone FROM donors";

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                donors.add(new Donor(id, name, email, phone));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return donors;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DonationItemsForm());
    }
}
