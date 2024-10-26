package esprit.marwa;

import esprit.tools.JenaEngine;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;

public class AssociateProjectTo {
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
                    "    ns:projet_biomasse a ns:Projet ;" +
                    "                    ns:nom \"Biomasse 1\" ;" +
                    "                    ns:utiliseSourceEnergie ns:Biomasse ;" +
                    "                    ns:estConçuPar ns:ingenieur_num2 ;" +
                    "                    ns:estGerePar ns:gestionnaire_num2 ." +
                    "    ns:ingenieur_num2 a ns:Inginieur ;" +
                    "                  ns:qualifications \"PhD en Ingénierie\" ;" +
                    "                  ns:spécialité \"Biomasse\" ;" +
                    "                  ns:contact \"marwa@example.com\" ;" +
                    "                  ns:nom \"marwa\" ;" +
                    "                  ns:statut \"Actif\" ." +
                    "    ns:gestionnaire_num2 a ns:Gestionnaire_de_Projet ;" +
                    "                     ns:expérience \"5 ans\" ;" +
                    "                     ns:nom \"Oumaima\" ;" +
                    "                     ns:contact \"oumaima@example.com\" ;" +
                    "                     ns:statut \"Actif\" ." +
                    "}";

            // Exécuter la requête de mise à jour sur le modèle
            UpdateRequest updateRequest = UpdateFactory.create(sparqlUpdate);
            UpdateAction.execute(updateRequest, model);

            // Écrire le modèle mis à jour dans le fichier OWL
            JenaEngine.writeModel(model, "data/test.owl"); // Assurez-vous que cette méthode existe

            // Afficher le modèle mis à jour
            model.write(System.out, "RDF/XML");
        } else {
            System.out.println("Erreur lors de la lecture du modèle depuis l'ontologie");
        }
    }
}
