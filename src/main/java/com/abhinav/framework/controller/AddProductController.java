package com.abhinav.framework.controller;

import static io.restassured.RestAssured.given;

import com.abhinav.framework.dto.AddProductRequestDto;
import com.abhinav.framework.dto.AddProductResponseDto;
import com.abhinav.framework.enums.ProductAddition;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class AddProductController {

  private static final String BASE_URL = "https://dummyjson.com";

  private static RequestSpecification getRequestSpec() {
    return new RequestSpecBuilder()
        .setBaseUri(BASE_URL)
        .setContentType(ContentType.JSON)
        .setBody(AddProductRequestDto.createDefault())
        .build();
  }

  private static ResponseSpecification getResponseSpec() {
    return new ResponseSpecBuilder()
        .expectStatusCode(201)
        .expectContentType(ContentType.JSON)
        .build();
  }

  public static Response addProductRaw() {
    return given()
        .spec(getRequestSpec())
        .when()
        .post(ProductAddition.ADD_PRODUCT.getPath())
        .then()
        .spec(getResponseSpec())
        .extract()
        .response();
  }

  public static AddProductResponseDto addProduct() {
    return addProductRaw().as(AddProductResponseDto.class);
  }
}
