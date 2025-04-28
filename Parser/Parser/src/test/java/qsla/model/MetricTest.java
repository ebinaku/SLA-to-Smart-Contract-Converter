package qsla.model;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.*;
import qsla.core.OntologyManager;
import static org.junit.Assert.*;

public class MetricTest {
    private OntologyManager ontologyManager;

    @Before
    public void setUp() throws Exception {
        ontologyManager = new OntologyManager();
    }

    @Test
    public void testMetricStructure() {
        // Verify Metric classes exist
        OWLClass metricClass = ontologyManager.getClass("Metric");
        OWLClass rawMetricClass = ontologyManager.getClass("RawMetric");
        OWLClass compositeMetricClass = ontologyManager.getClass("CompositeMetric");
        
        assertNotNull("Metric class should exist", metricClass);
        assertNotNull("RawMetric class should exist", rawMetricClass);
        assertNotNull("CompositeMetric class should exist", compositeMetricClass);
    }

    @Test
    public void testMetricProperties() {
        // Check metric-related properties exist
        OWLObjectProperty formula = ontologyManager.getObjectProperty("formula");
        OWLObjectProperty context = ontologyManager.getObjectProperty("context");
        
        assertNotNull("formula property should exist", formula);
        assertNotNull("context property should exist", context);
    }
}
