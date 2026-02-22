package com.abhinav.tests;

import com.abhinav.framework.controller.PostmanEchoController;
import com.abhinav.framework.dto.PostmanEchoResponseDto;
import com.abhinav.framework.utils.DtoComparisonUtil;
import org.testng.annotations.Test;

public class PostmanEchoPostTest {

  @Test
  public void testPostmanEchoPost() {
    PostmanEchoController controller = new PostmanEchoController();

    // 1. Prepare Expected Response
    PostmanEchoResponseDto expectedResponse = PostmanEchoResponseDto.createExpected();

    // 2. Make API Call
    PostmanEchoResponseDto actualResponse = controller.postRequest();

    // 4. Use DTO Comparison Util
    // We ignore headers, args, files, and form as these are dynamic parameters that
    // are not explicitly evaluated here
    DtoComparisonUtil.compareAndAssert(
        expectedResponse, actualResponse, "headers", "args", "files", "form");
  }
}
