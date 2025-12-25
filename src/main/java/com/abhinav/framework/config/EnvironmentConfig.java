package com.abhinav.framework.config;

import java.io.InputStream;
import java.util.Properties;

/*
 * ---------------------------------------------------------------------------------------------------------------------
 * File: EnvironmentConfig.java
 *
 * Purpose:
 * This class is responsible for loading and providing access to environment-specific configuration properties
 * (like Base URLs, API Keys, Database credentials) from a central `application.properties` file.
 *
 * Why we need this:
 * In a real-world project, we run tests against multiple environments (QA, Dev, Stage, Prod).
 * Hardcoding URLs (e.g., "http://qa.server.com") inside test methods is a bad practice because changing the environment
 * would require modifying every single test file.
 *
 * Why this is better (Props/Cons):
 * - Pros:
 *   1. **Separation of Concerns**: Configuration is separated from test logic.
 *   2. **Flexibility**: You can switch environments easily via command line (e.g., `gradle test -Denv=prod`).
 *   3. **Maintainability**: If a URL changes, you update it in ONE place (`application.properties`).
 * - Cons:
 *   1. Minimal overhead of loading a file at runtime.
 *
 * Summary:
 * This file loads the properties file once (using a static block) and provides a method `getBaseUrl()`
 * to figure out which environment URL to return based on the "env" system property.
 * ---------------------------------------------------------------------------------------------------------------------
 */
public class EnvironmentConfig {

    // Properties object to hold key-value pairs from the config file
    private static final Properties properties = new Properties();

    /*
     * Static Block:
     * This block runs ONLY ONCE when the class is loaded into memory.
     * We use this to load the `application.properties` file from the
     * `src/main/resources` folder.
     */
    static {
        try (InputStream input = EnvironmentConfig.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            // Check if file exists, if so, load it into the properties object
            if (input == null) {
                throw new RuntimeException("Sorry, unable to find application.properties");
            }
            properties.load(input);
        } catch (Exception e) {
            // Throw a runtime exception if something goes wrong during loading (e.g., file
            // not found)
            throw new RuntimeException("Failed to load environment configuration", e);
        }
    }

    /*
     * Method: getBaseUrl
     * Purpose: Retrieve the base URL based on the current active environment.
     *
     * Logic:
     * 1. Check if the user passed an environment via command line (e.g.,
     * -Denv=dev).
     * 2. If not, fallback to the default 'env' defined in application.properties.
     * 3. Use that environment key (e.g., 'qa') to look up the specific URL (e.g.,
     * 'qa.base.url').
     */
    public static String getBaseUrl() {
        // Step 1: Get the current environment (defaulting to value in file if not
        // provided)
        String env = System.getProperty("env", properties.getProperty("env"));

        // Step 2: Return the specific URL for that environment
        return properties.getProperty(env + ".base.url");
    }
}
