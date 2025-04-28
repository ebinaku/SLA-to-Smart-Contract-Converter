package qsla.core; // Use the same package as your QSLAParser

import qsla.model.SLA;
import qsla.model.ServiceLevel;
import java.util.List;

public class ParserTest {
    public static void main(String[] args) throws Exception {
        // Create a parser instance
        QSLAParser parser = new QSLAParser();
        
        // Get all SLAs
        List<SLA> slas = parser.parseSLAs();
        System.out.println("Found " + slas.size() + " SLAs");
        
        // For each SLA, get its service levels
        for (SLA sla : slas) {
            System.out.println("SLA: " + sla.getName());
            List<ServiceLevel> levels = sla.getServiceLevels();
            
            System.out.println("  Found " + levels.size() + " Service Levels");
            for (ServiceLevel level : levels) {
                System.out.println("  Service Level: " + level.getName());
                // You can add more detail here if needed
            }
        }
    }
}