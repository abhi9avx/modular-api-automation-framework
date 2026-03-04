package com.abhinav.controller;

import com.abhinav.framework.controller.HttpGetController;
import com.abhinav.framework.dto.HttpGetResponse;

public class GetController {

  public static HttpGetResponse getGet() {
    return HttpGetController.getGetEndpoint();
  }
}
