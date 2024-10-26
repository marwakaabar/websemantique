package esprit.marwa;

import esprit.tools.JenaEngine;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;

import java.io.FileOutputStream;
import java.io.OutputStream;

public class CREATEProjet {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String NS = "";

        // Lire le modèle à partir d'une ontologie
        Model model = JenaEngine.readModel("data/test.owl");

        if (model != null) {
            // Lire le namespace de l’ontologie
            NS = model.getNsPrefixURI("");

            // Appliquer nos règles sur le modèle inféré
            Model inferredModel = JenaEngine.readInferencedModelFromRuleFile(model, "data/rules.txt");
            String projectNom = "projet solaire 3";
            int projectCapacite = 100;
            String projectStartDate = "2024-10-25T10:00:00"; // Exemple de dateTime
            String projectEndDate = "2024-12-31T10:00:00"; // Exemple de DateFin

            // Créer une requête SPARQL d'update
            String sparqlUpdate = "PREFIX ns: <" + NS + ">\n" +
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "INSERT DATA {" +
                    "    ns:Project_" + projectNom.replaceAll(" ", "_") + " a ns:Project ; " +
                    "        ns:nom \"" + projectNom + "\" ; " +
                    "        ns:capacite \"" + projectCapacite + "\" ; " +
                    "        ns:dateDebut \"" + projectStartDate + "\"^^xsd:dateTime ; " +
                    "        ns:DateFin \"" + projectEndDate + "\"^^xsd:dateTime ." +
                    "}";

            // Exécuter la requête SPARQL d'update sur le modèle inféré
            UpdateRequest updateRequest = UpdateFactory.create(sparqlUpdate);
            UpdateAction.execute(updateRequest, inferredModel);

            // Afficher le modèle mis à jour
            inferredModel.write(System.out, "RDF/XML");

            // Écrire le modèle mis à jour dans le fichier test.owl
            try (OutputStream out = new FileOutputStream("data/test.owl")) {
                inferredModel.write(out, "RDF/XML");
                System.out.println("Model updated and saved to test.owl");
            } catch (Exception e) {
                System.err.println("Error writing model to file: " + e.getMessage());
            }
        } else {
            System.out.println("Error when reading model from ontology");
        }
    }
}
