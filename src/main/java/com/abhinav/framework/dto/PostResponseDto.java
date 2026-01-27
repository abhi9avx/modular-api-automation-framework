package com.abhinav.framework.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {
  private Integer id;
  private String title;
  private String body;
  private Integer userId;
}
