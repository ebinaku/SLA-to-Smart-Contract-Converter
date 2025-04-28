package qsla.core;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.*;
import static org.junit.Assert.*;
import java.util.Set;

public class QSLAParserTest {
    private QSLAParser parser;

    @Before
    public void setUp() throws Exception {
        parser = new QSLAParser();
        System.out.println("\n=== Testing Q-SLA Parser ===");
    }

    @Test
    public void testOntologyConnection() {
        OntologyManager om = parser.getOntologyManager();
        assertNotNull("Ontology should be loaded", om.getOntology());
        assertEquals("Should have correct ontology IRI", 
            "http://www.ics.forth.gr/ontologies/owl-q/owlq",
            om.getOntology().getOntologyID().getOntologyIRI().get().toString());
    }

    @Test
    public void testQSLAComponents() {
        System.out.println("\nTesting Q-SLA Components:");
        OntologyManager om = parser.getOntologyManager();
        
        // Test core Q-SLA classes exist
        OWLClass slaClass = om.getClass("SLA");
        OWLClass slClass = om.getClass("SL");
        OWLClass sloClass = om.getClass("SLO");
        
        assertNotNull("SLA class exists", slaClass);
        assertNotNull("Service Level class exists", slClass);
        assertNotNull("SLO class exists", sloClass);
        
        System.out.println("✓ Core Q-SLA classes found");
    }

    @Test
    public void testQSLAProperties() {
        System.out.println("\nTesting Q-SLA Properties:");
        OntologyManager om = parser.getOntologyManager();
        
        // Test core Q-SLA properties exist
        assertNotNull("serviceLevel property exists", 
            om.getObjectProperty("serviceLevel"));
        assertNotNull("constraint property exists", 
            om.getObjectProperty("constraint"));
        assertNotNull("metric property exists", 
            om.getObjectProperty("metric"));
        
        System.out.println("✓ Core Q-SLA properties found");
    }

    @Test
    public void testOperatorAvailability() {
        System.out.println("\nTesting Q-SLA Operators:");
        Set<OWLNamedIndividual> individuals = parser.getOntologyManager()
            .getOntology().getIndividualsInSignature();
        
        // Test essential operators exist
        assertTrue("AND operator exists", 
            individuals.stream().anyMatch(i -> i.getIRI().getShortForm().equals("AND")));
        assertTrue("OR operator exists",
            individuals.stream().anyMatch(i -> i.getIRI().getShortForm().equals("OR")));
        assertTrue("LESS_THAN operator exists",
            individuals.stream().anyMatch(i -> i.getIRI().getShortForm().equals("LESS_THAN")));
        
        System.out.println("✓ Core Q-SLA operators found");
    }
}
