package com.abhinav.framework.controller;

import static io.restassured.RestAssured.given;

import com.abhinav.framework.dto.EchoResponseDto;
import io.restassured.response.Response;

public class EchoGetController {

  private static final String BASE_URL = "https://reqbin.com";

  public EchoResponseDto getEcho(String id) {

    String path = "/echo/get/json/" + id;

    Response response =
        given().baseUri(BASE_URL).when().get(path).then().statusCode(200).extract().response();

    return response.as(EchoResponseDto.class);
  }
}
