package com.abhinav.framework.enums;

public enum ProductAddition {
  ADD_PRODUCT;

  private String path;

  ProductAddition(String path) {
    this.path = path;
  }

  ProductAddition() {}

  public String getPath() {
    return path;
  }
}
