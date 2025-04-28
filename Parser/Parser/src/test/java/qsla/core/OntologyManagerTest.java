package qsla.core;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.*;
import static org.junit.Assert.*;

public class OntologyManagerTest {
    private OntologyManager ontologyManager;

    @Before
    public void setUp() throws Exception {
        ontologyManager = new OntologyManager();
    }

    @Test
    public void testOntologyLoading() {
        assertNotNull("Ontology should be loaded", ontologyManager.getOntology());
        assertNotNull("Reasoner should be initialized", ontologyManager.getReasoner());
    }

    @Test
    public void testGetClass() {
        OWLClass slaClass = ontologyManager.getClass("sla");
        assertNotNull("Should get SLA class", slaClass);
        assertTrue("Class IRI should contain 'sla'", slaClass.getIRI().toString().contains("sla"));
    }

    @Test
    public void testGetObjectProperty() {
        OWLObjectProperty hasServiceLevel = ontologyManager.getObjectProperty("hasServiceLevel");
        assertNotNull("Should get hasServiceLevel property", hasServiceLevel);
    }

    @Test
    public void testGetDataProperty() {
        OWLDataProperty hasThreshold = ontologyManager.getDataProperty("hasThreshold");
        assertNotNull("Should get hasThreshold property", hasThreshold);
    }

    @Test
    public void testGetFactory() {
        assertNotNull("Should get OWL factory", ontologyManager.getFactory());
    }

    @Test
    public void testGetReasoner() {
        assertNotNull("Should get reasoner", ontologyManager.getReasoner());
    }
}