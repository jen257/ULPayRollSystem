package Database_Module;

// This class covers all methods for database management: adding, deleting, backing up, and restoring data.

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.io.Writer;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

public class DatabaseUtility {

    // Variables section: holds data and other helper variables
    private List<String> data; // List to store data
    private DatabaseUtility DBU; // Helper for recursive calls in backup and restore
    private String line; // Line variable for reading CSV content
    private static final String DEFAULT_CSV_PATH = "src/resource/csv/";

    // =======================================================================
    // CsvReader part: Reading CSV files
    // =======================================================================

    // Reads a CSV file and returns a list of string arrays (each array corresponds to a line in the CSV)
    public void writeCsv(String filePath, List<String[]> data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Iterate through the data and write each row to the file
            for (String[] row : data) {
                writer.write(String.join(",", row)); // Join the columns with commas
                writer.newLine(); // Write a new line after each row
            }
        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
            throw e; // Rethrow the exception
        }
    }

    //Set default path for csv
    public String getDefaultCsvPath() {
        return DEFAULT_CSV_PATH;  // Return the path to the default CSV directory
    }
    public List<String[]> readCsv(String filePath) throws IOException{
        List<String[]> data = new ArrayList<>();  // Initialize the list to hold CSV data

        // Check if the file path is valid
        if (filePath == null || filePath.trim().isEmpty()) {
            filePath = DEFAULT_CSV_PATH;  // Default path
            System.err.println("Invalid file path: " + filePath);
            return data;  // Return empty list
        }

        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("File not found: " + filePath);
            return data;  // Return empty list if the file does not exist
        }

        // Try to read the CSV file using BufferedReader
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");  // Split by commas
                data.add(values);  // Add the parsed line to the data list
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }

        return data;  // Return the populated list (could be empty)
    }



    // =======================================================================
    // DataEditor (Add and Delete Data) part
    // =======================================================================

    // Adds a new data entry to the list
    public void addData(String newData) {
        if (newData != null && !newData.trim().isEmpty()) {
            data.add(newData); // Add the new data to the list
            System.out.println("Data added: " + newData);
        } else {
            System.out.println("Invalid data. Cannot be empty or null.");
        }
    }

    public void addDataToCsvFile(String filePath, String newData) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(newData);  // Write the new data to the CSV file
            writer.newLine();  // Add a new line after writing the data
        } catch (IOException e) {
            System.out.println("Error writing to the CSV file: " + e.getMessage());
        }
    }

    // Deletes a data entry from the list
    public void deleteData(String dataToDelete) {
        if (data.contains(dataToDelete)) { // Check if the data exists in the list
            data.remove(dataToDelete); // Remove the data from the list
            System.out.println("Data deleted: " + dataToDelete);
        } else {
            System.out.println("Data not found for deletion: " + dataToDelete);
        }
    }

    public void deleteDataFromCsvFile(String filePath, String dataToDelete) throws IOException {
        // Read the current data from the CSV file
        List<String[]> currentData = readCsv(filePath);

        // Create a list to hold rows that do not match the data to delete
        List<String[]> updatedData = new ArrayList<>();

        // Iterate through each row and add rows that don't match the data to delete
        for (String[] row : currentData) {
            String rowData = String.join(",", row);
            if (!rowData.equals(dataToDelete)) {
                updatedData.add(row);
            }
        }

        // If no matching data was found to delete
        if (currentData.size() == updatedData.size()) {
            System.out.println("No matching data found to delete.");
            return;
        }

        // Write the updated data back to the CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] row : updatedData) {
                writer.write(String.join(",", row));
                writer.newLine();
            }
        }

        System.out.println("Data has been deleted successfully.");
    }

    public void deleteRowsByName(String filePath, String nameToDelete) throws IOException {
        // Read the data from the CSV file
        List<String[]> csvData = readCsv(filePath);

        // Create a list to store rows that do not match the name to delete
        List<String[]> updatedData = new ArrayList<>();

        // Iterate over the rows and add rows that do not match the name to delete
        for (String[] row : csvData) {
            // Assuming the name is in the first column (modify this as needed)
            if (!row[0].equalsIgnoreCase(nameToDelete)) {
                updatedData.add(row);  // Add rows that do not match the name
            }
        }

        // Write the updated data back to the CSV file
        writeCsv(filePath, updatedData);
    }


    // =======================================================================
    // CSV Header Reading (for adding data) part
    // =======================================================================

    // Reads and prints the headers from a CSV file
    public List<String> getDataHeaders(String filePath) throws IOException {
        List<String> headers = new ArrayList<>();

        // Read the first row (header) of the CSV to identify the column names
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();  // Read the first line of the file
            if (line != null) {
                String[] columns = line.split(",");  // Split the row by commas to get individual columns
                headers.addAll(Arrays.asList(columns));  // Add the columns to the list
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Error reading the CSV file.");
        }

        return headers;  // Return the list of column headers
    }

    // =======================================================================
    // Backup part: Backup database files to another directory
    // =======================================================================

    // Backs up the data from the original source directory to the destination directory
    public static void BackupData(File originalDataPathFile, File destinationDataPathFiles) throws IOException {
        // Check if the destination folder exists, if not create it
        if (!destinationDataPathFiles.exists()) {
            destinationDataPathFiles.mkdirs();  // Use mkdirs() to create the directory
        }

        // Get all files and subdirectories in the source directory
        File[] files = originalDataPathFile.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // If it's a directory, call BackupData recursively to copy the subdirectory
                    BackupData(file, new File(destinationDataPathFiles, file.getName()));
                } else {
                    // If it's a file, copy the file to the destination directory
                    Files.copy(file.toPath(), new File(destinationDataPathFiles, file.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    // Extends the BackupData method, calling it with predefined source and destination paths
    public void BackupDataExtends() {
        // Handle backup logic internally, no need to pass the backup path
        String backupPath = getBackpath(); // Get the backup path inside the method
        System.out.println("Backing up data to: " + backupPath);
        // Perform backup logic...
    }

    // =======================================================================
    // Restore part: Restore database files from backup
    // =======================================================================

    // Restores the data from the backup directory to the original directory
    public static void RestoreData(File backupDataPathFile, File originalDataPathFiles) throws IOException {

        // Check if the original data path exists, if not create it
        if (!originalDataPathFiles.exists()) {
            originalDataPathFiles.mkdirs();  // Use mkdirs() to create the directory
        }

        // Get all files and subdirectories in the backup directory
        File[] files = backupDataPathFile.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {

                    // If it's a directory, call RestoreData recursively to restore the subdirectory
                    RestoreData(file, new File(originalDataPathFiles, file.getName()));
                } else {

                    // If it's a file, restore the file to the original directory
                    Files.copy(file.toPath(), new File(originalDataPathFiles, file.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }
    // Method to get the path where backup files are stored
    public String getBackpath() {
        // Define the backup directory (you can customize this path)
        String backupDir = "/path/to/your/backup/directory"; // Change this to your backup folder
        File folder = new File(backupDir);

        // If the backup folder doesn't exist, create it
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            if (created) {
                System.out.println("Backup directory created: " + backupDir);
            } else {
                System.out.println("Failed to create backup directory.");
            }
        }

        // Return the path of the backup folder
        return backupDir;
    }

    // Extends the RestoreData method, calling it with predefined backup and original paths
    public void RestoreDataExtends() {
        // Handle restore logic internally, no need to pass the backup path
        String backupPath = getBackpath(); // Get the backup path inside the method
        System.out.println("Restoring data from: " + backupPath);
        // Perform restore logic...
    }

}
