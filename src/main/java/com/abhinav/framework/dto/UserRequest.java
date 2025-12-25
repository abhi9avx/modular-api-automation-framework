package com.abhinav.framework.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * ---------------------------------------------------------------------------------------------------------------------
 * File: UserRequest.java
 *
 * Purpose:
 * This is a DTO (Data Transfer Object) or POJO (Plain Old Java Object) class.
 * It represents the JSON payload (Body) sent when creating or updating a User via the API.
 *
 * Why we need this:
 * When sending data to an API, constructing raw JSON strings (e.g., "{\"name\":\"John\"}") is error-prone and messy.
 * Instead, we create a Java Object, set its fields, and let a library (Jackson) convert it to JSON automatically.
 *
 * Why this is better (Props/Cons):
 * - Pros:
 *   1. **Type Safety**: Ensure 'age' is an int, 'name' is a String, etc.
 *   2. **Readability**: Code is cleaner (`user.setName("John")`) compared to string manipulation.
 *   3. **Reusability**: Can be used across multiple tests.
 * - Cons:
 *   1. Requires creating a class for every request payload type.
 *
 * Summary:
 * A simple class with fields matching the API request keys (`name`, `job`).
 * We use Lombok annotations (`@Data`, `@Builder`) to automatically generate Getters, Setters, and Constructors options.
 * ---------------------------------------------------------------------------------------------------------------------
 */
@Data // Generates Getters, Setters, toString, equals, and hashCode methods
      // automatically
@Builder // Allows us to create objects using the builder pattern:
         // UserRequest.builder().name("John").build()
@NoArgsConstructor // Generates a constructor with no arguments (required by Jackson for JSON
                   // deserialization)
@AllArgsConstructor // Generates a constructor with all arguments
public class UserRequest {

    // Field mapping to JSON key "name"
    private String name;

    // Field mapping to JSON key "job"
    private String job;
}
