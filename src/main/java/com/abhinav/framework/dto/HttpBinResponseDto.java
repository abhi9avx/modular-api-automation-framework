package com.abhinav.framework.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HttpBinResponseDto {
  private String url;
  private HttpBinRequestDto json;
  private String origin;
  private java.util.Map<String, String> form;
}
