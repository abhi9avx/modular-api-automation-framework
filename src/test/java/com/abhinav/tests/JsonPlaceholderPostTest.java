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
}
