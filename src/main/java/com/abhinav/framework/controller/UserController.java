package com.abhinav.framework.controller;

import com.abhinav.framework.dto.CreateUserResponse;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class UserController {
    private static final String BASE_URL = "https://reqres.in";

    private static RequestSpecification getRequestSpec() {
        return new RequestSpecBuilder().setBaseUri(BASE_URL).setContentType(ContentType.JSON).build();
    }

    private static ResponseSpecification getResponseSpec() {
        return new ResponseSpecBuilder().expectContentType(ContentType.JSON).build();
    }

    public static Response createUser(String name, String job) {
        String requestBody = String.format("{\"name\": \"%s\", \"job\": \"%s\"}", name, job);

        return RestAssured.given().spec(getRequestSpec())
                .body(requestBody)
                .when().post("/api/users")
                .then().spec(getResponseSpec()).extract().response();
    }
}