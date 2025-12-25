package com.abhinav.framework.config;

/*
 * ---------------------------------------------------------------------------------------------------------------------
 * File: EndpointConfig.java
 *
 * Purpose:
 * This class stores all the API endpoint paths (Resources) as static constants.
 * It serves as a central registry for all URI paths used in the application.
 *
 * Why we need this:
 * API endpoints (like "/users", "/posts") are repeated across multiple tests and controllers.
 * Typing standard strings manually ("users") is prone to typos (e.g., "user" vs "users").
 *
 * Why this is better (Props/Cons):
 * - Pros:
 *   1. **Single Source of Truth**: If the backend changes an endpoint from `/v1/users` to `/v2/users`,
 *      you only change it here, and it reflects everywhere.
 *   2. **Readability**: Code like `POSTS_ENDPOINT` is more descriptive than a raw string "/posts".
 *   3. **Prevent Typos**: Compiler checks variable names, but it can't check string content.
 * - Cons:
 *   1. None really, it's a standard best practice.
 *
 * Summary:
 * A simple class with public static final variables representing distinct API routes.
 * ---------------------------------------------------------------------------------------------------------------------
 */
public class EndpointConfig {

  // Endpoint for Post-related operations (e.g., GET /posts, POST /posts)
  public static final String POSTS_ENDPOINT = "/posts";

  // Endpoint for User-related operations (e.g., GET /users, POST /users)
  public static final String USERS_ENDPOINT = "/users";
}
