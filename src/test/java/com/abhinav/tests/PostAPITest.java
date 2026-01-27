package com.abhinav.tests;

import com.abhinav.framework.controller.PostAPIController;
import com.abhinav.framework.dto.PostRequestDto;
import com.abhinav.framework.dto.PostResponseDto;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PostAPITest {

    @Test
    public void shouldCreatePostSuccessfully() {
        PostRequestDto requestDto = new PostRequestDto("My Test Post", "This is the body content", 1);

        PostResponseDto response = PostAPIController.createPost(requestDto);

        Assert.assertNotNull(response.getId());
        Assert.assertEquals(response.getTitle(), "My Test Post");
        Assert.assertEquals(response.getBody(), "This is the body content");
        Assert.assertEquals(response.getUserId(), Integer.valueOf(1));
    }
}
