package esprit.marwa;

import esprit.tools.JenaEngine;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class CountProjectHydrolique {

    public static void main(String[] args) {
        String NS = "";

        Model model = JenaEngine.readModel("data/test.owl");

        if (model != null) {
            NS = model.getNsPrefixURI("");
//Compter le nombre de projets avec energie_solaire
            String sparqlCount = "PREFIX ns: <" + NS + ">\n" +
                    "SELECT (COUNT(?project) AS ?numProjects) WHERE {\n" +
                    "    ?project a ns:Projet .\n" +
                    "    ?project ns:utiliseSourceEnergie ns:energie_hydrolique .\n" +
                    "}";

            try (QueryExecution qexec = QueryExecutionFactory.create(sparqlCount, model)) {
                ResultSet results = qexec.execSelect();
                if (results.hasNext()) {
                    QuerySolution solution = results.next();
                    System.out.println("Number of projects: " + solution.getLiteral("numProjects"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
