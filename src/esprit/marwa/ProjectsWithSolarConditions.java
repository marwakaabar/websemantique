package esprit.marwa;

import esprit.tools.JenaEngine;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class ProjectsWithSolarConditions {

    public static void main(String[] args) {
        String NS = "";

        Model model = JenaEngine.readModel("data/test.owl");

        if (model != null) {
            NS = model.getNsPrefixURI("");
//Projets avec conditions d'utilisation "solaire"
            String sparqlSelect = "PREFIX ns: <" + NS + ">\n" +
                    "SELECT ?project WHERE {\n" +
                    "    ?project a ns:Projet .\n" +
                    "    ?project ns:utiliseSourceEnergie ?source .\n" +
                    "    ?source ns:conditions_utilisation ?conditions .\n" +
                    "    FILTER(CONTAINS(?conditions, \"solaire\"))\n" +
                    "}";

            try (QueryExecution qexec = QueryExecutionFactory.create(sparqlSelect, model)) {
                ResultSet results = qexec.execSelect();
                while (results.hasNext()) {
                    QuerySolution solution = results.next();
                    System.out.println("Project: " + solution.getResource("project"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
