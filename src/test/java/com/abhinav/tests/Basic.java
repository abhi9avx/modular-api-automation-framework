package com.abhinav.tests;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

public class Basic {

    public static void main(String[] args) {

        // Set Base URI so all requests use same server endpoint
        RestAssured.baseURI = "https://rahulshettyacademy.com";


        // =============================
        // 1. ADD PLACE REQUEST
        // =============================

        // Create new place resource so we can test update + get APIs
        String addPlaceResponse =
                given()
                        .queryParam("key", "qaclick123") // pass API key for authentication
                        .header("Content-Type", "application/json") // specify JSON request body
                        .body("{\n" +
                                "  \"location\": {\n" +
                                "    \"lat\": -38.383494,\n" +
                                "    \"lng\": 33.427362\n" +
                                "  },\n" +
                                "  \"accuracy\": 50,\n" +
                                "  \"name\": \"Abhinav Kumar\",\n" +
                                "  \"phone_number\": \"(+91) 8406938733\",\n" +
                                "  \"address\": \"Ward No - 9, Purvi Thatha, Khagaria, Bihar\",\n" +
                                "  \"types\": [\"shoe park\", \"shop\"],\n" +
                                "  \"website\": \"http://google.com\",\n" +
                                "  \"language\": \"French-IN\"\n" +
                                "}")
                .when()
                        .post("/maps/api/place/add/json") // send POST request to create resource
                .then()
                        .assertThat().statusCode(200) // verify API executed successfully
                        .body("scope", equalTo("APP")) // validate response business value
                        .header("server", "Apache/2.4.52 (Ubuntu)") // verify correct server responded
                        .extract().response().asString(); // store response for reuse


        // Extract dynamic place_id from response for chaining next APIs
        JsonPath addPlaceJson = new JsonPath(addPlaceResponse);
        String placeId = addPlaceJson.getString("place_id");
        System.out.println("Generated Place ID: " + placeId);



        // =============================
        // 2. UPDATE PLACE REQUEST
        // =============================

        // Modify address of existing place to verify update functionality
        String updatedAddress = "Bangalore India";

        given()
                .log().all() // log full request for debugging visibility
                .queryParam("key", "qaclick123") // pass auth key again
                .header("Content-Type", "application/json") // define JSON body format
                .body("{\n" +
                        "  \"place_id\": \"" + placeId + "\",\n" +
                        "  \"address\": \"" + updatedAddress + "\",\n" +
                        "  \"key\": \"qaclick123\"\n" +
                        "}")
        .when()
                .put("/maps/api/place/update/json") // send PUT request to update existing record
        .then()
                .assertThat().statusCode(200) // validate update call success
                .body("msg", equalTo("Address successfully updated")); // verify update confirmation message



        // =============================
        // 3. GET PLACE REQUEST
        // =============================

        // Fetch place details to confirm updated data persisted correctly
        String getPlaceResponse =
                given()
                        .log().all() // log request for traceability
                        .queryParam("key", "qaclick123") // authentication key
                        .queryParam("place_id", placeId) // identify which place to retrieve
                .when()
                        .get("/maps/api/place/get/json") // send GET request to retrieve resource
                .then()
                        .assertThat().statusCode(200) // verify API success
                        .body("address", equalTo(updatedAddress)) // validate updated value returned
                        .extract().response().asString(); // extract response for assertion


        // Parse GET response JSON to extract actual address value
        JsonPath getPlaceJson = new JsonPath(getPlaceResponse);
        String actualAddress = getPlaceJson.getString("address");

        System.out.println("Actual Address: " + actualAddress);
        System.out.println("Full Response: " + getPlaceResponse);



        // =============================
        // 4. FINAL ASSERTION
        // =============================

        // Ensure backend data matches expected updated value
        Assert.assertEquals(actualAddress, updatedAddress);
    }
}
