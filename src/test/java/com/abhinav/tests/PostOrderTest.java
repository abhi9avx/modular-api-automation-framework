package com.abhinav.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.abhinav.framework.controller.EchoController;
import com.abhinav.framework.dto.EchoRequestDto;
import com.abhinav.framework.dto.EchoResponseDto;
import io.restassured.response.Response;

public class PostOrderTest {

    @Test
    public void testPostOrder() {
        EchoController echoController = new EchoController();

        EchoRequestDto request = EchoRequestDto.builder()
                .id(12345)
                .customer("John Smith")
                .quantity(1)
                .price(10.0)
                .build();

        Response response = echoController.postEchoRaw(request);

        Assert.assertEquals(response.getStatusCode(), 200,
                "Expected status code 200 but got " + response.getStatusCode());

        EchoResponseDto responseDto = response.as(EchoResponseDto.class);
        Assert.assertTrue(responseDto.isSuccess(), "Success field should be true");
    }
}
