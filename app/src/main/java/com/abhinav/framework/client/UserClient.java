package com.abhinav.framework.client;

import com.abhinav.framework.config.EnvironmentConfig;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class UserClient {

  private static final String BASE_URL = EnvironmentConfig.getBaseUrl();

  /** Get user details by ID */
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

  /** Example POST method (for future use) */
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
