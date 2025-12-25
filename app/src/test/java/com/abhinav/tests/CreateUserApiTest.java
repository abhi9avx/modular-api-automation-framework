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

/*
 * -----------------------------------------------------------------------------
 * File: CreateUserApiTest.java
 * Purpose: Test class for verifying "Create User" API functionality.
 *
 * Why:
 * - Ensures that the API allows creating new users.
 * - Checks if the API correctly processes the payload and returns the created entity.
 *
 * Summary:
 * 1. Defines test data for a new user (Name, Job).
 * 2. Calls the Create User API via UserController.
 * 3. Validates the 201 Created status.
 * 4. Verifies the response contains the correct name and job.
 * -----------------------------------------------------------------------------
 */
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

        // 1. Prepare Request Data: Static example data
        String userName = "Abhinav";
        String jobTitle = UserJob.QA.getJobTitle(); // Using Enum for type safety
        UserRequest userRequest = new UserRequest(userName, jobTitle);

        log.info("Request Body: " + JsonUtil.toJson(userRequest));

        // 2. Execute API Call: Delegate to Controller
        Response response = userController.createUser(userRequest);

        // 3. Validate Status: Expect 201 Created
        log.info("Validating Response Status Code...");
        Assert.assertEquals(response.getStatusCode(), 201, "Status code mismatch!");

        // 4. Validate Response Body
        String responseBody = response.getBody().asString();
        log.info("Response Body: " + responseBody);
        UserResponse createdUser = JsonUtil.fromJson(responseBody, UserResponse.class);

        // Assertions: Ensure returned data matches sent data
        Assert.assertNotNull(createdUser.getId(), "User ID should be generated");
        Assert.assertEquals(createdUser.getName(), userName, "User Name mismatch");
        Assert.assertEquals(createdUser.getJob(), jobTitle, "Job Title mismatch");

        log.info("Create User Test Passed Successfully!");
    }
}
