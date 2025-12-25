package com.abhinav.framework.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * -----------------------------------------------------------------------------
 * File: PostResponse.java
 * Purpose: DTO for mapping the API JSON response for Post operations.
 *
 * Why:
 * - Allows type-safe assertions on the response data (e.g., asserting 'id' is generated).
 * - Ignores unknown fields to keep tests resilient to minor API changes.
 * -----------------------------------------------------------------------------
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostResponse {
  private int id;
  private String title;
  private String body;
  private int userId;
}
