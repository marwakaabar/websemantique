package esprit.marwa;

import esprit.tools.JenaEngine;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class ComplexSelectProject {
    public static void main(String[] args) {
        String NS = "";

        // Charger le modèle depuis l'ontologie
        Model model = JenaEngine.readModel("data/test.owl");

        if (model != null) {
            // Lire le namespace de l’ontologie
            NS = model.getNsPrefixURI("");
            if (NS == null || NS.isEmpty()) {
                NS = "http://www.semanticweb.org/saidg/ontologies/2024/8/untitled-ontology-8#";
            }

            // Requête SPARQL complexe pour obtenir les informations du projet, ingénieur, et gestionnaire
            String complexSelectQuery = "PREFIX ns: <" + NS + ">\n" +
                    "SELECT DISTINCT ?project ?projectName ?energySource ?engineerName ?managerName \n" +
                    "WHERE {\n" +
                    "    ?project a ns:Projet ;\n" +
                    "            ns:nom ?projectName ;\n" +
                    "            ns:utiliseSourceEnergie ?energySource ;\n" +
                    "            ns:estConçuPar ?engineer ;\n" +
                    "            ns:estGerePar ?manager .\n" +
                    "    ?engineer ns:nom ?engineerName .\n" +
                    "    ?manager ns:nom ?managerName .\n" +
                    "}";

            // Exécution de la requête complexe
            try (QueryExecution qexec = QueryExecutionFactory.create(QueryFactory.create(complexSelectQuery), model)) {
                ResultSet results = qexec.execSelect();

                // Afficher les résultats
                while (results.hasNext()) {
                    QuerySolution soln = results.nextSolution();
                    String projectName = soln.getLiteral("projectName").getString();
                    String energySource = soln.getResource("energySource").getLocalName();
                    String engineerName = soln.getLiteral("engineerName").getString();
                    String managerName = soln.getLiteral("managerName").getString();

                    System.out.println("Projet: " + projectName);
                    System.out.println("Source d'énergie: " + energySource);
                    System.out.println("Ingénieur: " + engineerName);
                    System.out.println("Gestionnaire: " + managerName);
                    System.out.println("--------------------------------");
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de l'exécution de la requête SPARQL : " + e.getMessage());
            }
        } else {
            System.out.println("Erreur lors de la lecture du modèle depuis l'ontologie");
        }
    }
}
