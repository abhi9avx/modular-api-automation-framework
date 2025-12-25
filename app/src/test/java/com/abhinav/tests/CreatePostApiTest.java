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

    // Prepare Request Data using DTO, Enum, and Utils
    String title = RandomUtil.generateRandomString("Title");
    String body = RandomUtil.generateRandomString("Body");
    int userId = TestUser.ADMIN_USER.getId(); // Using Enum

    PostRequest postRequest = PostRequest.builder().title(title).body(body).userId(userId).build();

    log.info("Request Body: " + JsonUtil.toJson(postRequest));

    // Execute API Call using Controller
    Response response = postController.createPost(postRequest);

    // Validate Response Status
    log.info("Validating Response Status Code...");
    Assert.assertEquals(response.getStatusCode(), 201, "Status code mismatch!");

    // Parse Response to DTO
    String responseBody = response.getBody().asString();
    log.info("Response Body: " + responseBody);
    PostResponse createdPost = JsonUtil.fromJson(responseBody, PostResponse.class);

    // Assertions
    Assert.assertNotNull(createdPost.getId(), "Post ID should be generated");
    Assert.assertEquals(createdPost.getTitle(), title, "Title mismatch");
    Assert.assertEquals(createdPost.getBody(), body, "Body mismatch");
    Assert.assertEquals(createdPost.getUserId(), userId, "User ID mismatch");

    log.info("Create Post Test Passed Successfully!");
  }
}
