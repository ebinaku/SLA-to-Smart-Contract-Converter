package qsla.config;

import org.junit.Test;
import static org.junit.Assert.*;

public class OntologyConfigTest {
    
    @Test
    public void testLoadBaseConfiguration() {
        String ontologyPath = OntologyConfig.getProperty("ontology.file.path");
        assertNotNull("Should load ontology path", ontologyPath);
        assertTrue("Should contain owl file", ontologyPath.contains(".owl"));
        
        String baseUrl = OntologyConfig.getProperty("ontology.base.url");
        assertNotNull("Should load base URL", baseUrl);
    }
    
    @Test
    public void testClassMappings() {
        // Test only the core classes we need
        assertEquals("SLA", OntologyConfig.getClassIRI("sla"));
        assertEquals("SL", OntologyConfig.getClassIRI("servicelevel"));
        assertEquals("SLO", OntologyConfig.getClassIRI("slo"));
        assertEquals("Metric", OntologyConfig.getClassIRI("metric"));
        assertEquals("RawMetric", OntologyConfig.getClassIRI("rawmetric"));
        assertEquals("CompositeMetric", OntologyConfig.getClassIRI("compositemetric"));
        assertEquals("Constraint", OntologyConfig.getClassIRI("constraint"));
    }
    
    @Test
    public void testPropertyMappings() {
        // Test only the core properties we need
        assertEquals("serviceLevel", OntologyConfig.getPropertyIRI("servicelevel"));
        assertEquals("constraint", OntologyConfig.getPropertyIRI("constraint"));
        assertEquals("metric", OntologyConfig.getPropertyIRI("metric"));
        assertEquals("threshold", OntologyConfig.getPropertyIRI("threshold"));
    }
    
    @Test
    public void testOperatorMappings() {
        // Test core operators
        assertEquals("AND", OntologyConfig.getPropertyIRI("and"));
        assertEquals("OR", OntologyConfig.getPropertyIRI("or"));
        assertEquals("NOT", OntologyConfig.getPropertyIRI("not"));
        assertEquals("LESS_THAN", OntologyConfig.getPropertyIRI("lessthan"));
        assertEquals("GREATER_THAN", OntologyConfig.getPropertyIRI("greaterthan"));
    }
    
    @Test(expected = RuntimeException.class)
    public void testMissingProperty() {
        OntologyConfig.getProperty("non.existent.property");
    }
}
