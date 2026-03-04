package com.abhinav.framework.enums;

public enum EchoPost {
  ECHO_POST;

  private String path;

  EchoPost(String path) {
    this.path = path;
  }

  EchoPost() {}

  public String getPath() {
    return path;
  }
}
