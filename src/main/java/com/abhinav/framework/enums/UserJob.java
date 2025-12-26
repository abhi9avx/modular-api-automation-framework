package com.abhinav.framework.enums;

/*
 * -----------------------------------------------------------------------------
 * File: UserJob.java
 * Purpose: Enum defining valid Job Titles for a User.
 *
 * Why:
 * - Eliminates "Magic Strings". Instead of typing "Leader" manually (prone to typos),
 *   we use `UserJob.LEADER`.
 * - Centralizes the list of valid job titles allowed by the system/test.
 * -----------------------------------------------------------------------------
 */
public enum UserJob {
  LEADER("Leader"),
  DEVELOPER("Developer"),
  QA("QA Automation Engineer"),
  MANAGER("Manager");

  private final String jobTitle;

  UserJob(String jobTitle) {
    this.jobTitle = jobTitle;
  }

  public String getJobTitle() {
    return jobTitle;
  }
}
