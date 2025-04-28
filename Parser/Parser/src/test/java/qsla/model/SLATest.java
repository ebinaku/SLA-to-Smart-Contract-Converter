package qsla.model;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.*;
import qsla.core.OntologyManager;
import static org.junit.Assert.*;
import java.util.List;  // This fixes the List resolution error

public class SLATest {
    private OntologyManager ontologyManager;

    @Before
    public void setUp() throws Exception {
        ontologyManager = new OntologyManager();
    }

    @Test
    public void testSLAStructure() {
        // Verify SLA class exists
        OWLClass slaClass = ontologyManager.getClass("SLA");
        assertNotNull("SLA class should exist", slaClass);
        
        // Verify required properties exist
        OWLObjectProperty hasServiceLevel = ontologyManager.getObjectProperty("serviceLevel");
        assertNotNull("serviceLevel property should exist", hasServiceLevel);
    }

    @Test
    public void testServiceLevelsPropertyDomain() {
        OWLObjectProperty hasServiceLevel = ontologyManager.getObjectProperty("serviceLevel");
        OWLClass slaClass = ontologyManager.getClass("SLA");
        OWLClass slClass = ontologyManager.getClass("SL");
        
        // Verify property connections exist
        assertNotNull("SLA class should exist", slaClass);
        assertNotNull("SL class should exist", slClass);
        assertNotNull("serviceLevel property should exist", hasServiceLevel);
    }

    @Test
    public void testSLAHierarchy() {
        OWLClass slaClass = ontologyManager.getClass("SLA");
        OWLClass specificationClass = ontologyManager.getClass("Specification");
        
        assertTrue("SLA should be subclass of Specification",
            ontologyManager.getReasoner().getSubClasses(specificationClass, true)
                .containsEntity(slaClass));
    }
}
