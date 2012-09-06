package dblp;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Used for general author information
 * @author Martin Leinberger
 */
public class Author {
    private final static String AUTHOR_SEARCH_URL = "http://dblp.uni-trier.de/search/author?xauthor=:param:";
    private final static String PUBLICATIONS_URL = "http://dblp.uni-trier.de/rec/pers/:urlpt:/xk";
    
    
    public static List<Author> searchAuthors(String param) throws IOException, SAXException {
        List<Author> authors = new ArrayList<Author>();      
        String encoded = URLEncoder.encode(param, "UTF-8");
        HttpResponse response = Utils.executeRequest(new HttpGet(AUTHOR_SEARCH_URL.replace(":param:", encoded)));
        
        Document authorsDoc = Utils.parseXML(response.getEntity().getContent());       
        NodeList nodeList = authorsDoc.getElementsByTagName("author");
        for (int i = 0; i < nodeList.getLength(); i++)
            authors.add(new Author(nodeList.item(i).getTextContent(), nodeList.item(i).getAttributes().getNamedItem("urlpt").getNodeValue()));
        
        return authors;
    }
    
    
    private String name;
    private String urlpt;
    private List<String> publicationKeys;
    
    public Author(String name, String urlpt) {
        this.name = name;
        this.urlpt = urlpt;
    }
    
    private void cachePublicationKeys() {
        try {
            publicationKeys = new ArrayList<String>();
            HttpResponse response = Utils.executeRequest(new HttpGet(PUBLICATIONS_URL.replace(":urlpt:", urlpt)));

            Document publicationsDoc = Utils.parseXML(response.getEntity().getContent()); 
            NodeList nodeList = publicationsDoc.getElementsByTagName("dblpkey");
            for (int i = 0; i < nodeList.getLength(); i++)
                if (!nodeList.item(i).hasAttributes()) 
                    publicationKeys.add(nodeList.item(i).getTextContent());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlpt() {
        return urlpt;
    }

    public void setUrlpt(String urlpt) {
        this.urlpt = urlpt;
    }
    
    public List<String> getPublicationKeys() {
        if (publicationKeys == null)
            cachePublicationKeys();
        return publicationKeys;
    }
    
    @Override
    public String toString() {
        return "[name: " + name + ", urlpt: " + urlpt + "]";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Author)
            if (((Author)obj).name.equals(this.name))
                return true;
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + this.name.hashCode();
        return hash;
    }
}
