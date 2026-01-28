package com.abhinav.framework.dto;

import lombok.*;

// This class is a Data Transfer Object (DTO) to map the API response.
// It helps us easily access comment data using Java objects instead of parsing JSON manually.
@Data
public class CommentResponseDto {

  private Integer postId;
  private Integer id;
  private String name;
  private String email;
  private String body;
}

// {
// "postId": 1,
// "id": 1,
// "name": "id labore ex et quam laborum",
// "email": "Eliseo@gardner.biz",
// "body": "laudantium enim quasi est quidem magnam voluptate ipsam eos\ntempora
// quo necessitatibus\ndolor quam autem quasi\nreiciendis et nam sapiente
// accusantium"
// }
