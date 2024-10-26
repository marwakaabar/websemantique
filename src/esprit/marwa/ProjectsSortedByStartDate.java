package esprit.marwa;

import esprit.tools.JenaEngine;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProjectsSortedByStartDate {

    public static void main(String[] args) {
        String NS;

        // Lire le modèle
        Model model = JenaEngine.readModel("data/test.owl");

        if (model != null) {
            // Récupérer le namespace de l’ontologie ou définir un namespace par défaut
            NS = model.getNsPrefixURI("");
            if (NS == null || NS.isEmpty()) {
                NS = "http://www.semanticweb.org/msi/ontologies/2024/9/untitled-ontology-8#";
            }

            // Requête SPARQL pour récupérer les projets avec leurs dates
            String sparqlSelect = "PREFIX ns: <" + NS + ">\n" +
                    "SELECT ?project ?startDate ?endDate WHERE {\n" +
                    "    ?project a ns:Projet .\n" +
                    "    ?project ns:dateDebut ?startDate .\n" +
                    "    ?project ns:dateFin ?endDate .\n" +  // Corrigez ici "DateFin" -> "dateFin"
                    "} ORDER BY ?startDate";

            // Exécuter la requête
            try (QueryExecution qexec = QueryExecutionFactory.create(sparqlSelect, model)) {
                ResultSet results = qexec.execSelect();
                LocalDate today = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

                while (results.hasNext()) {
                    QuerySolution solution = results.next();

                    // Récupération des valeurs
                    String project = solution.getResource("project").getURI();
                    String startDate = solution.getLiteral("startDate").getString();
                    String endDateString = solution.getLiteral("endDate").getString();

                    // Conversion de la date de fin pour la comparaison
                    LocalDate endDate = LocalDate.parse(endDateString, formatter);

                    // Affichage selon l'état du projet
                    if (endDate.isBefore(today)) {
                        System.out.println("Project: " + project + ", Start Date: " + startDate + ", End Date: " + endDateString + " - Projet terminé");
                    } else {
                        System.out.println("Project: " + project + ", Start Date: " + startDate + ", End Date: " + endDateString + " - Projet en cours");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Erreur lors de la lecture du modèle depuis l'ontologie");
        }
    }
}
