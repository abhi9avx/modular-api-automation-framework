package com.abhinav.tests;

import com.abhinav.framework.controller.HttpBinController;
import com.abhinav.framework.dto.HttpBinPostRequestDto;
import com.abhinav.framework.dto.HttpBinPostResponseDto;
import com.abhinav.framework.utils.DtoComparisonUtil;
import org.testng.annotations.Test;

public class HttpBinMessagePostTest extends BaseTest {

  @Test(
      description =
          "Verify that POST request to httpbin.org/post with a message JSON is successful")
  public void testHttpBinPostMessage() {
    HttpBinController controller = new HttpBinController();

    // 1. Prepare Request Data
    HttpBinPostRequestDto requestPayload = HttpBinPostRequestDto.createDefault();
    log.info("Sending POST request to HttpBin with payload: " + requestPayload);

    // 2. Perform API Call
    HttpBinPostResponseDto response = controller.postHttpBinMessage(requestPayload);
    log.info("Received response from HttpBin: " + response);

    // 3. Assertions
    DtoComparisonUtil.compareAndAssert(requestPayload, response.getJson());
  }
}
