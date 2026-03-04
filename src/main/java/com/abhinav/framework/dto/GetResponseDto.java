package com.abhinav.framework.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetResponseDto {

  @JsonProperty("url")
  private String url;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public static GetResponseDto createDefault() {
    GetResponseDto defaultDto = new GetResponseDto();
    defaultDto.setUrl("https://httpbin.org/get");
    return defaultDto;
  }
}
