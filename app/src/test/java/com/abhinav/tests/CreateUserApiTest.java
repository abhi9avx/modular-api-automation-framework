package com.abhinav.tests;

import com.abhinav.framework.controller.UserController;
import com.abhinav.framework.dto.UserRequest;
import com.abhinav.framework.dto.UserResponse;
import com.abhinav.framework.enums.UserJob;
import com.abhinav.framework.utils.JsonUtil;
import com.abhinav.framework.utils.LoggerUtil;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("User Management")
@Feature("Create User")
public class CreateUserApiTest extends BaseTest {

  private static final Logger log = LoggerUtil.getLogger(CreateUserApiTest.class);
  private final UserController userController = new UserController();

  @Test(description = "Verify successful creation of a new user")
  @Severity(SeverityLevel.CRITICAL)
  @Story("Create User with Valid Data")
  public void verifyCreateUser() {
    log.info("Starting Create User Test...");

    // Prepare Request Data using DTO and Enum
    String userName = "Abhinav";
    String jobTitle = UserJob.QA.getJobTitle(); // Using Enum
    UserRequest userRequest = new UserRequest(userName, jobTitle);

    log.info("Request Body: " + JsonUtil.toJson(userRequest));

    // Execute API Call using Controller
    Response response = userController.createUser(userRequest);

    // Validate Response Status
    log.info("Validating Response Status Code...");
    Assert.assertEquals(response.getStatusCode(), 201, "Status code mismatch!");

    // Parse Response to DTO
    String responseBody = response.getBody().asString();
    log.info("Response Body: " + responseBody);
    UserResponse createdUser = JsonUtil.fromJson(responseBody, UserResponse.class);

    // Assertions
    Assert.assertNotNull(createdUser.getId(), "User ID should be generated");
    Assert.assertEquals(createdUser.getName(), userName, "User Name mismatch");
    Assert.assertEquals(createdUser.getJob(), jobTitle, "Job Title mismatch");
    // CreatedAt is not returned by JSONPlaceholder for /users endpoint
    // Assert.assertNotNull(createdUser.getCreatedAt(), "CreatedAt timestamp should
    // be present");

    log.info("Create User Test Passed Successfully!");
  }
}
