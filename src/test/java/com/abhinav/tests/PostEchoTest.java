package com.abhinav.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.testng.annotations.Test;

public class PostEchoTest {

    @Test
    public void postJsonTest(){

        String payload = "{\"Id\": 12345,\"Customer\": \"John Smith\",\"Quantity\": 1,\"Price\": 10.00}";

        given()
            .baseUri("https://reqbin.com")
            .body(payload)
        .when()
            .post("/echo/post/json")
        .then()
            .statusCode(200)
            .body("success", equalTo(true));
        
    }

    @Test
    public void postJsonTest2(){

        String payload = "{\"Id\": 12345,\"Customer\": \"John Smith\",\"Quantity\": 1}";

        given()
            .baseUri("https://reqbin.com")
            .body(payload)
        .when() 
            .post("/echo/post/json")
        .then()
            .statusCode(200)
            .body("success", equalTo(true));
        
    }

    @Test
    public void postJsonTest3(){
        String payload = "";
        given()
            .baseUri("https://reqbin.com")
            .body(payload)
        .when() 
            .post("/echo/post/json")
        .then()
            .statusCode(200)
            .body("success", equalTo(true));
        
    }
    

        
    
}
