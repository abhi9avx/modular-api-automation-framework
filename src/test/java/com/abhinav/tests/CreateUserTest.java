package com.abhinav.tests;

import com.abhinav.framework.controller.UserController;
import com.abhinav.framework.dto.CreateUserResponse;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CreateUserTest extends Basic {

    @Test
    public void testCreateUser() {
        String name = "autonomous-final-validation";
        String job = "leader";

        Response response = UserController.createUser(name, job);
        Assert.assertEquals(response.getStatusCode(), 201);

        CreateUserResponse createUserResponse = response.as(CreateUserResponse.class);
        Assert.assertNotNull(createUserResponse.getId());
        Assert.assertNotNull(createUserResponse.getCreatedAt());
        Assert.assertEquals(createUserResponse.getName(), name);
        Assert.assertEquals(createUserResponse.getJob(), job);
    }
}
