package qsla.model;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.*;
import qsla.core.OntologyManager;
import static org.junit.Assert.*;

public class SLOTest {
    private OntologyManager ontologyManager;

    @Before
    public void setUp() throws Exception {
        ontologyManager = new OntologyManager();
    }

    @Test
    public void testSLOStructure() {
        // Verify SLO class exists
        OWLClass sloClass = ontologyManager.getClass("SLO");
        assertNotNull("SLO class should exist", sloClass);
        
        // Check required properties exist
        OWLObjectProperty hasMetric = ontologyManager.getObjectProperty("metric");
        assertNotNull("metric property should exist", hasMetric);
        
        OWLDataProperty hasThreshold = ontologyManager.getDataProperty("threshold");
        assertNotNull("threshold property should exist", hasThreshold);
    }

    @Test
    public void testSLOClassification() {
        OWLClass sloClass = ontologyManager.getClass("SLO");
        OWLClass simpleConstraintClass = ontologyManager.getClass("SimpleConstraint");
        
        // Fix: use getSubClasses() to check hierarchy
        assertTrue("SLO should be subclass of SimpleConstraint",
            ontologyManager.getReasoner().getSubClasses(simpleConstraintClass, true)
                .containsEntity(sloClass));
    }
}
