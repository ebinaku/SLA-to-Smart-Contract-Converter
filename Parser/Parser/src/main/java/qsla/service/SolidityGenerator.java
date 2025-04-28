package qsla.service;

import qsla.model.*;
import java.util.*;

public class SolidityGenerator {
    
    /**
     * Generate Solidity contracts from the parsed SLA data
     */
    public Map<String, String> generateContracts(SLA sla) {
        Map<String, String> contracts = new HashMap<>();
        
        // Generate SLA contract initialization
        contracts.put("SLA.sol", generateSLAContract(sla));
        
        // Generate deployment script
        contracts.put("deployment-script.js", generateDeploymentScript(sla));
        
        return contracts;
    }
    
    /**
     * Generate the SLA Solidity contract parameters
     */
    private String generateSLAContract(SLA sla) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("// SPDX-License-Identifier: MIT\n");
        sb.append("pragma solidity ^0.8.20;\n\n");
        sb.append("import \"./SL.sol\";\n");
        sb.append("import \"./SLO.sol\";\n\n");
        
        sb.append("/**\n");
        sb.append(" * @title SLA Contract Generated from " + sla.getName() + "\n");
        sb.append(" */\n");
        sb.append("contract SLA {\n");
        
        // SLA identification
        sb.append("    // SLA identification\n");
        sb.append("    bytes32 public id = 0x").append(Integer.toHexString(sla.getName().hashCode())).append(";\n");
        sb.append("    string public name = \"").append(sla.getName()).append("\";\n");
        sb.append("    string public description = \"Generated from ontology\";\n\n");
        
        // Validity period - using current time plus one year as example
        sb.append("    // Validity period\n");
        sb.append("    uint256 public validFrom = ").append(System.currentTimeMillis() / 1000).append(";\n");
        sb.append("    uint256 public validUntil = ").append((System.currentTimeMillis() / 1000) + 31536000).append("; // Valid for 1 year\n\n");
        
        // Parties involved
        sb.append("    // Parties involved\n");
        sb.append("    address public serviceProvider;\n");
        sb.append("    address public serviceConsumer;\n\n");
        
        // SLA status
        sb.append("    // SLA status\n");
        sb.append("    enum Status { DRAFT, ACTIVE, SUSPENDED, TERMINATED }\n");
        sb.append("    Status public status = Status.DRAFT;\n\n");
        
        // Service levels
        sb.append("    // Connected Service Levels\n");
        sb.append("    address[] public serviceLevels;\n");
        
        // Add service levels based on what's in the SLA
        sb.append("\n    // Service Level details from ontology\n");
        for (ServiceLevel sl : sla.getServiceLevels()) {
            sb.append("    // Service Level: ").append(sl.getName()).append("\n");
            sb.append("    //   SLOs: ").append(sl.getSLOs().size()).append("\n");
            
            for (SLO slo : sl.getSLOs()) {
                sb.append("    //     - SLO: ").append(slo.getName());
                if (slo.getMetric() != null) {
                    sb.append(", Metric: ").append(slo.getMetric().getName());
                }
                if (!slo.getThreshold().isEmpty()) {
                    sb.append(", Threshold: ").append(slo.getThreshold());
                }
                sb.append("\n");
            }
            sb.append("\n");
        }
        
        // Constructor
        sb.append("    /**\n");
        sb.append("     * Constructor for SLA\n");
        sb.append("     */\n");
        sb.append("    constructor(address _serviceProvider, address _serviceConsumer) {\n");
        sb.append("        serviceProvider = _serviceProvider;\n");
        sb.append("        serviceConsumer = _serviceConsumer;\n");
        sb.append("    }\n\n");
        
        // Add a basic function for adding service levels
        sb.append("    /**\n");
        sb.append("     * Add a Service Level to this SLA\n");
        sb.append("     */\n");
        sb.append("    function addServiceLevel(address slAddress) public {\n");
        sb.append("        require(slAddress != address(0), \"Invalid service level address\");\n");
        sb.append("        serviceLevels.push(slAddress);\n");
        sb.append("    }\n");
        
        // End contract
        sb.append("}\n");
        
