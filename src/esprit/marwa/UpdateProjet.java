package esprit.marwa;

import esprit.tools.JenaEngine;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;

import java.io.FileOutputStream;
import java.io.OutputStream;

public class UpdateProjet {
    /**
     * Méthode principale pour mettre à jour un projet
     */
    public static void main(String[] args) {
        String NS = "";

        // Lire le modèle à partir de l'ontologie
        Model model = JenaEngine.readModel("data/test.owl");

        if (model != null) {
            // Lire le namespace de l’ontologie
            NS = model.getNsPrefixURI("");
            if (NS == null || NS.isEmpty()) {
                NS = "http://www.semanticweb.org/saidg/ontologies/2024/8/untitled-ontology-8#";
            }

            // Nom du projet à mettre à jour (utilisé comme identifiant)
            String projectNom = "projet solaire 3";
            int newCapacite = 200; // Nouvelle capacité
            String newStartDate = "2025-01-01T10:00:00"; // Nouvelle date de début
            String newEndDate = "2025-12-31T10:00:00"; // Nouvelle date de fin

            // Créer une requête SPARQL d'update pour supprimer les valeurs existantes
            String sparqlDeleteInsert = "PREFIX ns: <" + NS + ">\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "DELETE { " +
                    "    ns:Project_" + projectNom.replaceAll(" ", "_") + " ns:capacite ?oldCapacite ; " +
                    "                                              ns:dateDebut ?oldStartDate ; " +
                    "                                              ns:DateFin ?oldEndDate ." +
                    "}\n" +
                    "INSERT { " +
                    "    ns:Project_" + projectNom.replaceAll(" ", "_") + " ns:capacite \"" + newCapacite + "\" ; " +
                    "                                              ns:dateDebut \"" + newStartDate + "\"^^xsd:dateTime ; " +
                    "                                              ns:DateFin \"" + newEndDate + "\"^^xsd:dateTime ." +
                    "}\n" +
                    "WHERE { " +
                    "    ns:Project_" + projectNom.replaceAll(" ", "_") + " ns:capacite ?oldCapacite ; " +
                    "                                              ns:dateDebut ?oldStartDate ; " +
                    "                                              ns:DateFin ?oldEndDate ." +
                    "}";

            // Exécuter la requête SPARQL de mise à jour
            UpdateRequest updateRequest = UpdateFactory.create(sparqlDeleteInsert);
            UpdateAction.execute(updateRequest, model);

            // Afficher le modèle mis à jour
            model.write(System.out, "RDF/XML");

            // Sauvegarder le modèle mis à jour dans le fichier test.owl
            try (OutputStream out = new FileOutputStream("data/test.owl")) {
                model.write(out, "RDF/XML");
                System.out.println("Model updated and saved to test.owl");
            } catch (Exception e) {
                System.err.println("Error writing model to file: " + e.getMessage());
            }
        } else {
            System.out.println("Error when reading model from ontology");
        }
    }
}
