package com.abhinav.framework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EchoRequestDto {

  @JsonProperty("Id")
  private int id;

  @JsonProperty("Customer")
  private String customer;

  @JsonProperty("Quantity")
  private int quantity;

  @JsonProperty("Price")
  private double price;
}
