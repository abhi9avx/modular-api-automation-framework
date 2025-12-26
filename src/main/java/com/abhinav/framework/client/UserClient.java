package com.abhinav.framework.client;

import com.abhinav.framework.config.EnvironmentConfig;
import io.restassured.RestAssured;
import io.restassured.response.Response;

/*
 * -----------------------------------------------------------------------------
 * File: UserClient.java (Legacy/Alternative Approach)
 * Purpose: Direct usage of RestAssured specific to User API.
 *
 * *Note*: In a fully modular framework, we prefer using `UserController` -> `ApiClient`.
 * This file demonstrates how one might write a client if NOT using the generic ApiClient wrapper.
 *
 * Why:
 * - Sometimes specific endpoints need unique configs (timeouts, auth) not shared by others.
 *
 * Summary:
 * Contains methods to interact directly with User endpoints.
 * -----------------------------------------------------------------------------
 */
public class UserClient {

  private static final String BASE_URL = EnvironmentConfig.getBaseUrl();

  // Get user details by ID using direct RestAssured chaining
  public Response getUserById(int userId) {
    return RestAssured.given()
        .baseUri(BASE_URL)
        .header("Content-Type", "application/json")
        .when()
        .get("/users/" + userId)
        .then()
        .extract()
        .response();
  }

  // Create user using direct RestAssured chaining
  public Response createUser(Object requestBody) {
    return RestAssured.given()
        .baseUri(BASE_URL)
        .header("Content-Type", "application/json")
        .body(requestBody)
        .when()
        .post("/users")
        .then()
        .extract()
        .response();
  }
}
