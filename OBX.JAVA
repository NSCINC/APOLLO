import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class InvestmentDatabase {
    
    private static final String DB_URL = "jdbc:sqlite:investments.db";

    public static void main(String[] args) {
        initializeDatabase();
        readCSVData("investors_data.csv");
    }

    // Initialize the database and create tables
    private static void initializeDatabase() {
        String createInvestorsTable = 
            "CREATE TABLE IF NOT EXISTS Investors (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT NOT NULL, " +
            "email TEXT NOT NULL, " +
            "phone_number TEXT NOT NULL);";

        String createAssetsTable = 
            "CREATE TABLE IF NOT EXISTS Assets (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "asset_name TEXT NOT NULL, " +
            "asset_type TEXT NOT NULL, " +
            "total_slots INTEGER NOT NULL, " +
            "annual_return REAL NOT NULL);";

        String createInvestmentsTable = 
            "CREATE TABLE IF NOT EXISTS Investments (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "investor_id INTEGER, " +
            "asset_id INTEGER, " +
            "invested_amount REAL, " +
            "FOREIGN KEY(investor_id) REFERENCES Investors(id), " +
            "FOREIGN KEY(asset_id) REFERENCES Assets(id));";

        try (Connection conn = DriverManager.getConnection(DB_URL); 
             Statement stmt = conn.createStatement()) {
             
            // Execute the SQL statements
            stmt.execute(createInvestorsTable);
            stmt.execute(createAssetsTable);
            stmt.execute(createInvestmentsTable);
            
            System.out.println("Database and tables initialized successfully.");

        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
        }
    }

    // Read investor data from a CSV file and print it
    private static void readCSVData(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    String name = data[0].trim();
                    String email = data[1].trim();
                    String phone = data[2].trim();

                    System.out.printf("Name: %s, Email: %s, Phone: %s%n", name, email, phone);

                    // Here you can insert data into the database
                    // Example:
                    // insertInvestor(name, email, phone);
                }
            }
        } catch (IOException e) {
            System.err.println("Could not open file: " + e.getMessage());
        }
    }

    // Uncomment and implement this method to insert investor data into the database
    /*
    private static void insertInvestor(String name, String email, String phone) {
        String insertInvestorSQL = "INSERT INTO Investors (name, email, phone_number) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL); 
             PreparedStatement pstmt = conn.prepareStatement(insertInvestorSQL)) {
             
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, phone);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
        }
    }
    */
}
