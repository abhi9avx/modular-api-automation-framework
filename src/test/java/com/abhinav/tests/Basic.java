package com.abhinav.tests;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.testng.annotations.BeforeSuite;
import com.abhinav.framework.utils.LoggerUtil;
import org.slf4j.Logger;

/**
 * Basic - The Base Test class for all API Tests.
 *
 * <p>
 * Why:
 * 1. **Centralized Configuration**: All tests need common setup (like logging,
 * base URI).
 * 2. **Dry Principle**: Avoid repeating the same setup code in every test
 * class.
 * 3. **Consistency**: Ensures all tests behave similarly (e.g., they all log
 * requests/responses).
 */
public class Basic {

    protected static final Logger logger = LoggerUtil.getLogger(Basic.class);

    @BeforeSuite
    public void setup() {
        logger.info("Initializing API Automation Test Suite...");

        // Global RestAssured Filters for automatic logging of every request and
        // response
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        logger.info("RestAssured filters initialized for global logging.");
    }
}
