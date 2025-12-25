package com.abhinav.tests;

import org.testng.annotations.BeforeSuite;

public class BaseTest {

    @BeforeSuite
    public void setup() {
        System.out.println("Test framework initialized...");
    }
}
