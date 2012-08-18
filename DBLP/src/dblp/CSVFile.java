package dblp;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Martin Leinberger
 */
public class CSVFile {
    public class Line {
        private List<String> entries = new ArrayList<String>();
        
        public int getLineLength() {
            return entries.size();
        }
        
        public String getEntry(int pos) {
            return entries.get(pos);
        }
    }
    
    private List<Line> lines = new ArrayList<Line>();
    
    public CSVFile(File file) throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
           Line l = new Line();
           String[] splitted = line.split(";");
           for (String str : splitted)
               l.entries.add(str);
           
           lines.add(l);
        }
        reader.close();
    }
    
    public int getLength() {
        return lines.size();
    }
    
    
    public  Line getLine(int line) {
        return lines.get(line);
    }
    
    public List<Line> getLines() {
        return lines;
    }
}
