package config;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static Properties props = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties"))  //config.properties is in the resources folder
         {
            if (input != null) {
                props.load(input);
            } else {
                System.out.println("config.properties not found in classpath, using defaults.");
            }
        } catch (Exception e) {
            System.out.println("Could not load config.properties, using defaults.");
        }
    }

    public static String get(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}
