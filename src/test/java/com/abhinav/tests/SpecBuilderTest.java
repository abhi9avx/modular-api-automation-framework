package com.abhinav.tests;

import com.abhinav.framework.controller.PlaceController;
import com.abhinav.framework.dto.LocationRequestDto;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import java.util.List;
import org.testng.annotations.Test;

public class SpecBuilderTest {

  @Test
  public void testSpecBuilder() {

    // Setup Request Body (DTO)
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

    // Validation Spec
    ResponseSpecification resSpec =
        new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();

    // Controller Call
    Response response = PlaceController.addPlace(p);

    // Validation & Extraction
    String responseString = response.then().spec(resSpec).extract().response().asString();

    System.out.println(responseString);
  }
}
