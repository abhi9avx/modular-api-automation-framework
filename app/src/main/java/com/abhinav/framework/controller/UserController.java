package com.abhinav.framework.controller;

import com.abhinav.framework.client.ApiClient;
import com.abhinav.framework.config.EnvironmentConfig;
import io.restassured.response.Response;
import java.util.Map;

public class UserController {

  public Response getUserById(int userId) {
    String url = EnvironmentConfig.getBaseUrl() + "/users/" + userId;
    return ApiClient.get(url, Map.of());
  }

  public Response createUser(Object userRequest) {
    String url = EnvironmentConfig.getBaseUrl() + "/users";
    return ApiClient.post(url, userRequest, Map.of("Content-Type", "application/json"));
  }
}
