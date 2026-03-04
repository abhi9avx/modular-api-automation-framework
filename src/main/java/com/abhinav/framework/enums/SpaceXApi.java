package com.abhinav.framework.enums;

public enum SpaceXApi {
  GET_LAUNCHES;

  private String path;

  SpaceXApi(String path) {
    this.path = path;
  }

  SpaceXApi() {}

  public String getPath() {
    return path;
  }
}
