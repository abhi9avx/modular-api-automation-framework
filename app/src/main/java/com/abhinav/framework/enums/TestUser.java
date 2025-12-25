package com.abhinav.framework.enums;

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
