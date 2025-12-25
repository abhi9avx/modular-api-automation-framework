package com.abhinav.framework.config;

import java.io.InputStream;
import java.util.Properties;

public class EnvironmentConfig {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input =
                     EnvironmentConfig.class
                             .getClassLoader()
                             .getResourceAsStream("application.properties")) {

            properties.load(input);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load environment configuration", e);
        }
    }

    public static String getBaseUrl() {
        String env = System.getProperty("env", properties.getProperty("env"));
        return properties.getProperty(env + ".base.url");
    }
}
