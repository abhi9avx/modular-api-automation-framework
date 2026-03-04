package com.abhinav.framework.enums;

public enum GetApi {
  GET_COMMENT_BY_ID;

  private String path;

  GetApi(String path) {
    this.path = path;
  }

  GetApi() {}

  public String getPath() {
    return path;
  }
}
