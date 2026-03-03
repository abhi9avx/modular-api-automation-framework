package com.abhinav.framework.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * -----------------------------------------------------------------------------
 * File: PostRequest.java
 * Purpose: DTO representing the Payload for creating a Post.
 *
 * Why:
 * - Structure matches the expected JSON body for the POST /posts endpoint.
 * - Uses Lombok to avoid boilerplate code.
 * -----------------------------------------------------------------------------
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
  private String title;
  private String body;
  private int userId;

  public static PostRequest createDefault() {
    return PostRequest.builder()
        .title("Hello from curl")
        .body("This is a free test API")
        .userId(42)
        .build();
  }
}
