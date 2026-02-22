package com.abhinav.framework.controller;

import com.abhinav.framework.dto.PostmanEchoRequestDto;
import com.abhinav.framework.dto.PostmanEchoResponseDto;
import com.abhinav.framework.enums.PostmanEchoApi;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class PostmanEchoController {

  private static final String BASE_URL = "https://postman-echo.com";

  public RequestSpecification getRequestSpec() {
    return new RequestSpecBuilder()
        .setBaseUri(BASE_URL)
        .addHeader("Custom-Header", "test123")
        .setContentType(ContentType.JSON)
        .setAccept(ContentType.JSON)
        .setBody(PostmanEchoRequestDto.createDefault()) // JAVA OBJECT -> JSON (SERIALIZATION)
        .build();
  }

  public ResponseSpecification getResponseSpec() {
    return new ResponseSpecBuilder()
        .expectStatusCode(200)
        .expectContentType(ContentType.JSON)
        .build();
  }

  public PostmanEchoResponseDto postRequest() {
    return RestAssured.given()
        .spec(getRequestSpec())
        .when()
        .post(PostmanEchoApi.POST.getPath())
        .then()
        .spec(getResponseSpec())
        .extract()
        .as(PostmanEchoResponseDto.class); // JSON -> DTO (DESERIALIZATION)
  }
}
