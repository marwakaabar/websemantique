package esprit.marwa;

import esprit.tools.JenaEngine;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;

public class CreateProjectAndAssociateTo {
    public static void main(String[] args) {
        String NS = "";
        String engineerName = "marwa"; // Nom de l'ingénieur
        String managerName = "Oumaima"; // Nom du gestionnaire
        String engineerURI = ""; // URI de l'ingénieur
        String managerURI = ""; // URI du gestionnaire

        // Lire le modèle à partir d'une ontologie
        Model model = JenaEngine.readModel("data/test.owl");

        if (model != null) {
            // Lire le namespace de l’ontologie
            NS = model.getNsPrefixURI("");

            // Afficher le contenu du modèle pour déboguer
            model.write(System.out, "RDF/XML-ABBREV");

            // Vérifiez la présence de l'ingénieur "adam"
            String engineerQuery = "PREFIX ns: <" + NS + ">\n" +
                    "SELECT ?engineer WHERE { ?engineer a ns:Inginieur . ?engineer ns:nom \"" + engineerName + "\" }";

            // Exécutez la requête pour l'ingénieur
            try (QueryExecution qexec = QueryExecutionFactory.create(engineerQuery, model)) {
                ResultSet results = qexec.execSelect();
                if (results.hasNext()) {
                    engineerURI = results.nextSolution().getResource("engineer").getURI();
                    System.out.println("L'ingénieur existe : " + engineerURI);
                } else {
                    System.out.println("L'ingénieur n'existe pas.");
                    return; // Sortir si l'ingénieur n'existe pas
                }
            }

            // Vérifiez la présence du gestionnaire "Jean Dupont"
            String managerQuery = "PREFIX ns: <" + NS + ">\n" +
                    "SELECT ?manager WHERE { ?manager a ns:Gestionnaire_de_Projet . ?manager ns:nom \"" + managerName + "\" }";

            // Exécutez la requête pour le gestionnaire
            try (QueryExecution qexec = QueryExecutionFactory.create(managerQuery, model)) {
                ResultSet results = qexec.execSelect();
                if (results.hasNext()) {
                    managerURI = results.nextSolution().getResource("manager").getURI();
                    System.out.println("Le gestionnaire existe : " + managerURI);
                } else {
                    System.out.println("Le gestionnaire n'existe pas.");
                    return; // Sortir si le gestionnaire n'existe pas
                }
            }

            // Créer une requête de mise à jour SPARQL pour créer le projet
            String sparqlUpdate = "PREFIX ns: <" + NS + ">\n" +
                    "INSERT DATA {" +
                    "    ns:projet_biomasse a ns:Projet ;" +
                    "                    ns:nom \"test projet\" ;" +
                    "                    ns:utiliseSourceEnergie ns:energie_solaire ;" +
                    "                    ns:estConçuPar <" + engineerURI + "> ;" +
                    "                    ns:estGerePar <" + managerURI + "> ." +
                    "}";

            // Exécuter la requête de mise à jour sur le modèle
            UpdateRequest updateRequest = UpdateFactory.create(sparqlUpdate);
            UpdateAction.execute(updateRequest, model);

            // Écrire le modèle mis à jour dans le fichier OWL
            JenaEngine.writeModel(model, "data/test.owl");

            // Afficher le modèle mis à jour
            model.write(System.out, "RDF/XML-ABBREV");
        } else {
            System.out.println("Erreur lors de la lecture du modèle depuis l'ontologie");
        }
    }
}
