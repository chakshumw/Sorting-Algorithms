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

    // Get column names from first row (header)
    public static List<String> getColumnNames(List<String[]> rows) {
        List<String> names = new ArrayList<>();
        if (rows == null || rows.isEmpty()) return names;

        String[] header = rows.get(0);
        for (String col : header) {
            names.add(col.trim());
        }
        return names;
    }

    // Detect which columns are fully numeric
    public static List<Integer> getNumericColumnIndices(List<String[]> rows) {
        List<Integer> numericCols = new ArrayList<>();
        if (rows == null || rows.size() < 2) return numericCols;

        int colCount = rows.get(0).length;

        for (int col = 0; col < colCount; col++) {
            boolean numeric = true;

            for (int row = 1; row < rows.size(); row++) {
                String value = rows.get(row)[col].trim();
                if (value.isEmpty()) {
                    numeric = false;
                    break;
                }
                try {
                    Double.parseDouble(value);
                } catch (NumberFormatException e) {
                    numeric = false;
                    break;
                }
            }

            if (numeric) {
                numericCols.add(col);
            }
        }
        return numericCols;
    }

    // Turn one numeric column into double[]
    public static double[] getColumnAsDoubleArray(List<String[]> rows, int colIndex) {
        if (rows == null || rows.size() < 2) return new double[0];

        double[] result = new double[rows.size() - 1]; // skip header

        for (int i = 1; i < rows.size(); i++) {
            result[i - 1] = Double.parseDouble(rows.get(i)[colIndex].trim());
        }
        return result;
    }
    
}
