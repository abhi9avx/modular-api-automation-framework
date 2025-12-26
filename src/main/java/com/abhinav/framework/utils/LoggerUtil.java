package com.abhinav.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * -----------------------------------------------------------------------------
 * File: LoggerUtil.java
 * Purpose: Central factory for creating Logger instances.
 *
 * Why:
 * - Provides a consistent way to initialize loggers across the project.
 * - Wraps SLF4J LoggerFactory to decouple code from the underlying logging implementation.
 * -----------------------------------------------------------------------------
 */
public class LoggerUtil {

  private LoggerUtil() {
    // Prevent instantiation
  }

  // Returns a standard Logger instance for the calling class
  public static Logger getLogger(Class<?> clazz) {
    return LoggerFactory.getLogger(clazz);
  }
}
