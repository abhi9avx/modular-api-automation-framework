package com.abhinav.tests;

import com.abhinav.framework.utils.LoggerUtil;
import io.qameta.allure.testng.AllureTestNg;
import org.slf4j.Logger;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

@Listeners({AllureTestNg.class})
public class BaseTest {

  protected static final Logger log = LoggerUtil.getLogger(BaseTest.class);

  @BeforeSuite
  public void setupSuite() {
    log.info("=== Test Suite Execution Started ===");
  }
}
