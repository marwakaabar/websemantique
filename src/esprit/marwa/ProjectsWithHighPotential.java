package esprit.marwa;

import esprit.tools.JenaEngine;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class ProjectsWithHighPotential {

    public static void main(String[] args) {
        String NS = "";

        Model model = JenaEngine.readModel("data/test.owl");

        if (model != null) {
            NS = model.getNsPrefixURI("");
//Projets avec potentiel énergétique supérieur à 1000
            String sparqlSelect = "PREFIX ns: <" + NS + ">\n" +
                    "SELECT ?project ?name WHERE {\n" +
                    "    ?project a ns:Projet .\n" +
                    "    ?project ns:utiliseSourceEnergie ?source .\n" +
                    "    ?source ns:potentiel_nergétique ?potential .\n" +
                    "    ?project ns:nom ?name .\n" +
                    "    FILTER (?potential > 1000)\n" +
                    "}";

            try (QueryExecution qexec = QueryExecutionFactory.create(sparqlSelect, model)) {
                ResultSet results = qexec.execSelect();
                while (results.hasNext()) {
                    QuerySolution solution = results.next();
                    System.out.println("Project: " + solution.getResource("project") + ", Name: " + solution.getLiteral("name"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
