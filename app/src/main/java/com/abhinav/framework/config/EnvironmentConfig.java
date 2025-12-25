package com.abhinav.framework.config;

public class EnvironmentConfig {

    private static final String ENV = System.getProperty("env", "qa");

    public static String getBaseUrl() {
        switch (ENV) {
            case "prod":
                return "https://reqres.in/api";
            case "qa":
            default:
                return "https://jsonplaceholder.typicode.com";
        }
    }
}
