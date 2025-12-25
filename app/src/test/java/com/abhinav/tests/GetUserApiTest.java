package com.abhinav.tests;

import com.abhinav.framework.config.EnvironmentConfig;
import io.restassured.RestAssured;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.qameta.allure.*;

public class GetUserApiTest {

    @Test
    public void getUserDetails() {

        String baseUrl = EnvironmentConfig.getBaseUrl();

        int statusCode = RestAssured
                .given()
                .baseUri(baseUrl)
                .when()
                .get("/users/2")
                .getStatusCode();

        Assert.assertEquals(statusCode, 200);
    }
}
