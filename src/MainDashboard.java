import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainDashboard extends JFrame {
    public MainDashboard() {
        setTitle("CTIE Donations Management System");
        setSize(1300, 900);
        setMinimumSize(new Dimension(800, 600));  // Optionally, set a minimum size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize components
        JButton manageDonorsButton = new JButton("Manage Donors");
        manageDonorsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ManageDonorsForm();
            }
        });

//        JButton manageSchoolsButton = new JButton("Manage Schools");
//        manageSchoolsButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                new ManageSchoolsForm();
//            }
//        });

        JButton manageStudentsButton = new JButton("Manage Students");
        manageStudentsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ManageStudentsForm();
            }
        });

        JButton manageDonationsButton = new JButton("Manage Donations");
        manageDonationsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new DonationItemsForm();
            }
        });

        JButton issueDonationsButton = new JButton("Issue Donations");
        issueDonationsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new IssueDonationsForm();
            }
        });

        // Add components to the frame
        JPanel panel = new JPanel();
        panel.add(manageDonorsButton);
        // panel.add(manageSchoolsButton);
        panel.add(manageStudentsButton);
        panel.add(manageDonationsButton);
        panel.add(issueDonationsButton);

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        // Show the splash screen on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> new MainDashboard());
    }
}
