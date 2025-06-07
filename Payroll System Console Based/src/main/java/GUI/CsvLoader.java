package GUI;

/**
 *
 * @author kayejoanneangelikaplaza
 */

import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;

public class CsvLoader {
    
    //reads d csv from an inputstream n returns string array
    public static List<String[]> loadCsv(InputStream inputStream, boolean skipHeader, String delimiter) {
        List<String[]> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            boolean firstLine = true;
            
            //line by line
            while ((line = br.readLine()) != null) {
               
                // skipheader = true, skip 1st line
                if (firstLine && skipHeader) {
                    firstLine = false;
                    continue;
                }
                
                // comma usually
                String[] values = line.split(delimiter);
                data.add(values);
            }
            //print error 
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}
