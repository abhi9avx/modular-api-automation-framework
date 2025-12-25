package com.abhinav.framework.client;

import com.abhinav.framework.config.EndpointConfig;
import com.abhinav.framework.config.EnvironmentConfig;
import io.restassured.response.Response;
import java.util.Map;

public class PostClient {

  private static final String BASE_URL = EnvironmentConfig.getBaseUrl();

  public Response createPost(Object requestBody) {
    String url = BASE_URL + EndpointConfig.POSTS_ENDPOINT;
    return ApiClient.post(url, requestBody, Map.of("Content-Type", "application/json"));
  }
}
