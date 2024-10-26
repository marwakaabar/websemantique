
package esprit.marwa;

import esprit.tools.JenaEngine;
import org.apache.jena.rdf.model.Model;
/**
 * @author DO.ITSUDPARIS
 */
public class Main {
    /**
     * @param args
     * the command line arguments
     */
    public static void main(String[] args) {
        String NS = "";
// lire le model a partir d'une ontologie
        Model model = JenaEngine.readModel("data/test.owl");
        if (model != null) {
//lire le Namespace de l’ontologie
            NS = model.getNsPrefixURI("");
// apply our rules on the owlInferencedModel
            Model inferedModel = JenaEngine.readInferencedModelFromRuleFile(model, "data/rules.txt");
// query on the model after inference
            System.out.println(JenaEngine.executeQueryFile(inferedModel,
                    "data/queries.txt"));
        } else {
            System.out.println("Error when reading model from ontology");
        }
    }
}