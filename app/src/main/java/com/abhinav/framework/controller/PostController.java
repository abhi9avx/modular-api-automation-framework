package com.abhinav.framework.controller;

import com.abhinav.framework.client.PostClient;
import io.restassured.response.Response;

public class PostController {

  private final PostClient postClient = new PostClient();

  public Response createPost(Object postRequest) {
    return postClient.createPost(postRequest);
  }
}
