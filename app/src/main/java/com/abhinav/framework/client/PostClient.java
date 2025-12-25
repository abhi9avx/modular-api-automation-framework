package com.abhinav.framework.client;

import com.abhinav.framework.config.EndpointConfig;
import com.abhinav.framework.config.EnvironmentConfig;
import io.restassured.response.Response;
import java.util.Map;

/*
 * -----------------------------------------------------------------------------
 * File: PostClient.java
 * Purpose: Handling Post-related API requests.
 *
 * Why:
 * - Specific logic for Posts (like Endpoint construction) belongs here.
 * - Uses ApiClient for the actual HTTP execution.
 * -----------------------------------------------------------------------------
 */
public class PostClient {

  private static final String BASE_URL = EnvironmentConfig.getBaseUrl();

  // Prepares the URL and calls the generic POST method
  public Response createPost(Object requestBody) {
    String url = BASE_URL + EndpointConfig.POSTS_ENDPOINT;
    return ApiClient.post(url, requestBody, Map.of("Content-Type", "application/json"));
  }
}
