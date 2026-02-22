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

  private RequestSpecification getRequestSpecForFormData(java.util.Map<String, String> formData) {
    RequestSpecBuilder builder =
        new RequestSpecBuilder().setBaseUri(BASE_URL).setContentType("multipart/form-data");

    formData.forEach(builder::addMultiPart);
    return builder.build();
  }

  public HttpBinResponseDto postFormData(java.util.Map<String, String> formData) {
    return RestAssured.given()
        .spec(getRequestSpecForFormData(formData))
        .post(HttpBinApi.POST.getPath())
        .then()
        .spec(getResponseSpec())
        .extract()
        .as(HttpBinResponseDto.class);
  }

  private RequestSpecification getRequestSpecForFormDataWithFile(
      java.util.Map<String, String> formData, java.io.File file) {
    RequestSpecBuilder builder =
        new RequestSpecBuilder().setBaseUri(BASE_URL).setContentType("multipart/form-data");

    if (formData != null) {
      formData.forEach(builder::addMultiPart);
    }
    if (file != null && file.exists()) {
      builder.addMultiPart("file", file);
    }
    return builder.build();
  }

  public HttpBinResponseDto postFormDataWithFile(
      java.util.Map<String, String> formData, java.io.File file) {
    return RestAssured.given()
        .spec(getRequestSpecForFormDataWithFile(formData, file))
        .post(HttpBinApi.POST.getPath())
        .then()
        .spec(getResponseSpec())
        .extract()
        .as(HttpBinResponseDto.class);
  }

  private RequestSpecification getRequestSpecForUrlEncodedForm(
      java.util.Map<String, String> formData) {
    return new RequestSpecBuilder()
        .setBaseUri(BASE_URL)
        .setContentType(ContentType.URLENC)
        .addFormParams(formData)
        .build();
  }

  public HttpBinResponseDto postUrlEncodedForm(java.util.Map<String, String> formData) {
    return RestAssured.given()
        .spec(getRequestSpecForUrlEncodedForm(formData))
        .post(HttpBinApi.POST.getPath())
        .then()
        .spec(getResponseSpec())
        .extract()
        .as(HttpBinResponseDto.class);
  }
}
