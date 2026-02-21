package com.abhinav.framework.controller;

import com.abhinav.framework.dto.SpaceXLaunchDto;
import com.abhinav.framework.enums.SpaceXApi;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import java.util.List;

public class SpaceXController {

  private static final String BASE_URL = "https://api.spacexdata.com";

  private static RequestSpecification getRequestSpec() {
    return new RequestSpecBuilder().setBaseUri(BASE_URL).setContentType(ContentType.JSON).build();
  }

  private static ResponseSpecification getResponseSpec() {
    return new ResponseSpecBuilder()
        .expectStatusCode(200)
        .expectContentType(ContentType.JSON)
        .build();
  }

  public static Response getLaunchesRaw() {
    // Non-BDD approach
    return RestAssured.given()
        .spec(getRequestSpec())
        .get(SpaceXApi.GET_LAUNCHES.getPath())
        .then()
        .spec(getResponseSpec())
        .extract()
        .response();
  }

  public static List<SpaceXLaunchDto> getLaunches() {
    Response response = getLaunchesRaw();
    return response.body().jsonPath().getList(".", SpaceXLaunchDto.class);
  }
}
