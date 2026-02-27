package com.abhinav.framework.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddProductRequestDto {
  private String title;
  private String description;
  private int price;

  public static AddProductRequestDto createDefault() {
    return AddProductRequestDto.builder()
        .title("SAMSUNG S26 Ultra")
        .description("THE BRAND NEW SAMSUNG S26 ULTRA")
        .price(1630000)
        .build();
  }
}
