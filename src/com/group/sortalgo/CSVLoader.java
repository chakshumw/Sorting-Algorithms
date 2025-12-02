package com.group.sortalgo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVLoader {

    //Load CSV + parse rows into List<String[]>
    public static List<String[]> loadCSV(String filePath) throws IOException {
        List<String[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                // Skip empty lines safely (Requirement: Task 1)
                if (line.trim().isEmpty()) continue;

                // Basic CSV split (comma-separated)
                String[] parts = line.split(",");

                rows.add(parts);
            }
        }

        return rows;
    }
    
}
