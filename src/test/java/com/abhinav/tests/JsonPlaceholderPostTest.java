package com.abhinav.tests;

import com.abhinav.framework.controller.JsonPlaceholderController;
import com.abhinav.framework.dto.PostRequest;
import com.abhinav.framework.dto.PostResponse;
import com.abhinav.framework.utils.DtoComparisonUtil;
import org.testng.annotations.Test;

public class JsonPlaceholderPostTest extends BaseTest {

  @Test
  public void testCreatePost() {
    // 1. Prepare Request and Expected Response
    PostRequest request = PostRequest.createDefault();
    PostResponse expectedResponse = PostResponse.createExpected();

    // 2. Make API Call
    PostResponse actualResponse = JsonPlaceholderController.createPost(request);

    // 3. Verify Response using DTO Comparison Util
    DtoComparisonUtil.compareAndAssert(expectedResponse, actualResponse);
  }

  @Test
  public void testCreatePostWithEmptyBody() {
    // Negative Scenario: Sending a request with an empty body or empty fields
    PostRequest request = PostRequest.builder().title("").body("").userId(1).build();

    PostResponse actualResponse = JsonPlaceholderController.createPost(request);

    // JSONPlaceholder typically accepts anything but we assert the returned
    // structure
    assert actualResponse.getTitle().isEmpty();
    assert actualResponse.getBody().isEmpty();
    assert actualResponse.getId() != 0;
  }

  @Test
  public void testCreatePostWithInvalidUserId() {
    // Edge Case: Invalid user ID (though JSONPlaceholder is mock, we test the
    // logic)
    PostRequest request = PostRequest.builder().title("Test").body("Body").userId(-1).build();

    PostResponse actualResponse = JsonPlaceholderController.createPost(request);

    assert actualResponse.getUserId() == -1;
    assert actualResponse.getId() != 0;
  }
}
