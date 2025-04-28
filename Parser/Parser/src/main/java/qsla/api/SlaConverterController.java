package qsla.api;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import qsla.core.QSLAParser;
import qsla.model.SLA;
import qsla.service.SolidityGenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sla")
@CrossOrigin(origins = "*") // Allow requests from any origin for testing
public class SlaConverterController {
    
    private final SolidityGenerator solidityGenerator = new SolidityGenerator();
    
    @PostMapping("/convert")
    public ResponseEntity<?> convertOwlToSolidity(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("Received file: " + file.getOriginalFilename());
            
            // Create a temp directory to store the uploaded OWL file
            File tempDir = Files.createTempDirectory("sla-converter").toFile();
            File tempFile = new File(tempDir, "uploaded.owl");
            file.transferTo(tempFile);
            
            System.out.println("Saved file to: " + tempFile.getAbsolutePath());
            
            // Update the property to use the uploaded file
            System.setProperty("ontology.file.path", tempFile.getAbsolutePath());
            
            // Parse the OWL file
            QSLAParser parser = new QSLAParser();
            List<SLA> slas = parser.parseSLAs();
            
            System.out.println("Parsed " + slas.size() + " SLAs from the file");
            
            if (slas.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "No SLAs found in the uploaded file"));
            }
            
            // Generate Solidity contract code for the first SLA
            Map<String, String> contracts = solidityGenerator.generateContracts(slas.get(0));
            
            System.out.println("Generated " + contracts.size() + " contracts");
            
            // Clean up temp files
            tempFile.delete();
            tempDir.delete();
            
            return ResponseEntity.ok(contracts);
            
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Error processing file: " + e.getMessage()));
        } catch (OWLOntologyCreationException e) {
            System.err.println("Ontology Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Error parsing ontology: " + e.getMessage()));
        } catch (Exception e) {
            System.err.println("Unexpected Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Unexpected error: " + e.getMessage()));
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Service is up and running");
    }
}