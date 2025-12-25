package com.abhinav.tests;

import io.restassured.RestAssured;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetUserApiTest extends BaseTest {

    @Test
    public void getUserDetails() {

        int statusCode = RestAssured
                .given()
                .baseUri("https://jsonplaceholder.typicode.com/users/1")
                .when()
                .get()
                .then()
                .extract()
                .statusCode();

        Assert.assertEquals(statusCode, 200);
    }
}
