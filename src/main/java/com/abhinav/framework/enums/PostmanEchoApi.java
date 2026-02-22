package com.abhinav.framework.enums;

public enum PostmanEchoApi {
  POST("/post");

  private final String path;

  PostmanEchoApi(String path) {
    this.path = path;
  }

  public String getPath() {
    return path;
  }
}
