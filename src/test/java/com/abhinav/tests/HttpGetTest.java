package com.abhinav.tests;

import com.abhinav.framework.controller.HttpGetController;
import com.abhinav.framework.dto.HttpGetResponse;
import com.abhinav.framework.utils.DtoComparisonUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HttpGetTest extends com.abhinav.tests.Basic {

  @Test
  public void testGetEndpoint() {
    HttpGetResponse actualResponse = HttpGetController.getGetEndpoint();
    DtoComparisonUtil.compareAndAssert(HttpGetResponse.createDefault(), actualResponse, "url");
    Assert.assertEquals(actualResponse.getUrl(), "https://httpbin.org/get", "Mismatch!");
  }
}
