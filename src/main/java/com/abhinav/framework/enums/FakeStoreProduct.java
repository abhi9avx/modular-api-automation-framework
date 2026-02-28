package com.abhinav.framework.enums;

import lombok.*;

@Getter
@AllArgsConstructor
public enum FakeStoreProduct {
  PRODUCT("/products");

  private final String path;
}
