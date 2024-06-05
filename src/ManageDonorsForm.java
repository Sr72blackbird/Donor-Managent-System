import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;




public class ManageDonorsForm extends JFrame {
    private JTextField nameField, emailField, phoneField;
    private JButton addButton, updateButton, deleteButton, viewButton;

    public ManageDonorsForm() {
        setTitle("Manage Donors");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        nameField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(20);

        addButton = new JButton("Add Donor");
        updateButton = new JButton("Update Donor");
        deleteButton = new JButton("Delete Donor");
        viewButton = new JButton("View Donors");

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addDonor();
            }
        });

        // Implement action listeners for update, delete, and view buttons

        JPanel panel = new JPanel();
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(viewButton);

        add(panel);
        setVisible(true);
    }

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

    // Implement methods for updateDonor, deleteDonor, and viewDonors
}
