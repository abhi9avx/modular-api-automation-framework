package com.abhinav.tests;
import io.restassured.RestAssured;
import static org.hamcrest.Matchers.equalTo;
import static io.restassured.RestAssured.given;

import org.testng.annotations.Test;

public class GetCustomerTest {

    @Test
    public void customerFound(){
        given()
            .baseUri("https://reqbin.com")
            .header("Accept","application/json")
        .when()
            .get("/echo/get/json/12345")
        .then()
            .statusCode(200);
    
    }

    @Test
    public void customerNotFound(){
        given()
            .baseUri("https://reqbin.com")
            .header("Accept","application/json")
        .when()
            .get("/echo/get/json/000")
        .then()
            .statusCode(404);
    
    }

    
}
