package com.abhinav.framework.controller;

import static io.restassured.RestAssured.given;

import com.abhinav.framework.dto.LocationRequestDto;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class PlaceController {

    private static final String BASE_URL = "https://rahulshettyacademy.com";
    private static final String ADD_PLACE_ENDPOINT = "/maps/api/place/add/json";

    /**
     * Adds a new place using the Google Maps API.
     *
     * @param locationRequest The DTO containing the place details.
     * @return The Response object for further validation in the test.
     */
    public static Response addPlace(LocationRequestDto locationRequest) {

        RequestSpecification reqSpec = new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .addQueryParam("key", "qaclick123")
                .setContentType(ContentType.JSON)
                .build();

        return given()
                .spec(reqSpec)
                .body(locationRequest)
                .when()
                .post(ADD_PLACE_ENDPOINT);
    }
}
