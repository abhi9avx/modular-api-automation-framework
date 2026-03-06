package com.abhinav.tests;

import com.abhinav.framework.controller.UserController;
import com.abhinav.framework.dto.CreateUserRequest;
import com.abhinav.framework.dto.CreateUserResponse;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CreateUserTest extends Basic {

    @Test
    public void verifyCreateUser() {
        // Prepare request body
        CreateUserRequest requestBody = CreateUserRequest.builder()
                .name("test")
                .job("leader")
                .build();

        // Perform the API call
        Response response = UserController.createUser(requestBody);

        // Assert the status code
        Assert.assertEquals(response.getStatusCode(), 201);

        // Deserialize the response
        CreateUserResponse actualResponse = response.as(CreateUserResponse.class);

        // Assert the response body
        Assert.assertNotNull(actualResponse.getId());
        Assert.assertNotNull(actualResponse.getCreatedAt());
        Assert.assertEquals(actualResponse.getName(), requestBody.getName());
        Assert.assertEquals(actualResponse.getJob(), requestBody.getJob());
    }
}