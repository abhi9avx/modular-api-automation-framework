package com.abhinav.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {

  private LoggerUtil() {
    // Prevent instantiation
  }

  /** Returns a logger instance for the given class */
  public static Logger getLogger(Class<?> clazz) {
    return LoggerFactory.getLogger(clazz);
  }
}
