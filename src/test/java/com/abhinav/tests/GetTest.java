package com.abhinav.tests;

import com.abhinav.controller.GetController;
import com.abhinav.framework.dto.HttpGetResponse;
import com.abhinav.framework.utils.DtoComparisonUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetTest extends Basic {

  @Test
  public void testGetEndpoint() {
    HttpGetResponse actualResponse = GetController.getGet();
    DtoComparisonUtil.compareAndAssert(HttpGetResponse.createDefault(), actualResponse, "url");
    Assert.assertEquals(actualResponse.getUrl(), "https://httpbin.org/get", "Mismatch!");
  }
}
