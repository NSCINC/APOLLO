import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// Classe para lidar com investidores
class Investor {
    private String name;
    private String email;
    private String phoneNumber;

    public Investor(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    // Getters
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
}

// Conexão com o banco de dados (simples, mas pode ser distribuído com Ignite)
public class InvestorDatabase {
    private Connection connect() {
        String url = "jdbc:sqlite:sea_lake_investments.db"; // Aqui pode-se usar um banco distribuído
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS Investors ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "name TEXT NOT NULL,"
                    + "email TEXT NOT NULL,"
                    + "phone_number TEXT NOT NULL);";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Inserir investidores na base
    public void insertInvestor(Investor investor) {
        String sql = "INSERT INTO Investors(name, email, phone_number) VALUES(?, ?, ?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, investor.getName());
            pstmt.setString(2, investor.getEmail());
            pstmt.setString(3, investor.getPhoneNumber());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Ler dados do arquivo CSV
    public void readCsvData(String filePath) {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Investor investor = new Investor(values[0], values[1], values[2]);
                insertInvestor(investor);
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        InvestorDatabase db = new InvestorDatabase();
        db.initializeDatabase();
        db.readCsvData("investors_data.csv"); // O caminho do arquivo CSV
    }
}
