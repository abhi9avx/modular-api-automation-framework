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

    // Call API
    Response response = userClient.getUserById(2);

    // Validate response
    log.info("Validating response status code");
    Assert.assertEquals(response.getStatusCode(), 200);

    // Parse response
    String responseBody = response.getBody().asString();
    log.info("Response Body: {}", responseBody);

    // Convert JSON to Object
    UserResponse user = JsonUtil.fromJson(responseBody, UserResponse.class);

    // Assertions
    Assert.assertNotNull(user.getId(), "User ID should not be null");
    Assert.assertNotNull(user.getEmail(), "Email should not be null");

    log.info("Test Passed Successfully");
  }
}
