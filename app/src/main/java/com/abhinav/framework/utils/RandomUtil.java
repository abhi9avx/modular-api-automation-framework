package com.abhinav.framework.utils;

import java.util.UUID;

public class RandomUtil {

  public static String generateRandomString(String prefix) {
    return prefix + "-" + UUID.randomUUID().toString().substring(0, 8);
  }
}
