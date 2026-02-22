package com.abhinav.tests;

import static io.restassured.RestAssured.given;

import com.abhinav.framework.dto.LocationRequestDto;
import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import java.util.List;
import org.testng.annotations.Test;

public class SerializeTest {
  @Test
  public void testSerialize() {

    RestAssured.baseURI = "https://rahulshettyacademy.com";

    // Add global request and response logging filters
    RestAssured.filters(
        new RequestLoggingFilter(LogDetail.BODY), new ResponseLoggingFilter(LogDetail.STATUS));

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

    String res =
        given()
            .queryParam("key", "qaclick123")
            .body(p)
            .when()
            .post("/maps/api/place/add/json")
            .then()
            .assertThat()
            .statusCode(200)
            .extract()
            .response()
            .asString();

    System.out.println(res);
  }
}
