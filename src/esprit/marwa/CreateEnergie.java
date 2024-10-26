package esprit.marwa;

import esprit.tools.JenaEngine;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;

public class CreateEnergie {
    public static void main(String[] args) {
        String NS = "";

        // Lire le modèle à partir d'une ontologie
        Model model = JenaEngine.readModel("data/test.owl");

        if (model != null) {
            // Lire le namespace de l’ontologie
            NS = model.getNsPrefixURI("");

            // Créer une requête de mise à jour SPARQL
            String sparqlUpdate = "PREFIX ns: <" + NS + ">\n" +
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "INSERT DATA {" +
                    "    ns:projet_hydrolique a ns:Projet ;" +
                    "                    ns:dateFin \"2024-02-23T00:00:00\"^^xsd:dateTime ;" +
                    "                    ns:dateDebut \"2024-01-01T00:00:00\"^^xsd:dateTime ;" +
                    "                    ns:nom \"Projet 2\" ;" +
                    "                    ns:utiliseSourceEnergie ns:energie_hydrolique ." +
                    "    ns:energie_hydrolique a ns:Energie_Hydrolique ;" +  // Correction ici
                    "                        ns:conditions_utilisation \"Conditions pour l'énergie solaire\" ;" +
                    "                        ns:potentiel_nergétique 7000 ." +
                    "}";

            // Exécuter la requête de mise à jour sur le modèle
            UpdateRequest updateRequest = UpdateFactory.create(sparqlUpdate);
            UpdateAction.execute(updateRequest, model);

            // Écrire le modèle mis à jour dans le fichier OWL
            JenaEngine.writeModel(model, "data/test.owl");

            // Afficher le modèle mis à jour
            model.write(System.out, "RDF/XML");
        } else {
            System.out.println("Erreur lors de la lecture du modèle depuis l'ontologie");
        }
    }
}
