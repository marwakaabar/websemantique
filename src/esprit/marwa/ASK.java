package esprit.marwa;

import esprit.tools.JenaEngine;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.rdf.model.Model;

public class ASK {
    public static void main(String[] args) {
        String NS = "";
        String engineerIndividual = "http://www.semanticweb.org/saidg/ontologies/2024/8/untitled-ontology-8#ingénieur_energitique"; // URI de l'ingénieur
        String engineerName = "adam"; // Nom de l'ingénieur

        // Lire le modèle à partir d'une ontologie
        Model model = JenaEngine.readModel("data/test.owl");

        if (model != null) {
            // Lire le namespace de l’ontologie
            NS = model.getNsPrefixURI("");

            // Créer une requête ASK SPARQL pour vérifier si l'ingénieur existe
            String askQuery = "PREFIX ns: <" + NS + ">\n" +
                    "ASK WHERE { " +
                    "    <" + engineerIndividual + "> a ns:Personne ; " +
                    "               ns:nom \"" + engineerName + "\" . " +
                    "}";

            // Exécuter la requête ASK
            try (QueryExecution qexec = QueryExecutionFactory.create(askQuery, model)) {
                boolean exists = qexec.execAsk();
                if (exists) {
                    System.out.println("L'individu \"ingénieur_energitique\" avec le nom \"" + engineerName + "\" existe.");
                } else {
                    System.out.println("L'individu \"ingénieur_energitique\" avec le nom \"" + engineerName + "\" n'existe pas.");
                }
            }
        } else {
            System.out.println("Erreur lors de la lecture du modèle depuis l'ontologie");
        }
    }
}
