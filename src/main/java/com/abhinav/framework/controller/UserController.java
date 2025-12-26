package com.abhinav.framework.controller;

import com.abhinav.framework.client.ApiClient;
import com.abhinav.framework.config.EnvironmentConfig;
import io.restassured.response.Response;
import java.util.Map;

/*
 * -----------------------------------------------------------------------------
 * File: UserController.java
 * Purpose: Business Logic Layer for User API.
 * Acts as a bridge between Tests and the ApiClient.
 *
 * Why:
 * - Tests should not know *how* a request is sent (HTTP client details).
 * - Tests just want "Create User" or "Get User".
 *
 * Pros:
 * - Modular: If the endpoint URL changes, we fix it here, not in 50 tests.
 * - Simple: Constructs specific URLs and payloads before handing off to ApiClient.
 * -----------------------------------------------------------------------------
 */
public class UserController {

  // Constructs the URL for a specific user and calls the generic GET method
  public Response getUserById(int userId) {
    String url = EnvironmentConfig.getBaseUrl() + "/users/" + userId;
    return ApiClient.get(url, Map.of());
  }

  // Constructs URL for creating a user and calls the generic POST method
  public Response createUser(Object userRequest) {
    String url = EnvironmentConfig.getBaseUrl() + "/users";
    return ApiClient.post(url, userRequest, Map.of("Content-Type", "application/json"));
  }
}
