package com.abhinav.framework.controller;

import com.abhinav.framework.client.PostClient;
import io.restassured.response.Response;

/*
 * -----------------------------------------------------------------------------
 * File: PostController.java
 * Purpose: Business Logic for Post API.
 * Wrapper/Abstraction over PostClient.
 *
 * Why:
 * - Keeps tests clean.
 * - Allows us to add extra logic (like attaching auth tokens) before calling the client.
 * -----------------------------------------------------------------------------
 */
public class PostController {

  private final PostClient postClient = new PostClient();

  // Delegates the creation logic to PostClient
  public Response createPost(Object postRequest) {
    return postClient.createPost(postRequest);
  }
}
