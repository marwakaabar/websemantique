package esprit.marwa;

import esprit.tools.JenaEngine;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class ASKProjetEnergieExistance {

    public static void main(String[] args) {
        String NS = "";

        // Charger le modèle depuis le fichier ontologique
        Model model = JenaEngine.readModel("data/test.owl");

        if (model != null) {
            // Lire le namespace de l'ontologie
            NS = model.getNsPrefixURI(""); // Assurez-vous que l'URI de l'ontologie est bien défini

            // Appliquer les règles sur le modèle inféré
            Model inferredModel = JenaEngine.readInferencedModelFromRuleFile(model, "data/rules.txt");

            // Créer une requête SPARQL ASK pour vérifier l'existence de projets utilisant l'énergie solaire
            String sparqlAsk = "PREFIX ns: <" + NS + ">\n" +
                    "ASK WHERE {\n" +
                    "    ?project a ns:Projet .\n" +
                    "    ?project ns:utiliseSourceEnergie ns:energie_hydrolique .\n" +
                    "}";

            // Exécuter la requête SPARQL ASK sur le modèle inféré
            try (QueryExecution qexec = QueryExecutionFactory.create(sparqlAsk, inferredModel)) {
                boolean exists = qexec.execAsk();

                if (exists) {
                    System.out.println("Des projets utilisant l'énergie solaire existent dans le modèle.");

                    // Créer une requête SPARQL SELECT pour obtenir les noms des projets
                    String sparqlSelect = "PREFIX ns: <" + NS + ">\n" +
                            "SELECT ?projectName WHERE {\n" +
                            "    ?project a ns:Projet .\n" +
                            "    ?project ns:utiliseSourceEnergie ns:energie_solaire .\n" +
                            "    ?project ns:nom ?projectName .\n" +
                            "}";

                    // Exécuter la requête SPARQL SELECT
                    try (QueryExecution selectExec = QueryExecutionFactory.create(sparqlSelect, inferredModel)) {
                        ResultSet results = selectExec.execSelect();

                        // Itérer sur les résultats et imprimer les noms des projets
                        while (results.hasNext()) {
                            QuerySolution sol = results.nextSolution();
                            String projectName = sol.get("projectName").toString();
                            System.out.println("Nom du projet : " + projectName);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Aucun projet utilisant l'énergie solaire n'existe dans le modèle.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Erreur lors de la lecture du modèle depuis l'ontologie");
        }
    }
}
