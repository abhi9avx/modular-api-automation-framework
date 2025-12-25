package com.abhinav.framework.enums;

/*
 * -----------------------------------------------------------------------------
 * File: TestUser.java
 * Purpose: Enum defining predefined Test Users and their IDs.
 *
 * Why:
 * - Keeps User IDs consistent across tests.
 * - `TestUser.ADMIN_USER.getId()` is more readable than `1`.
 * -----------------------------------------------------------------------------
 */
public enum TestUser {
  ADMIN_USER(1, "Admin User"),
  REGULAR_USER(2, "Regular User"),
  GUEST_USER(10, "Guest User");

  private final int id;
  private final String description;

  TestUser(int id, String description) {
    this.id = id;
    this.description = description;
  }

  public int getId() {
    return id;
  }

  public String getDescription() {
    return description;
  }
}
