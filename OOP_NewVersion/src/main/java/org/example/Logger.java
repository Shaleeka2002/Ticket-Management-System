package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class Logger {

    private static final String JSON_FILE = "tickets.json";  // Path to JSON file
    private static final String TXT_FILE = "tickets.txt";    // Path to TXT file
    private static final Gson gson = new Gson();            // Gson instance for JSON handling

    // Write data to JSON file
    public static void writeToJsonFile(Map<String, Integer> data) throws IOException {
        try (Writer writer = new FileWriter(JSON_FILE)) {
            gson.toJson(data, writer);
            System.out.println("Data successfully written to JSON file.");
        } catch (IOException e) {
            System.err.println("Error writing to JSON file: " + e.getMessage());
            throw e;  // Rethrow exception after logging
        }
    }

    // Read data from JSON file
    public static Map<String, Integer> readFromJsonFile() throws IOException {
        File file = new File(JSON_FILE);
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                Type type = new TypeToken<Map<String, Integer>>() {}.getType();
                return gson.fromJson(reader, type);
            } catch (IOException e) {
                System.err.println("Error reading from JSON file: " + e.getMessage());
                throw e;  // Rethrow exception after logging
            }
        } else {
            System.out.println("No existing JSON file found, returning empty data.");
            return new HashMap<>(); // Return empty map if file does not exist
        }
    }

    // Write data to TXT file
    public static void writeToTxtFile(Map<String, Integer> data) throws IOException {
        try (FileWriter writer = new FileWriter(TXT_FILE)) {
            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue() + System.lineSeparator());
            }
            System.out.println("Data successfully written to TXT file.");
        } catch (IOException e) {
            System.err.println("Error writing to TXT file: " + e.getMessage());
            throw e;  // Rethrow exception after logging
        }
    }

    // Read data from TXT file
    public static Map<String, Integer> readFromTxtFile() throws IOException {
        File file = new File(TXT_FILE);
        Map<String, Integer> data = new HashMap<>();
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(": ");
                    if (parts.length == 2) {
                        data.put(parts[0], Integer.parseInt(parts[1]));
                    }
                }
                System.out.println("Data successfully read from TXT file.");
            } catch (IOException e) {
                System.err.println("Error reading from TXT file: " + e.getMessage());
                throw e;  // Rethrow exception after logging
            }
        } else {
            System.out.println("No existing TXT file found, returning empty data.");
        }
        return data;
    }
}
