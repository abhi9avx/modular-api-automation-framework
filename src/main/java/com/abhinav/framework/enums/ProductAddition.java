package com.abhinav.framework.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductAddition {
  ADD_PRODUCT("/products/add");

  private final String path;
}
