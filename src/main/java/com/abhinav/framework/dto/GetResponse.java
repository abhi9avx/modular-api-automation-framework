package com.abhinav.framework.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetResponse {
  private String url;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  // Add default constructor
  public GetResponse() {}

  @Override
  public String toString() {
    return "GetResponse{" + "url='" + url + '\'' + '}';
  }
}
