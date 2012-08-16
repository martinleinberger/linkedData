/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dblp;

import java.io.IOException;
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
public class Publication {
    private final static String PUBLICATION_URL = "http://dblp.uni-trier.de/rec/bibtex/:key:.xml";
    
    private String key;
    private List<String> authors = new ArrayList<>();
    private String title;
    private String year;
    
    public Publication(String key) throws IOException, SAXException {
        this.key = key;
        HttpResponse response = Utils.executeRequest(new HttpGet(PUBLICATION_URL.replace(":key:", key)));
        Document pubDoc = Utils.parseXML(response.getEntity().getContent());

        NodeList nodeList = pubDoc.getElementsByTagName("author");
        for (int i = 0; i  < nodeList.getLength(); i++)
            authors.add(nodeList.item(i).getTextContent());
        title = pubDoc.getElementsByTagName("title").item(0).getTextContent();
        year = pubDoc.getElementsByTagName("year").item(0).getTextContent();
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
    
    @Override
    public String toString() {
        return "[ title: " + title + ", year: " + year + " ]";
    }
}
