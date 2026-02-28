package com.abhinav.framework.controller;

import static io.restassured.RestAssured.given;

import com.abhinav.framework.dto.FakeStoreRequestDto;
import com.abhinav.framework.dto.FakeStoreResponseDto;
import com.abhinav.framework.enums.FakeStoreProduct;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

/**
 * FakeStoreController handles API interactions for the FakeStore products endpoint. Refactored to
 * include Request/Response specifications for improved modularity.
 */
public class FakeStoreController {

  private static final String BASE_URL = "https://fakestoreapi.com";

  /** Builds the request specification for a product creation. */
  private static RequestSpecification getRequestSpec(FakeStoreRequestDto requestDto) {
    return new RequestSpecBuilder()
        .setBaseUri(BASE_URL)
        .setContentType(ContentType.JSON)
        .setBody(requestDto)
        .build();
  }

  /** Builds the response specification for a product creation. */
  private static ResponseSpecification getResponseSpec(int statusCode) {
    return new ResponseSpecBuilder()
        .expectStatusCode(statusCode)
        .expectContentType(ContentType.JSON)
        .build();
  }

  /**
   * Creates a product on FakeStore using the provided request DTO.
   *
   * @param requestDto The product details.
   * @return The created product details response.
   */
  public static FakeStoreResponseDto createProduct(FakeStoreRequestDto requestDto) {
    return given()
        .spec(getRequestSpec(requestDto))
        .log()
        .all()
        .when()
        .post(FakeStoreProduct.PRODUCT.getPath())
        .then()
        .spec(getResponseSpec(201))
        .log()
        .all()
        .extract()
        .as(FakeStoreResponseDto.class);
  }
}
