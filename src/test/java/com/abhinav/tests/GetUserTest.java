package com.abhinav.tests;

import com.abhinav.framework.controller.UserAPIController;
import com.abhinav.framework.dto.UserResponseDTO;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetUserTest {

  @Test
  public void shouldFetchUserSuccessfully() {

    UserResponseDTO response = UserAPIController.getUserById(1);

    Assert.assertNotNull(response);
    Assert.assertNotNull(response.getData());

    Assert.assertEquals(response.getData().getId(), 1);
    Assert.assertEquals(response.getData().getFirst_name(), "George");
    Assert.assertEquals(response.getData().getLast_name(), "Bluth");

    Assert.assertTrue(response.getData().getEmail().contains("@reqres.in"));

    Assert.assertNotNull(response.getSupport());
    Assert.assertNotNull(response.get_meta());
  }
}
