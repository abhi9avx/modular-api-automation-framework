package com.abhinav.framework.enums;

public enum PostApi {
  CREATE_POST;

  private String path;

  PostApi(String path) {
    this.path = path;
  }

  PostApi() {}

  public String getPath() {
    return path;
  }
}
