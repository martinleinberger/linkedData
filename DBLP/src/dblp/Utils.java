/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dblp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author Martin
 */
public class Utils {
    private final static DocumentBuilderFactory dbFactory;
    private final static DocumentBuilder dbuilder;
    
    private static DefaultHttpClient httpClient = new DefaultHttpClient();
    
    static {
        try {
            dbFactory = DocumentBuilderFactory.newInstance();
            dbuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            throw new RuntimeException(ex);
        }
    }
 
    public static HttpResponse executeRequest(HttpUriRequest getRequest) throws IOException {
        return httpClient.execute(getRequest);
    }
    
    public static String toString(InputStream stream) throws IOException {
        StringBuilder buffer = new StringBuilder();
        String str;
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        while ((str = reader.readLine()) != null)
            buffer.append(str);
        
        return buffer.toString();
    }

    public static Document parseXML(InputStream xml) throws IOException, SAXException {
        return dbuilder.parse(xml);
    }
}
