package Database_Module;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class DatabaseMenu {

    // Create an instance of DatabaseUtility to interact with the database
    private static final DatabaseUtility dbUtility = new DatabaseUtility();

    public static void main(String[] args) throws IOException {
        // Create a Scanner object for user input
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            // Display the main menu for database management
            System.out.println("\n===== Database Management Menu =====");
            System.out.println("1. Add Data");
            System.out.println("2. Delete Data");
            System.out.println("3. Backup Data");
            System.out.println("4. Restore Data");
            System.out.println("5. View CSV File Data");
            System.out.println("6. Exit");

            // Prompt the user to choose an option
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Clear the buffer

            // Execute the corresponding action based on the user's choice
            switch (choice) {
                case 1:
                    addData(scanner);  // Add new data to the database
                    break;
                case 2:
                    deleteData(scanner);  // Delete data from the database
                    break;
                case 3:
                    backupData();  // Backup the current data
                    break;
                case 4:
                    restoreData();  // Restore data from a backup
                    break;
                case 5:
                    viewCsvFile(scanner);  // View the data from a CSV file
                    break;
                case 6:
                    System.out.println("Exiting the system...");  // Exit the application
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option, please try again!");  // Invalid input handling
            }
        }
        scanner.close();  // Close the scanner object after use
    }

    // Method to add new data to the database
    // After choosing show the number of every csv file and show their structure

    private static void addData(Scanner scanner) throws IOException {
        // List all available CSV files for the user to choose from
        String defaultCsvPath = dbUtility.getDefaultCsvPath();  // Assuming this is the directory containing CSV files
        File folder = new File(defaultCsvPath);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".csv"));

        if (files != null && files.length > 0) {
            System.out.println("\n===== List of CSV Files =====");

            // Display the list of CSV files with an index
            for (int i = 0; i < files.length; i++) {
                System.out.println((i + 1) + ". " + files[i].getName());
            }

            boolean validChoice = false;
            while (!validChoice) {
                System.out.print("\nEnter the number of the CSV file you want to modify: ");
                int choice = scanner.nextInt();  // Get user choice
                scanner.nextLine();  // Consume the newline character

                if (choice >= 1 && choice <= files.length) {
                    validChoice = true;  // Set the flag to true to exit the loop
                    String selectedFileName = files[choice - 1].getName();  // Get the selected file name
                    String fullFilePath = defaultCsvPath + selectedFileName;  // Construct full file path

                    displayCsvStructure(fullFilePath);  // Display only the header (class/columns)

                    // Prompt the user to add new data
                    System.out.print("\nEnter data to add (comma-separated): ");
                    String newData = scanner.nextLine();

                    // Add the new data to the selected CSV file
                    dbUtility.addDataToCsvFile(fullFilePath, newData);

                    // Optionally, display the structure again after the modification
                    System.out.println("\n===== Updated Structure of " + selectedFileName + " =====");
                    displayCsvStructure(fullFilePath);  // Display structure again

                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            }
        } else {
            System.out.println("No CSV files found in the directory.");
        }
    }



    // Method to delete data from the database
    // After choosing show the number of every csv file and show their structure
    private static void deleteData(Scanner scanner) throws IOException {
        // List all available CSV files for the user to choose from
        String defaultCsvPath = dbUtility.getDefaultCsvPath();
        File folder = new File(defaultCsvPath);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".csv"));

        if (files != null && files.length > 0) {
            System.out.println("\n===== List of CSV Files =====");

            // Display the list of CSV files with an index
            for (int i = 0; i < files.length; i++) {
                System.out.println((i + 1) + ". " + files[i].getName());
            }

            boolean validChoice = false;
            while (!validChoice) {
                System.out.print("\nEnter the number of the CSV file you want to delete data from: ");
                int choice = scanner.nextInt();  // Get user choice
                scanner.nextLine();  // Consume the newline character

                if (choice >= 1 && choice <= files.length) {
                    validChoice = true;  // Set the flag to true to exit the loop
                    String selectedFileName = files[choice - 1].getName();  // Get the selected file name
                    String fullFilePath = defaultCsvPath + File.separator + selectedFileName;  // Construct full file path

                    // Display the structure (first row) of the file instead of all data
                    System.out.println("\n===== Structure of " + selectedFileName + " =====");
                    displayCsvStructure(fullFilePath);  // Display only the header (class/columns)

                    // Prompt the user to enter the name to delete data
                    System.out.print("\nEnter the name to delete data: ");
                    String nameToDelete = scanner.nextLine();

                    // Call a method to delete rows where the name matches
                    dbUtility.deleteRowsByName(fullFilePath, nameToDelete);

                    // Optionally, display the updated structure after deletion
                    System.out.println("\n===== Updated Structure of " + selectedFileName + " =====");
                    displayCsvStructure(fullFilePath);  // Display structure again

                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            }
        } else {
            System.out.println("No CSV files found in the directory.");
        }
    }



    // Method to back up the current data in the database
    private static void backupData() {
        System.out.println("\n===== Backup Data =====");
        dbUtility.BackupDataExtends();  // Call the BackupDataExtends method of DatabaseUtility to back up data
    }

    // Method to restore data from a backup
    private static void restoreData() {
        System.out.println("\n===== Restore Data =====");
        dbUtility.RestoreDataExtends();  // Call the RestoreDataExtends method of DatabaseUtility to restore data
    }

    // Method to view CSV file data
    public static void viewCsvFile(Scanner scanner) throws IOException {
        // Retrieve the default CSV directory path from DatabaseUtility
        String defaultCsvPath = dbUtility.getDefaultCsvPath();  // Assuming getDefaultCsvPath() is defined in DatabaseUtility

        // Create a File object to represent the CSV directory
        File folder = new File(defaultCsvPath);

        // List all files in the directory that end with ".csv"
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".csv"));

        // Check if any CSV files are found in the directory
        if (files != null && files.length > 0) {
            System.out.println("\n===== List of CSV Files =====");

            // Display the list of CSV files in the directory
            for (int i = 0; i < files.length; i++) {
                System.out.println((i + 1) + ". " + files[i].getName());  // List the CSV files with an index number
            }

            boolean validChoice = false;
            while (!validChoice) {
                // Prompt the user to select a CSV file to view
                System.out.print("\nEnter the number of the CSV file you want to view: ");
                int choice = scanner.nextInt();  // Get the user's choice of file
                scanner.nextLine();  // Consume the newline character

                // Validate the user's choice and display the selected CSV file's data
                if (choice >= 1 && choice <= files.length) {
                    validChoice = true;  // Set the flag to true to exit the loop

                    String selectedFileName = files[choice - 1].getName();  // Get the name of the selected file
                    String fullPath = defaultCsvPath + selectedFileName;  // Construct the full path of the file
                    System.out.println("You selected: " + selectedFileName);

                    // Display the structure (column headers) of the CSV file
                    displayCsvStructure(fullPath);

                    // Display the data of the selected CSV file
                    displayCsvData(fullPath);  // Call the displayCsvData method to display the CSV file's content
                } else {
                    System.out.println("Invalid choice. Please try again.");  // Handle invalid input
                }
            }
        } else {
            System.out.println("No CSV files found in the directory.");  // Handle case where no CSV files are found
        }
    }

    // Method to display the structure (column headers) of a CSV file
    private static void displayCsvStructure(String filePath) throws IOException {
        // Retrieve the column headers (structure) of the CSV file
        List<String> columns = dbUtility.getDataHeaders(filePath);

        // Display the structure (column names) of the CSV file
        if (columns.isEmpty()) {
            System.out.println("No structure found in the CSV file (no columns).");
        } else {
            System.out.println("\n===== Structure of " + filePath + " =====");
            for (String column : columns) {
                System.out.println(column);  // Print each column (header)
            }
        }
    }



    // Method to display the data from a CSV file
    private static void displayCsvData(String filePath) throws IOException { // Declare that the method can throw IOException
        try {
            // Call the readCsv method from DatabaseUtility to read the contents of the selected CSV file
            var data = dbUtility.readCsv(filePath);

            // Check if the CSV file contains any data
            if (data != null && !data.isEmpty()) {
                System.out.println("\n===== CSV File Data =====");
                // Print each row from the CSV file
                for (String[] row : data) {
                    System.out.println(String.join(", ", row));  // Join the elements of the row with commas and print it
                }
            } else {
                System.out.println("No data found in the CSV file.");  // Handle case where the CSV file is empty
            }
        } catch (IOException e) {
            // Handle any exceptions that occur while reading the CSV file
            System.out.println("Error reading the CSV file: " + e.getMessage());
            throw e; // Rethrow the exception to propagate it
        }
    }



}
