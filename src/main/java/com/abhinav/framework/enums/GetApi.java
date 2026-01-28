package com.abhinav.framework.enums;

import lombok.*;

@Getter
@AllArgsConstructor
public enum GetApi {

  // Define the specific endpoint for fetching a comment.
  // Storing URLs in an enum makes it easier to manage and update them in one
  // place.
  GET_COMMENT_BY_ID("/comments/1");

  private final String path;
}
