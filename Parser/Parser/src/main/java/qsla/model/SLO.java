package qsla.model;

import org.semanticweb.owlapi.model.*;
import qsla.core.OntologyManager;

public class SLO {
    private final OntologyManager ontologyManager; //helps access the ontology
    private final OWLNamedIndividual individual; //SLO individual in the ontology
    private final String name; //name of the SLO
    private final Metric metric; //associated metric
    private final String threshold; //target value
    private final boolean isDerivedSLO;//checks if it is derived or runtime SLO 

    // Only parsing constructor
    public SLO(String name, Metric metric, String threshold, boolean isDerived, 
               OntologyManager ontologyManager, OWLNamedIndividual individual) {
        this.name = name;
        this.metric = metric;
        this.threshold = threshold;
        this.isDerivedSLO = isDerived;
        this.ontologyManager = ontologyManager;
        this.individual = individual;
    }

    // Only getters needed for parsing
    public String getName() { return name; }
    public Metric getMetric() { return metric; }
    public String getThreshold() { return threshold; }
    public boolean isDerivedSLO() { return isDerivedSLO; }
    public OWLNamedIndividual getIndividual() { return individual; }

    @Override
    public String toString() {
        return String.format("SLO: %s, Metric: %s, Threshold: %s", 
            name, metric.getName(), threshold);
    }
}
