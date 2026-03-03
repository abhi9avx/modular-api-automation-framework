package com.abhinav.framework.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HttpBinPostRequestDto {
  private String message;

  public static HttpBinPostRequestDto createDefault() {
    return HttpBinPostRequestDto.builder().message("Hello world").build();
  }
}
