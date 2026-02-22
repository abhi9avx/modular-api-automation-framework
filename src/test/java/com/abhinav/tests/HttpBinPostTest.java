package com.abhinav.tests;

import static org.testng.Assert.assertEquals;

import com.abhinav.framework.controller.HttpBinController;
import com.abhinav.framework.dto.HttpBinResponseDto;
import org.testng.annotations.Test;

public class HttpBinPostTest {

  @Test
  public void testHttpBinPost() {
    HttpBinController controller = new HttpBinController();
    // Dispatches the POST request with the JSON file and returns the serialized
    // response DTO
    HttpBinResponseDto response = controller.postHttpBin();

    assertEquals(response.getJson().getName(), "Abhinav");
    assertEquals(response.getJson().getRole(), "SDET");
    assertEquals(response.getJson().getExperience(), 4);
    assertEquals(response.getJson().getSkills().size(), 3);
  }
}
