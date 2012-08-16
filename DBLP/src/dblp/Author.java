/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author Martin
 */
public class Author {
    private final static String AUTHOR_SEARCH_URL = "http://dblp.uni-trier.de/search/author?xauthor=:param:";
    
    public static List<Author> searchAuthors(String param) throws IOException, SAXException {
        List<Author> authors = new ArrayList<>();      
        String encoded = URLEncoder.encode(param, "UTF-8");
        HttpResponse response = Utils.executeRequest(new HttpGet(AUTHOR_SEARCH_URL.replace(":param:", encoded)));
        
        Document authorsDoc = Utils.parseXML(response.getEntity().getContent());       
        NodeList nodeList = authorsDoc.getElementsByTagName("author");
        for (int i = 0; i < nodeList.getLength(); i++)
            authors.add(new Author(nodeList.item(i).getTextContent(), nodeList.item(i).getAttributes().getNamedItem("urlpt").getNodeValue()));
        
        return authors;
    }
    
    private final static String PUBLICATIONS_URL = "http://dblp.uni-trier.de/rec/pers/:urlpt:/xk";
    
    public static List<String> getPublicationKeyList(Author from) throws IOException, SAXException {
        List<String> publicationKeys = new ArrayList<>();
        HttpResponse response = Utils.executeRequest(new HttpGet(PUBLICATIONS_URL.replace(":urlpt:", from.getUrlpt())));
        
        Document publicationsDoc = Utils.parseXML(response.getEntity().getContent()); 
        NodeList nodeList = publicationsDoc.getElementsByTagName("dblpkey");
        for (int i = 0; i < nodeList.getLength(); i++)
            if (!nodeList.item(i).hasAttributes()) 
                publicationKeys.add(nodeList.item(i).getTextContent());
        
        return publicationKeys;
    }
    
    private String name;
    private String urlpt;
    
    public Author(String name, String urlpt) {
        this.name = name;
        this.urlpt = urlpt;
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
    
    @Override
    public String toString() {
        return "[name: " + name + ", urlpt: " + urlpt + "]";
    }
}
