package com.abhinav.framework.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostmanEchoResponseDto {

  private Map<String, Object> args;
  private PostmanEchoRequestDto data;
  private Map<String, Object> files;
  private Map<String, Object> form;
  private Map<String, String> headers;
  private PostmanEchoRequestDto json;
  private String url;

  public static PostmanEchoResponseDto createExpected() {
    return PostmanEchoResponseDto.builder()
        .data(PostmanEchoRequestDto.createDefault())
        .json(PostmanEchoRequestDto.createDefault())
        .url("https://postman-echo.com/post")
        .build();
  }
}
