package com.abhinav.framework.utils;

import java.util.UUID;

/*
 * ---------------------------------------------------------------------------------------------------------------------
 * File: RandomUtil.java
 *
 * Purpose:
 * Provides utility methods to generate random data for test execution.
 *
 * Why we need this:
 * Tests should be independent and repeatable. If we try to create a User with a static name like "JohnDoe",
 * the first test might pass, but the second might fail if the API enforces unique usernames.
 * Using random data ensures every test run uses unique inputs.
 *
 * Why this is better (Props/Cons):
 * - Pros:
 *   1. **Data Uniqueness**: Avoids "Resource already exists" errors from the API.
 *   2. **Robustness**: Tests various inputs rather than just one static string.
 * - Cons:
 *   1. **Debugging Difficulty**: If a test fails with a random string, you need logs to know exactly what was sent.
 *
 * Summary:
 * A helper class that wraps Java's UUID logic to give us easy-to-use random strings.
 * ---------------------------------------------------------------------------------------------------------------------
 */
public class RandomUtil {

  /*
   * Method: generateRandomString
   * Purpose: Generates a unique string composed of a prefix and a random
   * alphanumeric suffix.
   *
   * Logic:
   * 1. Takes a prefix (e.g., "User").
   * 2. Generates a random UUID (e.g., "123e4567-e89b...").
   * 3. Substrings the first 8 characters to keep it short and readable.
   * 4. Combines them: "User-123e4567".
   */
  public static String generateRandomString(String prefix) {
    return prefix + "-" + UUID.randomUUID().toString().substring(0, 8);
  }
}
