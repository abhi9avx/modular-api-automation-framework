package com.abhinav.framework.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SpaceXApi {
  GET_LAUNCHES("/v5/launches");

  private final String path;
}
