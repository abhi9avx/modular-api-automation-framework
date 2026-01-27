package com.abhinav.framework.enums;

import lombok.*;

@Getter
@AllArgsConstructor
public enum PostApi {
  CREATE_POST("/posts");

  private final String path;
}

/*curl --location 'https://jsonplaceholder.typicode.com/posts' \
--header 'Content-Type: application/json' \
--data '{"title": "My Test Post", "body": "This is the body content", "userId": 1}'
*/
