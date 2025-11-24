import java.io.*;
import java.util.*;

public class CSVLoader {
    
    // Task 1: Implement CSV file loading & parsing
    public static List<String[]> loadCSV(String filePath) throws IOException {
        List<String[]> rows = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        
        while ((line = br.readLine()) != null) {
            String[] row = line.split(",");
            rows.add(row);
        }
        br.close();
        return rows;
    }
