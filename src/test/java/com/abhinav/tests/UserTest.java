package com.abhinav.tests;

import com.abhinav.framework.controller.UserController;
import com.abhinav.framework.dto.CreateUserRequest;
import com.abhinav.framework.dto.CreateUserResponse;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UserTest extends Basic {
    @Test
    public void verifyCreateUser() {
        CreateUserRequest request = CreateUserRequest.builder().name("test").job("leader").build();
        Response response = UserController.createUser(request);
        Assert.assertEquals(response.getStatusCode(), 201);
        CreateUserResponse actualResponse = response.as(CreateUserResponse.class);

        Assert.assertNotNull(actualResponse.getId());
        Assert.assertNotNull(actualResponse.getCreatedAt());
        Assert.assertEquals(actualResponse.getName(), request.getName());
        Assert.assertEquals(actualResponse.getJob(), request.getJob());
    }
}
