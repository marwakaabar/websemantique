package esprit.marwa;

import esprit.tools.JenaEngine;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;

import java.io.FileOutputStream;
import java.io.OutputStream;

public class DELETEProjet {
    public static void main(String[] args) {
        String NS = "";
        String projectNom = "projet solaire 1"; // Nom du projet à supprimer

        // Lire le modèle à partir d'une ontologie
        Model model = JenaEngine.readModel("data/test.owl");

        if (model != null) {
            // Lire le namespace de l’ontologie
            NS = model.getNsPrefixURI("");

            // Créer une requête SPARQL de suppression
            String sparqlDelete = "PREFIX ns: <" + NS + ">\n" +
                    "DELETE WHERE { " +
                    "    ns:Project_" + projectNom.replaceAll(" ", "_") + " ?p ?o ." +
                    "}";

            // Exécuter la requête SPARQL de suppression sur le modèle
            UpdateRequest deleteRequest = UpdateFactory.create(sparqlDelete);
            UpdateAction.execute(deleteRequest, model);

            // Afficher le modèle mis à jour
            model.write(System.out, "RDF/XML");

            // Écrire le modèle mis à jour dans le fichier test.owl
            try (OutputStream out = new FileOutputStream("data/test.owl")) {
                model.write(out, "RDF/XML");
                System.out.println("Project deleted and model updated in test.owl");
            } catch (Exception e) {
                System.err.println("Error writing model to file: " + e.getMessage());
            }
        } else {
            System.out.println("Error when reading model from ontology");
        }
    }
}
