package com.abhinav.framework.controller;

import com.abhinav.framework.config.EnvironmentConfig;
import com.abhinav.framework.dto.PostRequestDto;
import com.abhinav.framework.dto.PostResponseDto;
import com.abhinav.framework.enums.PostApi;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class PostAPIController {

    private static final String BASE_URL = EnvironmentConfig.getBaseUrl();

    public static PostResponseDto createPost(PostRequestDto request) {

        // GIVEN: base URI and request body
        return RestAssured
                .given()
                    .baseUri(BASE_URL)
                    .contentType(ContentType.JSON)
                    .body(request)

                // WHEN: POST API is called
                .when()
                    .post(PostApi.CREATE_POST.getPath())

                // THEN: validate and extract response
                .then()
                    .statusCode(201)
                    .extract()
                    .as(PostResponseDto.class);
    }
}
