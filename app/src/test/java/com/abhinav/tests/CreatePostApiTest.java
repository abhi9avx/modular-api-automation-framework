package com.abhinav.tests;

import com.abhinav.framework.controller.PostController;
import com.abhinav.framework.dto.PostRequest;
import com.abhinav.framework.dto.PostResponse;
import com.abhinav.framework.enums.TestUser;
import com.abhinav.framework.utils.JsonUtil;
import com.abhinav.framework.utils.LoggerUtil;
import com.abhinav.framework.utils.RandomUtil;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/*
 * -----------------------------------------------------------------------------
 * File: CreatePostApiTest.java
 * Purpose: Test class for verifying "Create Post" API functionality.
 *
 * Why:
 * - Ensures that the API can successfully create new resources (Posts).
 * - Validates that the response matches the request data.
 *
 * Summary:
 * 1. Prepares test data using RandomUtil and TestUser Enum.
 * 2. Sends a POST request via PostController.
 * 3. Validates status code (201 Created).
 * 4. Deserializes response to PostResponse DTO and asserts field values.
 * -----------------------------------------------------------------------------
 */
@Epic("Post Management")
@Feature("Create Post")
public class CreatePostApiTest extends BaseTest {

  private static final Logger log = LoggerUtil.getLogger(CreatePostApiTest.class);
  private final PostController postController = new PostController();

  @Test(description = "Verify successful creation of a new post")
  @Severity(SeverityLevel.CRITICAL)
  @Story("Create Post with Random Data")
  public void verifyCreatePost() {
    log.info("Starting Create Post Test...");

    // 1. Prepare Request Data: Use random strings to ensure uniqueness
    String title = RandomUtil.generateRandomString("Title");
    String body = RandomUtil.generateRandomString("Body");
    int userId = TestUser.ADMIN_USER.getId(); // Use Enum for consistent User ID

    // Build the request DTO using Lombok Builder
    PostRequest postRequest = PostRequest.builder().title(title).body(body).userId(userId).build();

    log.info("Request Body: " + JsonUtil.toJson(postRequest));

    // 2. Execute API Call: Delegate execution to Controller
    Response response = postController.createPost(postRequest);

    // 3. Validate Status: Expecting 201 Created
    log.info("Validating Response Status Code...");
    Assert.assertEquals(response.getStatusCode(), 201, "Status code mismatch!");

    // 4. Validate Response Body: Deserialize JSON -> Java Object
    String responseBody = response.getBody().asString();
    log.info("Response Body: " + responseBody);
    PostResponse createdPost = JsonUtil.fromJson(responseBody, PostResponse.class);

    // Assertions: Verify response data matches request data
    Assert.assertNotNull(createdPost.getId(), "Post ID should be generated");
    Assert.assertEquals(createdPost.getTitle(), title, "Title mismatch");
    Assert.assertEquals(createdPost.getBody(), body, "Body mismatch");
    Assert.assertEquals(createdPost.getUserId(), userId, "User ID mismatch");

    log.info("Create Post Test Passed Successfully!");
  }
}
