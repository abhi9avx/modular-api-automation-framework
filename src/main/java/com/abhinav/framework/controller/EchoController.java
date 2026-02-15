package com.abhinav.framework.controller;

import static io.restassured.RestAssured.given;

import com.abhinav.framework.dto.EchoRequestDto;
import com.abhinav.framework.dto.EchoResponseDto;
import com.abhinav.framework.enums.EchoPost;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class EchoController {

  private static final String BASE_URL = "https://postman-echo.com";

  public Response postEchoRaw(EchoRequestDto request) {
    return given()
        .baseUri(BASE_URL)
        .header("User-Agent", "Mozilla/5.0")
        .contentType(ContentType.JSON)
        .body(request)
        .when()
        .post(EchoPost.ECHO_POST.getPath());
  }

  public EchoResponseDto postEcho(EchoRequestDto request) {
    Response response = postEchoRaw(request);

    if (response.getStatusCode() != 200) {
      // Log error if needed, but avoid System.err in production-like framework
    }

    return response.as(EchoResponseDto.class);
  }
}
