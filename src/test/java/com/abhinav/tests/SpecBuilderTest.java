package com.abhinav.tests;

import static io.restassured.RestAssured.given;

import com.abhinav.framework.dto.LocationRequestDto;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import java.util.List;
import org.testng.annotations.Test;

public class SpecBuilderTest {

  @Test
  public void testSpecBuilder() {

    RestAssured.baseURI = "https://rahulshettyacademy.com";

    LocationRequestDto p = new LocationRequestDto();
    p.setAccuracy(50);
    p.setAddress("ward No - 9 , Purvi Thatha , Khagaria , Bihar");
    p.setLanguage("French-IN");
    p.setName("Abhinav Kumar");
    p.setPhone_number("(+91) 8406938733");
    p.setWebsite("http://google.com");
    p.setTypes(List.of("shoe park", "shop"));

    LocationRequestDto.Location l = new LocationRequestDto.Location();
    l.setLat(-38.383494);
    l.setLng(33.427362);
    p.setLocation(l);

    RequestSpecification req =
        new RequestSpecBuilder()
            .setBaseUri("https://rahulshettyacademy.com")
            .addQueryParam("key", "qaclick123")
            .setContentType(ContentType.JSON)
            .build();

    ResponseSpecification resSpec =
        new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();

    RequestSpecification res = given().spec(req).body(p);

    String responseString =
        res.when()
            .post("/maps/api/place/add/json")
            .then()
            .spec(resSpec)
            .extract()
            .response()
            .asString();

    System.out.println(responseString);
  }
}
