/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dblp;

import java.io.IOException;
import java.util.List;
import org.xml.sax.SAXException;

/**
 *
 * @author Martin
 */
public class DBLP {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, SAXException {
        List<Author> authors = Author.searchAuthors("scheck");
        for (Author a : authors) {
            System.out.println(a);
        }
    }
}
