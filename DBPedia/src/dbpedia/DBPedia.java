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
        
        //prepare query
        String service = "http://dbpedia.org/sparql";
        String queryString = "SELECT ?abstract WHERE { <http://dbpedia.org/resource/:language:> <http://dbpedia.org/ontology/abstract> ?abstract . FILTER (LANG(?abstract) = 'en') }";
        
        //Get data from wiki dump
        JsonObject wikiObj = new JsonParser().parse(new JsonReader(new FileReader(wikiPath))).getAsJsonObject();
        JsonObject languages = wikiObj.get("Language").getAsJsonObject();
        
        //iterate through all languages
        for (Map.Entry<String, JsonElement> entry : languages.entrySet()) {
            //create and execute final query
            String finalQuery = queryString.replace(":language:", entry.getKey());
            Query query = QueryFactory.create(finalQuery);
            QueryExecution execution = QueryExecutionFactory.sparqlService(service, query);
            ResultSet result = execution.execSelect();
            
            //process the results
            if (result.hasNext()) {
                //an entry was found => combine the results
                JsonObject combinedResult = new JsonObject();
                combinedResult.add("name", entry.getValue().getAsJsonObject().get("name"));
                combinedResult.add("url", entry.getValue().getAsJsonObject().get("url"));
                combinedResult.add("implementations", entry.getValue().getAsJsonObject().get("implementations"));
                combinedResult.add("abstract", new JsonPrimitive(result.next().get("abstract").toString()));
                
                matchedObjects.add(combinedResult);
            } else //nothing found => just add the data from the 101Wiki dump
                unmatchedObjects.add(entry.getValue());
        }
        
        
        //create and write the result
        JsonObject result = new JsonObject();
        result.add("matched", matchedObjects);
        result.add("unmatched", unmatchedObjects);
        
        FileWriter writer = new FileWriter(output);
        writer.write(GSON.toJson(result));
        writer.close();
    }
}
