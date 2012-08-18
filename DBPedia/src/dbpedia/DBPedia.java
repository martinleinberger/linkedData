package dbpedia;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.hp.hpl.jena.query.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 *
 * @author Martin Leinberger
 */
public class DBPedia {
    private final static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    
    //query details
    private final static String service = "http://dbpedia.org/sparql";
    private final static String queryString = "SELECT ?abstract WHERE { <http://dbpedia.org/resource/:language:> <http://dbpedia.org/ontology/abstract> ?abstract . FILTER (LANG(?abstract) = 'en') }";
    
    private static JsonArray matchedObjects = new JsonArray();
    private static JsonArray unmatchedObjects = new JsonArray();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        if (args.length < 2) {
            System.out.println("Expected 2 parameters: wikiDump output");
            System.exit(1);
        }
        
        String wikiPath = args[0];
        String output = args[1];
        
        //Get data from wiki dump
        JsonObject wikiObj = new JsonParser().parse(new JsonReader(new FileReader(wikiPath))).getAsJsonObject();
        JsonObject languages = wikiObj.get("Language").getAsJsonObject();
        
        //iterate through all languages
        for (Map.Entry<String, JsonElement> entry : languages.entrySet()) {
            String abstr = null;
            ResultSet result = normalQuery(entry.getKey());
            //if we have no results or the abstract doesn't contain some key words,
            //then we probably have the wrong abstract and need to try again
            if (result.hasNext()) {
                abstr = result.next().get("abstract").toString();
                if (checkForKeywords(abstr))
                    abstr = null;
            }
            
            //so let's try again, if we have a result this time, then it's probably the right one
            if (abstr == null) {
                result = queryWithExtension(entry.getKey());
                if (result.hasNext())
                    abstr = result.next().get("abstract").toString();
            }
            
            //if we couldn't find any abstract, then we have an unmatched object,
            //otherwise, we can combine the results
            if (abstr == null)
                unmatchedObjects.add(entry.getValue());
            else {
                JsonObject combinedResult = new JsonObject();
                combinedResult.add("name", entry.getValue().getAsJsonObject().get("name"));
                combinedResult.add("url", entry.getValue().getAsJsonObject().get("url"));
                combinedResult.add("implementations", entry.getValue().getAsJsonObject().get("implementations"));
                combinedResult.add("abstract", new JsonPrimitive(abstr));
                
                matchedObjects.add(combinedResult); 
            }
        }
        
        
        System.out.println("matched " + matchedObjects.size() + " objects");
        
        //create and write the result
        JsonObject result = new JsonObject();
        result.add("matched", matchedObjects);
        result.add("unmatched", unmatchedObjects);
        
        FileWriter writer = new FileWriter(output);
        writer.write(GSON.toJson(result));
        writer.close();
    }
    
    private static boolean checkForKeywords(String abstr) {
        String str = abstr.toLowerCase();
        return !str.contains("programming") && !str.contains("scripting") &&
               !str.contains("query") && !str.contains("executable") &&
               !str.contains("markup") && !str.contains("object");
    }
    
    private static ResultSet normalQuery(String language) {
            String finalQuery = queryString.replace(":language:", language);
            Query query = QueryFactory.create(finalQuery);
            QueryExecution execution = QueryExecutionFactory.sparqlService(service, query);
            
            return execution.execSelect();
    }
    
    private static ResultSet queryWithExtension(String language) {
            String finalQuery = queryString.replace(":language:", language + "_(programming_language)");
            Query query = QueryFactory.create(finalQuery);
            QueryExecution execution = QueryExecutionFactory.sparqlService(service, query);
           
            return execution.execSelect();
    }
}
