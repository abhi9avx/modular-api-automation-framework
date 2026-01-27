package com.abhinav.framework.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDto {

  private String title;
  private String body;
  private int userId;
}

/*. curl --location 'https://jsonplaceholder.typicode.com/posts' \
--header 'Content-Type: application/json' \
--data '{"title": "My Test Post", "body": "This is the body content", "userId": 1}' */
