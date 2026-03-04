package com.abhinav.framework.controller;

import com.abhinav.framework.dto.ExampleDto;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class ExampleController {

  private static RequestSpecification getRequestSpec() {
    return new RequestSpecBuilder()
        .setBaseUri("https://example.com")
        .setContentType(ContentType.JSON)
        .build();
  }

  private static ResponseSpecification getResponseSpec() {
    return new ResponseSpecBuilder()
        .expectStatusCode(200)
        .expectContentType(ContentType.JSON)
        .build();
  }

  public static Response getExampleRaw() {
    return RestAssured.given()
        .spec(getRequestSpec())
        .get("/")
        .then()
        .spec(getResponseSpec())
        .extract()
        .response();
  }

  public static ExampleDto getExample() {
    return getExampleRaw().as(ExampleDto.class);
  }
}
