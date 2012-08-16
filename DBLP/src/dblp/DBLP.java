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
        Author a = Author.searchAuthors("Ralf Lämmel").get(0);
        
        System.out.println(a.getName() + ":");
        List<String> pubs = Author.getPublicationKeyList(a);
        for (String str : pubs) {
            System.out.println(new Publication(str));
        }
        
        a = Author.searchAuthors("Jean-Marie Favre").get(0);
        
        System.out.println(a.getName() + ":");
        pubs = Author.getPublicationKeyList(a);
        for (String str : pubs) {
            System.out.println(new Publication(str));
        }
    }
}
