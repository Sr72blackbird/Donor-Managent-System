import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    // Connect to SQLite database
    private static final String URL = "jdbc:sqlite:C:/Users/edwar/IdeaProjects/Donations Management System/DonationDB.sqlite";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }


    public static void createTables() throws SQLException {
        String createDonorsTable = "CREATE TABLE IF NOT EXISTS donors ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL,"
                + "email TEXT NOT NULL,"
                + "phone TEXT"
                + ");";

//        String createSchoolsTable = "CREATE TABLE IF NOT EXISTS schools ("
//                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
//                + "name TEXT NOT NULL,"
//                + "address TEXT NOT NULL"
//                + ");";

        String createStudentsTable = "CREATE TABLE IF NOT EXISTS students ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL,"
                + "admno TEXT NOT NULL ,"
                + "course TEXT NOT NULL"
                + ");";

        String createDonationsTable = "CREATE TABLE IF NOT EXISTS donations ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "donor_id INTEGER,"
                + "school_id INTEGER,"
                + "date TEXT NOT NULL,"
                + "FOREIGN KEY(donor_id) REFERENCES donors(id)"
                + ");";

        String createDonationItemsTable = "CREATE TABLE IF NOT EXISTS donation_items ("
                + "item_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "donation_id INTEGER,"
                + "student_id integer,"
                + "item_name TEXT NOT NULL,"
                + "quantity INTEGER NOT NULL,"
                + "donation_date TEXT,"
                + "FOREIGN KEY(donation_id) REFERENCES donations(id),"
                + "FOREIGN KEY(student_id) REFERENCES students(id)"
                + ");";

        try (Connection conn = connect();
             PreparedStatement stmt1 = conn.prepareStatement(createDonorsTable);
//             PreparedStatement stmt2 = conn.prepareStatement(createSchoolsTable);
             PreparedStatement stmt2 = conn.prepareStatement(createStudentsTable);
             PreparedStatement stmt3 = conn.prepareStatement(createDonationsTable);
             PreparedStatement stmt4 = conn.prepareStatement(createDonationItemsTable)) {
            stmt1.execute();
//            stmt2.execute();
            stmt2.execute();
            stmt3.execute();
            stmt4.execute();
        }
    }
    // Methods for CRUD operations on donors, schools, students, donations, and donation items

    // Fetch list of donors from the database
    public static List<Donor> getDonors() {
        List<Donor> donors = new ArrayList<>();
        String sql = "SELECT id, name, email, phone FROM donors";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            // Loop through the result set and create Donor objects
            while (rs.next()) {
                donors.add(new Donor(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")
                ));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return donors;
    }






}