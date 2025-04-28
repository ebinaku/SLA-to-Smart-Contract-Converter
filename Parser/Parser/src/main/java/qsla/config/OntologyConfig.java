package qsla.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class OntologyConfig {
	
	//configuration loader for ontology settings and manages configuration from the properties file
    private static final Properties properties = new Properties();

    //loads the properties file when class is first loaded and throws exception if file is not found
    static {
        try (InputStream input = OntologyConfig.class.getClassLoader()
                .getResourceAsStream("ontology-config.properties")) {
            if (input == null) {
                throw new RuntimeException("Cannot find ontology-config.properties");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load ontology configuration", e);
        }
    }

    //finds the property name form the properties file = key
    public static String getProperty(String key) {
    	
    	//looks up for the key, throws an exception if null and returns it if found like it is in the properties file
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Missing configuration property: " + key);
        }
        return value;
    }

    //maps ontology class names to Java names
    public static String getClassIRI(String className) {
        return getProperty("ontology.class." + className.toLowerCase());
    }

    //maps property names from the ontology to Java name 
    public static String getPropertyIRI(String propertyName) {
        return getProperty("ontology.property." + propertyName.toLowerCase());
    }
}