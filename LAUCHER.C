import java.io.File;

public class ApolloLauncher {

    public static void main(String[] args) {
        // Check if the correct number of arguments is provided
        if (args.length != 1) {
            System.err.println("Usage: java ApolloLauncher <path-to-csv-file>");
            System.exit(1);
        }

        String csvFilePath = args[0];

        // Validate the CSV file path
        File csvFile = new File(csvFilePath);
        if (!csvFile.exists() || !csvFile.isFile()) {
            System.err.println("Error: The file " + csvFilePath + " does not exist or is not a valid file.");
            System.exit(1);
        }

        // Call the method to read the CSV and insert into the database
        CSVToDatabase.readCSVAndInsertIntoDatabase(csvFilePath);
    }
}
