/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dblp;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.xml.sax.SAXException;

/**
 *
 * @author Martin
 */
public class DBLP {
    private static Set<String> friends = new HashSet<String>();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, SAXException {
        if (args.length != 1) {
            System.out.println("Expected one parameter: authorName");
            System.exit(1);
        }
        
        Author a = Author.searchAuthors(args[0]).get(0);
        List<String> pubs = Author.getPublicationKeyList(a);
        for (String key : pubs) {
            Publication pub = new Publication(key);
            friends.addAll(pub.getAuthors());
        }
        
        friends.remove(a.getName());
        
        System.out.println(a.getName() + " worked with:");
        for (String friend : friends)
            System.out.println(friend);
    }
}
