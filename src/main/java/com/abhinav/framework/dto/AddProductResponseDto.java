package com.abhinav.framework.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddProductResponseDto {
  private int id;
  private String title;
  private int price;
  private String description;

  public static AddProductResponseDto createDefault() {
    return AddProductResponseDto.builder()
        .id(195)
        .title("SAMSUNG S26 Ultra")
        .price(1630000)
        .description("THE BRAND NEW SAMSUNG S26 ULTRA")
        .build();
  }
}
