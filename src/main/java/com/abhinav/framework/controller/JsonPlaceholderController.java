package com.abhinav.framework.controller;

import com.abhinav.framework.dto.JsonPlaceholderUserDto;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class JsonPlaceholderController {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    private static RequestSpecification getRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setContentType(ContentType.JSON)
                .build();
    }

    private static ResponseSpecification getResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();
    }

    public static Response getUsersRaw() {
        return RestAssured.given()
                .spec(getRequestSpec())
                .get("/users")
                .then()
                .spec(getResponseSpec())
                .extract()
                .response();
    }

    public static JsonPlaceholderUserDto[] getUsers() {
        Response response = getUsersRaw();
        return response.as(JsonPlaceholderUserDto[].class);
    }
}
