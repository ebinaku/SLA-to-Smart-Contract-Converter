package qsla.model;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.*;
import qsla.core.OntologyManager;
import static org.junit.Assert.*;

public class ServiceLevelTest {
    private OntologyManager ontologyManager;

    @Before
    public void setUp() throws Exception {
        ontologyManager = new OntologyManager();
    }

    @Test
    public void testServiceLevelStructure() {
        // Verify ServiceLevel class exists and has correct properties
        OWLClass slClass = ontologyManager.getClass("SL");
        assertNotNull("ServiceLevel class should exist", slClass);
        
        // Check required properties exist
        OWLObjectProperty hasSLO = ontologyManager.getObjectProperty("constraint");
        assertNotNull("constraint property should exist", hasSLO);
    }

    @Test
    public void testServiceLevelHierarchy() {
        OWLClass slClass = ontologyManager.getClass("SL");
        OWLClass complexConstraintClass = ontologyManager.getClass("ComplexConstraint");
        
        // Fix: use getSubClasses() to check hierarchy
        assertTrue("SL should be subclass of ComplexConstraint",
            ontologyManager.getReasoner().getSubClasses(complexConstraintClass, true)
                .containsEntity(slClass));
    }
}
