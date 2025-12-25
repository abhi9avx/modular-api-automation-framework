package com.abhinav.framework.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/*
 * ---------------------------------------------------------------------------------------------------------------------
 * File: UserResponse.java
 *
 * Purpose:
 * This DTO represents the JSON response received from the API after calling User-related endpoints.
 * It maps the JSON fields (like `id`, `createdAt`) back to Java class fields.
 *
 * Why we need this:
 * Validating raw JSON strings (response.jsonPath().getString("name")) is okay for simple tests.
 * but for complex objects, converting the response to a Java Object allows us to perform easy, compiler-checked assertions.
 * e.g., `Assert.assertEquals(user.getName(), "John")`.
 *
 * Why this is better (Props/Cons):
 * - Pros:
 *   1. **Strict Validation**: If the API stops returning a field we expect, deserialization might fail (or we check for null), alerting us immediately.
 *   2. **Easy Access**: We can use `user.getId()` instead of parsing the string repeatedly.
 * - Cons:
 *   1. Extra code to maintain if the API response schema changes frequently.
 *
 * Summary:
 * Maps the JSON response to Java fields. Uses `@JsonIgnoreProperties(ignoreUnknown = true)` to prevent crashes
 * if the API returns extra fields we haven't defined here.
 * ---------------------------------------------------------------------------------------------------------------------
 */
@Data // Auto-generates getters/setters/toString/etc.
@NoArgsConstructor // Required for Jackson
@AllArgsConstructor // Convenience constructor
@Builder // Builder pattern support
@JsonIgnoreProperties(ignoreUnknown = true) // Crucial: Tells Jackson to ignore any JSON fields not defined below
                                            // (prevents "UnrecognizedPropertyException")
public class UserResponse {

    // Unique ID returned by the API
    private int id;

    // Name of the user
    private String name;

    // Username (if applicable)
    private String username;

    // Email (if applicable)
    private String email;

    // Job title (returned by Create User API)
    private String job;

    // Timestamp of creation (returned by Create User API)
    private String createdAt;
}
