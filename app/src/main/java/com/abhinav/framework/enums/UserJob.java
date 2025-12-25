package com.abhinav.framework.enums;

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
