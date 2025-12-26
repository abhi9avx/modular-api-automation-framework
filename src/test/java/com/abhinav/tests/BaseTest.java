package com.abhinav.tests;

import com.abhinav.framework.utils.LoggerUtil;
import io.qameta.allure.testng.AllureTestNg;
import org.slf4j.Logger;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

/*
 * -----------------------------------------------------------------------------
 * File: BaseTest.java
 * Purpose: Parent class for all test classes.
 *
 * Why:
 * - Centralizes common setup logic (like initializing reports).
 * - Avoids code duplication across multiple test files.
 *
 * Details:
 * - @Listeners: Hooks Allure into TestNG to automatically generate reports.
 * - @BeforeSuite: Runs once before any test in the suite starts.
 * -----------------------------------------------------------------------------
 */
@Listeners({AllureTestNg.class})
public class BaseTest {

  protected static final Logger log = LoggerUtil.getLogger(BaseTest.class);

  @BeforeSuite
  public void setupSuite() {
    log.info("=== Test Suite Execution Started ===");
  }
}
