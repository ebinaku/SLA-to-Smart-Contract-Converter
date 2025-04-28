package qsla.core;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import openllet.owlapi.OpenlletReasonerFactory;
import qsla.config.OntologyConfig;

import java.io.File;

public class OntologyManager {
    private final OWLOntologyManager manager;
    private final OWLOntology ontology;
    private final OWLDataFactory factory;
    private final DefaultPrefixManager prefixManager;
    private final OWLReasoner reasoner;

    //the constructor
    
    public OntologyManager() throws OWLOntologyCreationException {
    	//gets the configuration from the properties files
        String ontologyPath = OntologyConfig.getProperty("ontology.file.path");
        String defaultPrefix = OntologyConfig.getProperty("ontology.base.url");
        
        //creates the OWL API Manager
        this.manager = OWLManager.createOWLOntologyManager();
        
        //Loads the ontology from file
        File ontologyFile = new File(ontologyPath);
        this.ontology = manager.loadOntologyFromOntologyDocument(ontologyFile);
        
        //creates the factory for the owl entities
        this.factory = manager.getOWLDataFactory();
        
        //sets up the prefix manager for IRIs
        this.prefixManager = new DefaultPrefixManager(defaultPrefix);
        
        //Initializes Pellet reasoner
        OWLReasonerFactory reasonerFactory = OpenlletReasonerFactory.getInstance();
        this.reasoner = reasonerFactory.createReasoner(ontology);
    }

    // Methods for accessing ontology elements
    
    //Gets the owl class by name using the prefix manager
    public OWLClass getClass(String className) {
        return factory.getOWLClass(IRI.create(prefixManager.getDefaultPrefix() + className));
    }

    //gets the object properties a.k.a the relationships between the individuals
    public OWLObjectProperty getObjectProperty(String propertyName) {
        return factory.getOWLObjectProperty(IRI.create(prefixManager.getDefaultPrefix() + propertyName));
    }

    //gets the data property a.k.a the attributes of individuals 
    public OWLDataProperty getDataProperty(String propertyName) {
        return factory.getOWLDataProperty(IRI.create(prefixManager.getDefaultPrefix() + propertyName));
    }

    
//    public OWLNamedIndividual createIndividual(String name, OWLClass owlClass) {
//        OWLNamedIndividual individual = factory.getOWLNamedIndividual(
//            IRI.create(prefixManager.getDefaultPrefix() + name));
//        OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClass, individual);
//        manager.addAxiom(ontology, classAssertion);
//        return individual;
//    }
//
//    public void addObjectPropertyAxiom(OWLNamedIndividual subject, OWLObjectProperty property, OWLNamedIndividual object) {
//        OWLObjectPropertyAssertionAxiom propertyAssertion = 
//            factory.getOWLObjectPropertyAssertionAxiom(property, subject, object);
//        manager.addAxiom(ontology, propertyAssertion);
//    }
//
//    public void addDataPropertyAxiom(OWLNamedIndividual subject, OWLDataProperty property, String value) {
//        OWLLiteral literal = factory.getOWLLiteral(value);
//        OWLDataPropertyAssertionAxiom propertyAssertion = 
//            factory.getOWLDataPropertyAssertionAxiom(property, subject, literal);
//        manager.addAxiom(ontology, propertyAssertion);
//    }
//
//   
//    public OWLLiteral createTypedLiteral(String value) {
//        return factory.getOWLLiteral(value);
//    }
//
//    public OWLLiteral createTypedLiteral(int value) {
//        return factory.getOWLLiteral(value);
//    }
//
//    public OWLLiteral createTypedLiteral(double value) {
//        return factory.getOWLLiteral(value);
//    }
//
//    public OWLLiteral createTypedLiteral(boolean value) {
//        return factory.getOWLLiteral(value);
//    }

    // Getters
    public OWLReasoner getReasoner() { return reasoner; }



    public OWLOntology getOntology() {
        return ontology;
    }

    public OWLDataFactory getFactory() {
        return factory;
    }

//    public DefaultPrefixManager getPrefixManager() {
//        return prefixManager;
//    }
    
 // 
//  public OWLOntologyManager getManager() {
//      return manager;
//  }
}
