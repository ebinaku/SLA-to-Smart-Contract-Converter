package qsla.model;

import org.semanticweb.owlapi.model.*;
import qsla.core.OntologyManager;
import java.util.*;

public class ServiceLevel {
    private final OntologyManager ontologyManager; //helps accessing  the ontology
    private final OWLNamedIndividual individual; //reference of the SL in the ontology
    private final List<SLO> slos; //list for the SLOs related to this SL
    private final String name; //SL identifier

    // Only parsing constructor
    public ServiceLevel(String name, OntologyManager ontologyManager, OWLNamedIndividual individual) {
        this.name = name;
        this.ontologyManager = ontologyManager;
        this.individual = individual;
        this.slos = loadSLOs();
    }

    private List<SLO> loadSLOs() {
    	
    	//gets the hasConstarints property that links the SL to SLOs
        OWLObjectProperty hasConstraint = ontologyManager.getObjectProperty("hasConstraint");
        
        //uses the reasoner to find the SLO individuals connected through that property
        Set<OWLNamedIndividual> sloIndividuals = ontologyManager.getReasoner()
            .getObjectPropertyValues(individual, hasConstraint).getFlattened();
        
        //a list to store the SLOs
        List<SLO> slos = new ArrayList<>();
        
        //for each SLO found
        for (OWLNamedIndividual sloIndividual : sloIndividuals) {
        	
        	//get the SLO name from the IRI
            String sloName = sloIndividual.getIRI().getShortForm();
            
            //check if it is dslo or otherwise rslo
            boolean isDerived = isIndividualOfClass(sloIndividual, ontologyManager.getClass("dslo"));
            
            //load the associated metric and threshold
            Metric metric = loadMetric(sloIndividual);
            String threshold = loadThreshold(sloIndividual);
            
            //create the SLO object and add it to the list
            SLO slo = new SLO(sloName, metric, threshold, isDerived, ontologyManager, sloIndividual);
            slos.add(slo);
        }
        return slos;
    }

    
    
    private Metric loadMetric(OWLNamedIndividual sloIndividual) {
    	
    	//gets the hasMetric property
        OWLObjectProperty hasMetric = ontologyManager.getObjectProperty("hasMetric");
        
        //uses reasoner to find the metric individual
        Set<OWLNamedIndividual> metrics = ontologyManager.getReasoner()
            .getObjectPropertyValues(sloIndividual, hasMetric).getFlattened();
        
        if (!metrics.isEmpty()) {
            OWLNamedIndividual metricIndividual = metrics.iterator().next();
            String metricName = metricIndividual.getIRI().getShortForm();
            
            //checks if the metric is composite otherwise raw
            boolean isComposite = isIndividualOfClass(metricIndividual, 
                ontologyManager.getClass("compositemetric"));
            
            return new Metric(metricName, isComposite, ontologyManager, metricIndividual);
        }
        return null;
    }

    private String loadThreshold(OWLNamedIndividual sloIndividual) {
    	
    	//get the hasThreshold property
        OWLDataProperty hasThreshold = ontologyManager.getDataProperty("hasThreshold");
        
        //use reasoner to find it and get the value
        Set<OWLLiteral> thresholds = ontologyManager.getReasoner()
            .getDataPropertyValues(sloIndividual, hasThreshold);
        return thresholds.isEmpty() ? "" : thresholds.iterator().next().getLiteral();
    }

    private boolean isIndividualOfClass(OWLNamedIndividual individual, OWLClass owlClass) {
        return ontologyManager.getReasoner().getTypes(individual, true).containsEntity(owlClass);
    }

    // Only getters needed for parsing
    public List<SLO> getSLOs() { return slos; }
    public String getName() { return name; }
    public OWLNamedIndividual getIndividual() { return individual; }

    //toString method to better display the SL (in a mo)
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Service Level: ").append(name).append("\n");
        for (SLO slo : slos) {
            sb.append("\t").append(slo.toString()).append("\n");
        }
        return sb.toString();
    }
}
