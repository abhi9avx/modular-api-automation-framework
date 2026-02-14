package com.abhinav.framework.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EchoPost {
  ECHO_POST("/post");

  private final String path;
}
