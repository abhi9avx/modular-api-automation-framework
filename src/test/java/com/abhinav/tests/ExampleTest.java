package com.abhinav.tests;

import com.abhinav.framework.controller.ExampleController;
import com.abhinav.framework.dto.ExampleDto;
import com.abhinav.framework.utils.DtoComparisonUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ExampleTest extends Basic {

  @Test
  public void testGetExample() {
    ExampleDto actualResponse = ExampleController.getExample();
    DtoComparisonUtil.compareAndAssert(ExampleDto.createDefault(), actualResponse, "id");
    Assert.assertEquals(actualResponse.getFieldName(), "expected", "Mismatch!");
  }
}
