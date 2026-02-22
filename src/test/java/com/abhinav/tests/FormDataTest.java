package com.abhinav.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import com.abhinav.framework.controller.HttpBinController;
import com.abhinav.framework.dto.HttpBinResponseDto;
import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.Test;

public class FormDataTest {

  @Test
  public void testMultipartFormData() {
    // 1. Prepare form data payload
    Map<String, String> formData = new HashMap<>();
    formData.put("name", "Abhinav");
    formData.put("role", "SDET");
    formData.put("city", "Chandigarh");
    formData.put("experience", "4");

    // 2. Pass to controller
    HttpBinController controller = new HttpBinController();
    HttpBinResponseDto response = controller.postFormData(formData);

    // 3. Assertions
    assertNotNull(response);
    assertNotNull(response.getForm(), "Form data should not be null in the response");

    assertEquals(response.getForm().get("name"), "Abhinav");
    assertEquals(response.getForm().get("role"), "SDET");
    assertEquals(response.getForm().get("city"), "Chandigarh");
    assertEquals(response.getForm().get("experience"), "4");
  }
}
