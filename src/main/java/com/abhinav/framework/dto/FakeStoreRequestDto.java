package com.abhinav.framework.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FakeStoreRequestDto {

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

  public static FakeStoreRequestDto createDefault() {
    return FakeStoreRequestDto.builder()
        .title("Test product")
        .price(100.06)
        .description("Automation Practice")
        .category("electronics")
        .rating(Rating.builder().rate(34.8).count(1231).build())
        .build();
  }
}

/*
curl --location 'https://fakestoreapi.com/products' \
--header 'Content-Type: application/json' \
--data '{
  "title": "Test Product",
  "price": 99.99,
  "description": "Automation Practice",
  "category": "electronics",
  "rating": {
    "rate": 4.8,
    "count": 120
  }
}'

*/
