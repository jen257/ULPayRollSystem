package Database_Module;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class DatabaseMenu {

    private static final DatabaseUtility dbUtility = new DatabaseUtility();

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n===== Database Management Menu =====");
            System.out.println("1. Add or Delete Data");
            System.out.println("2. Backup Data");
            System.out.println("3. Restore Data");
            System.out.println("4. View CSV File Data");
            System.out.println("5. Exit");

            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addOrDeleteData(scanner);  // Add or delete data in one case
                    break;
                case 2:
                    backupData();  // Backup data
                    break;
                case 3:
                    restoreData();  // Restore data
                    break;
                case 4:
                    viewCsvFile(scanner);  // View CSV data
                    break;
                case 5:
                    System.out.println("Exiting the system...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option, please try again!");
            }
        }
        scanner.close();
    }

    // Method to add or delete data (based on user's choice)
    private static void addOrDeleteData(Scanner scanner) throws IOException {
        System.out.println("Choose an option:");
        System.out.println("1. Add Data");
        System.out.println("2. Delete Data");

        int actionChoice = scanner.nextInt();
        scanner.nextLine();  // Clear the buffer

        switch (actionChoice) {
            case 1:
                addData(scanner);  // Add data to CSV file
                break;
            case 2:
                deleteData(scanner);  // Delete data from CSV file
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    // Method to add new data
    private static void addData(Scanner scanner) throws IOException {
        String defaultCsvPath = dbUtility.getDefaultCsvPath();
        File folder = new File(defaultCsvPath);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".csv"));

        if (files != null && files.length > 0) {
            System.out.println("\n===== List of CSV Files =====");
            for (int i = 0; i < files.length; i++) {
                System.out.println((i + 1) + ". " + files[i].getName());
            }

            boolean validChoice = false;
            while (!validChoice) {
                System.out.print("\nEnter the number of the CSV file you want to modify: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice >= 1 && choice <= files.length) {
                    validChoice = true;
                    String selectedFileName = files[choice - 1].getName();
                    String fullFilePath = defaultCsvPath + File.separator + selectedFileName;

                    displayCsvStructure(fullFilePath);

                    System.out.print("\nEnter data to add (comma-separated): ");
                    String newData = scanner.nextLine();

                    dbUtility.addDataToCsvFile(fullFilePath, newData);

                    System.out.println("\n===== Updated Structure of " + selectedFileName + " =====");
                    displayCsvStructure(fullFilePath);

                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            }
        } else {
            System.out.println("No CSV files found in the directory.");
        }
    }

    // Method to delete data
    private static void deleteData(Scanner scanner) throws IOException {
        String defaultCsvPath = dbUtility.getDefaultCsvPath();
        File folder = new File(defaultCsvPath);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".csv"));

        if (files != null && files.length > 0) {
            System.out.println("\n===== List of CSV Files =====");
            for (int i = 0; i < files.length; i++) {
                System.out.println((i + 1) + ". " + files[i].getName());
            }

            boolean validChoice = false;
            while (!validChoice) {
                System.out.print("\nEnter the number of the CSV file you want to delete data from: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice >= 1 && choice <= files.length) {
                    validChoice = true;
                    String selectedFileName = files[choice - 1].getName();
                    String fullFilePath = defaultCsvPath + File.separator + selectedFileName;

                    System.out.println("\n===== Structure of " + selectedFileName + " =====");
                    displayCsvStructure(fullFilePath);

                    System.out.print("\nEnter the name to delete data: ");
                    String nameToDelete = scanner.nextLine();

                    dbUtility.deleteRowsByName(fullFilePath, nameToDelete);

                    System.out.println("\n===== Updated Structure of " + selectedFileName + " =====");
                    displayCsvStructure(fullFilePath);

                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            }
        } else {
            System.out.println("No CSV files found in the directory.");
        }
    }

    // Method to backup data
    private static void backupData() {
        System.out.println("\n===== Backup Data =====");
        dbUtility.BackupDataExtends(); // No need to pass the backup path
    }

    // Method to restore data
    private static void restoreData() {
        System.out.println("\n===== Restore Data =====");
        dbUtility.RestoreDataExtends(); // No need to pass the backup path
    }

    // Method to view CSV file data
    public static void viewCsvFile(Scanner scanner) throws IOException {
        String defaultCsvPath = dbUtility.getDefaultCsvPath();
        File folder = new File(defaultCsvPath);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".csv"));

        if (files != null && files.length > 0) {
            System.out.println("\n===== List of CSV Files =====");
            for (int i = 0; i < files.length; i++) {
                System.out.println((i + 1) + ". " + files[i].getName());
            }

            boolean validChoice = false;
            while (!validChoice) {
                System.out.print("\nEnter the number of the CSV file you want to view: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice >= 1 && choice <= files.length) {
                    validChoice = true;
                    String selectedFileName = files[choice - 1].getName();
                    String fullPath = defaultCsvPath + selectedFileName;
                    System.out.println("You selected: " + selectedFileName);

                    displayCsvStructure(fullPath);
                    displayCsvData(fullPath);
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            }
        } else {
            System.out.println("No CSV files found in the directory.");
        }
    }

    private static void displayCsvStructure(String filePath) throws IOException {
        List<String> columns = dbUtility.getDataHeaders(filePath);

        if (columns.isEmpty()) {
            System.out.println("No structure found in the CSV file (no columns).");
        } else {
            System.out.println("\n===== Structure of " + filePath + " =====");
            for (String column : columns) {
                System.out.println(column);
            }
        }
    }

    private static void displayCsvData(String filePath) throws IOException {
        try {
            var data = dbUtility.readCsv(filePath);

            if (data != null && !data.isEmpty()) {
                System.out.println("\n===== CSV File Data =====");
                for (String[] row : data) {
                    System.out.println(String.join(", ", row));
                }
            } else {
                System.out.println("No data found in the CSV file.");
            }
        } catch (IOException e) {
            System.out.println("Error reading the CSV file: " + e.getMessage());
            throw e;
        }
    }
}
