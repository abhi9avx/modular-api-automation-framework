package com.abhinav.framework.controller;

import com.abhinav.framework.config.ApiConfig;
import com.abhinav.framework.dto.UserResponseDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class UserAPIController {

  static {
    RestAssured.baseURI = ApiConfig.BASE_URL;
  }

  public static UserResponseDTO getUserById(int userId) {

    return RestAssured.given()
        .contentType(ContentType.JSON)
        .header("x-api-key", ApiConfig.API_KEY)
        .pathParam("id", userId)
        .when()
        .get(ApiConfig.USERS_ENDPOINT)
        .then()
        .statusCode(200)
        .extract()
        .as(UserResponseDTO.class);
  }
}
