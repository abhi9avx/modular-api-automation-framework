package com.abhinav.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import com.abhinav.framework.controller.HttpBinController;
import com.abhinav.framework.dto.ComplexResponseDto;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.testng.annotations.Test;

public class ComplexPayloadTest {

  @Test
  public void testComplexPayloadWithMap() {
    // 1. Construct the payload using Map and List
    Map<String, Object> payload = new HashMap<>();

    // -- User Object Construction --
    Map<String, Object> userMap = new HashMap<>();
    userMap.put("id", 101);
    userMap.put("name", "Abhinav");

    List<String> roles = Arrays.asList("SDET", "Automation Engineer");
    userMap.put("roles", roles);

    // Profile Object
    Map<String, Object> profileMap = new HashMap<>();
    profileMap.put("email", "abhinav@test.com");

    // Phones List
    List<Map<String, String>> phones = new ArrayList<>();
    Map<String, String> personalPhone = new HashMap<>();
    personalPhone.put("type", "personal");
    personalPhone.put("number", "9999999999");
    phones.add(personalPhone);

    Map<String, String> workPhone = new HashMap<>();
    workPhone.put("type", "work");
    workPhone.put("number", "8888888888");
    phones.add(workPhone);

    profileMap.put("phones", phones);

    // Address Object
    Map<String, Object> addressMap = new HashMap<>();
    addressMap.put("street", "Sector 21");
    addressMap.put("city", "Chandigarh");

    Map<String, Double> geoMap = new HashMap<>();
    geoMap.put("lat", 30.7333);
    geoMap.put("lng", 76.7794);
    addressMap.put("geo", geoMap);

    profileMap.put("address", addressMap);
    userMap.put("profile", profileMap);

    // -- Project Object Construction --
    Map<String, Object> projectMap = new HashMap<>();
    projectMap.put("name", "Self Healing Framework");

    List<Map<String, String>> modules = new ArrayList<>();
    Map<String, String> apiModule = new HashMap<>();
    apiModule.put("module", "API");
    apiModule.put("status", "completed");
    modules.add(apiModule);

    Map<String, String> uiModule = new HashMap<>();
    uiModule.put("module", "UI");
    uiModule.put("status", "in-progress");
    modules.add(uiModule);

    projectMap.put("modules", modules);
    projectMap.put("active", true);

    // Add User and Project to main Payload Map
    payload.put("user", userMap);
    payload.put("project", projectMap);

    // 2. Pass to controller
    HttpBinController controller = new HttpBinController();
    ComplexResponseDto response = controller.postComplexPayload(payload);

    // 3. Assertions
    assertEquals(response.getJson().getUser().getName(), "Abhinav");
    assertEquals(response.getJson().getUser().getProfile().getEmail(), "abhinav@test.com");
    assertEquals(
        response.getJson().getUser().getProfile().getPhones().get(0).getNumber(), "9999999999");

    assertEquals(response.getJson().getProject().getName(), "Self Healing Framework");
    assertTrue(response.getJson().getProject().isActive());
    assertEquals(response.getJson().getProject().getModules().get(0).getStatus(), "completed");
  }
}
