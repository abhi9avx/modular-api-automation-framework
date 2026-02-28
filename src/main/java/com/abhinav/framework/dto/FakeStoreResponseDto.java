package com.abhinav.framework.dto;

import lombok.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FakeStoreResponseDto {
  private Integer id;
  private String title;
  private Double price;
  private String description;
  private String category;
  private Rating rating;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Rating {
    private Double rate;
    private Integer count;
  }

  public static FakeStoreResponseDto createDefault() {
    return FakeStoreResponseDto.builder()
        .id(21)
        .title("Test Product")
        .price(99.99)
        .description("Automation Practice")
        .category("electronics")
        .rating(Rating.builder().rate(4.8).count(120).build())
        .build();
  }
}

/*
 * {
 * "id": 21,
 * "title": "Test Product",
 * "price": 99.99,
 * "description": "Automation Practice",
 * "category": "electronics"
 * }
 */
