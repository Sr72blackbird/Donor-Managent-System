import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class MainDashboard extends JFrame {
    public MainDashboard() {
        setTitle("Donations Management System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

        JPanel panel = new JPanel();
        panel.add(manageDonorsButton);
       // panel.add(manageSchoolsButton);
        panel.add(manageStudentsButton);
        panel.add(manageDonationsButton);

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainDashboard();
            }
        });
    }
}
