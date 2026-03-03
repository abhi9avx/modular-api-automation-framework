package com.abhinav.tests;

import com.abhinav.framework.client.UserClient;
import com.abhinav.framework.dto.UserResponse;
import com.abhinav.framework.utils.JsonUtil;
import com.abhinav.framework.utils.LoggerUtil;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/*
 * -----------------------------------------------------------------------------
 * File: GetUserApiTest.java
 * Purpose: Test class for verifying "Get User" API functionality.
 *
 * Why:
 * - Ensures that we can retrieve details of an existing user.
 * - Validates schema consistency (ID, Email presence).
 *
 * Summary:
 * 1. Calls GET API for a known User ID (2).
 * 2. Validates 200 OK status.
 * 3. Deserializes response to UserResponse object.
 * 4. Asserts that critical fields (ID, Email) are not null.
 * -----------------------------------------------------------------------------
 */
@Epic("User API")
@Feature("User Management")
public class GetUserApiTest {

  private static final Logger log = LoggerUtil.getLogger(GetUserApiTest.class);
  private final UserClient userClient = new UserClient();

  @Test(description = "Verify GET user API returns valid user data")
  @Severity(SeverityLevel.CRITICAL)
  @Story("Get User By ID")
  public void verifyGetUserById() {

    log.info("Starting GET User API Test");

    // 1. Call API: Fetch user with ID 2
    Response response = userClient.getUserById(2);

    // 2. Validate Status: Expect 200 OK
    log.info("Validating response status code");
    Assert.assertEquals(response.getStatusCode(), 200);

    // 3. Parse Response
    String responseBody = response.getBody().asString();
    log.info("Response Body: {}", responseBody);

    // 4. Convert JSON to Object
    UserResponse user = JsonUtil.fromJson(responseBody, UserResponse.class);

    // 5. Assertions: Check data integrity
    Assert.assertNotNull(user.getId(), "User ID should not be null");
    Assert.assertNotNull(user.getEmail(), "Email should not be null");

    log.info("Test Passed Successfully");
  }
}
