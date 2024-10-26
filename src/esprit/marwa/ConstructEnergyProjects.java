package esprit.marwa;

import esprit.tools.JenaEngine;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

public class ConstructEnergyProjects {

    public static void main(String[] args) {
        String NS = "";

        // Load the model from the ontology file
        Model model = JenaEngine.readModel("data/test.owl");

        if (model != null) {
            NS = model.getNsPrefixURI("");

            // Create a SPARQL CONSTRUCT query
            String sparqlConstruct = "PREFIX ns: <" + NS + ">\n" +
                    "CONSTRUCT { \n" +
                    "    ?project a ns:ProjetEnergy .\n" +
                    "    ?project ns:utiliseSourceEnergie ns:energie_solaire .\n" +
                    "} WHERE { \n" +
                    "    ?project a ns:Projet .\n" +
                    "    ?project ns:utiliseSourceEnergie ns:energie_solaire .\n" +
                    "}";

            // Execute the SPARQL CONSTRUCT query on the model
            Model constructedModel = ModelFactory.createDefaultModel();
            try (QueryExecution qexec = QueryExecutionFactory.create(sparqlConstruct, model)) {
                constructedModel = qexec.execConstruct();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Print the constructed model
            constructedModel.write(System.out, "RDF/XML");
        } else {
            System.out.println("Erreur lors de la lecture du modèle depuis l'ontologie");
        }
    }
}
