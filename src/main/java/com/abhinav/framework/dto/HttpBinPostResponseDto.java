package com.abhinav.framework.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HttpBinPostResponseDto {
  private Map<String, Object> args;
  private String data;
  private Map<String, Object> files;
  private Map<String, Object> form;
  private Map<String, String> headers;
  private HttpBinPostRequestDto json;
  private String origin;
  private String url;
}
