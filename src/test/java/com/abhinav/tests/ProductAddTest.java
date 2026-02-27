package com.abhinav.tests;

import com.abhinav.framework.controller.AddProductController;
import com.abhinav.framework.dto.AddProductRequestDto;
import com.abhinav.framework.dto.AddProductResponseDto;
import com.abhinav.framework.utils.DtoComparisonUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ProductAddTest {

  @Test
  public void testAddProduct() {
    AddProductResponseDto actualResponse = AddProductController.addProduct();

    DtoComparisonUtil.compareAndAssert(AddProductResponseDto.createDefault(), actualResponse, "id");

    Assert.assertEquals(
        actualResponse.getTitle(),
        AddProductRequestDto.createDefault().getTitle(),
        "Title mismatch detected in Response!");
  }
}
