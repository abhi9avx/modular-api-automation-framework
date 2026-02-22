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

  @Test
  public void testFileUpload() {
    // 1. Prepare form data and file payload
    Map<String, String> formData = new HashMap<>();
    formData.put("username", "abhinav");
    formData.put("role", "SDET");

    java.io.File fileToUpload = new java.io.File("src/test/resources/sample-upload.txt");

    // 2. Pass to controller
    HttpBinController controller = new HttpBinController();
    HttpBinResponseDto response = controller.postFormDataWithFile(formData, fileToUpload);

    // 3. Assertions
    assertNotNull(response);
    assertNotNull(response.getForm(), "Form data should not be null in the response");
    assertNotNull(response.getFiles(), "Files map should not be null in the response");

    assertEquals(response.getForm().get("username"), "abhinav");
    assertEquals(response.getForm().get("role"), "SDET");
    // HttpBin responds with files object mapping the key ("file") to the content of
    // the file or base64 data.
    assertNotNull(response.getFiles().get("file"), "Uploaded file content should be present");
  }
}
