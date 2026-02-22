package com.abhinav.framework.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HttpBinRequestDto {
  private String name;
  private String role;
  private int experience;
  private List<String> skills;
}
