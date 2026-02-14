package com.abhinav.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import org.testng.annotations.Test;

public class PostEchoTest {

  @Test
  public void postJsonTest() {
    RestAssured.baseURI = "https://postman-echo.com";

    given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
        .body("{\n" +
            "  \"location\": {\n" +
            "    \"lat\": -38.383494,\n" +
            "    \"lng\": 33.427362\n" +
            "  },\n" +
            "  \"accuracy\": 50,\n" +
            "  \"name\": \"Abhinav Kumar\",\n" +
            "  \"phone_number\": \"(+91) 8406938733\",\n" +
            "  \"address\": \"ward No - 9 , Purvi Thatha , Khagaria , Bihar\",\n" +
            "  \"types\": [\n" +
            "    \"shoe park\",\n" +
            "    \"shop\"\n" +
            "  ],\n" +
            "  \"website\": \"http://google.com\",\n" +
            "  \"language\": \"French-IN\"\n" +
            "}")
        .when().post("/post")
        .then().log().all().assertThat().statusCode(200).body("json.name", equalTo("Abhinav Kumar"));
  }

  @Test
  public void postJsonTest2() {
    RestAssured.baseURI = "https://postman-echo.com";

    given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
        .body("{\n" +
            "  \"location\": {\n" +
            "    \"lat\": -38.383494,\n" +
            "    \"lng\": 33.427362\n" +
            "  },\n" +
            "  \"accuracy\": 50,\n" +
            "  \"name\": \"Abhinav Kumar\",\n" +
            "  \"phone_number\": \"(+91) 8406938733\",\n" +
            "  \"address\": \"ward No - 9 , Purvi Thatha , Khagaria , Bihar\",\n" +
            "  \"types\": [\n" +
            "    \"shoe park\",\n" +
            "    \"shop\"\n" +
            "  ],\n" +
            "  \"website\": \"http://google.com\",\n" +
            "  \"language\": \"French-IN\"\n" +
            "}")
        .when().post("/post")
        .then().log().all().assertThat().statusCode(200);
  }

  @Test
  public void postJsonTest3() {
    RestAssured.baseURI = "https://postman-echo.com";

    given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
        .body("{\n" +
            "  \"location\": {\n" +
            "    \"lat\": -38.383494,\n" +
            "    \"lng\": 33.427362\n" +
            "  },\n" +
            "  \"accuracy\": 50,\n" +
            "  \"name\": \"Abhinav Kumar\",\n" +
            "  \"phone_number\": \"(+91) 8406938733\",\n" +
            "  \"address\": \"ward No - 9 , Purvi Thatha , Khagaria , Bihar\",\n" +
            "  \"types\": [\n" +
            "    \"shoe park\",\n" +
            "    \"shop\"\n" +
            "  ],\n" +
            "  \"website\": \"http://google.com\",\n" +
            "  \"language\": \"French-IN\"\n" +
            "}")
        .when().post("/post")
        .then().log().all().assertThat().statusCode(200).body("json.accuracy", equalTo(50));
  }
}
