package com.abhinav.framework.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponseDTO {

  private UserData data;
  private Support support;

  @JsonProperty("_meta")
  private Meta _meta;

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class UserData {
    private int id;
    private String email;
    private String first_name;
    private String last_name;
    private String avatar;
  }

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Support {
    private String url;
    private String text;
  }

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Meta {
    private String powered_by;
    private String docs_url;
    private String upgrade_url;
    private String example_url;
    private String variant;
    private String message;
    private String context;
  }
}
