package dblp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import org.xml.sax.SAXException;

/**
 *
 * @author Martin Leinberger
 */
public class DBLP {
    private static Set<String> participants = new HashSet<String>();
    private static HashMap<Author, Set<String>> workedWith = new HashMap<Author, Set<String>>();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, SAXException {
        if (args.length != 2) {
            System.out.println("Expecting 2 parameters - inputFile(.csv) and outputFile(.dot)");
            System.exit(1);
        }
        String input = args[0];
        String output = args[1];

        //searching for the authors
        CSVFile file = new CSVFile(new File(input)); 
        System.out.println("gathering initial author information");
        for (CSVFile.Line line : file.getLines()) {
            String name = line.getEntry(0).replace("﻿", "") + " " + line.getEntry(1);
            List<Author> searchResults = Author.searchAuthors(name);
            if (searchResults.size() > 0) {
                participants.add(name);
                for (Author author : searchResults)
                    if (author.getName().equals(name))
                        workedWith.put(author, new HashSet<String>());
            }
        }
        
        //gathering all information about each publication every author wrote according to DBLP
        System.out.println("gathering publication information");
        for (Author participant : workedWith.keySet()) {
            List<String> publicationKeys = participant.getPublicationKeys();
            for (String pubKey : publicationKeys) {
                Publication publication = new Publication(pubKey);
                workedWith.get(participant).addAll(publication.getAuthors());
            }
        }
        
        //Serializing the result as a .dot file
        System.out.println("serializing result");
        FileWriter writer = new FileWriter(output);
        writer.write("digraph G {\n");
        for (Map.Entry<Author, Set<String>> entry : workedWith.entrySet()) {
            for (String str : entry.getValue()) 
                if (participants.contains(str) && !entry.getKey().getName().equals(str))
                    writer.write("\t" + encode(entry.getKey().getName()) + " -> " +
                                        encode(str) + ";\n");
        }
        writer.write("}");
        writer.close();
    }

    public static String encode(String str) {
        return str.replace(" ", "").replace("-", "").replace("ä", "ae")
                  .replace("ü", "ue").replace("ö", "oe").replace("á", "a")
                  .replace("ã", "a").replace("é", "e");
    }
}
