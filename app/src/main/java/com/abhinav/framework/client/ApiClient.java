package com.abhinav.framework.client;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.Map;

public class ApiClient {

  public static Response get(String url, Map<String, String> headers) {
    return RestAssured.given().headers(headers).when().get(url);
  }

  public static Response post(String url, Object body, Map<String, String> headers) {
    return RestAssured.given().headers(headers).body(body).when().post(url);
  }

  public static Response put(String url, Object body, Map<String, String> headers) {
    return RestAssured.given().headers(headers).body(body).when().put(url);
  }

  public static Response delete(String url, Map<String, String> headers) {
    return RestAssured.given().headers(headers).when().delete(url);
  }
}
