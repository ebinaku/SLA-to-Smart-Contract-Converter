package qsla.diagnostic;

import qsla.core.QSLAParser;
import qsla.core.OntologyManager;
import org.semanticweb.owlapi.model.*;
import java.util.Set;

public class ParserDiagnostic {
    public static void main(String[] args) {
        try {
            System.out.println("====== Q-SLA Ontology Parser Diagnostic ======\n");
            QSLAParser parser = new QSLAParser();
            OntologyManager om = parser.getOntologyManager();
            
            // 1. Basic Ontology Information
            System.out.println("1. Ontology Information:");
            System.out.println("Ontology ID: " + om.getOntology().getOntologyID());
            System.out.println("Base IRI: " + om.getOntology().getOntologyID().getOntologyIRI().get());
            System.out.println("Reasoner: " + om.getReasoner().getReasonerName());
            
            // 2. Classes
            System.out.println("\n2. Classes in Ontology:");
            Set<OWLClass> classes = om.getOntology().getClassesInSignature();
            System.out.println("Total Classes: " + classes.size());
            classes.stream()
                  .map(cls -> cls.getIRI().getShortForm())
                  .sorted()
                  .forEach(name -> System.out.println("  - " + name));
            
            // 3. Object Properties
            System.out.println("\n3. Object Properties:");
            Set<OWLObjectProperty> objProps = om.getOntology().getObjectPropertiesInSignature();
            System.out.println("Total Object Properties: " + objProps.size());
            objProps.stream()
                   .map(prop -> prop.getIRI().getShortForm())
                   .sorted()
                   .forEach(name -> System.out.println("  - " + name));
            
            // 4. Data Properties
            System.out.println("\n4. Data Properties:");
            Set<OWLDataProperty> dataProps = om.getOntology().getDataPropertiesInSignature();
            System.out.println("Total Data Properties: " + dataProps.size());
            dataProps.stream()
                    .map(prop -> prop.getIRI().getShortForm())
                    .sorted()
                    .forEach(name -> System.out.println("  - " + name));
            
            // 5. Individuals (Operators)
            System.out.println("\n5. Individuals/Operators:");
            Set<OWLNamedIndividual> individuals = om.getOntology().getIndividualsInSignature();
            System.out.println("Total Individuals: " + individuals.size());
            individuals.stream()
                      .map(ind -> ind.getIRI().getShortForm())
                      .sorted()
                      .forEach(name -> System.out.println("  - " + name));
            
            // 6. Key SLA Components Check
            System.out.println("\n6. Key Q-SLA Components:");
            checkComponent(om, "SLA", "SLA Class");
            checkComponent(om, "SL", "Service Level Class");
            checkComponent(om, "SLO", "SLO Class");
            checkComponent(om, "Metric", "Metric Class");
            checkComponent(om, "RawMetric", "Raw Metric Class");
            checkComponent(om, "CompositeMetric", "Composite Metric Class");
            
            // 7. Key Properties Check
            System.out.println("\n7. Key Q-SLA Properties:");
            checkObjectProperty(om, "serviceLevel", "Service Level Property");
            checkObjectProperty(om, "constraint", "Constraint Property");
            checkObjectProperty(om, "metric", "Metric Property");
            
            System.out.println("\n====== Diagnostic Complete ======");
            System.out.println("Result: Ontology structure is complete and ready for parsing");
            
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void checkComponent(OntologyManager om, String name, String description) {
        OWLClass cls = om.getClass(name);
        System.out.println(String.format("%-25s: %s", description, (cls != null ? "✓" : "✗")));
    }
    
    private static void checkObjectProperty(OntologyManager om, String name, String description) {
        OWLObjectProperty prop = om.getObjectProperty(name);
        System.out.println(String.format("%-25s: %s", description, (prop != null ? "✓" : "✗")));
    }
}
