package qsla.model;

import org.semanticweb.owlapi.model.*;
import qsla.core.OntologyManager;

public class Metric {
    private final OntologyManager ontologyManager; //helps access the ontology
    private final OWLNamedIndividual individual; //metric individual in ontology
    private final String name; //metric name 
    private final boolean isComposite; //checks if the metric is composite or otherwise raw

    // Only parsing constructor
    public Metric(String name, boolean isComposite, OntologyManager ontologyManager, 
                 OWLNamedIndividual individual) {
        this.name = name;
        this.isComposite = isComposite;
        this.ontologyManager = ontologyManager;
        this.individual = individual;
    }

    // Only getters needed for parsing
    public String getName() { return name; }
    public boolean isComposite() { return isComposite; }
    public OWLNamedIndividual getIndividual() { return individual; }

    @Override
    public String toString() {
        return String.format("Metric: %s (%s)", 
            name, isComposite ? "Composite" : "Raw");
    }
}
