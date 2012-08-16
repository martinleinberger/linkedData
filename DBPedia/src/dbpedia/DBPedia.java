/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dbpedia;

import com.hp.hpl.jena.query.*;

/**
 *
 * @author Martin
 */
public class DBPedia {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String service = "http://dbpedia.org/sparql";
        String queryString = "SELECT ?abstract WHERE { <http://dbpedia.org/resource/Java_(programming_language)> <http://dbpedia.org/ontology/abstract> ?abstract . FILTER (LANG(?abstract) = 'en') }";
        Query query = QueryFactory.create(queryString);
        QueryExecution execution = QueryExecutionFactory.sparqlService(service, query);
        ResultSet result = execution.execSelect();
        ResultSetFormatter.out(System.out, result, query);  
    }
}
