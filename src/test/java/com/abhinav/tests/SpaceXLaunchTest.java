package com.abhinav.tests;

import com.abhinav.framework.controller.SpaceXController;
import com.abhinav.framework.dto.SpaceXLaunchDto;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SpaceXLaunchTest extends BaseTest {

  @Test
  public void testGetLaunches() {
    log.info("Fetching SpaceX launches...");
    List<SpaceXLaunchDto> launches = SpaceXController.getLaunches();

    // Basic validations
    Assert.assertNotNull(launches, "Launches list should not be null");
    Assert.assertFalse(launches.isEmpty(), "Launches list should not be empty");

    // Validate the first launch (FalconSat)
    SpaceXLaunchDto firstLaunch = launches.get(0);
    log.info("Validating the very first SpaceX launch: {}", firstLaunch.getName());

    Assert.assertEquals(firstLaunch.getName(), "FalconSat", "First launch name mismatch");
    Assert.assertEquals(firstLaunch.getFlight_number(), 1, "First launch flight number mismatch");

    // This launch failed
    Assert.assertFalse(firstLaunch.getSuccess(), "First launch should be a failure");

    // Ensure that failure details are correctly parsed
    Assert.assertNotNull(firstLaunch.getFailures(), "Failures list should not be null");
    Assert.assertFalse(firstLaunch.getFailures().isEmpty(), "Failures list should contain items");
    Assert.assertEquals(
        firstLaunch.getFailures().get(0).getReason(),
        "merlin engine failure",
        "Failure reason mismatch");

    // Check some data from later launches
    SpaceXLaunchDto falcon9DemoInfo = launches.get(5);
    Assert.assertEquals(
        falcon9DemoInfo.getName(), "Falcon 9 Test Flight", "6th launch name mismatch");
    Assert.assertTrue(falcon9DemoInfo.getSuccess(), "Falcon 9 demo launch should be successful");
  }

  @Test
  public void testPayloadFromLocalFile() {
    log.info("Sending ResponseCompex.json as payload to an echo service...");

    java.io.File payloadFile = new java.io.File("src/main/resources/ResponseCompex.json");

    io.restassured.specification.RequestSpecification requestSpec =
        new io.restassured.builder.RequestSpecBuilder()
            .setBaseUri("https://postman-echo.com")
            .setContentType(io.restassured.http.ContentType.JSON)
            .setBody(payloadFile)
            .build();

    io.restassured.specification.ResponseSpecification responseSpec =
        new io.restassured.builder.ResponseSpecBuilder().expectStatusCode(200).build();

    // Calling this with RequestSpecification and ResponseSpecification
    io.restassured.RestAssured.given()
        .spec(requestSpec)
        .when()
        .post("/post")
        .then()
        .spec(responseSpec);

    log.info("Successfully passed ResponseCompex.json using request and response specifications");
  }
}
