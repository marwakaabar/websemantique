package esprit.marwa;

import esprit.tools.JenaEngine;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;

public class SELECTEnergie {

    public static void main(String[] args) {
        String NS = "";

        // Load the model from the ontology file
        Model model = JenaEngine.readModel("data/test.owl");

        if (model != null) {
            // Read the namespace of the ontology
            NS = model.getNsPrefixURI("");

            // Apply rules on the inferred model
            Model inferredModel = JenaEngine.readInferencedModelFromRuleFile(model, "data/rules.txt");

            // Create a SPARQL SELECT query
            String sparqlSelect = "PREFIX ns: <" + NS + ">\n" +
                    "SELECT ?project ?name WHERE {\n" +
                    "    ?project a ns:Projet .\n" +
                    "    ?project ns:utiliseSourceEnergie ns:energie_hydrolique .\n" +
                    "    ?project ns:nom ?name .\n" +
                    "}";

            // Execute the SPARQL SELECT query on the inferred model
            try (QueryExecution qexec = QueryExecutionFactory.create(sparqlSelect, inferredModel)) {
                ResultSet resultSet = qexec.execSelect();

                // Iterate over the results and print them
                while (resultSet.hasNext()) {
                    QuerySolution solution = resultSet.nextSolution();
                    RDFNode project = solution.get("project");
                    RDFNode name = solution.get("name");
                    System.out.println("Project: " + project + ", Name: " + name);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Error when reading model from ontology");
        }
    }
}
