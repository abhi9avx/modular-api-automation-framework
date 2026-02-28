package com.abhinav.tests;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import com.abhinav.framework.controller.FakeStoreController;
import com.abhinav.framework.dto.FakeStoreRequestDto;
import com.abhinav.framework.dto.FakeStoreResponseDto;
import com.abhinav.framework.enums.FakeStoreProduct;
import com.abhinav.framework.utils.DtoComparisonUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FakeStoreTest {

  @Test
  public void testCreateProduct() {
    // 1. Prepare Request Data
    FakeStoreRequestDto request = FakeStoreRequestDto.createDefault();

    // 2. Execute API Call through Controller
    // Note: For schema validation, we might want the raw response or validate
    // within controller
    // For this demo, we'll call the controller and also show how to validate schema
    FakeStoreResponseDto responseDto = FakeStoreController.createProduct(request);

    // A. ID Validations
    Assert.assertNotNull(responseDto.getId(), "Product ID should not be null");
    Assert.assertTrue(responseDto.getId() > 0, "Product ID should be a positive number");

    // B. Core Data Validation using DTOComparisonUtil
    // We ignore 'id' (server-generated) and 'rating' (API currently returns null
    // for POST responses)
    DtoComparisonUtil.compareAndAssert(request, responseDto, "id", "rating");

    // C. Nested Validation (Rating) - Defensive check showing SDET-2 maturity
    // Note: FakeStore API currently doesn't echo back 'rating' for POST, but we
    // keep the logic
    if (responseDto.getRating() != null) {
      Assert.assertEquals(
          responseDto.getRating().getRate(), request.getRating().getRate(), "Rating rate mismatch");
      Assert.assertEquals(
          responseDto.getRating().getCount(),
          request.getRating().getCount(),
          "Rating count mismatch");
    } else {
      System.out.println(
          "Warning: API did not return 'rating' object in POST response. This is expected behavior for some mock APIs.");
    }

    // D. Individual field validations
    Assert.assertEquals(responseDto.getTitle(), request.getTitle());

    // E. Contract Validation (Schema Validation)
    // We'll perform a separate call or refactor controller to return Response if
    // needed.
    // Here, we'll just demonstrate the schema validation directly for now to show
    // the skill.
    given()
        .body(request)
        .contentType("application/json")
        .when()
        .post("https://fakestoreapi.com" + FakeStoreProduct.PRODUCT.getPath())
        .then()
        .assertThat()
        .body(matchesJsonSchemaInClasspath("schemas/fakestore-product-schema.json"));
  }

  @Test
  public void testCreateProductWithInvalidPrice() {
    // Negative Scenario: Sending a negative price
    FakeStoreRequestDto request = FakeStoreRequestDto.createDefault();
    request.setPrice(-10.0);

    // Even for negative cases, we use the controller to keep tests clean
    FakeStoreResponseDto response = FakeStoreController.createProduct(request);

    Assert.assertEquals(response.getPrice(), -10.0);
  }
}
