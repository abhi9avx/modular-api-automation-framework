package com.abhinav.tests;

import com.abhinav.framework.dto.JsonPlaceholderUserDto;
import com.abhinav.framework.utils.DtoComparisonUtil;
import org.testng.annotations.Test;

public class JsonPlaceholderUsersTest {

    @Test
    public void testUsersApiWithDtoComparison() {
        // 1. Fetch the actual response from the API using Controller
        JsonPlaceholderUserDto[] actualUsers = com.abhinav.framework.controller.JsonPlaceholderController.getUsers();

        // 2. Build the expected DTO for the first user
        JsonPlaceholderUserDto expectedFirstUser = JsonPlaceholderUserDto.createDefault();

        // 3. Compare the Expected DTO vs Actual DTO using the Utility
        // This will recursively verify every field matches exactly and throw an
        // informative error if anything is off
        System.out.println("Comparing expected user (ID: 1) with the first user from the API response...");
        DtoComparisonUtil.compareAndAssert(expectedFirstUser, actualUsers[0]);
        System.out.println("DTO Comparison successful. The actual user matches the expected user exactly.");
    }
}
