import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CSVToDatabase {

    private static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/your_database";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";

    public static void readCSVAndInsertIntoDatabase(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {

            String line;
            String insertSQL = "INSERT INTO users (name, email, token) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertSQL);

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length == 3) { // Ensure that there are exactly three columns
                    String name = data[0].trim();
                    String email = data[1].trim();
                    String token = data[2].trim();

                    pstmt.setString(1, name);
                    pstmt.setString(2, email);
                    pstmt.setString(3, token);

                    pstmt.executeUpdate();
                    System.out.println("Dados inseridos: Nome: " + name + ", Email: " + email + ", Token: " + token);
                } else {
                    System.err.println("Linha com formato inválido: " + line);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro de conexão com o banco de dados: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo CSV: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        readCSVAndInsertIntoDatabase("investors_data.csv");
    }
}
