package com.abhinav.framework.controller;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class GetController {

  private static RequestSpecification getRequestSpec() {
    return new RequestSpecBuilder()
        .setBaseUri("https://httpbin.org")
        .setContentType(ContentType.JSON)
        .build();
  }

  private static ResponseSpecification getResponseSpec() {
    return new ResponseSpecBuilder().expectContentType(ContentType.JSON).build();
  }

  public static Response get() {
    return RestAssured.given()
        .spec(getRequestSpec())
        .when()
        .get("/get")
        .then()
        .spec(getResponseSpec())
        .extract()
        .response();
  }
}
