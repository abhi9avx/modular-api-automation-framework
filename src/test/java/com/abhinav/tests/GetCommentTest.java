package com.abhinav.tests;

import com.abhinav.framework.controller.CommentController;
import com.abhinav.framework.dto.CommentResponseDto;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetCommentTest {

  // Test the get comment API to ensure it returns the correct data.
  // We verify that the ID, email, and content match exactly what we expect from
  // the server.
  @Test
  public void testGetCommentById() {

    CommentResponseDto comment = CommentController.getCommentById();

    // Assert
    Assert.assertEquals(comment.getId(), 1);
    Assert.assertEquals(comment.getPostId(), 1);
    Assert.assertEquals(comment.getName(), "id labore ex et quam laborum");
    Assert.assertEquals(comment.getEmail(), "Eliseo@gardner.biz");
    Assert.assertEquals(
        comment.getBody(),
        "laudantium enim quasi est quidem magnam voluptate ipsam eos\ntempora quo necessitatibus\ndolor quam autem quasi\nreiciendis et nam sapiente accusantium");
  }
}
