package esprit.marwa;

import esprit.tools.JenaEngine;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class SELECTProjet {
    public static void main(String[] args) {
        String NS = "";

        // Charger le modèle à partir du fichier d'ontologie
        Model model = JenaEngine.readModel("data/test.owl");

        if (model != null) {
            // Lire le namespace de l’ontologie
            NS = model.getNsPrefixURI("");

            // Créer une requête SPARQL SELECT
            String sparqlSelect = "PREFIX ns: <" + NS + ">\n" +
                    "SELECT ?projectName ?engineerName ?managerName WHERE {\n" +
                    "    ?project a ns:Projet ;\n" +
                    "             ns:nom ?projectName ;\n" +
                    "             ns:utiliseSourceEnergie ns:Biomasse ;\n" +
                    "             ns:estConçuPar ?engineer ;\n" +
                    "             ns:estGerePar ?manager .\n" +
                    "    ?engineer ns:nom ?engineerName .\n" +
                    "    ?manager ns:nom ?managerName .\n" +
                    "}";

            // Exécuter la requête SPARQL SELECT
            try (QueryExecution qexec = QueryExecutionFactory.create(sparqlSelect, model)) {
                ResultSet resultSet = qexec.execSelect();

                // Imprimer l'en-tête du tableau
                System.out.printf("%-30s %-20s %-20s%n", "Project Name", "Engineer Name", "Manager Name");
                System.out.println(new String(new char[70]).replace("\0", "-")); // Ligne séparatrice

                // Itérer sur les résultats et les imprimer au format tableau
                while (resultSet.hasNext()) {
                    QuerySolution solution = resultSet.nextSolution();
                    String projectName = solution.getLiteral("projectName").getString();
                    String engineerName = solution.getLiteral("engineerName").getString();
                    String managerName = solution.getLiteral("managerName").getString();

                    System.out.printf("%-30s %-20s %-20s%n", projectName, engineerName, managerName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Erreur lors de la lecture du modèle depuis l'ontologie");
        }
    }
}
