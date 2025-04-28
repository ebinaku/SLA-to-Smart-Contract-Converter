package qsla.core;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.NodeSet;
import qsla.model.*;
import java.util.*;

public class QSLAParser {
    private final OntologyManager ontologyManager;

    public QSLAParser() throws OWLOntologyCreationException {
        this.ontologyManager = new OntologyManager();
    }

    // Main Q-SLA parsing methods
    public List<SLA> parseSLAs() {
        OWLClass slaClass = ontologyManager.getClass("SLA");
        NodeSet<OWLNamedIndividual> slaIndividuals = ontologyManager.getReasoner().getInstances(slaClass, true);
        
        List<SLA> slas = new ArrayList<>();
        for (OWLNamedIndividual slaIndividual : slaIndividuals.getFlattened()) {
            System.out.println("Found SLA: " + slaIndividual.getIRI().getShortForm());
            slas.add(new SLA(slaIndividual.getIRI().getShortForm(), ontologyManager, slaIndividual));
        }
        return slas;
    }

    public List<ServiceLevel> parseServiceLevels(OWLNamedIndividual slaIndividual) {
        Set<OWLNamedIndividual> slIndividuals = getPropertyValues(slaIndividual, "serviceLevel");
        
        List<ServiceLevel> serviceLevels = new ArrayList<>();
        for (OWLNamedIndividual slIndividual : slIndividuals) {
            System.out.println("Found Service Level: " + slIndividual.getIRI().getShortForm());
            serviceLevels.add(new ServiceLevel(slIndividual.getIRI().getShortForm(), ontologyManager, slIndividual));
        }
        return serviceLevels;
    }

    // Helper methods
    private Set<OWLNamedIndividual> getPropertyValues(OWLNamedIndividual individual, String propertyName) {
        OWLObjectProperty property = ontologyManager.getObjectProperty(propertyName);
        return ontologyManager.getReasoner().getObjectPropertyValues(individual, property).getFlattened();
    }

    public OntologyManager getOntologyManager() {
        return ontologyManager;
    }
}
