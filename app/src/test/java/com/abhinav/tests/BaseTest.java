package com.abhinav.tests;

import io.qameta.allure.testng.AllureTestNg;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

@Listeners({ AllureTestNg.class })
public class BaseTest {

    @BeforeSuite
    public void setup() {
        System.out.println("=== Test Suite Started ===");
    }
}
