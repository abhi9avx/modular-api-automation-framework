package com.abhinav.framework.controller;

import com.abhinav.framework.dto.ComplexResponseDto;
import com.abhinav.framework.dto.HttpBinResponseDto;
import com.abhinav.framework.enums.HttpBinApi;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.SneakyThrows;

public class HttpBinController {

  private static final String BASE_URL = "https://httpbin.org";

  @SneakyThrows
  private RequestSpecification getRequestSpec() {
    String jsonBody =
        new String(Files.readAllBytes(Paths.get("src/main/resources/httpsBinPost.json")));
    return new RequestSpecBuilder()
        .setBaseUri(BASE_URL)
        .setContentType(ContentType.JSON)
        .setBody(jsonBody)
        .build();
  }

  private ResponseSpecification getResponseSpec() {
    return new ResponseSpecBuilder()
        .expectStatusCode(200)
        .expectContentType(ContentType.JSON)
        .build();
  }

  public HttpBinResponseDto postHttpBin() {
    // Dispatches the request, asserts 200 OK, and maps directly to the response DTO
    return RestAssured.given()
        .spec(getRequestSpec())
        .post(HttpBinApi.POST.getPath())
        .then()
        .spec(getResponseSpec())
        .extract()
        .as(HttpBinResponseDto.class);
  }

  private RequestSpecification getRequestSpecForPayload(Object payload) {
    return new RequestSpecBuilder()
        .setBaseUri(BASE_URL)
        .setContentType(ContentType.JSON)
        .setBody(payload)
        .build();
  }

  public ComplexResponseDto postComplexPayload(Object payload) {
    return RestAssured.given()
        .spec(getRequestSpecForPayload(payload))
        .post(HttpBinApi.POST.getPath())
        .then()
        .spec(getResponseSpec())
        .extract()
        .as(ComplexResponseDto.class);
  }
}