        return sb.toString();
    }
    
    /**
     * Generate a deployment script
     */
    private String generateDeploymentScript(SLA sla) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("// Deployment script for SLA: ").append(sla.getName()).append("\n\n");
        
        sb.append("async function main() {\n");
        sb.append("    const [deployer] = await ethers.getSigners();\n");
        sb.append("    console.log(\"Deploying contracts with the account:\", deployer.address);\n\n");
        
        // Deploy SLA
        sb.append("    // 1. Deploy SLA Contract\n");
        sb.append("    console.log(\"Deploying SLA...\");\n");
        sb.append("    const SLA = await ethers.getContractFactory(\"SLA\");\n");
        sb.append("    const sla = await SLA.deploy(deployer.address, deployer.address);\n");
        sb.append("    await sla.deployed();\n");
        sb.append("    console.log(\"SLA deployed to:\", sla.address);\n\n");
        
        // Add service levels from the SLA
        for (ServiceLevel sl : sla.getServiceLevels()) {
            String tierName = determineTier(sl);
            
            sb.append("    // Deploy ").append(tierName).append(" Service Level\n");
            sb.append("    console.log(\"Deploying ").append(tierName).append(" Service Level...\");\n");
            sb.append("    const SL = await ethers.getContractFactory(\"SL\");\n");
            sb.append("    const ").append(tierName.toLowerCase()).append("SL = await SL.deploy(\n");
            sb.append("        SL.").append(tierName).append("_SERVICE_LEVEL_ID,\n");
            sb.append("        100, // minPrice\n");
            sb.append("        1000 // maxPrice\n");
            sb.append("    );\n");
            sb.append("    await ").append(tierName.toLowerCase()).append("SL.deployed();\n");
            sb.append("    console.log(\"").append(tierName).append(" SL deployed to:\", ").append(tierName.toLowerCase()).append("SL.address);\n\n");
            
            sb.append("    // Add ").append(tierName).append(" Service Level to SLA\n");
            sb.append("    await sla.addServiceLevel(").append(tierName.toLowerCase()).append("SL.address);\n");
            sb.append("    console.log(\"Added ").append(tierName).append(" SL to SLA\");\n\n");
            
            // Generate SLOs for this service level
            for (SLO slo : sl.getSLOs()) {
                String sloName = sanitizeName(slo.getName());
                sb.append("    // Deploy SLO: ").append(slo.getName()).append("\n");
                sb.append("    console.log(\"Deploying SLO: ").append(slo.getName()).append("...\");\n");
                sb.append("    const SLO = await ethers.getContractFactory(\"SLO\");\n");
                sb.append("    const ").append(sloName).append(" = await SLO.deploy(\n");
                sb.append("        ethers.utils.formatBytes32String(\"").append(slo.getName()).append("\"), // id\n");
                sb.append("        \"").append(slo.getMetric() != null ? slo.getMetric().getName() : "default").append("\", // firstArg\n");
                sb.append("        ").append(slo.getThreshold().isEmpty() ? "100" : slo.getThreshold()).append(", // secondArg\n");
                sb.append("        \"GREATER_THAN\", // operator\n");
                sb.append("        10, // compensation\n");
                sb.append("        ").append(tierName.toLowerCase()).append("SL.address // serviceLevel\n");
                sb.append("    );\n");
                sb.append("    await ").append(sloName).append(".deployed();\n");
                sb.append("    console.log(\"SLO deployed to:\", ").append(sloName).append(".address);\n\n");
                
                sb.append("    // Add SLO reference to Service Level\n");
                sb.append("    await ").append(tierName.toLowerCase()).append("SL.addSLOReference(").append(sloName).append(".address);\n");
                sb.append("    console.log(\"Added SLO to ").append(tierName).append(" SL\");\n\n");
            }
        }
        
        sb.append("    console.log(\"Deployment complete!\");\n");
        sb.append("}\n\n");
        
        sb.append("main()\n");
        sb.append("    .then(() => process.exit(0))\n");
        sb.append("    .catch((error) => {\n");
        sb.append("        console.error(error);\n");
        sb.append("        process.exit(1);\n");
        sb.append("    });\n");
        
        return sb.toString();
    }
    
    // Helper methods
    
    private String determineTier(ServiceLevel sl) {
        String name = sl.getName().toUpperCase();
        if (name.contains("GOLD")) {
            return "GOLD";
        } else if (name.contains("SILVER")) {
            return "SILVER";
        } else {
            return "BRONZE";
        }
    }
    
    private String sanitizeName(String name) {
        return "slo" + name.replaceAll("[^a-zA-Z0-9]", "");
    }
}