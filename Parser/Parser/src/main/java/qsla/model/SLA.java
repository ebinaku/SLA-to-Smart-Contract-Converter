package qsla.model;

import org.semanticweb.owlapi.model.*;
import qsla.core.OntologyManager;
import java.util.*;

public class SLA {
    private final OntologyManager ontologyManager; //references the ontology manager for accessing the ontology
    private final OWLNamedIndividual individual; //the actual SLA instance in OWL
    private final List<ServiceLevel> serviceLevels; //list of SL for this SLA
    private final String name; //name identifier for the SLA

    // Only parsing constructor
    public SLA(String name, OntologyManager ontologyManager, OWLNamedIndividual individual) {
        this.name = name;
        this.ontologyManager = ontologyManager;
        this.individual = individual;
        this.serviceLevels = loadServiceLevels();
    }

    //method to load the SL
    private List<ServiceLevel> loadServiceLevels() {
    	
    	//gets the hasServiceLevel property from the ontology
        OWLObjectProperty hasServiceLevel = ontologyManager.getObjectProperty("hasServiceLevel");
        
        //uses reasoner to find all the SL individuals connected 
        Set<OWLNamedIndividual> slIndividuals = ontologyManager.getReasoner()
            .getObjectPropertyValues(individual, hasServiceLevel).getFlattened();
        
        //saves them as objects in a list
        List<ServiceLevel> serviceLevels = new ArrayList<>();
        
        //for each SL individual found
        for (OWLNamedIndividual slIndividual : slIndividuals) {
            
        	//get the name from the IRI
        	String slName = slIndividual.getIRI().getShortForm();
            
        	//create the SL object 
        	ServiceLevel serviceLevel = new ServiceLevel(slName, ontologyManager, slIndividual);
            
        	//add it to the list
        	serviceLevels.add(serviceLevel);
        }
        //return the list
        return serviceLevels;
    }

    // Only getters needed for parsing
    public List<ServiceLevel> getServiceLevels() { return serviceLevels; }
    public String getName() { return name; }
    public OWLNamedIndividual getIndividual() { return individual; }

    //display the SLA structure in a better, readable format
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SLA: ").append(name).append("\n");
        for (ServiceLevel sl : serviceLevels) {
            sb.append(sl.toString()).append("\n");
        }
        return sb.toString();
    }
}
