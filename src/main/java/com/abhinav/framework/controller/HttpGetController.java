package com.abhinav.framework.controller;

import com.abhinav.framework.dto.HttpGetResponse;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class HttpGetController {

  private static RequestSpecification getRequestSpec() {
    return new RequestSpecBuilder()
        .setBaseUri("https://httpbin.org")
        .setContentType(ContentType.JSON)
        .build();
  }

  private static ResponseSpecification getResponseSpec() {
    return new ResponseSpecBuilder()
        .expectStatusCode(200)
        .expectContentType(ContentType.JSON)
        .build();
  }

  public static Response getGetEndpointRaw() {
    return RestAssured.given()
        .spec(getRequestSpec())
        .get("/get")
        .then()
        .spec(getResponseSpec())
        .extract()
        .response();
  }

  public static HttpGetResponse getGetEndpoint() {
    return getGetEndpointRaw().as(HttpGetResponse.class);
  }
}
