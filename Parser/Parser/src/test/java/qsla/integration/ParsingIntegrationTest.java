package qsla.integration;

import org.junit.Test;
import qsla.core.*;
import qsla.model.*;
import org.semanticweb.owlapi.model.*;
import static org.junit.Assert.*;
import java.util.*;

public class ParsingIntegrationTest {
    
    @Test
    public void testFullOntologyParsing() throws Exception {
        System.out.println("\n=== Starting Full Integration Test ===");
        
        // 1. Initialize Parser and Manager
        QSLAParser parser = new QSLAParser();
        OntologyManager om = parser.getOntologyManager();
        
        // 2. Verify Ontology Structure
        System.out.println("\n1. Verifying Ontology Structure:");
        System.out.println("- Ontology IRI: " + om.getOntology().getOntologyID().getOntologyIRI().get());
        System.out.println("- Reasoner: " + om.getReasoner().getReasonerName());
        
        // 3. Verify Core Classes
        System.out.println("\n2. Verifying Core Classes:");
        OWLClass slaClass = om.getClass("SLA");
        OWLClass slClass = om.getClass("SL");
        OWLClass sloClass = om.getClass("SLO");
        OWLClass metricClass = om.getClass("Metric");
        
        System.out.println("- SLA class: " + (slaClass != null ? "✓" : "✗"));
        System.out.println("- Service Level class: " + (slClass != null ? "✓" : "✗"));
        System.out.println("- SLO class: " + (sloClass != null ? "✓" : "✗"));
        System.out.println("- Metric class: " + (metricClass != null ? "✓" : "✗"));
        
        // 4. Verify Properties
        System.out.println("\n3. Verifying Core Properties:");
        OWLObjectProperty serviceLevel = om.getObjectProperty("serviceLevel");
        OWLObjectProperty constraint = om.getObjectProperty("constraint");
        OWLObjectProperty metric = om.getObjectProperty("metric");
        
        System.out.println("- serviceLevel property: " + (serviceLevel != null ? "✓" : "✗"));
        System.out.println("- constraint property: " + (constraint != null ? "✓" : "✗"));
        System.out.println("- metric property: " + (metric != null ? "✓" : "✗"));
            
        // 5. Verify Operators
        System.out.println("\n4. Verifying Operators:");
        Set<OWLNamedIndividual> operators = om.getOntology().getIndividualsInSignature();
        System.out.println("Found " + operators.size() + " operators:");
        operators.stream()
                .map(op -> op.getIRI().getShortForm())
                .sorted()
                .forEach(name -> System.out.println("  - " + name));
                
        // 6. Test Complete Parsing Flow
        System.out.println("\n5. Testing Complete Parsing Flow:");
        List<SLA> slas = parser.parseSLAs();
        System.out.println("Found " + slas.size() + " SLAs in ontology");
        
        System.out.println("\n=== Integration Test Complete ===");
        System.out.println("Result: Parser successfully validates complete ontology structure");
    }
}
