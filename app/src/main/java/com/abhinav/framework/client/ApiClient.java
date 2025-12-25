package com.abhinav.framework.client;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.Map;

/*
 * -----------------------------------------------------------------------------
 * File: ApiClient.java
 * Purpose: Wrapper around RestAssured to handle low-level HTTP requests.
 *
 * Why:
 * - Decouples tests from RestAssured. If we switch libraries later (e.g., to HttpClient), only this file changes.
 * - Centralizes logs, headers, and authentication logic.
 *
 * Pros:
 * - Reusability: Write HTTP logic once, use everywhere.
 * - Clean Code: Tests just call `ApiClient.get()` instead of verbose RestAssured configuration.
 *
 * Cons:
 * - Slight abstraction layer to maintain.
 * -----------------------------------------------------------------------------
 */
public class ApiClient {

    // Generic GET request
    public static Response get(String url, Map<String, String> headers) {
        return RestAssured.given().headers(headers).when().get(url);
    }

    // Generic POST request with body
    public static Response post(String url, Object body, Map<String, String> headers) {
        return RestAssured.given().headers(headers).body(body).when().post(url);
    }

    // Generic PUT request for updates
    public static Response put(String url, Object body, Map<String, String> headers) {
        return RestAssured.given().headers(headers).body(body).when().put(url);
    }

    // Generic DELETE request
    public static Response delete(String url, Map<String, String> headers) {
        return RestAssured.given().headers(headers).when().delete(url);
    }
}
