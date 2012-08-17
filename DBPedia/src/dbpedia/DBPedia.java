package dbpedia;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.hp.hpl.jena.query.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Martin Leinberger
 */
public class DBPedia {
    private final static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        if (args.length < 2) {
            System.out.println("Expected 2 parameters: wikiDump language [dbPedialanguage]");
            System.exit(1);
        }
        
        String wikiPath = args[0];
        String language101 = args[1];
        String dbPediaLanguage = language101;
        
        if (args.length > 2)
            dbPediaLanguage = args[2];
        
        //Get data from wiki dump
        JsonObject wikiObj = new JsonParser().parse(new JsonReader(new FileReader(wikiPath))).getAsJsonObject();
        JsonObject languages = wikiObj.get("Language").getAsJsonObject();
        JsonObject specificLanguage = languages.get(language101).getAsJsonObject();
        
        //prepare query
        String service = "http://dbpedia.org/sparql";
        String queryString = "SELECT ?abstract WHERE { <http://dbpedia.org/resource/:language:> <http://dbpedia.org/ontology/abstract> ?abstract . FILTER (LANG(?abstract) = 'en') }";
        String finalQuery = queryString.replace(":language:", dbPediaLanguage);
        
        //create and execute query
        Query query = QueryFactory.create(finalQuery);
        QueryExecution execution = QueryExecutionFactory.sparqlService(service, query);
        ResultSet result = execution.execSelect();
                
        //combine the results
        JsonObject combinedResult = new JsonObject();
        combinedResult.add("name", specificLanguage.get("name"));
        combinedResult.add("url", specificLanguage.get("url"));
        combinedResult.add("implementations", specificLanguage.get("implementations"));
        combinedResult.add("abstract", new JsonPrimitive(result.next().get("abstract").toString()));
        
        //write output
       FileWriter writer = new FileWriter("otuput.json");
       writer.write(GSON.toJson(combinedResult));
       writer.close();
    }
}
