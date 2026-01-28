package com.abhinav.framework.controller;

import com.abhinav.framework.enums.GetApi;
import static io.restassured.RestAssured.*;
import com.abhinav.framework.dto.CommentResponseDto;

public class CommentController {

    // Request the API to get a specific comment details using its ID.
    // We use RestAssured to make the call and convert the JSON response directly
    // into our DTO class.
    public static CommentResponseDto getCommentById() {
        return given()
                .baseUri("https://jsonplaceholder.typicode.com")
                .when()
                .get(GetApi.GET_COMMENT_BY_ID.getPath())
                .then()
                .statusCode(200)
                .extract()
                .as(CommentResponseDto.class);
    }

}
