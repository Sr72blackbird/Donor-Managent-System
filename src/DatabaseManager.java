import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:DonationDB.db";

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
                + "school_id INTEGER,"
                + "grade TEXT NOT NULL"
                + ");";

        String createDonationsTable = "CREATE TABLE IF NOT EXISTS donations ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "donor_id INTEGER,"
                + "school_id INTEGER,"
                + "date TEXT NOT NULL,"
                + "FOREIGN KEY(donor_id) REFERENCES donors(id)"
                + ");";

        String createDonationItemsTable = "CREATE TABLE IF NOT EXISTS donation_items ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "donation_id INTEGER,"
                + "student_id INTEGER,"
                + "item_description TEXT NOT NULL,"
                + "quantity INTEGER NOT NULL,"
                + "FOREIGN KEY(donation_id) REFERENCES donations(id),"
                + "FOREIGN KEY(student_id) REFERENCES students(admno)"
                + ");";

        try (Connection conn = connect();
             PreparedStatement stmt1 = conn.prepareStatement(createDonorsTable);
//             PreparedStatement stmt2 = conn.prepareStatement(createSchoolsTable);
             PreparedStatement stmt3 = conn.prepareStatement(createStudentsTable);
             PreparedStatement stmt4 = conn.prepareStatement(createDonationsTable);
             PreparedStatement stmt5 = conn.prepareStatement(createDonationItemsTable)) {
            stmt1.execute();
//            stmt2.execute();
            stmt3.execute();
            stmt4.execute();
            stmt5.execute();
        }
    }

    // Methods for CRUD operations on donors, schools, students, donations, and donation items
}
